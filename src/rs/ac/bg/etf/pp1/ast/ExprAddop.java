// generated with ast extension for cup
// version 0.8
// 6/1/2018 13:54:6


package rs.ac.bg.etf.pp1.ast;

public class ExprAddop extends ExprList {

    private ExprAdd ExprAdd;
    private Addop Addop;

    public ExprAddop (ExprAdd ExprAdd, Addop Addop) {
        this.ExprAdd=ExprAdd;
        if(ExprAdd!=null) ExprAdd.setParent(this);
        this.Addop=Addop;
        if(Addop!=null) Addop.setParent(this);
    }

    public ExprAdd getExprAdd() {
        return ExprAdd;
    }

    public void setExprAdd(ExprAdd ExprAdd) {
        this.ExprAdd=ExprAdd;
    }

    public Addop getAddop() {
        return Addop;
    }

    public void setAddop(Addop Addop) {
        this.Addop=Addop;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ExprAdd!=null) ExprAdd.accept(visitor);
        if(Addop!=null) Addop.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ExprAdd!=null) ExprAdd.traverseTopDown(visitor);
        if(Addop!=null) Addop.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ExprAdd!=null) ExprAdd.traverseBottomUp(visitor);
        if(Addop!=null) Addop.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ExprAddop(\n");

        if(ExprAdd!=null)
            buffer.append(ExprAdd.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Addop!=null)
            buffer.append(Addop.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ExprAddop]");
        return buffer.toString();
    }
}
