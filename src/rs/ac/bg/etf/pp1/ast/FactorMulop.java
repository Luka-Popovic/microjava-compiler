// generated with ast extension for cup
// version 0.8
// 6/1/2018 13:54:6


package rs.ac.bg.etf.pp1.ast;

public class FactorMulop extends TermMulop {

    private TermMul TermMul;
    private Mulop Mulop;

    public FactorMulop (TermMul TermMul, Mulop Mulop) {
        this.TermMul=TermMul;
        if(TermMul!=null) TermMul.setParent(this);
        this.Mulop=Mulop;
        if(Mulop!=null) Mulop.setParent(this);
    }

    public TermMul getTermMul() {
        return TermMul;
    }

    public void setTermMul(TermMul TermMul) {
        this.TermMul=TermMul;
    }

    public Mulop getMulop() {
        return Mulop;
    }

    public void setMulop(Mulop Mulop) {
        this.Mulop=Mulop;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(TermMul!=null) TermMul.accept(visitor);
        if(Mulop!=null) Mulop.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(TermMul!=null) TermMul.traverseTopDown(visitor);
        if(Mulop!=null) Mulop.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(TermMul!=null) TermMul.traverseBottomUp(visitor);
        if(Mulop!=null) Mulop.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("FactorMulop(\n");

        if(TermMul!=null)
            buffer.append(TermMul.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Mulop!=null)
            buffer.append(Mulop.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FactorMulop]");
        return buffer.toString();
    }
}
