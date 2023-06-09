import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LearnQATestsPartThree {

    @ParameterizedTest
    @ValueSource(strings = {"lessthanfifteen", "more than fifteen"})
    public void shortPhrase(@NotNull String string) {
        assertTrue(string.length() > 15, "String have less than fifteen characters");
    }
}
