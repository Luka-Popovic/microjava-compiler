// generated with ast extension for cup
// version 0.8
// 6/1/2018 13:54:6


package rs.ac.bg.etf.pp1.ast;

public class IfPart extends IfBothPart {

    private DoMathcPart DoMathcPart;
    private Matched Matched;

    public IfPart (DoMathcPart DoMathcPart, Matched Matched) {
        this.DoMathcPart=DoMathcPart;
        if(DoMathcPart!=null) DoMathcPart.setParent(this);
        this.Matched=Matched;
        if(Matched!=null) Matched.setParent(this);
    }

    public DoMathcPart getDoMathcPart() {
        return DoMathcPart;
    }

    public void setDoMathcPart(DoMathcPart DoMathcPart) {
        this.DoMathcPart=DoMathcPart;
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
        if(DoMathcPart!=null) DoMathcPart.accept(visitor);
        if(Matched!=null) Matched.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(DoMathcPart!=null) DoMathcPart.traverseTopDown(visitor);
        if(Matched!=null) Matched.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(DoMathcPart!=null) DoMathcPart.traverseBottomUp(visitor);
        if(Matched!=null) Matched.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("IfPart(\n");

        if(DoMathcPart!=null)
            buffer.append(DoMathcPart.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Matched!=null)
            buffer.append(Matched.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [IfPart]");
        return buffer.toString();
    }
}
