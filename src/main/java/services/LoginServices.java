package services;

import java.util.Map;

import org.json.simple.JSONObject;
import org.testng.annotations.DataProvider;

import base.BaseService;
import constants.APIEndPoints;
import io.restassured.response.Response;

public class LoginServices extends BaseService {
	
	GenerateTokenService generateTokenService = new GenerateTokenService();
	Response res;

	public Response login(String emailId, String password) {
//		String payload = "{\r\n"
//				+ "   \"email_id\":\""+emailId+"\",\r\n"
//				+ "   \"password\":\""+password+"\"\r\n"
//				+ "}\r\n"
//				+ "";
		JSONObject loginPayload = new JSONObject();
		loginPayload.put("email_id", emailId);
		loginPayload.put("password", password);
		
		return login(loginPayload);
	}

	public Response login(JSONObject loginPayload) {
		generateTokenService.getUserId(loginPayload.get("email_id").toString(), loginPayload.get("password").toString());
		Map<String,String> headerMap = getHeaderHavingAuth(generateTokenService.getToken());
		return executePostAPI(APIEndPoints.LOGIN, headerMap, loginPayload);
	}
	
	public int loginAndGetUserId(JSONObject loginPayload) {
		res = login(loginPayload);
		return res.jsonPath().getInt("content.userId");
	}
}
