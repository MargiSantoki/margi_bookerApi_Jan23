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

public class FMCTest2 {
	String token, email, otp;
	int userId;
	Response res;
	BaseService baseService = new BaseService();
	Map<String,String> headerMap;
	String emailId = dataGenerator.getEmailId();
	String fullName = dataGenerator.getFullName();
	String phoneNumber = dataGenerator.getPhoneNumber(10);
	String password = "maargie123";

	@Test(priority = 1)
	public void createToken() {
		headerMap = baseService.getHeaderWithoutAuth();
		res = baseService.executeGetAPI("/fmc/token", headerMap);
		Assert.assertEquals(res.getStatusCode(),StatusCode.OK);
		token = res.jsonPath().getString("accessToken");
	}
	
	@Test(priority = 2)
	public void emailSignup() {		
		JSONObject emailSignupPayload = new JSONObject();
		emailSignupPayload.put("email_id",emailId);
		
		headerMap = baseService.getHeaderHavingAuth(token);
		
		res = baseService.executePostAPI("/fmc/email-signup-automation", headerMap, emailSignupPayload);
		
		Assert.assertEquals(res.getStatusCode(),StatusCode.CREATED);
		otp = res.jsonPath().getString("content.otp");
	}
	
	@Test(priority = 3)
	public void verifyOtp() {
		JSONObject verifyOtpPayload = new JSONObject();
		verifyOtpPayload.put("email_id",emailId);
		verifyOtpPayload.put("full_name", fullName);
		verifyOtpPayload.put("phone_number", phoneNumber);
		verifyOtpPayload.put("password", password);
		verifyOtpPayload.put("otp", otp);
		
		headerMap = baseService.getHeaderHavingAuth(token);
		
		res = baseService.executePutAPI("/fmc/verify-otp", headerMap, verifyOtpPayload);
		
		Assert.assertEquals(res.getStatusCode(),StatusCode.OK);
		userId = res.jsonPath().getInt("content.userId");
	}
}
