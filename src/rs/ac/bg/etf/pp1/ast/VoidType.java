// generated with ast extension for cup
// version 0.8
// 6/1/2018 13:54:6


package rs.ac.bg.etf.pp1.ast;

public class VoidType implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    public rs.etf.pp1.symboltable.concepts.Obj obj = null;

    private String voidMethName;

    public VoidType (String voidMethName) {
        this.voidMethName=voidMethName;
    }

    public String getVoidMethName() {
        return voidMethName;
    }

    public void setVoidMethName(String voidMethName) {
        this.voidMethName=voidMethName;
    }

    public SyntaxNode getParent() {
        return parent;
    }

    public void setParent(SyntaxNode parent) {
        this.parent=parent;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line=line;
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
        buffer.append("VoidType(\n");

        buffer.append(" "+tab+voidMethName);
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [VoidType]");
        return buffer.toString();
    }
}
