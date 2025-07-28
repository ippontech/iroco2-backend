package fr.ippon.iroco2.estimateur.persistence;

import fr.ippon.iroco2.domain.estimateur.model.energy_mix.GlobalEnergyMix;
import fr.ippon.iroco2.domain.estimateur.spi.GlobalEnergyMixStorage;
import fr.ippon.iroco2.estimateur.persistence.repository.GlobalEnergyMixRepository;
import fr.ippon.iroco2.estimateur.persistence.repository.entity.GlobalEnergyMixEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class GlobalEnergyMixStorageAdapter implements GlobalEnergyMixStorage {
    private final GlobalEnergyMixRepository globalEnergyMixRepository;

    @Override
    public Optional<GlobalEnergyMix> findByIsoCode(String countryIsoCode) {
        return globalEnergyMixRepository.findByIsoCode(countryIsoCode).map(GlobalEnergyMixEntity::toDomain);
    }
}
