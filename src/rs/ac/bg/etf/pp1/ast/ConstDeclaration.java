// generated with ast extension for cup
// version 0.8
// 6/1/2018 13:54:6


package rs.ac.bg.etf.pp1.ast;

public class ConstDeclaration extends ConstDecl {

    private ConstTypeName ConstTypeName;
    private ConstTypeList ConstTypeList;
    private ConstDeclParam ConstDeclParam;

    public ConstDeclaration (ConstTypeName ConstTypeName, ConstTypeList ConstTypeList, ConstDeclParam ConstDeclParam) {
        this.ConstTypeName=ConstTypeName;
        if(ConstTypeName!=null) ConstTypeName.setParent(this);
        this.ConstTypeList=ConstTypeList;
        if(ConstTypeList!=null) ConstTypeList.setParent(this);
        this.ConstDeclParam=ConstDeclParam;
        if(ConstDeclParam!=null) ConstDeclParam.setParent(this);
    }

    public ConstTypeName getConstTypeName() {
        return ConstTypeName;
    }

    public void setConstTypeName(ConstTypeName ConstTypeName) {
        this.ConstTypeName=ConstTypeName;
    }

    public ConstTypeList getConstTypeList() {
        return ConstTypeList;
    }

    public void setConstTypeList(ConstTypeList ConstTypeList) {
        this.ConstTypeList=ConstTypeList;
    }

    public ConstDeclParam getConstDeclParam() {
        return ConstDeclParam;
    }

    public void setConstDeclParam(ConstDeclParam ConstDeclParam) {
        this.ConstDeclParam=ConstDeclParam;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ConstTypeName!=null) ConstTypeName.accept(visitor);
        if(ConstTypeList!=null) ConstTypeList.accept(visitor);
        if(ConstDeclParam!=null) ConstDeclParam.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ConstTypeName!=null) ConstTypeName.traverseTopDown(visitor);
        if(ConstTypeList!=null) ConstTypeList.traverseTopDown(visitor);
        if(ConstDeclParam!=null) ConstDeclParam.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ConstTypeName!=null) ConstTypeName.traverseBottomUp(visitor);
        if(ConstTypeList!=null) ConstTypeList.traverseBottomUp(visitor);
        if(ConstDeclParam!=null) ConstDeclParam.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ConstDeclaration(\n");

        if(ConstTypeName!=null)
            buffer.append(ConstTypeName.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ConstTypeList!=null)
            buffer.append(ConstTypeList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ConstDeclParam!=null)
            buffer.append(ConstDeclParam.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ConstDeclaration]");
        return buffer.toString();
    }
}
