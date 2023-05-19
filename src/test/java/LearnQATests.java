import io.restassured.RestAssured;
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

}
