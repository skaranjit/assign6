package assign6.parser;

import assign6.lexer.*;
import assign6.visitor.*;

public class LiteralNode extends Node
{
    public int value;
    public Num v;

    public LiteralNode()
    {

    }

    public LiteralNode(Num v)
    {
        this.value = v.value;
        this.v = v;
    }

    public void accept(ASTVisitor v)
    {
        v.visit(this);
    }

    public void printNode()
    {
        System.out.println("LiteralNode: " + value);
    }

}
