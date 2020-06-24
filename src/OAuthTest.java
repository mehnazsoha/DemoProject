import static io.restassured.RestAssured.given;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.testng.Assert;

import io.restassured.parsing.Parser;
import io.restassured.path.json.JsonPath;
import pojo.Api;
import pojo.GetCourse;
import pojo.WebAutomation;

public class OAuthTest {
	public static void main(String[] args) throws InterruptedException {
		String[] courseTitles = { "Selenium Webdriver Java", "Cypress", "Protractor" };

//		// This commented code does not required anymore as google updated there security issue
//		// Get authentication code
//		System.setProperty("webdriver.chrome.driver", "C://chromedriver.exe");
//		WebDriver driver = new ChromeDriver();
//		driver.get(
//				"https://accounts.google.com/signin/oauth/identifier?scope=https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email&auth_url=https%3A%2F%2Faccounts.google.com%2Fo%2Foauth2%2Fv2%2Fauth&client_id=692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com&response_type=code&redirect_uri=https%3A%2F%2Frahulshettyacademy.com%2FgetCourse.php&status=verifyfjdss&o2v=2&as=hnvuqy2mpTXYRJfWhdKsFA&flowName=GeneralOAuthFlow");
//		driver.findElement(By.cssSelector("input[type='email']")).sendKeys("nsrtmhnz7");
//		// tagname[attribute='value']
//		driver.findElement(By.cssSelector("input[type='email']")).sendKeys(Keys.ENTER);
//		Thread.sleep(3000);
//		driver.findElement(By.cssSelector("input[type='password']")).sendKeys("xxxx");
//		driver.findElement(By.cssSelector("input[type='email']")).sendKeys(Keys.ENTER);
//		Thread.sleep(4000);
//		String url=driver.getCurrentUrl();

		// Get authentication code.
		// Get this url after login with username and password. Just copy that and paste
		// it here.
		String url = "https://rahulshettyacademy.com/getCourse.php?code=4%2F0gHfRc3AF8IX4gRfmPGIAydMibBmTQz21KrHyrxOrVfzUiY7O2n6biJYwm6NLQ7xnrUmIfSCfx3Sd6gzl1dlNa4&scope=email+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email+openid&authuser=0&prompt=none";
		String partialCode = url.split("code=")[1];
		String code = partialCode.split("&scope")[0];
		System.out.println(code);
		// Get authentication code

		// Get access token
		String accessTokenResponse = given().urlEncodingEnabled(false).queryParam("code", code)
				.queryParam("client_id", "692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")
				.queryParam("client_secret", "erZOWM9g3UtwNRj340YYaK_W")
				.queryParam("redirect_uri", "https://rahulshettyacademy.com/getCourse.php")
				.queryParam("grant_type", "authorization_code").when().log().all()
				.post("https://www.googleapis.com/oauth2/v4/token").asString();
		JsonPath jsonPath = new JsonPath(accessTokenResponse);
		String accessToken = jsonPath.getString("access_token");
		// Get access token

//		// To login with access token and grab the informations from the server
//		String response = given().queryParam("access_token", accessToken).when().log().all()
//				.get("https://rahulshettyacademy.com/getCourse.php").asString();
//		System.out.println(response);

		// Add pojo class here for deserialization
		GetCourse gc = given().queryParam("access_token", accessToken).expect().defaultParser(Parser.JSON).when()
				.get("https://rahulshettyacademy.com/getCourse.php").as(GetCourse.class);
		System.out.println(gc.getLinkedIn());
		System.out.println(gc.getInstructor());
		System.out.println(gc.getCourses().getApi().get(1).getCourseTitle());

		// Get the price of api course "SoapUI Webservices testing"
		List<Api> apiCourses = gc.getCourses().getApi();
		for (int i = 0; i < apiCourses.size(); i++) {
			if (apiCourses.get(i).getCourseTitle().equalsIgnoreCase("SoapUI Webservices testing")) {
				System.out.println(apiCourses.get(i).getPrice());
			}
		}
		// Get the price of api course "SoapUI Webservices testing"

		// Get the course name of web automation
		List<WebAutomation> automationCourses = gc.getCourses().getWebAutomation();
		for (int i = 0; i < automationCourses.size(); i++) {
			System.out.println(automationCourses.get(i).getCourseTitle());
		}
		// Get the course name of web automation

		// Compare the expected and actual course titles
		ArrayList<String> actualList = new ArrayList<String>();
		List<WebAutomation> automationCourses2 = gc.getCourses().getWebAutomation();
		for (int i = 0; i < automationCourses2.size(); i++) {
			actualList.add(automationCourses2.get(i).getCourseTitle());
		}

		// Change array into arraylist
		List<String> expectedList = Arrays.asList(courseTitles);
		// Change array into arraylist

		Assert.assertTrue(actualList.equals(expectedList));
		// Compare the expected and actual course titles

		// Add pojo class here for deserialization
	}
}
