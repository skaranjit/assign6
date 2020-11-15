package assign6.ast;

import assign6.visitor.*;
import assign6.lexer.*;

public class IdentifierNode extends ExprNode
{
    public String id;
    public ArrayIDNode array;
    public Word w;


    public IdentifierNode()
    {

    }

    public IdentifierNode(Word w)
    {
        this.id = w.lexeme;
        this.w = w;
    }

    public void accept(ASTVisitor v)
    {
        v.visit(this);
    }

    public void printNode()
    {
        System.out.println("IdentifierNode: " + id);
    }

}
