package testscripts;

import static io.restassured.RestAssured.given;

import java.util.Map;

import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import base.BaseService;
import constants.StatusCode;
import io.restassured.response.Response;
import utilities.dataGenerator;

public class FMCTest {
	String token, email, userId;
	Response res;
	BaseService baseService = new BaseService();
	Map<String,String> headerMap;

	@Test
	public void signupTest() {
//		RestAssured.baseURI = "http://Fmc-env.eba-5akrwvvr.us-east-1.elasticbeanstalk.com";
		String emailId = dataGenerator.getEmailId();
		String fullName = dataGenerator.getFullName();
		String phoneNumber = dataGenerator.getPhoneNumber(10);
		String password = "maargie123";
		
		createToken();
		String otp = emailSignup(emailId);
		verifyOtp(emailId, fullName, phoneNumber, password, otp);
		
	}
	
	private void createToken() {
		headerMap = baseService.getHeaderWithoutAuth();
		res = baseService.executeGetAPI("/fmc/token", headerMap);
		token = res.jsonPath().getString("accessToken");
	}
	
	private String emailSignup(String emailId) {		
		JSONObject emailSignupPayload = new JSONObject();
		emailSignupPayload.put("email_id",emailId);
		
		headerMap = baseService.getHeaderHavingAuth(token);
		
		res = baseService.executePostAPI("/fmc/email-signup-automation", headerMap, emailSignupPayload);
		
		Assert.assertEquals(res.getStatusCode(),StatusCode.CREATED);
		String otp = res.jsonPath().getString("content.otp");
		return otp;
	}
	
	private void verifyOtp(String emailId, String fullName, String phoneNumber, String password, String otp) {
		JSONObject verifyOtpPayload = new JSONObject();
		verifyOtpPayload.put("email_id",emailId);
		verifyOtpPayload.put("full_name", fullName);
		verifyOtpPayload.put("phone_number", phoneNumber);
		verifyOtpPayload.put("password", password);
		verifyOtpPayload.put("otp", otp);
		
		headerMap = baseService.getHeaderHavingAuth(token);
		
		res = baseService.executePutAPI("/fmc/verify-otp", headerMap, verifyOtpPayload);
		
		Assert.assertEquals(res.getStatusCode(),StatusCode.OK);
		userId = res.jsonPath().getString("content.userId");
	}
}
