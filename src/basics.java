import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import static io.restassured.RestAssured.*;		// This import is used for given(), when() and then() method.
import static org.hamcrest.Matchers.*;			// This import is used for equalTo() method.

import files.ReUsableMethods;
import files.payload;

public class basics {

	public static void main(String[] args) {
		// Add place -> Update place with new address -> Get place to validate if new address is present in response or not
		// Insert all value from postman AddPlace file
		
		// Add place
		// Validate if AddPlace API is working as expected
		RestAssured.baseURI = "https://rahulshettyacademy.com";
		// Add a string variable here to get the response as a string
		String response = given().log().all().queryParam("key", "qaclick123").header("Content-Type","application/json")
			.body(payload.AddPlace())
		.when().post("/maps/api/place/add/json")
		.then().assertThat().statusCode(200).body("scope", equalTo("APP"))
			.header("Server", "Apache/2.4.18 (Ubuntu)").extract().response().asString();
		// .log().all() is used for output. Only given() and then() can use it.
		System.out.println(response);
		JsonPath js = new JsonPath(response);	// Object of the class for parsing JSON
		String placeId = js.getString("place_id");
		System.out.println("The place id is "+placeId);
		
		// Update place
		String newAddress = "70 Summer Street, USA";
		given().log().all().queryParam("key", "qaclick123").header("Content-Type","application/json")
			.body("{\r\n" + 
					"    \"place_id\": \""+placeId+"\",\r\n" + 
					"    \"address\": \""+newAddress+"\",\r\n" + 
					"    \"key\": \"qaclick123\"\r\n" + 
					"}")
		.when().put("/maps/api/place/update/json")
		.then().log().all().assertThat().statusCode(200).body("msg", equalTo("Address successfully updated"));
		
		// Get place
		String getPlaceResponse = given().log().all().queryParam("key", "qaclick123").queryParam("place_id", placeId)
		.when().get("/maps/api/place/get/json")
		.then().log().all().assertThat().statusCode(200).extract().response().asString();
		JsonPath jsonPath = ReUsableMethods.rawToJson(getPlaceResponse);	// Updated***
		String actualAddress = jsonPath.getString("address");
		System.out.println(actualAddress);
		org.testng.Assert.assertEquals(actualAddress, newAddress);
		
	}
}
