package lib;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class DataGeneration {
    public static String getRandomEmail() {
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());
        return "learnqa" + timestamp + "@example.com";
    }

    public static String getRandomEmailWithDefect() {
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());
        return "learnqa" + timestamp + "example.com";
    }

    public static Map<String, String> getRegistrationData() {
        Map<String, String> data = new HashMap<>();
        data.put("email", DataGeneration.getRandomEmail());
        data.put("password", "123");
        data.put("username", "lernqa");
        data.put("firstName", "lernqa");
        data.put("lastName", "lernqa");
        return data;
    }

    public static Map<String, String> getRegistrationDataWithOutParam(String paramForDelete) {
        Map<String, String> data = new HashMap<>();
        data.put("email", DataGeneration.getRandomEmail());
        data.put("password", "123");
        data.put("username", "lernqa");
        data.put("firstName", "lernqa");
        data.put("lastName", "lernqa");
        data.remove(paramForDelete);
        return data;
    }

    public static Map<String, String> getRegistrationDataWithShortName() {
        Map<String, String> data = new HashMap<>();
        data.put("email", DataGeneration.getRandomEmail());
        data.put("password", "123");
        data.put("username", "l");
        data.put("firstName", "lernqa");
        data.put("lastName", "lernqa");
        return data;
    }

    public static Map<String, String> getRegistrationDataWithLongName() {
        String name = "";
        for (int i = 0; i < 260; i++) {
            name = name + "l";
        }
        Map<String, String> data = new HashMap<>();
        data.put("email", DataGeneration.getRandomEmail());
        data.put("password", "123");
        data.put("username", name);
        data.put("firstName", "lernqa");
        data.put("lastName", "lernqa");
        return data;
    }


    public static Map<String, String> getRegistrationData(Map<String, String> nonDefaultValues) {
        Map<String, String> defaultValues = DataGeneration.getRegistrationData();
        Map<String, String> userData = new HashMap<>();
        String[] keys = {"email", "password", "username", "firstName", "lastName"};
        for (String key : keys) {
            if (nonDefaultValues.containsKey(key)) {
                userData.put(key, nonDefaultValues.get(key));
            } else {
                userData.put(key, defaultValues.get(key));
            }
        }
        return userData;
    }
}
