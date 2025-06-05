Selenium UI Test Automation Framework
-----------------------------------
his project serves as a comprehensive example and starting point for building a robust Selenium UI automation framework using Java, Maven, TestNG, and a variety of best practices and integrations. It demonstrates how to automate interactions with different UI elements, manage test data, perform database operations, and integrate with external tools for API testing, BDD, and reporting. It also includes configurations for RemoteWebDriver testing on cloud platforms like LambdaTest and Selenium Grid.

Features:
---------
Selenium WebDriver - Core for browser automation.
Maven - Project management and build automation.
TestNG - Test framework for powerful test organization, parallel execution, and data-driven testing.
Locators - Demonstrates effective use of various Selenium locators (ID, Name, ClassName, XPath, CSS Selector, LinkText, PartialLinkText, TagName).
WebDriver Actions - Examples of interacting with common UI elements (click, type, clear, select dropdowns, drag & drop, hover, keyboard actions).
Page Object Model (POM) - Structured approach for UI element organization and test maintenance.
Data Providers - TestNG's built-in mechanism for passing different sets of data to test methods.
MySQL Database Connectivity - Examples of connecting to a MySQL database to read/write test data or verify application state.
Apache POI - Reading and writing data from/to Excel spreadsheets.
Cross-Browser Testing - Configurable setup to run tests across different browsers (Chrome, Firefox, Edge, Safari).
Parallel Testing - Implementation of parallel test execution using TestNG and ThreadLocal for WebDriver instance management.
Remote WebDriver - Support for running tests on remote machines, including cloud platforms (LambdaTest) and local Selenium Grid.
Cucumber & Gherkin - Behavior-Driven Development (BDD) for writing human-readable test scenarios.
RestAssured - Examples of integrating API tests alongside UI tests, potentially for setting up test data or verifying backend operations.
Allure Reporting - Comprehensive and interactive test reporting.
Log4j - Flexible logging framework for detailed test execution logs.

Prerequisites:
--------------
- Java 8 or higher
- Maven
- An IDE (e.g., IntelliJ IDEA, Eclipse)
- MySQL

Build the project:
------------------
> mvn clean install

Run the Tests: (update the [testng.xml] file as per the tests/classes to be run)
--------------
mvn test
