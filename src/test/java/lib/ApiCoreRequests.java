package lib;

import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.http.Header;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class ApiCoreRequests {
    @Step("Make a GET-request with token and auth cookie")
    public Response makeGetRequest(String url, String token, String cookie) {
        return given()
                .filter(new AllureRestAssured())
                .header(new Header("x-csrf-token", token))
                .cookie("auth_sid", cookie)
                .get(url)
                .andReturn();
    }

    @Step("Make a GET-request with token and auth cookie")
    public JsonPath makeGetRequestJson(String url, String token, String cookie) {
        return given()
                .filter(new AllureRestAssured())
                .header(new Header("x-csrf-token", token))
                .cookie("auth_sid", cookie)
                .get(url)
                .jsonPath();
    }

    @Step("Make a GET-request with token and auth cookie")
    public JsonPath getUserDataJson(String userId, String token, String cookie) {
        return makeGetRequestJson("https://playground.learnqa.ru/api/user/" + userId, token, cookie);
    }

    @Step("Make a GET-request with token and auth cookie")
    public Response getUserData(String userId, String token, String cookie) {
        return makeGetRequest("https://playground.learnqa.ru/api/user/" + userId, token, cookie);
    }

    @Step("Make a GET-request with auth cookie only")
    public Response makeGetRequestWithCookie(String url, String cookie) {
        return given()
                .filter(new AllureRestAssured())
                .cookie("auth_sid", cookie)
                .get(url)
                .andReturn();
    }

    @Step("Make a GET-requst with token only")
    public Response makeGetRequestWithToken(String url, String token) {
        return given()
                .filter(new AllureRestAssured())
                .header(new Header("x-csrf-token", token))
                .get(url)
                .andReturn();
    }

    @Step("Make a POST-requst")
    public Response makePostRequest(String url, Map<String, String> authData) {
        return given()
                .filter(new AllureRestAssured())
                .body(authData)
                .post(url)
                .andReturn();
    }

    @Step("Make a POST-requst")
    public Response login(Map<String, String> authData) {
        return makePostRequest("https://playground.learnqa.ru/api/user/login", authData);
    }

    @Step("Make a PostJson")
    public JsonPath makePostJson(String url, Map<String, String> authData) {
        return given()
                .filter(new AllureRestAssured())
                .body(authData)
                .post(url)
                .jsonPath();
    }

    @Step("Registration new user")
    public JsonPath registrationNewUser(Map<String, String> authData) {
        return makePostJson("https://playground.learnqa.ru/api/user/", authData);
    }

    @Step("Registration new user with random data")
    public JsonPath registrationNewUserWithRandomData() {
        return makePostJson("https://playground.learnqa.ru/api/user/", DataGeneration.getRegistrationData());
    }

    @Step("Make a PUT-request with token and auth cookie")
    public Response makePutRequest(String url, String token, String cookie, Map<String, String> editData) {
        return given()
                .filter(new AllureRestAssured())
                .header(new Header("x-csrf-token", token))
                .cookie("auth_sid", cookie)
                .body(editData)
                .put(url)
                .andReturn();
    }

    @Step("Make a PUT-request with token and auth cookie")
    public Response changeUserData(String userId, String token, String cookie, Map<String, String> editData) {
        return makePutRequest("https://playground.learnqa.ru/api/user/" + userId, token, cookie, editData);
    }
}
