package assign6.ast;

import assign6.visitor.*;


public class Declarations extends Node
{
    public Declarations decls;
    public DeclarationNode decl;

    public Declarations()
    {

    }

    public Declarations(Declarations decls, DeclarationNode decl)
    {
        this.decls = decls;
        this.decl = decl;
    }

    public void accept(ASTVisitor v)
    {
        v.visit(this);
    }

}
