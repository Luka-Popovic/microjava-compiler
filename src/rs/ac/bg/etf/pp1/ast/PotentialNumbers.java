// generated with ast extension for cup
// version 0.8
// 6/1/2018 13:54:6


package rs.ac.bg.etf.pp1.ast;

public class PotentialNumbers extends PotentialNumber {

    private Integer N1;

    public PotentialNumbers (Integer N1) {
        this.N1=N1;
    }

    public Integer getN1() {
        return N1;
    }

    public void setN1(Integer N1) {
        this.N1=N1;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("PotentialNumbers(\n");

        buffer.append(" "+tab+N1);
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [PotentialNumbers]");
        return buffer.toString();
    }
}
