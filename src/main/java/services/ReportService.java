package services;

import java.util.Map;

import org.testng.Assert;

import base.BaseService;
import constants.APIEndPoints;
import io.restassured.response.Response;
import pojo.request.createBooking.CreateBookingRequest;
import pojo.request.fmcReport.AddReport;
import pojo.request.fmcReport.ChildDetails;
import pojo.request.fmcReport.IncidentDetails;
import pojo.request.fmcReport.ReporterDetails;
import utilities.dataGenerator;

public class ReportService extends BaseService{

	Response res;
	String requestId = dataGenerator.getNumber(6);
	String reportDate = dataGenerator.getDate();
	String reportFullName = dataGenerator.getFullName();
	String reporterAge = dataGenerator.getNumber(2);
	String reporterGender = dataGenerator.getGender();
	String reporterRelation = dataGenerator.getRelationship();
	String parentingType = dataGenerator.getRelationship();
	String contactAddressType = dataGenerator.getContactAddressType();
	String contactAddressLine1 = dataGenerator.getContactAddress1();
	String contactAddressLine2 = dataGenerator.getContactAddress2();
	String pincode = dataGenerator.getPincode();
	String country = dataGenerator.getCountry();
	String countryCode = dataGenerator.getCountryCode();
	String phoneNumber = dataGenerator.getPhoneNumber(10);
	String language = dataGenerator.getLanguage();
	
	String incidentDate = dataGenerator.getDate();
	String incidentCity = dataGenerator.getContactAddress1();
	
	String childFullName = dataGenerator.getFullName();
	String childAge = dataGenerator.getNumber(2);
	String childGender = dataGenerator.getGender();
	String childNickname = dataGenerator.getFirstName();
	
	GenerateTokenService generateTokenService = new GenerateTokenService();
	
	public AddReport addReportRequestBody(int userId) {
		ReporterDetails reportDetails = new ReporterDetails();
		reportDetails.setRequest_id(requestId);
		reportDetails.setUser_id(userId);
		reportDetails.setReport_date(reportDate);
		reportDetails.setReporter_fullname(reportFullName);
		reportDetails.setReporter_age(Integer.parseInt(reporterAge));
		reportDetails.setReporter_gender(reporterGender);
		reportDetails.setReporter_relation("Mother");
		reportDetails.setParenting_type("Own Child");
		reportDetails.setContact_address_type("Home");
		reportDetails.setContact_address_line_1(contactAddressLine1);
		reportDetails.setContact_address_line_2(contactAddressLine2);
		reportDetails.setPincode(pincode);
		reportDetails.setCountry(country);
		reportDetails.setPrimary_country_code(countryCode);
		reportDetails.setPrimary_contact_number(phoneNumber);
		reportDetails.setSecondary_country_code(countryCode);
		reportDetails.setSecondary_contact_number(phoneNumber);
		reportDetails.setCommunication_language("English");
		reportDetails.setStatus("INCOMPLETE");
		
		IncidentDetails incidentDetails = new IncidentDetails();
		incidentDetails.setIncident_date(incidentDate);
		incidentDetails.setIncident_brief("Missing from school");
		incidentDetails.setLocation(incidentCity);
		incidentDetails.setLandmark_signs("opp temple");
		incidentDetails.setNearby_police_station("City Police station");
		incidentDetails.setNearby_NGO("Sanskriti NGO");
		incidentDetails.setAllow_connect_police_NGO(true);
		incidentDetails.setSelf_verification(true);
		incidentDetails.setCommunity_terms(true);

		ChildDetails childDetails = new ChildDetails();
		childDetails.setFullname(childFullName);
		childDetails.setAge(Integer.parseInt(childAge));
		childDetails.setGender(childGender);
		childDetails.setHeight("5ft");
		childDetails.setWeight("45kg");
		childDetails.setComplexion("Fair");
		childDetails.setClothing("Dress");
		childDetails.setBirth_signs("mark on right hand");
		childDetails.setOther_details("spectacles");
		childDetails.setImage_file_key(null);
		childDetails.setNickname(childNickname);
		
		AddReport addReport = new AddReport();
		
		addReport.setReporter_details(reportDetails);
		addReport.setIncident_details(incidentDetails);
		addReport.setChild_details(childDetails);
		
		return addReport;
	}
	
	public Response getAddReportResponse(int userId, AddReport addReport) {
		Map<String,String> headerMap = generateTokenService.getHeaderHavingAuth(generateTokenService.getToken());
		return executePostAPI(APIEndPoints.ADDREPORT, headerMap, addReport);
	}
	
	public Response getGetReportResponse(int userId) {
		Map<String,String> headerMap = generateTokenService.getHeaderHavingAuth(generateTokenService.getToken());
		return executeGetAPI(APIEndPoints.GETREPORT + String.valueOf(userId), headerMap);
	}
	
	public Response getDeleteReportResponse(int content, int userId) {
		Map<String,String> headerMap = generateTokenService.getHeaderHavingAuth(generateTokenService.getToken());
		return executeDeleteAPI(APIEndPoints.GETREPORT + String.valueOf(content) + "/users/" + userId, headerMap);
	}
}
