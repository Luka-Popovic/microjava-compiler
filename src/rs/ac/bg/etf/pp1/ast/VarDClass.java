// generated with ast extension for cup
// version 0.8
// 6/1/2018 13:54:6


package rs.ac.bg.etf.pp1.ast;

public class VarDClass extends VarDeclParamClass {

    private VarDeclParam VarDeclParam;

    public VarDClass (VarDeclParam VarDeclParam) {
        this.VarDeclParam=VarDeclParam;
        if(VarDeclParam!=null) VarDeclParam.setParent(this);
    }

    public VarDeclParam getVarDeclParam() {
        return VarDeclParam;
    }

    public void setVarDeclParam(VarDeclParam VarDeclParam) {
        this.VarDeclParam=VarDeclParam;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(VarDeclParam!=null) VarDeclParam.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(VarDeclParam!=null) VarDeclParam.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(VarDeclParam!=null) VarDeclParam.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("VarDClass(\n");

        if(VarDeclParam!=null)
            buffer.append(VarDeclParam.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [VarDClass]");
        return buffer.toString();
    }
}
