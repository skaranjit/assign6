package assign6.parser;

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
