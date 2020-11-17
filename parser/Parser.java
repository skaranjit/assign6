package assign6.parser;

import assign6.ast.*;
import assign6.lexer.*;
import assign6.visitor.ASTVisitor;


import java.io.IOException;

public class Parser extends ASTVisitor {
    public CompilationUnit cu = null;
    public Lexer lexer = null;
    public Token look = null;
    public Token stmt = null;

    public Parser(Lexer lexer) {

        this.lexer = lexer;
        cu = new CompilationUnit();
        move();

        visit(cu);
    }

    public Parser() {

        cu = new CompilationUnit();
        move();

        visit(cu);
    }

    ////////////////////////////////////////
    //  Utility mothods
    ////////////////////////////////////////

    void move() {

        try {
       //         System.out.println(" Gone to lexer");
            look = lexer.scan();

        } catch (IOException e) {

            System.out.println("IOException");
        }
    }


    void error(String s) {

        throw new Error("near line " + lexer.line + ": " + s);
    }

    void match(int t) {

        try {

            if (look.tag == t) {
     //           System.out.println(" Matched");
                move();
            }else
                error("Syntax error");
        } catch (Error e) {


        }
    }


    ////////////////////////////////////////

    public void visit(CompilationUnit n) {

        System.out.println(" Compilation Unit");
        n.block = new BlockStatement();
        n.block.accept(this);
    }


    public void visit(BlockStatement n) {
        System.out.println(" BlockStatement");
        if (look.tag == '{') {
            //n.blck=look;
            //System.out.println(" Match with '{'");
            match('{');
            System.out.println(look.toString());

            n.decls= new Declarations();
            n.decls.accept(this);

            n.stmts= new Statements();
            n.stmts.accept(this);

        }

    }


    public void visit(Declarations n){
       // for(int i=0; i<level;i++){ System.out.print(indent); }
        System.out.println(" Declarations"+look.toString());
        if(look.tag== Tag.BASICS){
        n.decl= new DeclarationNode();
        //level++;
        n.decl.accept(this);
        //level--;

         n.decls= new Declarations();
        n.decls.accept(this);
         }else{System.out.println(" End of declarations");}
      //
    }
    public void visit (DeclarationNode n){

       // for(int i=0; i<level;i++){ System.out.print(indent); }
        System.out.println(" DeclarationNode"+look.toString());

        n.type= new TypeNode();
        //level++;
        n.type.accept(this);
        //level--;

        n.id = new IdentifierNode();
        //level++;
        n.id.accept(this);
        //System.out.println(" Hi");
        //level--;
        System.out.println(look.toString());
        match(';');
    }

    public void visit(TypeNode n){
        //for(int i=0; i<level;i++){ System.out.print(indent); }
        System.out.println(" TypeNode");

        if(look.toString().equals("int")){ n.basic= Type.Int; }
        else if(look.toString().equals("float")){n.basic=Type.Float;}

        match(look.tag);
System.out.println(look.toString()+" in Type");
        if(look.tag=='['){
            System.out.println(" Array found");
            n.array= new ArrayTypeNode();
          //  level++;
            n.array.accept(this);
            //level--;
        }
    }

    public void visit(IdentifierNode n){
        System.out.println(" In Identifier Node");
        n.id= look.toString();
        match(Tag.ID);
       // for(int i=0; i<level;i++){ System.out.print(indent); }
        n.printNode();
    }

    public void visit( ArrayTypeNode n){
      //  for(int i=0; i<level;i++){ System.out.print(indent); }
        System.out.println(" ArrayTypeNode"+look);
        match('[');
        n.size=((Num)look).value;
        //for(int i=0; i<level;i++){ System.out.print(indent); }
        System.out.println(" ArrayTypeNode"+((Num)look).value);


        match(Tag.NUM);
        match(']');
        // checking multi-dimensional array
        if(look.tag=='['){
            n.type = new ArrayTypeNode();
          //  level++;
            n.type.accept(this);
            //level--;
        }
    }

