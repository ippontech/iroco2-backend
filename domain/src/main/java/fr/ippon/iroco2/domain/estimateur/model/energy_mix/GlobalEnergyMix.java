package fr.ippon.iroco2.domain.estimateur.model.energy_mix;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class GlobalEnergyMix {
    private BigDecimal biofuelTWh;
    private BigDecimal carbonIntensity;
    private BigDecimal coalTWh;
    private String countryName;
    private BigDecimal fossilTWh;
    private BigDecimal gasTWh;
    private BigDecimal hydroelectricityTWh;
    private String isoCode;
    private BigDecimal lowCarbonTWh;
    private BigDecimal nuclearTWh;
    private BigDecimal oilTWh;
    private BigDecimal otherRenewableTWh;
    private BigDecimal otherRenewableExcBiofuelTWh;
    private BigDecimal perCapitaWh;
    private BigDecimal renewablesTWh;
    private BigDecimal solarTWh;
    private BigDecimal totalTWh;
    private BigDecimal windTWh;
    private int year;

    public double carbonGramByWhWeightedMix() {
        return EnergyType.listValues().stream().mapToDouble(this::carbonGramByWhWeight).sum();
    }

    private double carbonGramByWhWeight(EnergyType energyType) {
        return carbonGramByWhPercentage(energyType) * energyType.getGOfCarbonByWh();
    }

    private double carbonGramByWhPercentage(EnergyType energyType) {
        BigDecimal energyTWh = switch (energyType) {
            case EnergyType.GAS -> gasTWh;
            case EnergyType.OIL -> oilTWh;
            case EnergyType.COAL -> coalTWh;
            case EnergyType.WIND -> windTWh;
            case EnergyType.SOLAR -> solarTWh;
            case EnergyType.NUCLEAR -> nuclearTWh;
            // La géothermie n'est pas renseignée ici mais ça reste la majeure partie du "reste" des énergies renouvelable
            case EnergyType.GEOTHERMAL -> otherRenewableExcBiofuelTWh;
            case EnergyType.HYDROELECTRIC -> hydroelectricityTWh;
        };

        return energyTWh.doubleValue() / totalTWh.doubleValue();
    }
}
