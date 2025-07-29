Feature: create an infrastructure

  Scenario: Create a new infrastructure
    Given "Bob" is logged in as a member
    When "Bob" creates a new infrastructure with the following details:
      | name          | cloudServiceProvider                 | defaultCSPRegion                     |
      | Mon Infra AWS | a4f9e914-4a9c-4551-9717-66359a9298df | 123e4567-e89b-12d3-a456-426614174001 |
    Then The infrastructure is created for user "Bob"

  Scenario: Attempt to create an infrastructure by a non-member user
    Given "Allie" is not a member
    When "Allie" creates a new infrastructure with the following details:
      | name          | cloudServiceProvider                 | defaultCSPRegion                     |
      | Mon Infra AWS | a4f9e914-4a9c-4551-9717-66359a9298df | 123e4567-e89b-12d3-a456-426614174001 |
    Then The infrastructure is not created
    And "Allie" is informed that the permission to "create the infrastructure" is not granted

  Scenario: Create an infrastructure with invalid data
    Given "Bob" is logged in as a member
    When "Bob" creates a new infrastructure with the following details:
      | name | cloudServiceProvider                 | defaultCSPRegion                     |
      |      |                                      |                                      |
      | A    | a4f9e914-4a9c-4551-9717-66359a9298df | 123e4567-e89b-12d3-a456-426614174001 |
    Then The infrastructure is not created
    Then "Bob" is informed that he performed a bad request