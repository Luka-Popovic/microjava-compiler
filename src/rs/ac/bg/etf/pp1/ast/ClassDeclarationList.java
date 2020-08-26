// generated with ast extension for cup
// version 0.8
// 6/1/2018 13:54:6


package rs.ac.bg.etf.pp1.ast;

public class ClassDeclarationList extends ClassDeclList {

    private ClassDeclFirstPart ClassDeclFirstPart;
    private VarDeclParamClass VarDeclParamClass;
    private MethodDeclParam MethodDeclParam;

    public ClassDeclarationList (ClassDeclFirstPart ClassDeclFirstPart, VarDeclParamClass VarDeclParamClass, MethodDeclParam MethodDeclParam) {
        this.ClassDeclFirstPart=ClassDeclFirstPart;
        if(ClassDeclFirstPart!=null) ClassDeclFirstPart.setParent(this);
        this.VarDeclParamClass=VarDeclParamClass;
        if(VarDeclParamClass!=null) VarDeclParamClass.setParent(this);
        this.MethodDeclParam=MethodDeclParam;
        if(MethodDeclParam!=null) MethodDeclParam.setParent(this);
    }

    public ClassDeclFirstPart getClassDeclFirstPart() {
        return ClassDeclFirstPart;
    }

    public void setClassDeclFirstPart(ClassDeclFirstPart ClassDeclFirstPart) {
        this.ClassDeclFirstPart=ClassDeclFirstPart;
    }

    public VarDeclParamClass getVarDeclParamClass() {
        return VarDeclParamClass;
    }

    public void setVarDeclParamClass(VarDeclParamClass VarDeclParamClass) {
        this.VarDeclParamClass=VarDeclParamClass;
    }

    public MethodDeclParam getMethodDeclParam() {
        return MethodDeclParam;
    }

    public void setMethodDeclParam(MethodDeclParam MethodDeclParam) {
        this.MethodDeclParam=MethodDeclParam;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ClassDeclFirstPart!=null) ClassDeclFirstPart.accept(visitor);
        if(VarDeclParamClass!=null) VarDeclParamClass.accept(visitor);
        if(MethodDeclParam!=null) MethodDeclParam.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ClassDeclFirstPart!=null) ClassDeclFirstPart.traverseTopDown(visitor);
        if(VarDeclParamClass!=null) VarDeclParamClass.traverseTopDown(visitor);
        if(MethodDeclParam!=null) MethodDeclParam.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ClassDeclFirstPart!=null) ClassDeclFirstPart.traverseBottomUp(visitor);
        if(VarDeclParamClass!=null) VarDeclParamClass.traverseBottomUp(visitor);
        if(MethodDeclParam!=null) MethodDeclParam.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ClassDeclarationList(\n");

        if(ClassDeclFirstPart!=null)
            buffer.append(ClassDeclFirstPart.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(VarDeclParamClass!=null)
            buffer.append(VarDeclParamClass.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(MethodDeclParam!=null)
            buffer.append(MethodDeclParam.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ClassDeclarationList]");
        return buffer.toString();
    }
}
