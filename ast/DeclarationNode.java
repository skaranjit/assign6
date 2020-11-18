package assign6.ast;

import assign6.visitor.*;


public class DeclarationNode extends StatementNode
{
    public TypeNode type;
    public IdentifierNode id;

    public DeclarationNode()
    {

    }

    public DeclarationNode(TypeNode type, IdentifierNode id)
    {
        this.type = type;
        this.id = id;
    }

    public void accept(ASTVisitor v)
    {
        v.visit(this);
    }

}
