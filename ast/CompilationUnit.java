package assign5.ast;

import assign5.visitor.*;

// CompilationUnit is root node
// BlockStatementNode is just below CompilationUnit (i.e. - child of CompilationUnit)

public class CompilationUnit extends Node
{
    public BlockStatementNode block;

    public CompilationUnit()
    {

    }

    public CompilationUnit(BlockStatementNode assign)
    {
        this.block = block;
    }

    public void accept(ASTVisitor v)
    {
        v.visit(this);
    }

}
