package tests;

import io.qameta.allure.Description;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGeneration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class UserRegisterTest extends BaseTestCase {
    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @Test
    public void testCreateUserWithExistingEmail() {
        String email = "vinkotov@example.com";

        Map<String, String> userData = new HashMap<>();
        userData.put("email", email);
        userData = DataGeneration.getRegistrationData(userData);

        Response responseCreatAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/", userData);
        Assertions.asserResponseCodeEquals(responseCreatAuth, 400);
        Assertions.asserResponseTextEquals(responseCreatAuth, "Users with email '" + email + "' already exists");
    }

    @Test
    @Description("Successfully registration user with new data")
    @DisplayName("Create user successfully")
    public void testCresteUserSuccessfully() {

        Map<String,String> userData = DataGeneration.getRegistrationData();

        Response responseCreatAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/", userData);
        Assertions.asserResponseCodeEquals(responseCreatAuth, 200);
        Assertions.assertJsonHasField(responseCreatAuth, "id");
    }

}
