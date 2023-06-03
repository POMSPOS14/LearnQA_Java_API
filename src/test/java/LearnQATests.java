import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class LearnQATests {

    @Test
    public void hello() {
        System.out.println("Hello from Roman Miranyuk");
    }

    @Test
    public void responsePrint() {
        Response response = RestAssured
                .get("https://playground.learnqa.ru/api/get_text")
                .andReturn();
        response.prettyPrint();
    }

    @Test
    public void getSecondMessage() {
        JsonPath jsonPath = RestAssured
                .get("https://playground.learnqa.ru/api/get_json_homework")
                .jsonPath();

        String secondMessage = jsonPath.getList("messages.message").get(1).toString();

        System.out.println(secondMessage);

    }

    @Test
    public void getRedirect() {
        Response response = RestAssured
                .given()
                .redirects()
                .follow(false)
                .when()
                .get("https://playground.learnqa.ru/api/long_redirect")
                .andReturn();

        String location = response.getHeader("Location");

        System.out.println(location);
    }

    @Test
    public void getLongRedirect() {
        Response responseFirst = RestAssured
                .given()
                .redirects()
                .follow(false)
                .when()
                .get("https://playground.learnqa.ru/api/long_redirect")
                .andReturn();

        String location = responseFirst.getHeader("Location");

        int statusCode = responseFirst.statusCode();
        int count = 0;

        while (statusCode != 200) {

            System.out.println("Статус код " + statusCode);
            System.out.println("Редирект " + location);

            count++;
            Response response = RestAssured
                    .given()
                    .redirects()
                    .follow(false)
                    .when()
                    .get(location)
                    .andReturn();

            statusCode = response.statusCode();
            location = response.getHeader("Location");
        }

        System.out.println("\nФинальный ответ:\n");
        System.out.println("Количество редиректов: " + count);
    }

    @Test
    public void tokens() throws InterruptedException {
        String notReady = "Job is NOT ready";
        String ready = "Job is ready";

        JsonPath jsonPath = RestAssured
                .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                .jsonPath();

        jsonPath.prettyPrint();

        String token = jsonPath.getString("token");
        Integer seconds = jsonPath.getInt("seconds");

        String status = notReady;
        JsonPath jsonPathWithToken = null;

        while (!status.equals(ready)) {
            jsonPathWithToken = RestAssured
                    .given()
                    .queryParam("token", token)
                    .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                    .jsonPath();

            jsonPathWithToken.prettyPrint();

            status = jsonPathWithToken.getString("status");

            if (status.equals(notReady)) {
                System.out.println("Жду " + (seconds + 1) + " секунд");
                Thread.sleep((seconds + 1) * 1000);
            }
        }

        String result = jsonPathWithToken.getString("result");
        System.out.println("Результат присутствует: " + !result.equals(null));
        System.out.println("Результат: " + result);
    }

    @Test
    public void passwordSearch() {
        String login = "super_admin";
        String notAuth = "You are NOT authorized";
        HashSet<String> passwordSet = new HashSet<>();
        JsonPath jsonPath = RestAssured
                .get("https://sheet2api.com/wiki-api/List_of_the_most_common_passwords/en/Top+25+most+common+passwords+by+year+according+to+SplashData")
                .jsonPath();

        List<String> list2011 = jsonPath.getList("2011");
        passwordSet.addAll(list2011);
        List<String> list2012 = jsonPath.getList("2012");
        passwordSet.addAll(list2012);
        List<String> list2013 = jsonPath.getList("2013");
        passwordSet.addAll(list2013);
        List<String> list2014 = jsonPath.getList("2014");
        passwordSet.addAll(list2014);
        List<String> list2015 = jsonPath.getList("2015");
        passwordSet.addAll(list2015);
        List<String> list2016 = jsonPath.getList("2016");
        passwordSet.addAll(list2016);
        List<String> list2017 = jsonPath.getList("2017");
        passwordSet.addAll(list2017);
        List<String> list2018 = jsonPath.getList("2018");
        passwordSet.addAll(list2018);
        List<String> list2019 = jsonPath.getList("2019");
        passwordSet.addAll(list2019);

        for (String password : passwordSet) {

            HashMap<String, String> body = new HashMap<>();
            body.put("login", login);
            body.put("password", password);

            Response response = RestAssured
                    .given()
                    .body(body)
                    .when()
                    .post("https://playground.learnqa.ru/ajax/api/get_secret_password_homework")
                    .andReturn();

            String authCookie = response.getCookie("auth_cookie");

            HashMap<String, String> cookie = new HashMap<>();
            cookie.put("auth_cookie", authCookie);

            Response checkResponse = RestAssured
                    .given()
                    .cookies(cookie)
                    .when()
                    .get("https://playground.learnqa.ru/ajax/api/check_auth_cookie")
                    .andReturn();

            String responseBody = checkResponse.getBody().asString();
            if (!responseBody.equals(notAuth)) {
                System.out.println("Фраза: " + responseBody);
                System.out.println("Пароль: " + password);
                break;
            }
        }
    }
}