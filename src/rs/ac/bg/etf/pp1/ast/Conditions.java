// generated with ast extension for cup
// version 0.8
// 6/1/2018 13:54:6


package rs.ac.bg.etf.pp1.ast;

public class Conditions extends Condition {

    private CondTermLogicalOr CondTermLogicalOr;
    private CondTerm CondTerm;

    public Conditions (CondTermLogicalOr CondTermLogicalOr, CondTerm CondTerm) {
        this.CondTermLogicalOr=CondTermLogicalOr;
        if(CondTermLogicalOr!=null) CondTermLogicalOr.setParent(this);
        this.CondTerm=CondTerm;
        if(CondTerm!=null) CondTerm.setParent(this);
    }

    public CondTermLogicalOr getCondTermLogicalOr() {
        return CondTermLogicalOr;
    }

    public void setCondTermLogicalOr(CondTermLogicalOr CondTermLogicalOr) {
        this.CondTermLogicalOr=CondTermLogicalOr;
    }

    public CondTerm getCondTerm() {
        return CondTerm;
    }

    public void setCondTerm(CondTerm CondTerm) {
        this.CondTerm=CondTerm;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(CondTermLogicalOr!=null) CondTermLogicalOr.accept(visitor);
        if(CondTerm!=null) CondTerm.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(CondTermLogicalOr!=null) CondTermLogicalOr.traverseTopDown(visitor);
        if(CondTerm!=null) CondTerm.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(CondTermLogicalOr!=null) CondTermLogicalOr.traverseBottomUp(visitor);
        if(CondTerm!=null) CondTerm.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("Conditions(\n");

        if(CondTermLogicalOr!=null)
            buffer.append(CondTermLogicalOr.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(CondTerm!=null)
            buffer.append(CondTerm.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [Conditions]");
        return buffer.toString();
    }
}
