package tests;

import io.qameta.allure.Description;
import io.restassured.RestAssured;
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

public class UserGetTest extends BaseTestCase {
    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @Test
    @Description("This test check presence some fields and absence of others if request do unauthorized user")
    @DisplayName("Get data if user unauthorised")
    public void testGetUserDataNotAuth() {
        Response responseUserData = RestAssured
                .get("https://playground.learnqa.ru/api/user/2")
                .andReturn();
        String[] expectedFields = {"firstName", "lastName", "email"};
        Assertions.assertJsonHasNotFields(responseUserData, expectedFields);
        Assertions.assertJsonHasField(responseUserData, "username");
    }

    @Test
    @Description("Creates two users and checks if the second can get the data of the first")
    @DisplayName("Get someone else's data")
    public void testGetUserDetailsAuthAsSameUser() {
        JsonPath responseCreateAuthFirst = apiCoreRequests.registrationNewUserWithRandomData();

        String userIdFirst = responseCreateAuthFirst.getString("id");

        Map<String, String> userDataSecond = DataGeneration.getRegistrationData();
        JsonPath responseCreateAuthSecond = apiCoreRequests.registrationNewUser(userDataSecond);

        Map<String, String> authData = new HashMap<>();
        authData.put("email", userDataSecond.get("email"));
        authData.put("password", userDataSecond.get("password"));

        Response responseGetAuth = apiCoreRequests.login(authData);

        String header = this.getHeader(responseGetAuth, "x-csrf-token");
        String cookie = this.getCookie(responseGetAuth, "auth_sid");

        Response responseUserData = apiCoreRequests.getUserData(userIdFirst, header, cookie);

        String[] expectedFields = {"firstName", "lastName", "email"};
        Assertions.assertJsonHasNotFields(responseUserData, expectedFields);
        Assertions.assertJsonHasField(responseUserData, "username");

    }


}
