package assign5.ast;

import assign5.visitor.ASTVisitor;

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
