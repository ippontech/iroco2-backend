/*
 * Copyright 2025 Ippon Technologies
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package fr.ippon.iroco2.domain.scanner;

import fr.ippon.iroco2.domain.commons.AbstractElementSvc;
import fr.ippon.iroco2.domain.commons.DomainService;
import fr.ippon.iroco2.domain.commons.exception.UnauthorizedActionException;
import fr.ippon.iroco2.domain.commons.spi.ReportStorage;
import fr.ippon.iroco2.domain.commons.spi.SessionProvider;
import fr.ippon.iroco2.domain.commons.svc.DateProvider;
import fr.ippon.iroco2.domain.estimateur.CarbonEstimator;
import fr.ippon.iroco2.domain.scanner.api.ScannerSvc;
import fr.ippon.iroco2.domain.scanner.exception.ScanNotFoundException;
import fr.ippon.iroco2.domain.scanner.model.Scan;
import fr.ippon.iroco2.domain.scanner.spi.ScanStorage;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@DomainService
@RequiredArgsConstructor
public class ScannerSvcImpl extends AbstractElementSvc<Scan, ScanNotFoundException> implements ScannerSvc {
    private final ScanStorage scanStorage;
    private final SessionProvider sessionProvider;
    private final DateProvider dateProvider;
    private final CarbonEstimator carbonEstimator;

    @Override
    public UUID create() {
        String owner = sessionProvider.getConnectedUserEmail();
        String awsAccountId = sessionProvider.getConnectedAwsAccountId();
        Scan scan = Scan.create(owner, dateProvider.now(), awsAccountId);
        return scanStorage.save(scan);
    }

    @Override
    public void delete(UUID scanId) {
        Scan scan = existsControl(scanId);
        scanStorage.delete(scan.getId());
    }

    @Override
    public Scan findById(UUID id) throws ScanNotFoundException {
        var scan = scanStorage.findById(id).orElseThrow(() -> new ScanNotFoundException(id));
        String connectedUser = sessionProvider.getConnectedUserEmail();
        if (scan.getOwner().equals(connectedUser)) return scan;
        else throw new UnauthorizedActionException(
                "%s n'est pas autorisé à accéder à cette ressource".formatted(connectedUser)
        );
    }

    @Override
    public List<Scan> findAll() {
        String owner = sessionProvider.getConnectedUserEmail();
        return scanStorage.findByOwner(owner);
    }

    private Scan existsControl(UUID scanId) {
        return scanStorage
                .findById(scanId)
                .orElseThrow(() -> new ScanNotFoundException(scanId));
    }

    @Override
    protected ScanNotFoundException createException(UUID reportId) {
        return new ScanNotFoundException(reportId);
    }

    @Override
    protected ReportStorage<Scan> getStorage() {
        return scanStorage;
    }

    @Override
    protected CarbonEstimator getCarbonEstimator() {
        return carbonEstimator;
    }
}
