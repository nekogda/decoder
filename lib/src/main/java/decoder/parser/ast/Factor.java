package decoder.parser.ast;

import decoder.tokenizer.Token;

public class Factor extends Tree {
    private final Token value;

    public Factor(Token value) {
        this.value = value;
    }

    @Override
    public String eval() {
        return value.getValue();
    }

    @Override
    public String toString() {
        return "Factor{" +
                "value=" + value +
                '}';
    }
}
