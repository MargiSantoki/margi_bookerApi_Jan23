package testscripts;

import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class DemoTest {

	@Test
	public void phontNumbersTypeTest() {
		RestAssured.baseURI = "https://0e686aed-6e36-4047-bcb4-a2417455c2d7.mock.pstmn.io";
		Response res = RestAssured.given()
					.headers("Accept","application/json")
					.when().get("/test");
		
		List<String> listOfType = res.jsonPath().getList("phoneNumbers.type");
		System.out.println(listOfType);
	}
	
	@Test
	public void phontNumbersTest() {
		RestAssured.baseURI = "https://0e686aed-6e36-4047-bcb4-a2417455c2d7.mock.pstmn.io";
		Response res = RestAssured.given()
					.headers("Accept","application/json")
					.when().get("/test");
		
		List<Object> listOfPhoneNumbers = res.jsonPath().getList("phoneNumbers");
		
		for(Object obj : listOfPhoneNumbers) {
			Map<String,String> mapOfPhoneNumbers = (Map<String,String>) listOfPhoneNumbers.get(0);
//			System.out.println(mapOfPhoneNumbers.get("type"));
			if(mapOfPhoneNumbers.get("type").equals("iphone"))
				Assert.assertTrue(mapOfPhoneNumbers.get("name").startsWith("3456"));
			else if(mapOfPhoneNumbers.get("type").equals("home"))
				Assert.assertTrue(mapOfPhoneNumbers.get("name").startsWith("0123"));
		}
	}
}
