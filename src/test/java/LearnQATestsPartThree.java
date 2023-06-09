import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LearnQATestsPartThree {

    @ParameterizedTest
    @ValueSource(strings = {"lessfifteen", "more than fifteen"})
    public void shortPhrase(@NotNull String string) {
        assertTrue(string.length() > 15, "String have less than fifteen characters");
    }

    @Test
    public void responsePrint() {
        Response response = RestAssured
                .get("https://playground.learnqa.ru/api/homework_cookie")
                .andReturn();
        assertEquals(response.getCookie("HomeWork"),"hw_value", "Cookie not valid");
    }
}
