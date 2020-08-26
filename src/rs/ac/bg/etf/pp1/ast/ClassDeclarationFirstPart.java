// generated with ast extension for cup
// version 0.8
// 6/1/2018 13:54:6


package rs.ac.bg.etf.pp1.ast;

public class ClassDeclarationFirstPart extends ClassDeclFirstPart {

    private ClassDeclName ClassDeclName;
    private PotentialExtend PotentialExtend;

    public ClassDeclarationFirstPart (ClassDeclName ClassDeclName, PotentialExtend PotentialExtend) {
        this.ClassDeclName=ClassDeclName;
        if(ClassDeclName!=null) ClassDeclName.setParent(this);
        this.PotentialExtend=PotentialExtend;
        if(PotentialExtend!=null) PotentialExtend.setParent(this);
    }

    public ClassDeclName getClassDeclName() {
        return ClassDeclName;
    }

    public void setClassDeclName(ClassDeclName ClassDeclName) {
        this.ClassDeclName=ClassDeclName;
    }

    public PotentialExtend getPotentialExtend() {
        return PotentialExtend;
    }

    public void setPotentialExtend(PotentialExtend PotentialExtend) {
        this.PotentialExtend=PotentialExtend;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ClassDeclName!=null) ClassDeclName.accept(visitor);
        if(PotentialExtend!=null) PotentialExtend.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ClassDeclName!=null) ClassDeclName.traverseTopDown(visitor);
        if(PotentialExtend!=null) PotentialExtend.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ClassDeclName!=null) ClassDeclName.traverseBottomUp(visitor);
        if(PotentialExtend!=null) PotentialExtend.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ClassDeclarationFirstPart(\n");

        if(ClassDeclName!=null)
            buffer.append(ClassDeclName.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(PotentialExtend!=null)
            buffer.append(PotentialExtend.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ClassDeclarationFirstPart]");
        return buffer.toString();
    }
}
