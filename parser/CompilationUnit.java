package assign6.parser;

import assign6.visitor.*;


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
