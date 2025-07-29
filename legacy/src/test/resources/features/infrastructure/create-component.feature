Feature: Create a new Component

  Scenario: Successfully create a component with valid data
    Given "Bob" is logged in as a member
    And "Bob" has an infrastructure with the following details:
      | name          | cloudServiceProvider                 | defaultCSPRegion                     |
      | Mon Infra AWS | a4f9e914-4a9c-4551-9717-66359a9298df | 123e4567-e89b-12d3-a456-426614174001 |
    When "Bob" creates the component for the service of id "089a0a6c-bdd5-42f1-a683-ea885711fb81" with the following configuration:
      | name                | "MyComponent"                                                                                                                            |
      | configurationValues | [{ "id": "3580bd2a-b351-4964-9fc1-582e48be0c85", "value": "t3a.nano" }, { "id": "0d281333-7ad6-4920-bb74-3c9f75c9b16d", "value": "5" } ] |
    Then "Bob" sees that "the component" was created
    And the component is saved

  Scenario: Create a component with valid data form wrong infrastructure
    Given "Bob" is logged in as a member
    When "Bob" creates the component for the service of id "089a0a6c-bdd5-42f1-a683-ea885711fb81" with the following configuration:
      | name                | "MyComponent"                                                                                                                            |
      | configurationValues | [{ "id": "423e4567-e89b-12d3-a456-426614174003", "value": "t3a.nano" }, { "id": "0d281333-7ad6-4920-bb74-3c9f75c9b16d", "value": "5" } ] |
    Then "Bob" is informed that he performed a bad request
    And the component is not saved

  Scenario: Create a component with valid data while unIdentified
    Given "Bob" is not a member
    And "Bob" has an infrastructure with the following details:
      | name          | cloudServiceProvider                 | defaultCSPRegion                     | id                                   |
      | Mon Infra AWS | a4f9e914-4a9c-4551-9717-66359a9298df | 123e4567-e89b-12d3-a456-426614174001 | 123e4567-e89b-12d3-a456-426614174002 |
    When "Bob" creates the component for the service of id "089a0a6c-bdd5-42f1-a683-ea885711fb81" with the following configuration:
      | name                | "MyComponent"                                                                                                                            |
      | configurationValues | [{ "id": "423e4567-e89b-12d3-a456-426614174003", "value": "t3a.nano" }, { "id": "0d281333-7ad6-4920-bb74-3c9f75c9b16d", "value": "5" } ] |
    Then "Allie" is informed that the permission to "create the component" is not granted
    And the component is not saved

  Scenario: Successfully create a component with invalid data
    Given "Bob" is logged in as a member
    And "Bob" has an infrastructure with the following details:
      | name          | cloudServiceProvider                 | defaultCSPRegion                     | id                                   |
      | Mon Infra AWS | a4f9e914-4a9c-4551-9717-66359a9298df | 123e4567-e89b-12d3-a456-426614174001 | 123e4567-e89b-12d3-a456-426614174002 |
    When "Bob" creates the component for the service of id "089a0a6c-bdd5-42f1-a683-ea885711fb82" with the following configuration:
      | name                | "MyComponent"                                                                                                                            |
      | configurationValues | [{ "id": "423e4567-e89b-12d3-a456-426614174003", "value": "t3a.nano" }, { "id": "0d281333-7ad6-4920-bb74-3c9f75c9b16d", "value": "5" } ] |
    Then "Bob" is informed that there are missing ressources
    And the component is not saved

  Scenario: Create two components with same configurationValues id
    Given "Bob" is logged in as a member
    And "Bob" has an infrastructure with the following details:
      | name          | cloudServiceProvider                 | defaultCSPRegion                     | id                                   |
      | Mon Infra AWS | a4f9e914-4a9c-4551-9717-66359a9298df | 123e4567-e89b-12d3-a456-426614174001 | 123e4567-e89b-12d3-a456-426614174002 |
    When "Bob" creates the component for the service of id "089a0a6c-bdd5-42f1-a683-ea885711fb82" with the following configuration:
      | name                | "MyComponent1"                                                                                                                           |
      | configurationValues | [{ "id": "3580bd2a-b351-4964-9fc1-582e48be0c85", "value": "t3a.nano" }, { "id": "0d281333-7ad6-4920-bb74-3c9f75c9b16d", "value": "5" } ] |
    And "Bob" creates the component for the service of id "af60b3b7-dd0f-45e6-8818-0b44433bcb23" with the following configuration:
      | name                | "MyComponent2"                                                                                                                            |
      | configurationValues | [{ "id": "3580bd2a-b351-4964-9fc1-582e48be0c85", "value": "r7i.large" }, { "id": "0d281333-7ad6-4920-bb74-3c9f75c9b16d", "value": "2" } ] |
    Then "Bob" is informed that there are missing ressources
    And the component is not saved
