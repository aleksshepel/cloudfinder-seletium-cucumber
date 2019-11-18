@RestoreContact
Feature: Restore a contact
  As an admin user
  I want to be able to restore a contact
  In order to use app functionality

  Background: User logs in
    Given I am on the Login page
    When I sign in as "a.shepel81@gmail.com" with "Qwerty123"
    Then I should be on the Dashboard page
  @Ignore
  Scenario:
    Given A contact is backed up with parameters:
      | name     | Yosemite Sam                  |
      | email    | aqa_test@ukr.net              |
      | phone    | +1 333 444                    |
      | update   | 11/4/19 5:00 PM               |
      | address  | 200 E Main St. Phoenix USA    |
      | category | Contact                       |
      | calendar | 11/4/19 5:00 PM               |
      | account  | aqa_test@upts.onmicrosoft.com |
    When I enter a "Yosemite Sam" in the Search field and hit Enter
    Then I should be on the Search page
    And I should see the contact info displayed in a search results
    And Contact info should contain a "Yosemite Sam"
    When I select the search result by checking the box left to it
    And select "Restore" in Action above the search results
    And select "aqa_test@upts.onmicrosoft.com" in the Restore to user combobox
    And click Proceed button
    And confirm restoring
    | upts.onmicrosoft.com |
    Then The restore task should be scheduled and will be processed after some time
    When I click on Reports tab
    Then I should be on the Reports page
    And I should be able to track progress of the restore on the page by account
    | aqa_test@upts.onmicrosoft.com|