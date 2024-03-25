Feature: Register

  Scenario Outline: Register a new supporter account
    Given I am on the registration page
    When  I fill the date of birth field and email field
    And   I fill the registration form with "<firstname>", "<lastname>", "<password>" and "<confirmpassword>"
    And   I "<check>" the consent boxes
    Then  I should have successfully registered
    Examples:
      | firstname | lastname | password  | confirmpassword | check |
      | Stacey    | Jackson  | pass1234  | pass1234        | true  |
      | Bridget   |          | pass12345 | pass12345       | true  |
      | James     | Hudson   | pass4321  | bass4321        | true  |
      | Nicole    | Peterson | pass54321 | pass54321       | false |
