package files;

import io.restassured.path.json.JsonPath;

public class ReUsableMethods {
	public static JsonPath rawToJson(String response) {
		JsonPath jsonPath = new JsonPath(response);
		return jsonPath;
	}
}

// Static used to directly access the method with the class name.
// class_name.method