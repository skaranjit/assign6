package assign5.ast;

import assign5.lexer.*;
import assign5.visitor.*;

public class NumNode extends Node
{
    public int value;
    public Num v;

    public NumNode()
    {

    }

    public NumNode(Num v)
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
        System.out.println("NumNode: " + value);
    }

}
