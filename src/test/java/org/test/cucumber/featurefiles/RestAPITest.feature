Feature: To Validate GetAllUsers API

  Scenario: Verify GetAllUsers API response
    Given User invokes the "GetAllUsers" API with "GET" request
    When user gets the success response
    Then validate that it has "2" pages
    And validate there are "6" users