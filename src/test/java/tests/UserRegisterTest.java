package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Link;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGeneration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;

@Link("https://playground.learnqa.ru/api/map")
public class UserRegisterTest extends BaseTestCase {
    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @Test
    public void testCreateUserWithExistingEmail() {
        String email = "vinkotov@example.com";

        Map<String, String> userData = new HashMap<>();
        userData.put("email", email);
        userData = DataGeneration.getRegistrationData(userData);

        Response responseCreateAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/", userData);
        Assertions.asserResponseCodeEquals(responseCreateAuth, 400);
        Assertions.asserResponseTextEquals(responseCreateAuth, "Users with email '" + email + "' already exists");
    }

    @Test
    @Description("Successfully registration user with new data")
    @DisplayName("Create user successfully")
    public void testCreateUserSuccessfully() {

        Map<String,String> userData = DataGeneration.getRegistrationData();

        Response responseCreateAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/", userData);
        Assertions.asserResponseCodeEquals(responseCreateAuth, 200);
        Assertions.assertJsonHasField(responseCreateAuth, "id");
    }

    @Test
    @Description("Negative registration user with email without @")
    @DisplayName("Registration with defect email")
    public void testCreateUserWithDefectEmail() {
        String email = DataGeneration.getRandomEmailWithDefect();
        Map<String, String> userData = new HashMap<>();
        userData.put("email", email);
        userData = DataGeneration.getRegistrationData(userData);

        Response responseCreateAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/", userData);
        Assertions.asserResponseCodeEquals(responseCreateAuth, 400);
        Assertions.asserResponseTextEquals(responseCreateAuth, "Invalid email format");
    }

    @ParameterizedTest
    @Description("Negative to registration user without one of the parameters")
    @DisplayName("Creating a user without one of the parameters")
    @ValueSource(strings = { "email", "password", "username", "firstName", "lastName" })
    public void testCreateUserWithoutAnyParam(String param) {
        Map<String, String> userData = DataGeneration.getRegistrationDataWithOutParam(param);

        Response responseCreateAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/", userData);

        Assertions.asserResponseCodeEquals(responseCreateAuth, 400);
        Assertions.asserResponseTextEquals(responseCreateAuth, "The following required params are missed: " + param);
    }

    @Test
    @Description("Negative to registration user with short name")
    @DisplayName("Creating a user with short name")
    public void testCreateUserWithShortName() {
        Map<String, String> userData = DataGeneration.getRegistrationDataWithShortName();

        Response responseCreateAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/", userData);

        Assertions.asserResponseCodeEquals(responseCreateAuth, 400);
        Assertions.asserResponseTextEquals(responseCreateAuth, "The value of 'username' field is too short");
    }

    @Test
    @Description("Negative to registration user with long name")
    @DisplayName("Creating a user with long name")
    public void testCreateUserWithLongName() {
        Map<String, String> userData = DataGeneration.getRegistrationDataWithLongName();

        Response responseCreateAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/", userData);

        Assertions.asserResponseCodeEquals(responseCreateAuth, 400);
        Assertions.asserResponseTextEquals(responseCreateAuth, "The value of 'username' field is too long");
    }

}
