package assign5.parser;

import assign5.visitor.*;


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
