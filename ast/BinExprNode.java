package assign6.ast;

import assign6.visitor.*;
import assign6.lexer.*;

public class BinExprNode extends ExprNode
{
    public ExprNode left;
    public ExprNode right;
    public Token op;
    public Type type;

    public BinExprNode()
    {

    }

    public BinExprNode(ExprNode left, ExprNode right)
    {
        this.left = left;
        this.right = right;
    }

    public BinExprNode(Token op, ExprNode left, ExprNode right)
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
