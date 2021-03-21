package decoder.tokenizer;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.Iterator;
import java.util.function.Predicate;

public class Tokenizer implements Iterator<Token> {

    private final PushbackReader reader;
    private int currentPosition = -1;
    private Token pushBackToken = null;

    public Tokenizer(Reader reader) {
        this.reader = new PushbackReader(reader);
    }

    public static Tokenizer from(String source) {
        return new Tokenizer(new StringReader(source));
    }

    public Token peek() {
        if (pushBackToken == null) {
            pushBackToken = next();
        }
        return pushBackToken;
    }

    @Override
    public boolean hasNext() {
        if (pushBackToken != null) {
            return true;
        }
        return peekChar() != -1;
    }

    @Override
    public Token next() {
        if (pushBackToken != null) {
            var token = pushBackToken;
            pushBackToken = null;
            return token;
        }

        var ch = peekChar();

        if (Character.isDigit(ch)) {
            return getFactorToken();
        } else if (Character.isLetter(ch)) {
            return getSymbolToken();
        } else if (isLeftBracket(ch)) {
            return getLeftBracketToken();
        } else if (isRightBracket(ch)) {
            return getRightBracketToken();
        } else {
            throw new TokenizerError("unknown char: '" + ch + "'");
        }
    }

    private int peekChar() {
        try {
            var c = reader.read();
            if (c != -1) {
                reader.unread(c);
            }
            return c;
        } catch (IOException e) {
            throw new TokenizerError("reader.read() error", e);
        }
    }

    private int readChar() {
        try {
            var c = reader.read();
            if (c != -1) {
                ++currentPosition;
            }
            return c;
        } catch (IOException e) {
            throw new TokenizerError("reader.read() error", e);
        }
    }

    private void unreadChar(int c) {
        try {
            reader.unread(c);
            --currentPosition;
        } catch (IOException e) {
            throw new TokenizerError("reader.unread(c) error", e);
        }
    }

    private static boolean isLeftBracket(int c) {
        return c == '[';
    }

    private static boolean isRightBracket(int c) {
        return c == ']';
    }

    private String getLexeme(Predicate<Integer> predicate) {
        return getLexeme(predicate, -1);
    }

    private String getLexeme(Predicate<Integer> predicate, int lookupLength) {
        int ch;
        var chars = new StringBuilder();
        int currentLookupLength = 0;

        while ((lookupLength == -1 || currentLookupLength < lookupLength) && (ch = readChar()) != -1) {
            if (predicate.test(ch)) {
                chars.append((char) ch);
                ++currentLookupLength;
            } else {
                unreadChar(ch);
                break;
            }
        }
        return chars.toString();
    }

    private int getStartPosition(int tokenLength) {
        return currentPosition - (tokenLength - 1);
    }

    private Token getFactorToken() {
        var lex = getLexeme(Character::isDigit);
        return new Token(TokenType.FACTOR, lex, getStartPosition(lex.length()), lex.length());
    }

    private Token getSymbolToken() {
        var lex = getLexeme(Character::isLetter);
        return new Token(TokenType.SYMBOL, lex, getStartPosition(lex.length()), lex.length());
    }

    private Token getLeftBracketToken() {
        var lex = getLexeme(Tokenizer::isLeftBracket, 1);
        return new Token(TokenType.LEFT_BRACKET, lex, getStartPosition(lex.length()), lex.length());
    }

    private Token getRightBracketToken() {
        var lex = getLexeme(Tokenizer::isRightBracket, 1);
        return new Token(TokenType.RIGHT_BRACKET, lex, getStartPosition(lex.length()), lex.length());
    }

}
