// generated with ast extension for cup
// version 0.8
// 6/1/2018 13:54:6


package rs.ac.bg.etf.pp1.ast;

public class PrintStmt extends Matched {

    private Expr Expr;
    private PotentialNumber PotentialNumber;

    public PrintStmt (Expr Expr, PotentialNumber PotentialNumber) {
        this.Expr=Expr;
        if(Expr!=null) Expr.setParent(this);
        this.PotentialNumber=PotentialNumber;
        if(PotentialNumber!=null) PotentialNumber.setParent(this);
    }

    public Expr getExpr() {
        return Expr;
    }

    public void setExpr(Expr Expr) {
        this.Expr=Expr;
    }

    public PotentialNumber getPotentialNumber() {
        return PotentialNumber;
    }

    public void setPotentialNumber(PotentialNumber PotentialNumber) {
        this.PotentialNumber=PotentialNumber;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Expr!=null) Expr.accept(visitor);
        if(PotentialNumber!=null) PotentialNumber.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Expr!=null) Expr.traverseTopDown(visitor);
        if(PotentialNumber!=null) PotentialNumber.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Expr!=null) Expr.traverseBottomUp(visitor);
        if(PotentialNumber!=null) PotentialNumber.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("PrintStmt(\n");

        if(Expr!=null)
            buffer.append(Expr.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(PotentialNumber!=null)
            buffer.append(PotentialNumber.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [PrintStmt]");
        return buffer.toString();
    }
}
