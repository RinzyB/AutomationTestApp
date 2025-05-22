package org.test.api;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.is;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.test.util.Constants;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class RestAssuredTest {

	private static final Logger logger = LogManager.getLogger(RestAssuredTest.class);

	public RestAssuredTest() {
		RestAssured.baseURI = Constants.reqResAPI_baseURI;
	}

	@Test
	// GET request
	public void getAllUsers() {
		logger.debug("GET request : get all users..");
		String resp = given().header("Connection", "keep-alive").param("page", 2).when().get("/api/users") // sending
																											// GET
				.then().log().all().assertThat().statusCode(200).header("Server", "cloudflare")
				.body("total_pages", equalTo(2)).body("data[1].first_name", equalTo("Lindsay")).extract().response()
				.asString();
//		logger.debug("Response is: "+resp);

		JsonPath json = new JsonPath(resp);
		assertEquals(json.getInt("page"), 2);
		assertEquals(json.getString("data[0].email"), "michael.lawson@reqres.in");
		assertEquals(json.getInt("data.size()"), 6);
	}

	@Test
	@Parameters("StatusCode")
	public void getAllUsersStatusCheck(String statusCode) {
		Response resp = RestAssured.get("/api/users?page=2");

		String[] codesStr = statusCode.split(",");
		for (String code : codesStr) {
			if (resp.statusCode() == Integer.parseInt(code)) {
				assertTrue(resp.statusCode() == Integer.parseInt(code));
			}
		}
		
		// or can do it using the hamcrest Matchers
		given().when().get("/api/user")
        .then().statusCode(anyOf(
                    is(200), // Success
                    is(201), // Not Found
                    is(203)  // Server Error
                )
            );
	}

	@Test
	// POST request
	public void createUser() {
		logger.debug("POST request : Creating new user..");
		String resp = given().header("Content-Type", "application/json").header("x-api-key", "reqres-free-v1")
				.body("{\n" + "    \"name\": \"morpheus\",\n" + "    \"job\": \"leader\"\n" + "}").when()
				.post("/api/users") // sending POST
				.then().log().all().assertThat().statusCode(201).header("Server", "cloudflare").extract().response()
				.asString();
		JsonPath jsonResp = new JsonPath(resp);
		assertEquals(jsonResp.getString("name"), "morpheus");
	}

	@Test
	// GET request
	public void getAllUsersUsingPOJOs() {
		logger.debug("GET request : get all users using POJO..");
		UserDetailsPOJO resp = given().header("Connection", "keep-alive").param("page", 2).when().get("/api/users") // sending
																													// GET
				.then().log().all().assertThat().statusCode(200).header("Server", "cloudflare").extract().response()
				.as(UserDetailsPOJO.class);

		assertEquals(resp.getPage(), 2);
		assertEquals(resp.getData().get(1).getFirst_name(), "Lindsay");
		assertEquals(resp.getData().size(), 6);
		assertEquals(resp.getData().get(0).getEmail(), "michael.lawson@reqres.in");

	}

	@Test
	// POST request
	public void createUserUsingPOJOs() {
		logger.debug("POST request : Creating new user using POJO..");
		UserPOJO newUser = new UserPOJO();
		newUser.setName("AutoTest");
		newUser.setJob("TestLead");

		UserPOJO respPojo = given().header("Content-Type", "application/json").header("x-api-key", "reqres-free-v1")
				.body(newUser).when().post("/api/users") // sending POST
				.then().log().all().assertThat().statusCode(201).header("Server", "cloudflare").extract().response()
				.as(UserPOJO.class);

		assertEquals(respPojo.getName(), "AutoTest");
	}

}
