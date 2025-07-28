Feature: Update a component

  Scenario: Update the component's name
    Given "Charles" is logged in as a member
    And "Charles" has the component on this infrastructure with the ID "0ccf663c-19a4-4a34-8b4b-9dba60725b42"
    When "Charles" changes the name of the component to "New component name"
    Then "Charles" sees that "the update" was successful
    And the component has now the name "New component name"

  Scenario: Update the component's name when logged user is not owner
    Given "Bob" is logged in as a member
    And "Charles" has an infrastructure with the following details:
      | name          | cloudServiceProvider                 | defaultCSPRegion                     | id                                   |
      | Mon Infra AWS | a4f9e914-4a9c-4551-9717-66359a9298df | 123e4567-e89b-12d3-a456-426614174001 | 123e4567-e89b-12d3-a456-426614174002 |
    And "Charles" has the component on this infrastructure with the ID "0ccf663c-19a4-4a34-8b4b-9dba60725b42"
    When "Bob" changes the name of the component to "New component name"
    Then "Bob" is informed that it is not allowed the infrastructure with the ID "0ccf663c-19a4-4a34-8b4b-9dba60725b42"

  Scenario: Update the component's region
    Given "Charles" is logged in as a member
    And "Charles" has the component on this infrastructure with the ID "0ccf663c-19a4-4a34-8b4b-9dba60725b42"
    When "Charles" changes the region of the component to the region with ID "027311d5-9892-41f1-9ad1-8e45e6f0d374"
    Then "Charles" sees that "the update" was successful
    And the component has now the region "US East (N. Virginia)"

  Scenario: Update an non-existent component
    Given "Charles" is logged in as a member
    When "Charles" tries to change the name of a non-existent component to "New component name"
    Then "Charles" receives a client error
