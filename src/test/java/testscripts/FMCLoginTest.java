package testscripts;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import constants.StatusCode;
import io.restassured.response.Response;
import services.LoginServices;
import utilities.dataGenerator;

public class FMCLoginTest {
	
	LoginServices loginServices = new LoginServices();
	String emailId = dataGenerator.getEmailId();
	String password = "margi123";
	Response res;

	@Test
	public void loginTest() {
		res = loginServices.login(emailId,password);
		Assert.assertEquals(res.getStatusCode(), StatusCode.OK);
//		if(result.equals("success")) {
//			Assert.assertEquals(res.getStatusCode(), StatusCode.OK);
//		} else if(result.equals("fail")) {
//			Assert.assertEquals(res.getStatusCode(), StatusCode.BADREQUEST);
//		}
	}
	
	@DataProvider (name= "loginDataDetails")
	public String[][] getLoginData(){
		String[][] data = new String[2][3];
		
		data[0][0] = "margi.santoki2@abc.com";
		data[0][1] = "vdfvvd";
		data[0][2] = "success";
		
		data[1][0] = "margi.santoki3@abc.com";
		data[1][1] = "";
		data[1][2] = "fail";
		
		return data;
	}
}
