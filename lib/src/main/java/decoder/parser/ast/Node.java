package decoder.parser.ast;

public class Node extends Tree {
    private final Tree left;
    private final Tree right;

    public Node(Tree left, Tree right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public String eval() {
        if (left instanceof Factor) {
            return right.eval().repeat(Integer.parseInt(left.eval()));
        } else {
            return left.eval() + right.eval();
        }
    }

    @Override
    public String toString() {
        return "Node{" +
                "left=" + left +
                ", right=" + right +
                '}';
    }
}
