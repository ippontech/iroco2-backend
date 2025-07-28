Feature: Catalog API

  Scenario: Retrieve all services as anonymous user
    Given "Bob" is not a member
    When "Bob" requests all services from catalog
    Then "Bob" should receive a list of all the services of all the providers

  Scenario: Retrieve all services as authenticated user
    Given "Bob" is logged in as a member
    When "Bob" requests all services from catalog
    Then "Bob" should receive a list of all the services of all the providers

  Scenario: Get a service by ID as an anonymous user
    Given "Bob" is not a member
    When "Bob" requests the description of the service "089a0a6c-bdd5-42f1-a683-ea885711fb81"
    Then "Bob" should receive the description of the service "089a0a6c-bdd5-42f1-a683-ea885711fb81"

  Scenario: Get a service by ID as a authenticated user
    Given "Bob" is logged in as a member
    When "Bob" requests the description of the service "089a0a6c-bdd5-42f1-a683-ea885711fb81"
    Then "Bob" should receive the description of the service "089a0a6c-bdd5-42f1-a683-ea885711fb81"

  Scenario: Get a service that does not exist as an anonymous user
    Given "Bob" is logged in as a member
    When "Bob" requests the description of the service "089a0a6c-bdd5-42f1-a683-ea885711fb82"
    Then "Bob" is informed that the ressource does not exist
