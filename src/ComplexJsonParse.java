import files.payload;
import io.restassured.path.json.JsonPath;

public class ComplexJsonParse {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JsonPath jsonPath = new JsonPath(payload.coursePrice());

		// Print No of courses returned by API
		int count = jsonPath.getInt("courses.size()");
		System.out.println("Total course is " + count);

		// Print Purchase Amount
		int totalAmount = jsonPath.getInt("dashboard.purchaseAmount");
		System.out.println("Total amount is " + totalAmount);

		// Print Title of the first course
		String firstTitle = jsonPath.get("courses[0].title");
		System.out.println("First title is " + firstTitle);

		// Print All course titles and their respective Prices
		for (int i = 0; i < count; i++) {
			String courseName = jsonPath.getString("courses[" + i + "].title");
			int coursePrice = jsonPath.getInt("courses[" + i + "].price");
			System.out.println("Course name is " + courseName + " and price is " + coursePrice);
		}

		// Print no of copies sold by RPA Course
		for (int i = 0; i < count; i++) {
			String courseName = jsonPath.getString("courses[" + i + "].title");
			if (courseName.equalsIgnoreCase("RPA")) {
				int copiesOfRPA = jsonPath.getInt("courses[" + i + "].copies");
				System.out.println("No of copies of RPA is " + copiesOfRPA);
				break;
			}
		}
	}
}
