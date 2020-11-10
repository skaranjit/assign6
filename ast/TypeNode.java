package assign5.ast;

import assign5.visitor.*;
import assign5.lexer.*;


public class TypeNode extends Node
{
    public Type basic;
    public ArrayTypeNode array = null;

    public TypeNode()
    {

    }

    public TypeNode(Type basic, ArrayTypeNode array)
    {
        this.basic = basic;
        this.array = array;
    }

    public void accept(ASTVisitor v)
    {
        v.visit(this);
    }

}
