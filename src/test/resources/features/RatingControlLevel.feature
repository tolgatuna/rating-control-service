Feature: Rating Control
  Scenario: Rating Control level decision to read book
    Given I am customer who have set rating control level 12
    When I request to read equal level book B1234
    Then I get decision to read the book