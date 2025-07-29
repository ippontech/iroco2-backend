Feature: delete infrastructure(s)

  Scenario: Delete an infrastructure
    Given "Charles" is logged in as a member
    And "Charles" has an infrastructure with the following details:
      | name          | cloudServiceProvider                 | defaultCSPRegion                     | id                                   |
      | Mon Infra AWS | a4f9e914-4a9c-4551-9717-66359a9298df | 123e4567-e89b-12d3-a456-426614174001 | 123e4567-e89b-12d3-a456-426614174002 |
    When "Charles" delete the previously created infrastructure
    Then "Charles" sees that "the deletion of the infrastructure" was successful
    And "Charles" doesn't see the infrastructure anymore

  Scenario: Delete a not existing infrastructure
    Given "Charles" is logged in as a member
    And "Charles" has an infrastructure with the following details:
      | name          | cloudServiceProvider                 | defaultCSPRegion                     | id                                   |
      | Mon Infra AWS | a4f9e914-4a9c-4551-9717-66359a9298df | 123e4567-e89b-12d3-a456-426614174001 | 123e4567-e89b-12d3-a456-426614174002 |
    And "Charles" delete the previously created infrastructure
    When "Charles" delete the previously created infrastructure
    Then "Charles" sees that "the infrastructure" was already deleted
    And "Charles" doesn't see the infrastructure anymore

  Scenario: Attempt to delete an infrastructure by a non-member user
    Given "Vampire" is not a member
    And "David" has an infrastructure with the following details:
      | name          | cloudServiceProvider                 | defaultCSPRegion                     | id                                   |
      | Mon Infra AWS | a4f9e914-4a9c-4551-9717-66359a9298df | 123e4567-e89b-12d3-a456-426614174001 | 123e4567-e89b-12d3-a456-426614174002 |
    When "Vampire" delete the previously created infrastructure
    Then "Vampire" is informed that the permission to "delete the infrastructure" is not granted

  Scenario: Attempt to delete an infrastructure by the wrong user
    Given "Vampire" is logged in as a member
    And "David" has an infrastructure with the following details:
      | name           | cloudServiceProvider                 | defaultCSPRegion                     | id                                   |
      | Mon Infra AWS  | a4f9e914-4a9c-4551-9717-66359a9298df | 123e4567-e89b-12d3-a456-426614174001 | 123e4567-e89b-12d3-a456-426614174002 |
      | Mon Infra AWS2 | a4f9e914-4a9c-4551-9717-66359a9298df | 123e4567-e89b-12d3-a456-426614174001 | 123e4567-e89b-12d3-a456-426614174003 |
    When "Vampire" delete the previously created infrastructure
    Then "Vampire" is informed that the permission to "get the infrastructure" is not granted