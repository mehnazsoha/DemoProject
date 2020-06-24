package files;

import static io.restassured.RestAssured.given;

import java.io.File;

import org.testng.Assert;

import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.path.json.JsonPath;

public class JiraTest {

	public static void main(String[] args) {
		RestAssured.baseURI = "http://localhost:8082/";
		SessionFilter sessionFilter = new SessionFilter(); // Similar to the jsonpath but this is a shortcut method
		String expectedMessage = "Hi how are you?";

		// Login into JIRA
		String response = given().log().all().header("Content-Type", "application/json")
				.body("{\r\n" + "    \"username\": \"nsrtmhnz7\",\r\n" + "    \"password\": \"nmc.jira27_\"\r\n" + "}")
				.filter(sessionFilter).when().post("rest/auth/1/session").then().log().all().extract().response()
				.asString();

		// Add a comment to existing issue
		String addCommentResponse = given().pathParam("id", "10101").log().all()
				.header("Content-Type", "application/json")
				.body("{\r\n" + "    \"body\": \"" + expectedMessage + "\",\r\n" + "    \"visibility\": {\r\n"
						+ "        \"type\": \"role\",\r\n" + "        \"value\": \"Administrators\"\r\n" + "    }\r\n"
						+ "}")
				.filter(sessionFilter).when().post("rest/api/2/issue/{id}/comment").then().log().all().assertThat()
				.statusCode(201).extract().response().asString();
		JsonPath js = new JsonPath(addCommentResponse);
		String commentID = js.getString("id");

		// Add an attachment to existing issue
		given().header("X-Atlassian-Token", "no-check").header("Content-Type", "multipart/form-data")
				.filter(sessionFilter).pathParam("id", "10101").multiPart("file", new File("jira.txt")).when()
				.post("rest/api/2/issue/{id}/attachments").then().log().all().assertThat().statusCode(201);

		// Get issue
		String issueDetail = given().filter(sessionFilter).pathParam("key", "10101").queryParam("fields", "comment")
				.when().get("/rest/api/2/issue/{key}").then().log().all().extract().response().asString();
		JsonPath jsonPath = new JsonPath(issueDetail);
		int commentsCount = jsonPath.getInt("fields.comment.comments.size()");
		for (int i = 0; i < commentsCount; i++) {
			String commentIDIssue = jsonPath.get("fields.comment.comments[" + i + "].id").toString();
			if (commentIDIssue.equalsIgnoreCase(commentID)) {
				String message = jsonPath.get("fields.comment.comments[" + i + "].body").toString();
				System.out.println(message);
				Assert.assertEquals(message, expectedMessage);
			}
		}
	}
}
