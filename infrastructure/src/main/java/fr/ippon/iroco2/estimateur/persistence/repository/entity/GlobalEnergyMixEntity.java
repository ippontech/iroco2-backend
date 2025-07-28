package fr.ippon.iroco2.estimateur.persistence.repository.entity;

import fr.ippon.iroco2.domain.estimateur.model.energy_mix.GlobalEnergyMix;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor

@Entity
@Table(name = "GLOBALENERGYMIX")
public class GlobalEnergyMixEntity {

    @Id
    @Column(name = "Id")
    private int id;

    @Column(name = "Biofuel_TWh")
    private BigDecimal biofuelTWh = BigDecimal.ZERO;

    @Column(name = "Carbon_intensity")
    private BigDecimal carbonIntensity = BigDecimal.ZERO;

    @Column(name = "Coal_TWh")
    private BigDecimal coalTWh = BigDecimal.ZERO;

    @Column(name = "Country_name")
    private String countryName;

    @Column(name = "Fossil_TWh")
    private BigDecimal fossilTWh = BigDecimal.ZERO;

    @Column(name = "Gas_TWh")
    private BigDecimal gasTWh = BigDecimal.ZERO;

    @Column(name = "Hydroelectricity_TWh")
    private BigDecimal hydroelectricityTWh = BigDecimal.ZERO;

    @Column(name = "Iso_code")
    private String isoCode;

    @Column(name = "Low_carbon_TWh")
    private BigDecimal lowCarbonTWh = BigDecimal.ZERO;

    @Column(name = "Nuclear_TWh")
    private BigDecimal nuclearTWh = BigDecimal.ZERO;

    @Column(name = "Oil_TWh")
    private BigDecimal oilTWh = BigDecimal.ZERO;

    @Column(name = "Other_renewable_TWh")
    private BigDecimal otherRenewableTWh = BigDecimal.ZERO;

    @Column(name = "Other_renewable_exc_biofuel_TWh")
    private BigDecimal otherRenewableExcBiofuelTWh = BigDecimal.ZERO;

    @Column(name = "Per_capita_Wh")
    private BigDecimal perCapitaWh = BigDecimal.ZERO;

    @Column(name = "Renewables_TWh")
    private BigDecimal renewablesTWh = BigDecimal.ZERO;

    @Column(name = "Solar_TWh")
    private BigDecimal solarTWh = BigDecimal.ZERO;

    @Column(name = "Total_TWh")
    private BigDecimal totalTWh = BigDecimal.ZERO;

    @Column(name = "Wind_TWh")
    private BigDecimal windTWh = BigDecimal.ZERO;

    @Column(name = "Data_Year")
    private int dataYear;

    public GlobalEnergyMix toDomain() {
        GlobalEnergyMix globalEnergyMix = new GlobalEnergyMix();
        globalEnergyMix.setBiofuelTWh(getBiofuelTWh());
        globalEnergyMix.setCarbonIntensity(getCarbonIntensity());
        globalEnergyMix.setCoalTWh(getCoalTWh());
        globalEnergyMix.setCountryName(getCountryName());
        globalEnergyMix.setFossilTWh(getFossilTWh());
        globalEnergyMix.setGasTWh(getGasTWh());
        globalEnergyMix.setHydroelectricityTWh(getHydroelectricityTWh());
        globalEnergyMix.setIsoCode(getIsoCode());
        globalEnergyMix.setLowCarbonTWh(getLowCarbonTWh());
        globalEnergyMix.setNuclearTWh(getNuclearTWh());
        globalEnergyMix.setOilTWh(getOilTWh());
        globalEnergyMix.setOtherRenewableTWh(getOtherRenewableTWh());
        globalEnergyMix.setOtherRenewableExcBiofuelTWh(getOtherRenewableExcBiofuelTWh());
        globalEnergyMix.setPerCapitaWh(getPerCapitaWh());
        globalEnergyMix.setRenewablesTWh(getRenewablesTWh());
        globalEnergyMix.setSolarTWh(getSolarTWh());
        globalEnergyMix.setTotalTWh(getTotalTWh());
        globalEnergyMix.setWindTWh(getWindTWh());
        globalEnergyMix.setYear(getDataYear());
        return globalEnergyMix;
    }
}
