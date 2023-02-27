package testscripts;

import java.util.Map;

import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import base.BaseService;
import constants.StatusCode;
import io.restassured.response.Response;
import services.GenerateTokenService;
import utilities.dataGenerator;

public class FMCTest3 {
	String token, email, otp;
	int userId;
	Response res;
	BaseService baseService = new BaseService();
	Map<String,String> headerMap;
	String emailId = dataGenerator.getEmailId();
	String fullName = dataGenerator.getFullName();
	String phoneNumber = dataGenerator.getPhoneNumber(10);
	String password = "maargie123";
	GenerateTokenService generateTokenService = new GenerateTokenService();

	@Test(priority = 1)
	public void createToken() {
		res = generateTokenService.getTokenResponse();
		Assert.assertEquals(res.getStatusCode(),StatusCode.OK);
		token = generateTokenService.getToken();
		Assert.assertTrue(token.length()>0);
		Assert.assertEquals(res.jsonPath().get("tokenType"),"bearer");
	}
	
	@Test(priority = 2)
	public void emailSignup() {		
		JSONObject emailSignupPayload = new JSONObject();
		emailSignupPayload.put("email_id",emailId);
		res = generateTokenService.getEmailSignupResponse(emailSignupPayload);
		Assert.assertEquals(res.getStatusCode(),StatusCode.CREATED);
		otp = generateTokenService.getOtpFromEmailSignupResponse(emailSignupPayload);
	}
	
	@Test(priority = 3)
	public void verifyOtp() {
		JSONObject emailSignupPayload = new JSONObject();
		emailSignupPayload.put("email_id",emailId);
		otp = generateTokenService.getOtpFromEmailSignupResponse(emailSignupPayload);
		
		JSONObject verifyOtpPayload = new JSONObject();
		verifyOtpPayload.put("email_id",emailId);
		verifyOtpPayload.put("full_name", fullName);
		verifyOtpPayload.put("phone_number", phoneNumber);
		verifyOtpPayload.put("password", password);
		verifyOtpPayload.put("otp", otp);
		
		res = generateTokenService.getVerifyOtpResponse(verifyOtpPayload);
		Assert.assertEquals(res.getStatusCode(),StatusCode.OK);
//		userId = generateTokenService.getUserIdFromVerifyOtpResponse(verifyOtpPayload);
//		System.out.println(userId);
//		
//		emailId = dataGenerator.getEmailId();
//		userId = generateTokenService.getUserId(emailId, password);
//		System.out.println(userId);
	}
}
