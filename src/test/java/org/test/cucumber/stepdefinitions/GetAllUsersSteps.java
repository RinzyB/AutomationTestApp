package org.test.cucumber.stepdefinitions;

import static org.testng.Assert.assertEquals;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.test.api.UserDetailsPOJO;
import org.test.util.Constants;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;

public class GetAllUsersSteps {
	private static final Logger logger = LogManager.getLogger(GetAllUsersSteps.class);
	Response response;
	UserDetailsPOJO pojo;

	@Given("User invokes the {string} API with {string} request")
	public void user_invokes_the_api_with_request(String string, String string2) {
		// Write code here that turns the phrase above into concrete actions
		RestAssured.baseURI = Constants.reqResAPI_baseURI;
		response = RestAssured.get("/api/users?page=2");
		pojo = response.as(UserDetailsPOJO.class);
		logger.debug("Triggered GetAPI...");
	}

	@When("user gets the success response")
	public void user_gets_the_success_response() {
		logger.debug("Status code is: " + response.getStatusCode());
		assertEquals(response.getStatusCode(), 200);
	}

	@Then("validate that it has {string} pages")
	public void validate_that_it_has_pages(String pages) {
		logger.debug("Pge No. is is:" + pojo.getPage());
		assertEquals(pojo.getPage(), Integer.parseInt(pages));
	}

	@Then("validate there are {string} users")
	public void validate_there_are_users(String users) {
		logger.debug("No. of users is: " + pojo.getData().size());
		assertEquals(pojo.getData().size(), Integer.parseInt(users));
	}
}