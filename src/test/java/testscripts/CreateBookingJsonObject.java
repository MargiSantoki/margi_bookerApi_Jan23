package testscripts;

import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import constants.StatusCode;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import net.datafaker.Faker;
import pojo.request.createBooking.BookingDates;
import pojo.request.createBooking.CreateBookingRequest;

public class CreateBookingJsonObject {

	String token;
	int bookingId;

	@BeforeMethod
	public void generateToken() {
		RestAssured.baseURI = "https://restful-booker.herokuapp.com";

		String payload = "{\r\n" + "    \"username\" : \"admin\",\r\n" + "    \"password\" : \"password123\"\r\n" + "}";

		RequestSpecification reqSpec = RestAssured.given();
		reqSpec.baseUri("https://restful-booker.herokuapp.com");
		reqSpec.headers("Content-Type", "application/json");
		reqSpec.body(payload);

		Response res = reqSpec.post("/auth");

		Assert.assertEquals(res.statusCode(), 200);
		token = res.jsonPath().getString("token");
	}
	
//	@Test (enabled = false)
//	public void createBookingTest() {
//		Response res = RestAssured.given()
//		.headers("Content-Type", "application/json")
//		.headers("Accept","application/json")
//		.body("{\r\n"
//				+ "    \"firstname\" : \"Margi\",\r\n"
//				+ "    \"lastname\" : \"Vegada\",\r\n"
//				+ "    \"totalprice\" : 111,\r\n"
//				+ "    \"depositpaid\" : true,\r\n"
//				+ "    \"bookingdates\" : {\r\n"
//				+ "        \"checkin\" : \"2022-01-01\",\r\n"
//				+ "        \"checkout\" : \"2023-01-01\"\r\n"
//				+ "    },\r\n"
//				+ "    \"additionalneeds\" : \"Breakfast\"\r\n"
//				+ "}")
//		.when()
//		.post("/booking");
//		Assert.assertEquals(res.getStatusCode(), StatusCode.OK);
//		System.out.println(res.getStatusLine());
//	}
	
	@SuppressWarnings("unchecked")
	@Test (priority = 1)
	public void createBookingTestWithPojo() {
		Faker faker = new Faker();
		
		JSONObject jsonBookingDate = new JSONObject();
		jsonBookingDate.put("checkin", "2022-01-01");
		jsonBookingDate.put("checkout", "2022-03-01");
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("firstname", faker.name().firstName());
		jsonObject.put("lastname", faker.name().lastName());
		jsonObject.put("totalprice", Integer.parseInt(faker.number().digits(3)));
		jsonObject.put("depositpaid", faker.bool().bool());
		jsonObject.put("bookingdates", jsonBookingDate);
		jsonObject.put("additionalneeds", "Breakfast");

		Response res = RestAssured.given().headers("Content-Type", "application/json")
				.headers("Accept", "application/json").body(jsonObject).log().all().post("/booking");
		Assert.assertEquals(res.getStatusCode(), StatusCode.OK);
		bookingId = res.jsonPath().getInt("bookingid");
//		System.out.println(bookingId);
		Assert.assertTrue(bookingId > 0);
		System.out.println("Booking id: " + bookingId);
	}
}
