package tests;

import io.qameta.allure.Description;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGeneration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class UserEditTest extends BaseTestCase {
    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @Test
    @Description("Registers new user and changes his name with authorized")
    @DisplayName("Positive edit user name with authorized")
    public void testEditJustCreatedTest() {

        Map<String, String> userData = DataGeneration.getRegistrationData();
        JsonPath responseCreateAuth = apiCoreRequests
                .makePostJson("https://playground.learnqa.ru/api/user/", userData);

        String userId = responseCreateAuth.getString("id");

        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        Response responseGetAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);
        String header = this.getHeader(responseGetAuth, "x-csrf-token");
        String cookie = this.getCookie(responseGetAuth, "auth_sid");

        String newName = "Changed Name";
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", newName);

        Response responseEditUser = apiCoreRequests
                .makePutRequest(("https://playground.learnqa.ru/api/user/" + userId), header, cookie, editData);

        Response responseUserData = apiCoreRequests
                .makeGetRequest("https://playground.learnqa.ru/api/user/" + userId, header, cookie);

        Assertions.asserJsonByName(responseUserData, "firstName", newName);
    }

    @Test
    @Description("Try change user details while being unauthorized")
    @DisplayName("Negative change user details not auth")
    public void testEditNotAuthUser() {

        Map<String, String> userData = DataGeneration.getRegistrationData();
        JsonPath responseCreateAuth = apiCoreRequests.registrationNewUser(userData);

        String userId = responseCreateAuth.getString("id");

        String newName = "TestName";
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", newName);

        Response responseEditUser = apiCoreRequests.changeUserData(userId, null, null, editData);
        Assertions.asserResponseCodeEquals(responseEditUser, 400);
        Assertions.asserResponseTextEquals(responseEditUser, "Auth token not supplied");
    }

    @Test
    @Description("Try change user data while being authorized by another user")
    @DisplayName("Negative change user details while being authorized by another")
    public void testEditAnotherAuthUser() {

        Map<String, String> userDataFirst = DataGeneration.getRegistrationData();
        JsonPath responseCreateAuthFirst = apiCoreRequests.registrationNewUser(userDataFirst);
        String userIdFirst = responseCreateAuthFirst.getString("id");

        Response responseGetAuthFirst = apiCoreRequests.login(userDataFirst);
        String headerFirst = this.getHeader(responseGetAuthFirst, "x-csrf-token");
        String cookieFirst = this.getCookie(responseGetAuthFirst, "auth_sid");

        Map<String, String> userDataSecond = DataGeneration.getRegistrationData();
        JsonPath responseCreateAuthSecond = apiCoreRequests.registrationNewUser(userDataSecond);
        String userIdSecond = responseCreateAuthSecond.getString("id");

        Response responseGetAuth = apiCoreRequests.login(userDataSecond);
        String headerSecond = this.getHeader(responseGetAuth, "x-csrf-token");
        String cookieSecond = this.getCookie(responseGetAuth, "auth_sid");

        JsonPath responseUserDataFirst = apiCoreRequests.getUserDataJson(userIdFirst, headerFirst, cookieFirst);
        System.out.println(responseUserDataFirst.getString("firstName"));

        String newName = "TestName";
        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", newName);

        Response responseEditUser = apiCoreRequests.changeUserData(userIdFirst, headerSecond, cookieSecond, editData);

        JsonPath responseUserDataFirstAfterChange = apiCoreRequests.getUserDataJson(userIdFirst, headerFirst, cookieFirst);

        Assertions.asserResponseCodeEquals(responseEditUser, 200);
        assertEquals(responseUserDataFirst.getString("firstName"),
                responseUserDataFirstAfterChange.getString("firstName"), "firstName change");
    }

    @Test
    @Description("Try change the user's email, being authorized by the same user, to a new email without the @ symbol")
    @DisplayName("Negative edit email without @")
    public void testEditDataWithDefectEmail() {
        Map<String, String> userData = DataGeneration.getRegistrationData();
        JsonPath responseCreateAuth = apiCoreRequests.registrationNewUser(userData);

        String userId = responseCreateAuth.getString("id");

        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        Response responseGetAuth = apiCoreRequests.login(authData);
        String header = this.getHeader(responseGetAuth, "x-csrf-token");
        String cookie = this.getCookie(responseGetAuth, "auth_sid");

        String defectEmail = DataGeneration.getRandomEmailWithDefect();
        Map<String, String> editData = new HashMap<>();
        editData.put("email", defectEmail);

        Response responseEditUser = apiCoreRequests.changeUserData(userId, header, cookie, editData);

        Assertions.asserResponseCodeEquals(responseEditUser, 400);
        Assertions.asserResponseTextEquals(responseEditUser, "Invalid email format");
    }

    @Test
    @Description("Try change the firstName of the user, being authorized by the same user, to a very short value of one character")
    @DisplayName("Negative edit firstName to a short value")
    public void testEditDataWithDefectFirstName() {
        Map<String, String> userData = DataGeneration.getRegistrationData();
        JsonPath responseCreateAuth = apiCoreRequests.registrationNewUser(userData);

        String userId = responseCreateAuth.getString("id");

        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        Response responseGetAuth = apiCoreRequests.login(authData);
        String header = this.getHeader(responseGetAuth, "x-csrf-token");
        String cookie = this.getCookie(responseGetAuth, "auth_sid");

        Map<String, String> editData = new HashMap<>();
        editData.put("firstName", "l");

        Response responseEditUser = apiCoreRequests.changeUserData(userId, header, cookie, editData);

        Assertions.asserResponseCodeEquals(responseEditUser, 400);
        Assertions.asserJsonByName(responseEditUser, "error", "Too short value for field firstName");
    }
}
