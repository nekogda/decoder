package decoder.tokenizer;

public class TokenizerError extends RuntimeException {
    public TokenizerError(String message) {
        super(message);
    }

    public TokenizerError(String message, Throwable cause) {
        super(message, cause);
    }
}
