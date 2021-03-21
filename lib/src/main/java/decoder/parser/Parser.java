package decoder.parser;

import decoder.parser.ast.*;
import decoder.tokenizer.Token;
import decoder.tokenizer.TokenType;
import decoder.tokenizer.Tokenizer;
import decoder.tokenizer.TokenizerError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Parser {
    private static final Logger log = LoggerFactory.getLogger(Parser.class);

    private final Tokenizer tokenizer;
    private boolean validation = false;

    private static final String UNEXPECTED_END_OF_STREAM_ERR_MSG = "Unexpected end of token stream";
    private static final String UNEXPECTED_TOKEN_TYPE_ERR_MSG = "Unexpected token type. Expected: %s, got: %s";

    //    expression         = symbol / 1*([ symbol ] factor-expression) [ symbol ]
    //    factor-expression  = factor "[" expression "]"
    //    symbol             = 1*ALPHA
    //    factor             = 1*DIGIT
    //    ALPHA              = %x41-5A / %x61-7A
    //    DIGIT              = %x30-39

    public Parser(Tokenizer tokenizer) {
        this.tokenizer = tokenizer;
    }

    public Tree parse() {
        if (!tokenizer.hasNext()) {
            return new Empty();
        }
        return expression();
    }

    private Tree expression() {
        Tree result;

        switch (getNextTokenType()) {
            case FACTOR:
                result = factorExpression();
                break;
            case SYMBOL:
                var leaf = makeLeafSymbol(getNextToken());
                if (hasMoreExpressions()) {
                    checkNextTokenTypeIs(TokenType.FACTOR);
                    result = makeNode(leaf, factorExpression());
                } else {
                    result = leaf;
                }
                break;
            default:
                throw new ParsingError(String.format(UNEXPECTED_TOKEN_TYPE_ERR_MSG,
                        TokenType.FACTOR + "|" + TokenType.SYMBOL, getNextTokenType()));
        }
        return result;
    }

    private Token getNextToken() {
        if (!tokenizer.hasNext()) {
            throw new ParsingError(UNEXPECTED_END_OF_STREAM_ERR_MSG);
        }
        return tokenizer.next();
    }


    private Token getNextToken(TokenType type) {
        var token = getNextToken();
        if (token.getType() != type) {
            throw new ParsingError(String.format(UNEXPECTED_TOKEN_TYPE_ERR_MSG, type, token));
        }
        return token;
    }

    private Tree factorExpression() {
        // parse factor
        var left = makeLeafFactor(tokenizer.next());

        // drop `[` token
        getNextToken(TokenType.LEFT_BRACKET);

        // parse nested expression
        var right = expression();

        // drop `]` token
        getNextToken(TokenType.RIGHT_BRACKET);

        Tree result = makeNode(left, right);

        if (hasMoreExpressions()) {
            result = makeNode(result, expression());
        }

        return result;
    }

    private Tree makeNode(Tree left, Tree right) {
        return validation ? null : new Node(left, right);
    }

    private Tree makeLeafSymbol(Token token) {
        return validation ? null : new Symbol(token);
    }

    private Tree makeLeafFactor(Token token) {
        return validation ? null : new Factor(token);
    }

    private TokenType getNextTokenType() {
        if (!tokenizer.hasNext()) {
            throw new ParsingError(UNEXPECTED_END_OF_STREAM_ERR_MSG);
        }
        var token = tokenizer.peek();
        return token.getType();
    }

    private void checkNextTokenTypeIs(TokenType type) {
        if (getNextTokenType() != type) {
            throw new ParsingError(String.format(UNEXPECTED_TOKEN_TYPE_ERR_MSG, type, tokenizer.peek()));
        }
    }

    private boolean hasMoreExpressions() {
        return tokenizer.hasNext() && tokenizer.peek().getType() != TokenType.RIGHT_BRACKET;
    }

    public boolean validate() {
        try {
            validation = true;
            parse();
            return true;
        } catch (ParsingError | TokenizerError e) {
            log.info("Validation error: {}", e.getMessage());
        } finally {
            validation = false;
        }
        return false;
    }
}
