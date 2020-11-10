package assign6.ast;

import assign6.visitor.ASTVisitor;

public class BreakNode extends StatementNode
{
    public BreakNode()
    {

    }

    public void accept(ASTVisitor v)
    {
        v.visit(this);
    }

}
