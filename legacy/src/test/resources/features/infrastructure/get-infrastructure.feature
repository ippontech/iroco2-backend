Feature: get infrastructure(s)

  Scenario: Access an infrastructure
    Given "David" is logged in as a member
    And "David" has an infrastructure with the following details:
      | name          | cloudServiceProvider                 | defaultCSPRegion                     | id                                   |
      | Mon Infra AWS | a4f9e914-4a9c-4551-9717-66359a9298df | 123e4567-e89b-12d3-a456-426614174001 | 123e4567-e89b-12d3-a456-426614174002 |
    When "David" accesses the previously created infrastructure
    Then "David" sees the id of the infrastructure

  Scenario: Attempt to access an infrastructure by a non-member user
    Given "Vampire" is not a member
    And "David" has an infrastructure with the following details:
      | name          | cloudServiceProvider                 | defaultCSPRegion                     | id                                   |
      | Mon Infra AWS | a4f9e914-4a9c-4551-9717-66359a9298df | 123e4567-e89b-12d3-a456-426614174001 | 123e4567-e89b-12d3-a456-426614174002 |
    When "Vampire" accesses the previously created infrastructure
    Then "Vampire" is informed that the permission to "get the infrastructure" is not granted

  Scenario: Attempt to access an infrastructure by the wrong user
    Given "Vampire" is logged in as a member
    And "David" has an infrastructure with the following details:
      | name           | cloudServiceProvider                 | defaultCSPRegion                     | id                                   |
      | Mon Infra AWS  | a4f9e914-4a9c-4551-9717-66359a9298df | 123e4567-e89b-12d3-a456-426614174001 | 123e4567-e89b-12d3-a456-426614174002 |
      | Mon Infra AWS2 | a4f9e914-4a9c-4551-9717-66359a9298df | 123e4567-e89b-12d3-a456-426614174001 | 123e4567-e89b-12d3-a456-426614174003 |
    When "Vampire" accesses the previously created infrastructure
    Then "Vampire" is informed that the permission to "get the infrastructure" is not granted

  Scenario: Access all infrastructures
    Given "David" is logged in as a member
    And "David" has an infrastructure with the following details:
      | name           | cloudServiceProvider                 | defaultCSPRegion                     | id                                   |
      | Mon Infra AWS  | a4f9e914-4a9c-4551-9717-66359a9298df | 123e4567-e89b-12d3-a456-426614174001 | 123e4567-e89b-12d3-a456-426614174002 |
      | Mon Infra AWS2 | a4f9e914-4a9c-4551-9717-66359a9298df | 123e4567-e89b-12d3-a456-426614174001 | 123e4567-e89b-12d3-a456-426614174003 |
    When "David" accesses all infrastructures
    Then "David" sees 2 infrastructures

  Scenario: Access all infrastructures by a non-member
    Given "Vampire" is not a member
    When "Vampire" accesses all infrastructures
    Then "Vampire" is informed that the permission to "see all his infrastructures" is not granted

  Scenario: Access all infrastructures and should only see his own
    Given "Vampire" is logged in as a member
    And "David" has an infrastructure with the following details:
      | name          | cloudServiceProvider                 | defaultCSPRegion                     | id                                   |
      | Mon Infra AWS | a4f9e914-4a9c-4551-9717-66359a9298df | 123e4567-e89b-12d3-a456-426614174001 | 123e4567-e89b-12d3-a456-426614174002 |
    When "Vampire" accesses all infrastructures
    Then "Vampire" sees 0 infrastructures

