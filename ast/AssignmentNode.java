package assign6.ast;

import assign6.lexer.Type;
import assign6.visitor.*;

public class AssignmentNode extends StatementNode
{
    // **for the future version of Compiler, it should not be IdentifierNode, it should be Array for final version**
    public IdentifierNode left;
    public ExprNode right;

    public AssignmentNode()
    {

    }

    //public AssignmentNode(IdentifierNode, id, BinExprNode right){
    public AssignmentNode(IdentifierNode id, ExprNode right)
    {
        this.left = id;
        this.right = right;
    }

    public void accept(ASTVisitor v)
    {
        v.visit(this);
    }
}
