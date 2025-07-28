package fr.ippon.iroco2.domain.estimateur.model.energy_mix;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public enum EnergyType {
    COAL(0.995),
    OIL(0.816),
    GAS(0.743),
    GEOTHERMAL(0.038),
    HYDROELECTRIC(0.026),
    NUCLEAR(0.029),
    SOLAR(0.048),
    WIND(0.026);

    private final double gOfCarbonByWh;

    public static List<EnergyType> listValues() {
        return List.of(values());
    }
}
