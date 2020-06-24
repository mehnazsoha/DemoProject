import static io.restassured.RestAssured.given;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.testng.annotations.Test;

import files.ReUsableMethods;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

public class StaticJson {
	@Test
	public void addBook() throws IOException {
		RestAssured.baseURI = "http://216.10.245.166";
		String response = given().header("Content-Type", "application/json")
				.body(GenerateStringFromResource("C:\\Users\\User\\eclipse-workspace\\BookData.json")).when()
				.post("/Library/Addbook.php").then().assertThat().statusCode(200).extract().response().asString();
		JsonPath jPath = ReUsableMethods.rawToJson(response);
		String id = jPath.getString("ID");
		System.out.println("The book id is " + id);
	}

	public static String GenerateStringFromResource(String path) throws IOException {
		return new String(Files.readAllBytes((Paths.get(path))));
	}
}
