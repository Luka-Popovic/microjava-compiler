// generated with ast extension for cup
// version 0.8
// 6/1/2018 13:54:6


package rs.ac.bg.etf.pp1.ast;

public class FactorNewTypeWithExpr extends Factor {

    private Type Type;
    private PotentialExpr PotentialExpr;

    public FactorNewTypeWithExpr (Type Type, PotentialExpr PotentialExpr) {
        this.Type=Type;
        if(Type!=null) Type.setParent(this);
        this.PotentialExpr=PotentialExpr;
        if(PotentialExpr!=null) PotentialExpr.setParent(this);
    }

    public Type getType() {
        return Type;
    }

    public void setType(Type Type) {
        this.Type=Type;
    }

    public PotentialExpr getPotentialExpr() {
        return PotentialExpr;
    }

    public void setPotentialExpr(PotentialExpr PotentialExpr) {
        this.PotentialExpr=PotentialExpr;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Type!=null) Type.accept(visitor);
        if(PotentialExpr!=null) PotentialExpr.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Type!=null) Type.traverseTopDown(visitor);
        if(PotentialExpr!=null) PotentialExpr.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Type!=null) Type.traverseBottomUp(visitor);
        if(PotentialExpr!=null) PotentialExpr.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("FactorNewTypeWithExpr(\n");

        if(Type!=null)
            buffer.append(Type.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(PotentialExpr!=null)
            buffer.append(PotentialExpr.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FactorNewTypeWithExpr]");
        return buffer.toString();
    }
}
