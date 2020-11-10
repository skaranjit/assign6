package assign5.ast;

import assign5.visitor.*;


public class Statements extends Node
{
    public Statements stmts;
    public StatementNode stmt;

    public Statements()
    {

    }

    public Statements(Statements stmts, AssignmentNode assign)
    {
        this.stmts = stmts;
        this.stmt = assign;
    }

    public void accept(ASTVisitor v)
    {
        v.visit(this);
    }

}
