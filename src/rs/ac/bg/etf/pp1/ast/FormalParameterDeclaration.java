// generated with ast extension for cup
// version 0.8
// 6/1/2018 13:54:6


package rs.ac.bg.etf.pp1.ast;

public class FormalParameterDeclaration extends FormalParamDecl {

    private FormalParamName FormalParamName;

    public FormalParameterDeclaration (FormalParamName FormalParamName) {
        this.FormalParamName=FormalParamName;
        if(FormalParamName!=null) FormalParamName.setParent(this);
    }

    public FormalParamName getFormalParamName() {
        return FormalParamName;
    }

    public void setFormalParamName(FormalParamName FormalParamName) {
        this.FormalParamName=FormalParamName;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(FormalParamName!=null) FormalParamName.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(FormalParamName!=null) FormalParamName.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(FormalParamName!=null) FormalParamName.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("FormalParameterDeclaration(\n");

        if(FormalParamName!=null)
            buffer.append(FormalParamName.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FormalParameterDeclaration]");
        return buffer.toString();
    }
}