    public void visit(Statements n){
        System.out.println(" In Statements Node");
        if(look.tag!='}') {

           // level ++;
            n.stmt=parseStatementNode(n.stmt);
            //level--;

           // n.stmts= new Statements();
            //level++;
            //n.stmts.accept(this);
           // level--;

        }

    }
    public StatementNode parseStatementNode(StatementNode stmt){
      //  for(int i=0;i<level;i++){System.out.println(indent);}
        System.out.println(" look in parseStatement is "+look.toString());
        switch (look.tag){
            case Tag.ID:
             // stmt.assign = new AssignmentNode();
                ((AssignmentNode)stmt.assign).accept(this);
                return stmt;

            case Tag.IF:

                stmt= new IfStatementNode();
                ((IfStatementNode)stmt).accept(this);
                return  stmt;


            case Tag.WHILE:
                stmt= new WhileStatementNode();
                ((WhileStatementNode)stmt).accept(this);
                return stmt;
            case Tag.DO:
                stmt= new DoWhileStatementNode();
                ((DoWhileStatementNode)stmt).accept(this);
                return stmt;
            case Tag.BREAK:
                stmt= new BreakStatementNode();
                ((BreakStatementNode)stmt).accept(this);
                return stmt;
            default:return null;


        }

    }


    public void visit(WhileStatementNode n){
     //   for(int i=0;i<level;i++){System.out.println(indent);}
        System.out.println("WhileStatementNode");

        match(Tag.WHILE);
       // for(int i=0;i<level;i++){System.out.println(indent);}
        System.out.println("WHILE");

        n.cond= new ParenNode();
        //level++;
        n.cond.accept(this);
        //level--;
System.out.println("in While look is"+look.toString());
        if(look.tag=='{'){
            n.stmt= new BlockStatement();
          //  level++;
            ((BlockStatement)n.stmt).accept(this);
           // level--;
        }else{
            n.stmt=parseStatementNode(n.stmt);
        }
    }

    public void visit(ParenNode n){

        // (ExprNode)(a[(i+j)])
     //   for(int i=0;i<level;i++){System.out.println(indent);}
        System.out.println(" Parenthesis Node"+look.toString());
        match('(');

        if(look.tag=='('){
            n.expr= new ParenNode();
       //     level++;
            ((ParenNode)n.expr).accept(this);
         //   level--;
        }else if(look.tag==Tag.ID){  // (i)
            n.expr= new IdentifierNode();
           // level++;
            ((IdentifierNode)n.expr).accept(this);
            //level --;

            if(look.tag=='['){
                n.expr=parseArrayAccessNode((IdentifierNode)n.expr);
            }
        }else if(look.tag==Tag.NUM){
            n.expr= new NumNode();
            //level++;
            ((NumNode)n.expr).accept(this);
            //level--;
        }else if(look.tag==Tag.REAL){
            n.expr= new RealNode();
            ((RealNode)n.expr).accept(this);
        }else if(look.tag==Tag.TRUE){
            n.expr= new TrueNode();
            //level++;
            ((TrueNode)n.expr).accept(this);
        }else if(look.tag==Tag.FALSE){
            n.expr= new FalseNode();
            //level++;
            ((FalseNode)n.expr).accept(this);
            //level--;
        }

        if(look.tag!=')'){  //(i+j)
            //level++;
            n.expr=parseBinExprNode(n.expr,0);
            //level--;
        }
        match(')');

    }

    ExprNode parseArrayAccessNode(IdentifierNode id){
      //  for(int i=0; i<level;i++){ System.out.print(indent); }
        System.out.println(" parse array access node");
        ExprNode index= new ArrayDimsNode();
        //level++;
        index.accept(this);
        //level--;
        return new ArrayAccessNode(id,index);

    }
    Node parseBinExprNode(Node lhs, int precedence){
        while(getPrecedence(look.tag)>= precedence){
            Token token_op=look;
            int op= getPrecedence(look.tag);
            move();
     //       for(int i=0; i<level;i++){ System.out.print(indent); }

            Node rhs= null;
            if(look.tag=='('){
                rhs=new ParenNode();
       //         level++;
                ((ParenNode)rhs).accept(this);
         //       level--;
            }
            else if(look.tag== Tag.ID){
                rhs= new IdentifierNode();
           //     level++;
                ((IdentifierNode)rhs).accept(this);
             //   level--;
                if(look.tag=='['){
                    rhs=parseArrayAccessNode(((IdentifierNode)rhs));
                }
            }else if (look.tag==Tag.NUM){
                rhs= new NumNode();
               // level++;
                ((NumNode)rhs).accept(this);
                //level--;
            }else if(look.tag==Tag.REAL){
                rhs= new RealNode();
                //level++;
                ((RealNode)rhs).accept(this);
            //    level--;
            }
            //for(int i=0; i<level;i++){ System.out.print(indent); }
            System.out.println(" operator"+look);

            while(getPrecedence(look.tag)>op){
                rhs= parseBinExprNode(rhs,getPrecedence(look.tag));
            }
            lhs= new BinExprNode(token_op,lhs,rhs);

        }
        return lhs;
    }

