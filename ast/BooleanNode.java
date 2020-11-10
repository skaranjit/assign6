package assign5.ast;

import assign5.lexer.Word;
import assign5.visitor.ASTVisitor;

public class BooleanNode extends Node
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


