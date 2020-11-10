package assign6.parser;

import assign6.lexer.*;
import assign6.visitor.*;

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


