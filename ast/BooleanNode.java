package assign6.ast;

import assign6.lexer.Word;
import assign6.visitor.ASTVisitor;

public class BooleanNode extends ExprNode
{
    public Word bool;

    public BooleanNode(Word v)
    {
        this.bool = v;
    }

    public BooleanNode()
    {

    }

    public void accept(ASTVisitor v)
    {
        v.visit(this);
    }

}


