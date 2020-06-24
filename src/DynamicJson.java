import static io.restassured.RestAssured.given;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import files.ReUsableMethods;
import files.payload;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

public class DynamicJson {
	// Add a book
	@Test(dataProvider = "BooksData")
	public void addBook(String isbn, String aisle) {
		RestAssured.baseURI = "http://216.10.245.166";
		String response = given().header("Content-Type", "application/json").body(payload.AddBook(isbn, aisle)).when()
				.post("/Library/Addbook.php").then().assertThat().statusCode(200).extract().response().asString();
		JsonPath jPath = ReUsableMethods.rawToJson(response);
		String id = jPath.getString("ID");
		System.out.println("The book id is " + id);
	}

	// Get a book
	@DataProvider(name = "BooksData")
	public Object[][] getBook() {
		return new Object[][] { { "adlf", "1857" }, { "afgj", "805" }, { "eyws", "82" } };
	}
}
