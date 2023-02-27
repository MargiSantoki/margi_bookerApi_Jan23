package services;

import java.util.Map;

import org.json.simple.JSONObject;

import base.BaseService;
import constants.APIEndPoints;
import io.restassured.response.Response;
import utilities.dataGenerator;

public class GenerateTokenService extends BaseService{
	
	String emailId = dataGenerator.getEmailId();
	
	public Response getTokenResponse() {
		Map<String,String> headerMap = getHeaderWithoutAuth();
		Response res = executeGetAPI(APIEndPoints.TOKEN, headerMap);
		return res;
	}
	
	public String getToken() {
		Response res = getTokenResponse();
		return res.jsonPath().getString("accessToken");
	}
	
	public Response getEmailSignupResponse(JSONObject emailSignupPayload) { //Object can handle POJO as well as JSONObject
		Map<String,String> headerMap = getHeaderHavingAuth(getToken());
		return executePostAPI(APIEndPoints.EMAILSIGNUP, headerMap, emailSignupPayload);
	}
	
	public Response getEmailSignupResponse() {
		JSONObject emailSignupPayload = new JSONObject();
		emailSignupPayload.put("email_id",emailId);
		Map<String,String> headerMap = getHeaderHavingAuth(getToken());
		return executePostAPI(APIEndPoints.EMAILSIGNUP, headerMap, emailSignupPayload);
	}
	
	public String getOtpFromEmailSignupResponse(JSONObject emailSignupPayload) {
		Response res = getEmailSignupResponse(emailSignupPayload);
		return res.jsonPath().getString("content.otp");
	}
	
	public Response getVerifyOtpResponse(JSONObject verifyOtpPayload) {
		Map<String,String> headerMap = getHeaderHavingAuth(getToken());
		return executePutAPI(APIEndPoints.VERIFYOTP, headerMap, verifyOtpPayload);
	}
	
	public int getUserIdFromVerifyOtpResponse(JSONObject verifyOtpPayload) {
		Response res = getVerifyOtpResponse(verifyOtpPayload);
		return res.jsonPath().getInt("content.userId");
	}
	
	public int getUserId(String email_id, String password) {
		JSONObject emailSignupPayload = new JSONObject();
		emailSignupPayload.put("email_id",email_id);
		String otp = getOtpFromEmailSignupResponse(emailSignupPayload);
		
		String fullName = dataGenerator.getFullName();
		String phoneNumber = dataGenerator.getPhoneNumber(10);
		
		JSONObject verifyOtpPayload = new JSONObject();
		verifyOtpPayload.put("email_id",email_id);
		verifyOtpPayload.put("full_name", fullName);
		verifyOtpPayload.put("phone_number", phoneNumber);
		verifyOtpPayload.put("password", password);
		verifyOtpPayload.put("otp", otp);
		
		return getUserIdFromVerifyOtpResponse(verifyOtpPayload);
	}
}
