import org.testng.Assert;
import org.testng.annotations.Test;

import files.payload;
import io.restassured.path.json.JsonPath;

public class SumValidation {
	// Verify if Sum of all Course prices matches with Purchase Amount
	@Test // We don't need the main method as we have TestNG / JUnit Jar installed here.
	public void sumOfCourses() {
		JsonPath jsonPath = new JsonPath(payload.coursePrice());
		int count = jsonPath.getInt("courses.size()");
		int i, perAmount, totalAmount = 0;
		for (i = 0; i < count; i++) {
			int coursePrice = jsonPath.getInt("courses[" + i + "].price");
			int courseCopies = jsonPath.getInt("courses[" + i + "].copies");
			perAmount = coursePrice * courseCopies;
			totalAmount += perAmount;
		}
		System.out.println("The total amount is " + totalAmount);
		int purchaseAmount = jsonPath.getInt("dashboard.purchaseAmount");
//		if (purchaseAmount == totalAmount) {
//			System.out.println("Matched");
//		} else {
//			System.out.println("Not matched");
//		}
		// The above if else condition can be run in single assertion line.
		Assert.assertEquals(totalAmount, purchaseAmount);
	}
}
