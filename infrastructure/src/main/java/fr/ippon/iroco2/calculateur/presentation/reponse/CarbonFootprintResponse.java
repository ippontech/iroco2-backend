package fr.ippon.iroco2.calculateur.presentation.reponse;


import fr.ippon.iroco2.domain.calculateur.model.Component;

public record CarbonFootprintResponse(String componentName, String serviceName, int co2Gr) {

    public static CarbonFootprintResponse createFrom(Component component, Integer co2Gr) {
        return new CarbonFootprintResponse(component.getName(), component.getService().getName(), co2Gr);
    }
}
