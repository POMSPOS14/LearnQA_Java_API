import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

public class LearnQATests {

    @Test
    public void hello(){
        System.out.println("Hello from Roman Miranyuk");
    }

    @Test
    public void responsePrint(){
        Response response = RestAssured
                .get("https://playground.learnqa.ru/api/get_text")
                .andReturn();
        response.prettyPrint();
    }
    @Test
    public void getSecondMessage(){
        JsonPath jsonPath = RestAssured
                .get("https://playground.learnqa.ru/api/get_json_homework")
                .jsonPath();

        String secondMessage = jsonPath.getList("messages.message").get(1).toString();

        System.out.println(secondMessage);

    }

}
