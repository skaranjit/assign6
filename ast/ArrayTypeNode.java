package assign6.ast;

import assign6.visitor.*;
import assign6.lexer.*;
public class ArrayTypeNode extends TypeNode
{
    public TypeNode type;
    public BinExprNode size;

    public ArrayTypeNode()
    {

    }

    public ArrayTypeNode(BinExprNode size, TypeNode type)
    {
        this.size = size;
        this.type = type;
    }

    public void accept(ASTVisitor v)
    {
        v.visit(this);
    }
}
