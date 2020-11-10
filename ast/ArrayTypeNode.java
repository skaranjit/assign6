package assign6.ast;

import assign6.visitor.*;
import assign6.lexer.*;
public class ArrayTypeNode extends TypeNode
{
    public TypeNode type;
    public int size = 1;
    public Type ofType;

    public ArrayTypeNode()
    {

    }

    public ArrayTypeNode(int size, TypeNode type)
    {
        this.size = size;
        this.type = type;
    }

    public void accept(ASTVisitor v)
    {
        v.visit(this);
    }
}
