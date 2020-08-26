// generated with ast extension for cup
// version 0.8
// 6/1/2018 13:54:6


package rs.ac.bg.etf.pp1.ast;

public class MatchedIf extends Matched {

    private IfBothPart IfBothPart;
    private Matched Matched;

    public MatchedIf (IfBothPart IfBothPart, Matched Matched) {
        this.IfBothPart=IfBothPart;
        if(IfBothPart!=null) IfBothPart.setParent(this);
        this.Matched=Matched;
        if(Matched!=null) Matched.setParent(this);
    }

    public IfBothPart getIfBothPart() {
        return IfBothPart;
    }

    public void setIfBothPart(IfBothPart IfBothPart) {
        this.IfBothPart=IfBothPart;
    }

    public Matched getMatched() {
        return Matched;
    }

    public void setMatched(Matched Matched) {
        this.Matched=Matched;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(IfBothPart!=null) IfBothPart.accept(visitor);
        if(Matched!=null) Matched.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(IfBothPart!=null) IfBothPart.traverseTopDown(visitor);
        if(Matched!=null) Matched.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(IfBothPart!=null) IfBothPart.traverseBottomUp(visitor);
        if(Matched!=null) Matched.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("MatchedIf(\n");

        if(IfBothPart!=null)
            buffer.append(IfBothPart.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Matched!=null)
            buffer.append(Matched.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [MatchedIf]");
        return buffer.toString();
    }
}