    int getPrecedence(int op){
        switch(op){
            case '*':case '/':case '%': return 12; // multiplicative
            case '+': case'-':return 11;  // additive
            case '>':case '<': return 9;
            case Tag.LE: case Tag.GE: return 9;
            case Tag.EQ: case Tag.NE: return 8;
            default:
                return -1;
        }
    }
    public void visit(AssignmentNode n){
     //   for(int i=0; i<level;i++){ System.out.print(indent); }
        System.out.println(" In Assignment Node");

        n.id= new IdentifierNode();
       // level++;
        n.id.accept(this);
        //level--;

        if(look.tag=='['){
            n.right=parseArrayAccessNode((IdentifierNode)n.id);
        }



        match('=');

        //for(int i=0; i<level;i++){ System.out.print(indent); }
        System.out.println(" Operator =");


        Node rhs_assign= null;
        if(look.tag=='('){
            rhs_assign= new ParenNode();
          //  level++;
            ((ParenNode)rhs_assign).accept(this);
            //level--;
        } else if(look.tag == Tag.ID){ //(i)+j [i]
            rhs_assign= new IdentifierNode();
            //level++;
            ((IdentifierNode)rhs_assign).accept(this);
            //level--;

            if(look.tag=='['){
                rhs_assign=parseArrayAccessNode(((IdentifierNode)rhs_assign));
            }
        }else if(look.tag == Tag.NUM){
            rhs_assign= new NumNode();
            //level ++;
            ((NumNode)rhs_assign).accept(this);
            //level--;

        }else if(look.tag==Tag.REAL){
            rhs_assign= new RealNode();
            //level++;
            ((RealNode)rhs_assign).accept(this);
            //level--;
        }


        if(look.tag==';'){
            n.right= rhs_assign;
        }else{
            //for(int i=0; i<level;i++){ System.out.print(indent); }
            System.out.println(" operator "+look);
            //level++;
            n.right=((BinExprNode)parseBinExprNode(rhs_assign,0));
            //level--;

            //  System.out.println("***Root Node Operator"+ ((BinExprNode)n.right).op);
        }

        match(';');

    }





    public void visit(DoWhileStatementNode n){
       // for(int i=0;i<level;i++){System.out.println(indent);}
        System.out.println("DoWhileStatementNode");

        match(Tag.DO);
        //for(int i=0;i<level;i++){System.out.println(indent);}
        System.out.println("Do");

        if(look.tag== '{'){
          //  n.stmt= new BlockStatement();
            //level++;
            ((BlockStatement)n.stmt).accept(this);
            //level--;
        }else{
            n.stmt=parseStatementNode(n.stmt);
        }

        match(Tag.WHILE);
        //for(int i=0;i<level;i++){System.out.println(indent);}
        System.out.println("Do's While");
        n.cond= new ParenNode();
        //level++;
        n.cond.accept(this);
        //level--;
        match(';');
    }

    public void visit ( BinExprNode n){ // (i[a]+j)
     //   for(int i=0; i<level;i++){ System.out.print(indent); }
        /*System.out.println(" In BinNode");

        if(look.tag=='('){
            n.left= new ParenNode();
       //     level++;
            ((ParenNode)n.left).accept(this);
         //   level--;
        }else if(look.tag==Tag.ID){
            n.left= new IdentifierNode();
           // level++;
            ((IdentifierNode)n.left).accept(this);
            //level--;

            if(look.tag=='['){
                n.left=parseArrayAccessNode(((IdentifierNode)n.left));
            }
        }else if(look.tag==Tag.NUM){
            n.left= new NumNode();
            //level++;
            ((NumNode)n.left).accept(this);
            //level--;
        }else if(look.tag==Tag.REAL){
            n.left= new RealNode();
            //level++;
            ((RealNode)n.left).accept(this);
            //level--;

         */
        /*}


        //for(int i=0; i<level;i++){ System.out.print(indent); }
        System.out.println(" &&&& BinExpr In operator"+look);

        //level++;
        BinExprNode binary=(BinExprNode)parseBinExprNode(n.left,0);
        n.op= binary.op;
        n.right=binary.right;
        //level--;*/
    }






    public void visit(TrueNode n){
       // for(int i=0; i<level;i++){ System.out.print(indent); }
        System.out.println(" In TrueNode");
        match(Tag.TRUE);
    }
    public void visit(FalseNode n){
       // for(int i=0; i<level;i++){ System.out.print(indent); }
        System.out.println(" In FalseNode");
        match(Tag.FALSE);
    }


}

