package fr.ippon.iroco2.domain.calculateur.exception;

import fr.ippon.iroco2.domain.calculateur.model.emu.AWSDataCenter;

import java.util.List;

public class AWSDataCenterNotFoundException extends RuntimeException {

    public AWSDataCenterNotFoundException(String awsDataCenterName, List<AWSDataCenter> availableAwsDataCenterList) {
        super("AWS data center %s does not belong to the list of available region %s".formatted(awsDataCenterName, availableAwsDataCenterList));
    }
}
