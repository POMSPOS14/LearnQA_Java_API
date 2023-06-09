import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class LearnQATestsPartThree {

    @ParameterizedTest
    @ValueSource(strings = {"lessfifteen", "more than fifteen"})
    public void shortPhrase(@NotNull String string) {
        assertTrue(string.length() > 15, "String have less than fifteen characters");
    }

    @Test
    public void cookieCheck() {
        Response response = RestAssured
                .get("https://playground.learnqa.ru/api/homework_cookie")
                .andReturn();
        assertEquals(response.getCookie("HomeWork"), "hw_value", "Cookie not valid");
    }

    @Test
    public void headerCheck() {
        Response response = RestAssured
                .get("https://playground.learnqa.ru/api/homework_header")
                .andReturn();
        assertEquals(response.getHeader("x-secret-homework-header"), "Some secret value", "Header not valid");
    }

    @ParameterizedTest
    @MethodSource("userAgentInfoProvider")
    public void userAgentCheck(UserAgentInfo userAgentInfo) {
        JsonPath jsonPath = RestAssured
                .given()
                .contentType("application/json")
                .header("user-agent", userAgentInfo.user_agent)
                .get("https://playground.learnqa.ru/ajax/api/user_agent_check")
                .jsonPath();


        assertAll("Assert UserAgentInfo",
                () -> assertEquals(jsonPath.get("platform"), userAgentInfo.getPlatform(), "Platform not valid"),
                () -> assertEquals(jsonPath.get("browser"), userAgentInfo.getBrowser(), "Browser not valid"),
                () -> assertEquals(jsonPath.get("device"), userAgentInfo.getDevice(), "Device not valid")
        );
    }

    static List<UserAgentInfo> userAgentInfoProvider() {
        return Arrays.asList(new UserAgentInfo("Mozilla/5.0 (Linux; U; Android 4.0.2; en-us; Galaxy Nexus Build/ICL53F) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30",
                        "Mobile", "No", "Android"),
                new UserAgentInfo("Mozilla/5.0 (iPad; CPU OS 13_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) CriOS/91.0.4472.77 Mobile/15E148 Safari/604.1",
                        "Mobile", "Chrome", "iOS"),
                new UserAgentInfo("Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)",
                        "Googlebot", "Unknown", "Unknown"),
                new UserAgentInfo("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.77 Safari/537.36 Edg/91.0.100.0",
                        "Web", "Chrome", "No"),
                new UserAgentInfo("Mozilla/5.0 (iPad; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1",
                        "Mobile", "No", "iPhone")
        );
    }


    public static class UserAgentInfo {
        private final String user_agent;
        private final String platform;
        private final String browser;
        private final String device;

        public UserAgentInfo(String user_agent, String platform, String browser, String device) {
            this.user_agent = user_agent;
            this.platform = platform;
            this.browser = browser;
            this.device = device;
        }

        public String getUser_agent() {
            return user_agent;
        }

        public String getPlatform() {
            return platform;
        }

        public String getBrowser() {
            return browser;
        }

        public String getDevice() {
            return device;
        }

        @Override
        public String toString() {
            return "UserAgentInfo{" +
                    "user_agent='" + user_agent + '\'' +
                    ", platform='" + platform + '\'' +
                    ", browser='" + browser + '\'' +
                    ", device='" + device + '\'' +
                    '}';
        }
    }
}
