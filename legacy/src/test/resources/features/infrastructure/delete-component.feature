Feature: delete component

  Scenario: Delete an existing component
    Given "Charles" is logged in as a member
    When "Charles" delete the component with id "0ccf663c-19a4-4a34-8b4b-9dba60725b42"
    Then "Charles" does not see the component anymore

  Scenario: Delete an non-existent component
    Given "Charles" is logged in as a member
    When "Charles" delete the component with id "0d9a251d-c60a-44bb-af32-da01a9e9dd61"
    Then "Charles" receives a client error
