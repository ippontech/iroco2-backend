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
package fr.ippon.iroco2.domain.calculator.model.emu;

import fr.ippon.iroco2.domain.calculator.exception.AWSDataCenterNotFoundException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Getter
@RequiredArgsConstructor
public enum AWSDataCenter {
    US_EAST_N_VIRGINIA("US East (N. Virginia)", Region.NORTH_AMERICA, "us-east-1", "USA"), U_EAST_OHIO("US East (Ohio)", Region.NORTH_AMERICA, "us-east-2", "USA"), U_WEST_N_CALIFORNIA("US West (N. California)", Region.NORTH_AMERICA, "us-west-1", "USA"), U_WEST_OREGON("US West (Oregon)", Region.NORTH_AMERICA, "us-west-2", "USA"), CANADA_CENTRAL("Canada (Central)", Region.NORTH_AMERICA, "ca-central-1", "CAN"), CANADA_WEST_CALGARY("Canada West (Calgary)", Region.NORTH_AMERICA, "ca-west-1", "CAN"),

    AFRICA_CAPE_TOWN("Africa (Cape Town)", Region.AFRICA, "af-south-1", "ZAF"),

    ASIA_PACIFIC_HONG_KONG("Asia Pacific (Hong Kong)", Region.ASIA, "ap-east-1", "HKG"), ASIA_PACIFIC_HYDERABAD("Asia Pacific (Hyderabad)", Region.ASIA, "ap-south-2", "IND"), ASIA_PACIFIC_JAKARTA("Asia Pacific (Jakarta)", Region.ASIA, "ap-southeast-3", "IDN"), ASIA_PACIFIC_MELBOURNE("Asia Pacific (Melbourne)", Region.ASIA, "ap-southeast-4", "AUS"), ASIA_PACIFIC_MUMBAI("Asia Pacific (Mumbai)", Region.ASIA, "ap-south-1", "IND"), ASIA_PACIFIC_OSAKA("Asia Pacific (Osaka)", Region.ASIA, "ap-northeast-3", "JPN"), ASIA_PACIFIC_SEOUL("Asia Pacific (Seoul)", Region.ASIA, "ap-northeast-2", "KOR"), ASIA_PACIFIC_SINGAPORE("Asia Pacific (Singapore)", Region.ASIA, "ap-southeast-1", "SGP"), ASIA_PACIFIC_SYDNEY("Asia Pacific (Sydney)", Region.ASIA, "ap-southeast-2", "AUS"), ASIA_PACIFIC_TOKYO("Asia Pacific (Tokyo)", Region.ASIA, "ap-northeast-1", "JPN"),

    EUROPE_FRANKFURT("Europe (Frankfurt)", Region.EUROPE, "eu-central-1", "DEU"), EUROPE_IRELAND("Europe (Ireland)", Region.EUROPE, "eu-west-1", "IRL"), EUROPE_LONDON("Europe (London)", Region.EUROPE, "eu-west-2", "GBR"), EUROPE_MILAN("Europe (Milan)", Region.EUROPE, "eu-south-1", "ITA"), EUROPE_PARIS("Europe (Paris)", Region.EUROPE, "eu-west-3", "FRA"), EUROPE_SPAIN("Europe (Spain)", Region.EUROPE, "eu-south-2", "ESP"), EUROPE_STOCKHOLM("Europe (Stockholm)", Region.EUROPE, "eu-north-1", "SWE"), EUROPE_ZURICH("Europe (Zurich)", Region.EUROPE, "eu-central-1", "CHE"),

    ISRAEL_TEL_AVIV("Israel (Tel Aviv)", Region.ISRAEL, "il-central-1", "ISR"),

    MIDDLE_EAST_BAHRAIN("Middle East (Bahrain)", Region.MIDDLE_EAST, "me-south-1", "BHR"), MIDDLE_EAST_UAE("Middle East (UAE)", Region.MIDDLE_EAST, "me-central-1", "ARE"),

    SOUTH_AMERICA_SAO_PAULO("South America (Sao Paulo)", Region.SOUTH_AMERICA, "sa-east-1", "BRA");

    private final String name;
    private final Region region;
    private final String abbreviation;
    private final String associatedCountryIsoCode;

    public static AWSDataCenter findByName(String name) {
        return findOptionalByName(name).orElseThrow(() -> new AWSDataCenterNotFoundException(name, Arrays.asList(values())));
    }

    public static Optional<AWSDataCenter> findOptionalByName(String name) {
        for (AWSDataCenter awsDataCenter : values()) {
            if (awsDataCenter.name.equals(name)) {
                return Optional.of(awsDataCenter);
            }
        }
        return Optional.empty();
    }

    public static List<AWSDataCenter> findByRegion(Region region) {
        List<AWSDataCenter> awsDataCenterList = new ArrayList<>();
        for (AWSDataCenter awsDataCenter : values()) {
            if (awsDataCenter.region == region) {
                awsDataCenterList.add(awsDataCenter);
            }
        }
        return awsDataCenterList;
    }

    public static AWSDataCenter fromValue(String awsDataCenter) {
        for (AWSDataCenter status : AWSDataCenter.values()) {
            if (status.name().equalsIgnoreCase(awsDataCenter) || status.getAbbreviation().equalsIgnoreCase(awsDataCenter)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid aws data center: " + awsDataCenter);
    }
}
