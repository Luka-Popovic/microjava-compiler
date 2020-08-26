// generated with ast extension for cup
// version 0.8
// 6/1/2018 13:54:6


package rs.ac.bg.etf.pp1.ast;

public class ConstDeclarationParam extends ConstDeclParam {

    private ConstDeclParam ConstDeclParam;
    private ConstDeclarIdent ConstDeclarIdent;
    private ConstTypeList ConstTypeList;

    public ConstDeclarationParam (ConstDeclParam ConstDeclParam, ConstDeclarIdent ConstDeclarIdent, ConstTypeList ConstTypeList) {
        this.ConstDeclParam=ConstDeclParam;
        if(ConstDeclParam!=null) ConstDeclParam.setParent(this);
        this.ConstDeclarIdent=ConstDeclarIdent;
        if(ConstDeclarIdent!=null) ConstDeclarIdent.setParent(this);
        this.ConstTypeList=ConstTypeList;
        if(ConstTypeList!=null) ConstTypeList.setParent(this);
    }

    public ConstDeclParam getConstDeclParam() {
        return ConstDeclParam;
    }

    public void setConstDeclParam(ConstDeclParam ConstDeclParam) {
        this.ConstDeclParam=ConstDeclParam;
    }

    public ConstDeclarIdent getConstDeclarIdent() {
        return ConstDeclarIdent;
    }

    public void setConstDeclarIdent(ConstDeclarIdent ConstDeclarIdent) {
        this.ConstDeclarIdent=ConstDeclarIdent;
    }

    public ConstTypeList getConstTypeList() {
        return ConstTypeList;
    }

    public void setConstTypeList(ConstTypeList ConstTypeList) {
        this.ConstTypeList=ConstTypeList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ConstDeclParam!=null) ConstDeclParam.accept(visitor);
        if(ConstDeclarIdent!=null) ConstDeclarIdent.accept(visitor);
        if(ConstTypeList!=null) ConstTypeList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ConstDeclParam!=null) ConstDeclParam.traverseTopDown(visitor);
        if(ConstDeclarIdent!=null) ConstDeclarIdent.traverseTopDown(visitor);
        if(ConstTypeList!=null) ConstTypeList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ConstDeclParam!=null) ConstDeclParam.traverseBottomUp(visitor);
        if(ConstDeclarIdent!=null) ConstDeclarIdent.traverseBottomUp(visitor);
        if(ConstTypeList!=null) ConstTypeList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ConstDeclarationParam(\n");

        if(ConstDeclParam!=null)
            buffer.append(ConstDeclParam.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ConstDeclarIdent!=null)
            buffer.append(ConstDeclarIdent.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ConstTypeList!=null)
            buffer.append(ConstTypeList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ConstDeclarationParam]");
        return buffer.toString();
    }
}
