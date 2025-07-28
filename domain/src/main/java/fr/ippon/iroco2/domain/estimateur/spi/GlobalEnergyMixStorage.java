package fr.ippon.iroco2.domain.estimateur.spi;

import fr.ippon.iroco2.domain.estimateur.model.energy_mix.GlobalEnergyMix;

import java.util.Optional;

public interface GlobalEnergyMixStorage {
    Optional<GlobalEnergyMix> findByIsoCode(String countryIsoCode);
}
