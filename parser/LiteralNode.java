package assign5.parser;

import assign5.lexer.*;
import assign5.visitor.*;

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
