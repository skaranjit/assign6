package assign5.parser;

import assign5.lexer.*;
import assign5.visitor.*;

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


