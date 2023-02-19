package testscripts;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import constants.StatusCode;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import pojo.request.createBooking.BookingDates;
import pojo.request.createBooking.CreateBookingRequest;

public class CreateBookingTest {

	String token;
	int bookingId;
	CreateBookingRequest payload;

	@BeforeMethod
	public void generateToken() {
		RestAssured.baseURI = "https://restful-booker.herokuapp.com";

		String payload = "{\r\n" 
		+ "    \"username\" : \"admin\",\r\n" 
				+ "    \"password\" : \"password123\"\r\n" 
		+ "}";

		RequestSpecification reqSpec = RestAssured.given();
		reqSpec.baseUri("https://restful-booker.herokuapp.com");
		reqSpec.headers("Content-Type", "application/json");
		reqSpec.body(payload);

		Response res = reqSpec.post("/auth");

		Assert.assertEquals(res.statusCode(), 200);
		token = res.jsonPath().getString("token");
	}

//	public void generateToken() { //bdd format
//		RestAssured.baseURI = "https://restful-booker.herokuapp.com";
//
//		Response res = RestAssured.given()
////				.log().all()
//				.headers("Content-Type", "application/json")
//				.body("{\r\n" 
//				+ "    \"username\" : \"admin\",\r\n" 
//				+ "    \"password\" : \"password123\"\r\n" 
//				+ "}")
//				.when()
//				.post("/auth")
//				.then()
//				.assertThat().statusCode(200)
//				.log().all()
//				.extract()
//				.response();
//		
////		System.out.println(res.asPrettyString());
//		System.out.println(res.statusCode());
//		Assert.assertEquals(res.statusCode(), 200);
//	}

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
	
	@Test
	public void createBookingTestWithPojo() {
		BookingDates bookingDates = new BookingDates();
		bookingDates.checkin = "2022-01-01";
		bookingDates.checkout = "2022-01-05";
		
		payload = new CreateBookingRequest();
		payload.firstname = "Margi";
		payload.lastname = "Vegada";
		payload.totalprice = 1000;
		payload.depositpaid = true;
		payload.additionalneeds = "Breakfast";
		payload.bookingdates = bookingDates;
		
		
		Response res = RestAssured.given()
		.headers("Content-Type", "application/json")
		.headers("Accept","application/json")
		.body(payload)
		.when()
		.post("/booking");
		Assert.assertEquals(res.getStatusCode(), StatusCode.OK);
//		Assert.assertTrue(Integer.valueOf(res.jsonPath().getInt("bookingid")) instanceof Integer);
		bookingId = res.jsonPath().getInt("bookingid");
//		System.out.println(bookingId);
		Assert.assertTrue(bookingId > 0);
		validateResponse(res, payload, "booking.");
	}
	
	@Test (priority = 1, enabled = false)
	public void getAllBookingTest() {
//		int bookingId = 2522;
		Response res = RestAssured.given()
						.headers("Accept","application/json")
						.when()
						.get("/booking");
		Assert.assertEquals(res.getStatusCode(),StatusCode.OK);
//		System.out.println(res.jsonPath());
		List<Integer> listOfBookingId = res.jsonPath().getList("bookingid");
//		System.out.println(listOfBookingId.size());
		Assert.assertTrue(listOfBookingId.contains(bookingId));
	}
	
	@Test (priority = 2, enabled = false)
	public void getBookingIdTest() {
		Response res = RestAssured.given()
						.headers("Accept","application/json")
						.when()
						.get("/booking/" + bookingId);
		Assert.assertEquals(res.getStatusCode(),StatusCode.OK);
		System.out.println(res.asPrettyString());
		validateResponse(res, payload, "");
	}
	
	@Test (priority = 2)
	public void getBookingIdPojoDeserializedTest() {
		Response res = RestAssured.given()
						.headers("Accept","application/json")
						.when()
						.get("/booking/" + bookingId);
		Assert.assertEquals(res.getStatusCode(),StatusCode.OK);
		System.out.println(res.asPrettyString());
		CreateBookingRequest responseBody = res.as(CreateBookingRequest.class);
		//payload : all details of request
		//responseBody : all details of getBooking
		System.out.println(responseBody);
		Assert.assertTrue(responseBody.equals(payload));
	}
	
	@Test (priority = 3)
	public void updateBookingIdTest() {
		payload.setFirstname("Harsh");
		Response res = RestAssured.given()
				.headers("Content-Type","application/json")
				.headers("Accept","application/json")
				.headers("Cookie","token=" + token)
				.body(payload)
				.when()
				.put("/booking/" + bookingId);
		Assert.assertEquals(res.getStatusCode(),StatusCode.OK);
		System.out.println(res.asPrettyString());
		CreateBookingRequest responseBody = res.as(CreateBookingRequest.class);
		Assert.assertTrue(responseBody.equals(payload));
	}
	
	private void validateResponse(Response res, CreateBookingRequest payload, String object) {
		Assert.assertEquals(res.jsonPath().getString(object + "firstname"), payload.getFirstname());
		Assert.assertEquals(res.jsonPath().getString(object + "lastname"), payload.getLastname());
		Assert.assertEquals(res.jsonPath().getInt(object + "totalprice"), payload.getTotalprice());
		Assert.assertEquals(res.jsonPath().getBoolean(object + "depositpaid"), payload.isDepositpaid());
		Assert.assertEquals(res.jsonPath().getString(object + "bookingdates.checkin"), payload.getBookingDates().getCheckin());
		Assert.assertEquals(res.jsonPath().getString(object + "bookingdates.checkout"), payload.getBookingDates().getCheckout());
		Assert.assertEquals(res.jsonPath().getString(object + "additionalneeds"), payload.getAdditionalneeds());
	}
}
