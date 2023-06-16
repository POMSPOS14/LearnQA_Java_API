package tests;

import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
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
                .get ("https://playground.learnqa.ru/api/user/2")
                .andReturn();
        String[] expectedFields = {"firstName", "lastName","email"};
        Assertions.assertJsonHasNotFields(responseUserData, expectedFields);
        Assertions.assertJsonHasField(responseUserData, "username");
    }

    @Test
    @Description("This test check presence all fields if request do authorized user")
    @DisplayName("Get data if user authorised")
    public void testGetUserDetailsAuthAsSameUser() {
        Map<String,String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password","1234");
        Response responseGetAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login", authData);

        String header = this.getHeader(responseGetAuth,"x-csrf-token");
        String cookie = this.getCookie(responseGetAuth, "auth_sid");
        Response responseUserData = apiCoreRequests
                .makeGetRequest("https://playground.learnqa.ru/api/user/2", header, cookie);
        String[] expectedFields = {"username", "firstName", "lastName","email"};
        Assertions.assertJsonHasFields(responseUserData, expectedFields);

    }
}
