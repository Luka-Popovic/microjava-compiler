// generated with ast extension for cup
// version 0.8
// 6/1/2018 13:54:6


package rs.ac.bg.etf.pp1.ast;

public class VarDeclarationParam extends VarDeclParam {

    private VarDeclParam VarDeclParam;
    private VarDeclList VarDeclList;

    public VarDeclarationParam (VarDeclParam VarDeclParam, VarDeclList VarDeclList) {
        this.VarDeclParam=VarDeclParam;
        if(VarDeclParam!=null) VarDeclParam.setParent(this);
        this.VarDeclList=VarDeclList;
        if(VarDeclList!=null) VarDeclList.setParent(this);
    }

    public VarDeclParam getVarDeclParam() {
        return VarDeclParam;
    }

    public void setVarDeclParam(VarDeclParam VarDeclParam) {
        this.VarDeclParam=VarDeclParam;
    }

    public VarDeclList getVarDeclList() {
        return VarDeclList;
    }

    public void setVarDeclList(VarDeclList VarDeclList) {
        this.VarDeclList=VarDeclList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(VarDeclParam!=null) VarDeclParam.accept(visitor);
        if(VarDeclList!=null) VarDeclList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(VarDeclParam!=null) VarDeclParam.traverseTopDown(visitor);
        if(VarDeclList!=null) VarDeclList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(VarDeclParam!=null) VarDeclParam.traverseBottomUp(visitor);
        if(VarDeclList!=null) VarDeclList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("VarDeclarationParam(\n");

        if(VarDeclParam!=null)
            buffer.append(VarDeclParam.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(VarDeclList!=null)
            buffer.append(VarDeclList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [VarDeclarationParam]");
        return buffer.toString();
    }
}
