package fr.ippon.iroco2.domain.calculateur;

import fr.ippon.iroco2.domain.calculateur.api.InfrastructureSvc;
import fr.ippon.iroco2.domain.calculateur.exception.InfrastructureNotFound;
import fr.ippon.iroco2.domain.calculateur.model.Infrastructure;
import fr.ippon.iroco2.domain.calculateur.spi.InfrastructureStorage;
import fr.ippon.iroco2.domain.commons.DomainService;
import fr.ippon.iroco2.domain.commons.exception.UnauthorizedActionException;
import fr.ippon.iroco2.domain.commons.spi.SessionProvider;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@DomainService
@RequiredArgsConstructor
public class InfrastructureSvcImpl implements InfrastructureSvc {

    private final InfrastructureStorage infrastructureStorage;
    private final SessionProvider sessionProvider;

    @Override
    public void save(Infrastructure infrastructure) {
        infrastructureStorage.save(infrastructure);
    }

    @Override
    public List<Infrastructure> findAll() {
        final String email = sessionProvider.getConnectedUserEmail();
        return infrastructureStorage.findAllByOwner(email);
    }

    @Override
    public Infrastructure findById(UUID id) {
        final String email = sessionProvider.getConnectedUserEmail();
        Infrastructure infrastructure = infrastructureStorage
                .findById(id)
                .orElseThrow(() -> new InfrastructureNotFound(id));

        if (!infrastructure.owner().equals(email)) throw new UnauthorizedActionException(
                "You don't have the right to access this infrastructure"
        );

        return infrastructure;
    }

    @Override

    public void delete(UUID id) {
        infrastructureStorage.delete(this.findById(id));
    }
}
