package assign6.ast;

import assign6.visitor.*;
import assign6.lexer.*;

public class BinExprNode extends Node
{
    public Node left;
    public Node right;
    public Token op;

    public BinExprNode()
    {

    }

    public BinExprNode(Node left, Node right)
    {
        this.left = left;
        this.right = right;
    }

    public BinExprNode(Token op, Node left, Node right)
    {
        this.op = op;
        this.left = left;
        this.right = right;
    }

    public void accept(ASTVisitor v)
    {
        v.visit(this);
    }

}
