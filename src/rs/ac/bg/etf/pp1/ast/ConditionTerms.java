// generated with ast extension for cup
// version 0.8
// 6/1/2018 13:54:6


package rs.ac.bg.etf.pp1.ast;

public class ConditionTerms extends CondTerm {

    private CondTermLogicalAnd CondTermLogicalAnd;
    private CondFact CondFact;

    public ConditionTerms (CondTermLogicalAnd CondTermLogicalAnd, CondFact CondFact) {
        this.CondTermLogicalAnd=CondTermLogicalAnd;
        if(CondTermLogicalAnd!=null) CondTermLogicalAnd.setParent(this);
        this.CondFact=CondFact;
        if(CondFact!=null) CondFact.setParent(this);
    }

    public CondTermLogicalAnd getCondTermLogicalAnd() {
        return CondTermLogicalAnd;
    }

    public void setCondTermLogicalAnd(CondTermLogicalAnd CondTermLogicalAnd) {
        this.CondTermLogicalAnd=CondTermLogicalAnd;
    }

    public CondFact getCondFact() {
        return CondFact;
    }

    public void setCondFact(CondFact CondFact) {
        this.CondFact=CondFact;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(CondTermLogicalAnd!=null) CondTermLogicalAnd.accept(visitor);
        if(CondFact!=null) CondFact.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(CondTermLogicalAnd!=null) CondTermLogicalAnd.traverseTopDown(visitor);
        if(CondFact!=null) CondFact.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(CondTermLogicalAnd!=null) CondTermLogicalAnd.traverseBottomUp(visitor);
        if(CondFact!=null) CondFact.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ConditionTerms(\n");

        if(CondTermLogicalAnd!=null)
            buffer.append(CondTermLogicalAnd.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(CondFact!=null)
            buffer.append(CondFact.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ConditionTerms]");
        return buffer.toString();
    }
}
