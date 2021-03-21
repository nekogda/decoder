package decoder.tokenizer;

import java.util.Objects;

public class Token {
    private final TokenType type;
    private final String value;
    private final int startIndex;
    private final int size;

    public Token(TokenType type, String value, int startIndex, int size) {
        this.type = type;
        this.value = value;
        this.startIndex = startIndex;
        this.size = size;
    }

    public TokenType getType() {
        return type;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public int getSize() {
        return size;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Token{" +
                "type=" + type +
                ", value=" + value +
                ", startIndex=" + startIndex +
                ", size=" + size +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Token token = (Token) o;
        return startIndex == token.startIndex && size == token.size && type == token.type && value.equals(token.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, value, startIndex, size);
    }
}
