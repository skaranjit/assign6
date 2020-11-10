package assign6.parser;

import assign6.visitor.*;

public class AssignmentNode extends StatementNode
{
    public IdentifierNode left;
    public Node right;

    public AssignmentNode()
    {

    }

    public AssignmentNode(IdentifierNode id, Node right)
    {
        this.left = id;
        this.right = right;
    }

    public void accept(ASTVisitor v)
    {
        v.visit(this);
    }
}
