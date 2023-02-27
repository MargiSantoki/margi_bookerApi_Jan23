package utilities;

import net.datafaker.Faker;

public class dataGenerator {

	private static Faker faker = new Faker();
	
	public static String getEmailId() {
		return faker.name().firstName() + "." + faker.name().lastName() + "@gmail.com";
	}
	
	public static String getFullName() {
		return faker.name().fullName();
	}
	
	public static String getPhoneNumber(int digit) {
		return faker.number().digits(digit);
	}
	
	public static String getNumber(int digit) {
		return faker.number().digits(digit);
	}
	
	public static String getDate() {
		return faker.date().birthday("yyyy-MM-dd'T'HH:mm:ss'Z'");
	}
	
	public static String getGender() {
		return faker.gender().binaryTypes();
	}
	
	public static String getRelationship() {
		return faker.relationships().any();
	}
	
	public static String getContactAddressType() {
		return faker.house().room();
	}
	
	public static String getContactAddress1() {
		return faker.address().cityPrefix();
	}
	
	public static String getContactAddress2() {
		return faker.address().citySuffix();
	}
	
	public static String getPincode() {
		return faker.address().postcode();
	}
	
	public static String getCountry() {
		return faker.address().country();
	}
	
	public static String getCountryCode() {
		return faker.address().countryCode();
	}
	
	public static String getLanguage() {
		return faker.programmingLanguage().name();
	}
	
	public static String getFirstName() {
		return faker.name().firstName();
	}
}
