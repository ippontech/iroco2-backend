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
package fr.ippon.iroco2.domain.analyzer;

import fr.ippon.iroco2.domain.analyzer.exception.AnalysisNotFoundException;
import fr.ippon.iroco2.domain.analyzer.model.Analysis;
import fr.ippon.iroco2.domain.analyzer.spi.AnalysisStorage;
import fr.ippon.iroco2.domain.commons.exception.FunctionalException;
import fr.ippon.iroco2.domain.commons.exception.NotFoundException;
import fr.ippon.iroco2.domain.commons.exception.UnauthorizedActionException;
import fr.ippon.iroco2.domain.commons.model.Payload;
import fr.ippon.iroco2.domain.commons.model.ReportStatus;
import fr.ippon.iroco2.domain.commons.spi.SessionProvider;
import fr.ippon.iroco2.domain.commons.svc.DateProvider;
import fr.ippon.iroco2.domain.estimateur.CarbonEstimator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnalyzerSvcTest {
    private static final String CONNECTED_USER_MAIL = "connected-user-mail";

    @Captor
    ArgumentCaptor<Analysis> captor;

    @Mock
    private AnalysisStorage analysisStorage;
    @Mock
    private SessionProvider sessionProvider;
    @Mock
    private DateProvider dateProvider;
    @Mock
    private CarbonEstimator carbonEstimator;

    @InjectMocks
    private AnalyzerSvcImpl analyzerSvc;

    private static Payload createPayload(UUID analysisId, int expectedPayloads) {
        return new Payload(analysisId, "countryCode", "name", Duration.ZERO, expectedPayloads, new HashMap<>());
    }

    private static Analysis createSavedAnalysis(String currentUser) {
        return Analysis.load(
                UUID.randomUUID(),
                currentUser,
                ReportStatus.CREATED,
                LocalDateTime.now(),
                new ArrayList<>()
        );
    }

    @Test
    void create_should_save_analysis_with_connected_user_and_now() {
        // GIVEN
        when(sessionProvider.getConnectedUserEmail()).thenReturn(CONNECTED_USER_MAIL);
        LocalDateTime creationDate = LocalDateTime.now();
        when(dateProvider.now()).thenReturn(creationDate);

        // WHEN
        var analysisId = analyzerSvc.create();

        // THEN
        assertThat(analysisId).isNotNull();
        Analysis savedAnalysis = getSavedAnalysis();
        assertThat(savedAnalysis.getOwner()).isEqualTo(CONNECTED_USER_MAIL);
        assertThat(savedAnalysis.getCreationDate()).isEqualTo(creationDate);
    }

    @Test
    void analysisStatus_isEqualTo_created_afterCreation() {
        // WHEN
        analyzerSvc.create();

        // THEN
        Analysis analysis = getSavedAnalysis();
        assertThat(analysis.getStatus()).isEqualTo(ReportStatus.CREATED);
    }

    @Test
    void findById_should_return_analyses_when_current_user_is_owner() {
        // GIVEN
        when(sessionProvider.getConnectedUserEmail()).thenReturn(CONNECTED_USER_MAIL);
        var savedsavedAnalysis = createSavedAnalysis(CONNECTED_USER_MAIL);
        when(analysisStorage.findById(savedsavedAnalysis.getId())).thenReturn(Optional.of(savedsavedAnalysis));

        // WHEN
        var analysis = analyzerSvc.findById(savedsavedAnalysis.getId());

        // THEN
        assertThat(analysis).isEqualTo(savedsavedAnalysis);
    }

    @Test
    void findById_should_throw_exception_when_missing_analysis() {
        // GIVEN
        UUID analysisId = UUID.randomUUID();
        when(analysisStorage.findById(analysisId)).thenReturn(Optional.empty());

        // WHEN
        var error = catchThrowable(() -> analyzerSvc.findById(analysisId));

        // THEN
        assertThat(error)
                .isInstanceOf(AnalysisNotFoundException.class)
                .hasMessage("Analyse avec l'identifiant %s est introuvable".formatted(analysisId));
    }

    @Test
    void findById_should_throw_exception_when_current_user_is_not_owner() {
        // GIVEN
        when(sessionProvider.getConnectedUserEmail()).thenReturn(CONNECTED_USER_MAIL);
        var savedAnalysis = createSavedAnalysis("another-email");
        when(analysisStorage.findById(savedAnalysis.getId())).thenReturn(Optional.of(savedAnalysis));

        // WHEN
        var error = catchThrowable(() -> analyzerSvc.findById(savedAnalysis.getId()));

        // THEN
        assertThat(error)
                .isInstanceOf(UnauthorizedActionException.class)
                .hasMessage("%s n'est pas autorisé à accéder à cette ressource".formatted(CONNECTED_USER_MAIL));
    }

    @Test
    void addEstimation_should_throw_when_missing_analysis() {
        // GIVEN
        var payload = createPayload(UUID.randomUUID(), 0);
        when(analysisStorage.findById(payload.reportId())).thenReturn(Optional.empty());

        // WHEN
        var error = catchThrowable(() -> analyzerSvc.addEstimation(payload));

        // THEN
        assertThat(error)
                .isInstanceOf(AnalysisNotFoundException.class)
                .hasMessage("Analyse avec l'identifiant %s est introuvable".formatted(payload.reportId()));
    }

    @Test
    void analysisStatus_isEqualTo_inProcess_afterFirstPayloadAdding() throws FunctionalException {
        // GIVEN
        var analysis = givenExistingAnalysis();
        var payload = createPayload(analysis.getId(), 0);

        // WHEN
        analyzerSvc.addEstimation(payload);

        // THEN
        var savedAnalysis = getSavedAnalysis();
        assertThat(savedAnalysis.getStatus()).isEqualTo(ReportStatus.IN_PROGRESS);
    }

    @Test
    void analysisStatus_isEqualTo_success_afterLastPayloadAdding() throws FunctionalException {
        // GIVEN
        var analysis = givenExistingAnalysis();
        var payload = createPayload(analysis.getId(), analysis.getPayloads().size() + 1);

        // WHEN
        analyzerSvc.addEstimation(payload);

        // THEN
        var savedAnalysis = getSavedAnalysis();
        assertThat(savedAnalysis.getStatus()).isEqualTo(ReportStatus.SUCCESS);
        assertThat(savedAnalysis.getPayloads()).hasSize(payload.expectedPayloads());
    }

    @Test
    void addEstimation_should_call_carbonEstimator() throws FunctionalException {
        //given
        Payload payload = mock(Payload.class);
        when(payload.countryIsoCode()).thenReturn("isocode");
        when(analysisStorage.findById(any())).thenReturn(Optional.of(mock(Analysis.class)));

        //when
        analyzerSvc.addEstimation(payload);

        //then
        verify(carbonEstimator).estimatePayload("isocode", payload);
    }

    @Test
    void should_succeed_when_deleting_existing_analysis() {
        // GIVEN
        UUID analysisId = UUID.randomUUID();
        Analysis existingAnalysis = createSavedAnalysis("current-user");
        when(analysisStorage.findById(analysisId)).thenReturn(Optional.of(existingAnalysis));

        // WHEN
        analyzerSvc.delete(analysisId);

        // THEN
        verify(analysisStorage).findById(analysisId);
        verify(analysisStorage).delete(existingAnalysis.getId());
    }

    @Test
    void should_throw_error_when_deleting_nonexistent_analysis() {
        // GIVEN
        UUID analysisId = UUID.randomUUID();
        when(analysisStorage.findById(analysisId)).thenReturn(Optional.empty());

        // WHEN
        Throwable error = catchThrowable(() -> analyzerSvc.delete(analysisId));

        // THEN
        assertThat(error)
                .isInstanceOf(NotFoundException.class)
                .hasMessage("The analysis with ID '%s' is not found".formatted(analysisId));
        verify(analysisStorage).findById(analysisId);
        verify(analysisStorage, never()).delete(any());
    }

    private Analysis givenExistingAnalysis() {
        var analysis = createSavedAnalysis("owner");
        when(analysisStorage.findById(analysis.getId())).thenReturn(Optional.of(analysis));
        return analysis;
    }

    private Analysis getSavedAnalysis() {
        verify(analysisStorage).save(captor.capture());
        return captor.getValue();
    }

    @Test
    void findAll_should_search_in_storage_for_connected_user() {
        //given
        String connectedUser = "connected user";
        when(sessionProvider.getConnectedUserEmail()).thenReturn(connectedUser);

        List<Analysis> list = List.of(mock(Analysis.class));
        when(analysisStorage.findByOwner(connectedUser)).thenReturn(list);

        //when
        var result = analyzerSvc.findAll();

        //then
        assertThat(result).isEqualTo(list);
    }
}
