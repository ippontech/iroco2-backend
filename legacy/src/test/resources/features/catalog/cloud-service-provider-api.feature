Feature: Cloud Service Provider API

  Scenario: Retrieve all Cloud Service Providers
    Given "Bob" is logged in as a member
    When "Bob" requests all Cloud Service Providers
    Then "Bob" should receive a list of Cloud Service Providers

  Scenario: Retrieve all Regions for a Cloud Service Provider
    Given "Bob" is logged in as a member
    When "Bob" requests all Regions for the Cloud Service Provider with ID "a4f9e914-4a9c-4551-9717-66359a9298df"
    Then "Bob" should receive a list of Regions for that Cloud Service Provider

  Scenario: Retrieve all Services for a Cloud Service Provider
    Given "Bob" is logged in as a member
    When "Bob" requests all Services for the Cloud Service Provider with ID "a4f9e914-4a9c-4551-9717-66359a9298df"
    Then "Bob" should receive a list of Services for that Cloud Service Provider