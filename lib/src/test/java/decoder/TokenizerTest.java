package decoder;

import decoder.tokenizer.Token;
import decoder.tokenizer.TokenType;
import decoder.tokenizer.Tokenizer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class TokenizerTest {

    @ParameterizedTest
    @MethodSource("provideTokenizersForPeekTest")
    void peek(Tokenizer tokenizer, Token expected) {
        assertEquals(tokenizer.peek(), expected);
    }

    @Test
    void hasNext_returnFalseWhenInvokedOnEmptyString() {
        var t = Tokenizer.from("");
        assertFalse(t.hasNext());
    }

    @Test
    void hasNext_returnFalseWhenInvokedOnExhaustedTokenizer() {
        var t = Tokenizer.from("x");
        t.next();
        assertFalse(t.hasNext());
    }

    @Test
    void hasNext_returnTrueWhenInvokedOnNonEmptyTokenizer() {
        var t = Tokenizer.from("x42[");
        assertEquals(t.next(), new Token(TokenType.SYMBOL, "x", 0, 1));
        assertEquals(t.next(), new Token(TokenType.FACTOR, "42", 1, 2));
        assertEquals(t.next(), new Token(TokenType.LEFT_BRACKET, "[", 3, 1));
        assertFalse(t.hasNext());
    }

    private static Stream<Arguments> provideTokenizersForPeekTest() {
        var shiftedTokenizer = Tokenizer.from("x1");
        shiftedTokenizer.next();
        var repeatedPeekTokenizer = Tokenizer.from("x1");
        repeatedPeekTokenizer.peek();

        return Stream.of(
                Arguments.of(Tokenizer.from("x"), new Token(TokenType.SYMBOL, "x", 0, 1)),
                Arguments.of(shiftedTokenizer, new Token(TokenType.FACTOR, "1", 1, 1)),
                Arguments.of(repeatedPeekTokenizer, new Token(TokenType.SYMBOL, "x", 0, 1))
        );
    }
}