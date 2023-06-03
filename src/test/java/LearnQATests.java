import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

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

}
