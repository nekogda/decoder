package decoder.parser.ast;

import decoder.tokenizer.Token;

public class Symbol extends Tree {
    private final Token value;

    public Symbol(Token value) {
        this.value = value;
    }

    @Override
    public String eval() {
        return value.getValue();
    }

    @Override
    public String toString() {
        return "Symbol{" +
                "value=" + value +
                '}';
    }
}
