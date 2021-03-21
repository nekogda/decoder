package decoder.parser.ast;

public class Empty extends Tree {
    @Override
    public String eval() {
        return "";
    }

    @Override
    public String toString() {
        return "Empty{}";
    }
}
