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

}
