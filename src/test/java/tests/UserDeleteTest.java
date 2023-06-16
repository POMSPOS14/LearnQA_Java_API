package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Link;
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

@Epic("Delete cases")
@Feature("Delete")
@Link("https://playground.learnqa.ru/api/map")
public class UserDeleteTest extends BaseTestCase {

    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @Test
    @Description("Delete with authorization by user with id 2")
    @DisplayName("Negative delete auth user with id 2")
    public void testDeleteAdminUserTest() {
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = apiCoreRequests.login(authData);
        String header = this.getHeader(responseGetAuth, "x-csrf-token");
        String cookie = this.getCookie(responseGetAuth, "auth_sid");

        Response responseDeleteUser = apiCoreRequests.deleteUser("2", header, cookie);

        Assertions.asserResponseCodeEquals(responseDeleteUser, 400);
        Assertions.asserResponseTextEquals(responseDeleteUser, "Please, do not delete test users with ID 1, 2, 3, 4 or 5.");
    }

    @Test
    @Description("Creates a user, logs in from under him, deletes, then tries to get his data by ID and makes sure that the user is really deleted.")
    @DisplayName("Positive register new user, delete him and check it")
    public void testDeleteAuthUserTest() {
        Map<String, String> userData = DataGeneration.getRegistrationData();
        JsonPath responseCreateAuth = apiCoreRequests.registrationNewUser(userData);

        String userId = responseCreateAuth.getString("id");

        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        Response responseGetAuth = apiCoreRequests.login(authData);
        String header = this.getHeader(responseGetAuth, "x-csrf-token");
        String cookie = this.getCookie(responseGetAuth, "auth_sid");

        Response responseDeleteUser = apiCoreRequests.deleteUser(userId, header, cookie);

        Assertions.asserResponseCodeEquals(responseDeleteUser, 200);

        Response responseUserData = apiCoreRequests.getUserData(userId, header, cookie);

        Assertions.asserResponseTextEquals(responseUserData, "User not found");
    }

    @Test
    @Description("Try to delete the user while being logged in by another user.")
    @DisplayName("Negative, try to delete the user while being logged in by another user.")
    public void testDeleteAuthAnotherUserTest() {
        Map<String, String> userDataFirst = DataGeneration.getRegistrationData();
        JsonPath responseCreateAuthFirst = apiCoreRequests.registrationNewUser(userDataFirst);

        String userIdFirst = responseCreateAuthFirst.getString("id");

        Map<String, String> userData = DataGeneration.getRegistrationData();
        JsonPath responseCreateAuth = apiCoreRequests.registrationNewUser(userData);

        String userId = responseCreateAuth.getString("id");

        Map<String, String> authData = new HashMap<>();
        authData.put("email", userData.get("email"));
        authData.put("password", userData.get("password"));

        Response responseGetAuth = apiCoreRequests.login(authData);
        String header = this.getHeader(responseGetAuth, "x-csrf-token");
        String cookie = this.getCookie(responseGetAuth, "auth_sid");

        Response responseDeleteUser = apiCoreRequests.deleteUser(userIdFirst, header, cookie);

        Assertions.asserResponseCodeEquals(responseDeleteUser, 200);

        Response responseUserData = apiCoreRequests.getUserData(userIdFirst, header, cookie);

        Assertions.asserJsonByName(responseUserData, "username", userDataFirst.get("username"));
    }
}
