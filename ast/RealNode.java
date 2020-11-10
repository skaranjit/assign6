package assign6.ast;

import assign6.visitor.*;
import assign6.lexer.*;

public class RealNode extends Node
{
    public float value;
    public Real v;

    public RealNode(Real v)
    {
        this.value = v.value;
        this.v = v;
    }

    public RealNode()
    {

    }

    public void accept(ASTVisitor v)
    {
        v.visit(this);
    }

    public void printNode()
    {
        System.out.println("RealNode: " + value);
    }

}
