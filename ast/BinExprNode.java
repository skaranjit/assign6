package assign5.ast;

import assign5.visitor.*;
import assign5.lexer.*;

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
