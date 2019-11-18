@SearchContact

Feature: Search for a contact
  As an admin user
  I want to be able to search for a contact being backed up recently
  In order to use app functionality

  Background: User logs in
    Given I am on the Login page
    When I sign in as "a.shepel81@gmail.com" with "Qwerty123"
    Then I should be on the Dashboard page

  Scenario Outline: Successful search for a contact
    Given A contact is backed up with parameters:
    | name     | Yosemite Sam                  |
    | email    | aqa_test@ukr.net              |
    | phone    | +1 333 444                    |
    | update   | 11/4/19 5:00 PM               |
    | address  | 200 E Main St. Phoenix USA    |
    | category | Contact                       |
    | calendar | 11/4/19 5:00 PM               |
    | account  | aqa_test@upts.onmicrosoft.com |
    When I enter a "<searchTag>" in the Search field and hit Enter
    Then I should be on the Search page
    And I should see the contact info displayed in a search results
    And Contact info should contain a "<searchTag>"
    When I click on the search result
    Then An additional info is displayed
    When I click on view file button
    Then A detailed view info is displayed
    Examples:
      | searchTag    |
      | Sam		     |
      | Sam Phoenix  |
      | Yosemite Sam |