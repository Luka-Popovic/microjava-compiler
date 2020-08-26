// generated with ast extension for cup
// version 0.8
// 6/1/2018 13:54:6


package rs.ac.bg.etf.pp1.ast;

public class MethodDeclarationVoid extends MethodDecl {

    private VoidType VoidType;
    private FormalPars FormalPars;
    private VarDeclParam VarDeclParam;
    private StatementList StatementList;

    public MethodDeclarationVoid (VoidType VoidType, FormalPars FormalPars, VarDeclParam VarDeclParam, StatementList StatementList) {
        this.VoidType=VoidType;
        if(VoidType!=null) VoidType.setParent(this);
        this.FormalPars=FormalPars;
        if(FormalPars!=null) FormalPars.setParent(this);
        this.VarDeclParam=VarDeclParam;
        if(VarDeclParam!=null) VarDeclParam.setParent(this);
        this.StatementList=StatementList;
        if(StatementList!=null) StatementList.setParent(this);
    }

    public VoidType getVoidType() {
        return VoidType;
    }

    public void setVoidType(VoidType VoidType) {
        this.VoidType=VoidType;
    }

    public FormalPars getFormalPars() {
        return FormalPars;
    }

    public void setFormalPars(FormalPars FormalPars) {
        this.FormalPars=FormalPars;
    }

    public VarDeclParam getVarDeclParam() {
        return VarDeclParam;
    }

    public void setVarDeclParam(VarDeclParam VarDeclParam) {
        this.VarDeclParam=VarDeclParam;
    }

    public StatementList getStatementList() {
        return StatementList;
    }

    public void setStatementList(StatementList StatementList) {
        this.StatementList=StatementList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(VoidType!=null) VoidType.accept(visitor);
        if(FormalPars!=null) FormalPars.accept(visitor);
        if(VarDeclParam!=null) VarDeclParam.accept(visitor);
        if(StatementList!=null) StatementList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(VoidType!=null) VoidType.traverseTopDown(visitor);
        if(FormalPars!=null) FormalPars.traverseTopDown(visitor);
        if(VarDeclParam!=null) VarDeclParam.traverseTopDown(visitor);
        if(StatementList!=null) StatementList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(VoidType!=null) VoidType.traverseBottomUp(visitor);
        if(FormalPars!=null) FormalPars.traverseBottomUp(visitor);
        if(VarDeclParam!=null) VarDeclParam.traverseBottomUp(visitor);
        if(StatementList!=null) StatementList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("MethodDeclarationVoid(\n");

        if(VoidType!=null)
            buffer.append(VoidType.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(FormalPars!=null)
            buffer.append(FormalPars.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(VarDeclParam!=null)
            buffer.append(VarDeclParam.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(StatementList!=null)
            buffer.append(StatementList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [MethodDeclarationVoid]");
        return buffer.toString();
    }
}
