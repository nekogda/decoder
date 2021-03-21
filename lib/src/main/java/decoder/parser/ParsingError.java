package decoder.parser;

public class ParsingError extends RuntimeException {
    public ParsingError(String message) {
        super(message);
    }

    public ParsingError(String message, Throwable cause) {
        super(message, cause);
    }
}
