package decoder;

import decoder.parser.Parser;
import decoder.tokenizer.Tokenizer;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ParserTest {

    @ParameterizedTest
    @MethodSource("provideStringsForParseTest")
    void parse(String text, String expected) {
        var parser = new Parser(Tokenizer.from(text));
        var tree = parser.parse();
        assertEquals(tree.eval(), expected);

    }

    @ParameterizedTest
    @MethodSource("provideStringsForValidationTest")
    void validate(String text, boolean expected) {
        var parser = new Parser(Tokenizer.from(text));
        assertEquals(parser.validate(), expected);
    }

    private static Stream<Arguments> provideStringsForParseTest() {
        return Stream.of(
                Arguments.of("", ""),
                Arguments.of("x", "x"),
                Arguments.of("xy", "xy"),
                Arguments.of("x3[z]", "xzzz"),
                Arguments.of("x2[z]x", "xzzx"),
                Arguments.of("1[z]1[y]", "zy"),
                Arguments.of("x3[z]2[xy]", "xzzzxyxy"),
                Arguments.of("10[z]", "zzzzzzzzzz"),
                Arguments.of("2[2[z]x]", "zzxzzx"),
                Arguments.of("2[2[z]]", "zzzz"),
                Arguments.of("0[z]x", "x"),
                Arguments.of("01[z]x", "zx"),
                Arguments.of("2[z2[y]c]x", "zyyczyycx")
        );
    }

    private static Stream<Arguments> provideStringsForValidationTest() {
        var st = provideStringsForParseTest().map((arguments -> Arguments.of(arguments.get()[0], true)));
        var sf = Stream.of(
                Arguments.of("3", false),
                Arguments.of("-", false),
                Arguments.of("]", false),
                Arguments.of("1x", false),
                Arguments.of("1[x]1", false),
                Arguments.of("1[x1[y]", false),
                Arguments.of("1[2[3]]", false)
        );
        return Stream.concat(st, sf);
    }
}