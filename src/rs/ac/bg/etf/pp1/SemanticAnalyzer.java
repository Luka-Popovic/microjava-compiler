package rs.ac.bg.etf.pp1;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import org.apache.log4j.Logger;


import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.symboltable.*;
import rs.etf.pp1.symboltable.concepts.*;
import rs.etf.pp1.symboltable.factory.SymbolTableFactory;
import rs.etf.pp1.symboltable.structure.HashTableDataStructure;
import rs.etf.pp1.symboltable.structure.SymbolDataStructure;


public class SemanticAnalyzer extends VisitorAdaptor {

	boolean errorDetected = false;
	int printCallCount = 0;
	
	public String printObjNode(Obj objToVisit) {
		//output.append("[");
		StringBuilder output = new StringBuilder();
		
		switch (objToVisit.getKind()) {
		case Obj.Con:  output.append("Con "); break;
		case Obj.Var:  output.append("Var "); break;
		case Obj.Type: output.append("Type "); break;
		case Obj.Meth: output.append("Meth "); break;
		case Obj.Fld:  output.append("Fld "); break;
		case Obj.Prog: output.append("Prog "); break;
		}
		
		output.append(objToVisit.getName());
		output.append(": ");
		if(objToVisit.getType().getKind() != 3){
		output.append(arrType(objToVisit.getType().getKind()));
		
		}
		else{// u pitanju je niz
			output.append("Arr of ");
			output.append(arrType(objToVisit.getType().getElemType().getKind()));
		}
			output.append("");
		
		output.append(", ");
		output.append(objToVisit.getAdr());
		output.append(", ");
		output.append(objToVisit.getLevel() + " ");
	
		return output.toString();
	}
	
	Obj currentMethod = null;
	Obj currentVarType = null;
	Obj currentConstType = null;
	Obj currentConstNode = null;
	Struct currentDotStruct = null;
	int line_num = 0;
	//class
	boolean classDecl = false;
	boolean classAccess = false;
	boolean potentialExtends = false;
	Obj classToBeExtended = null;
	Obj currentClassType = null;
	int classFieldNum = 0;
	boolean methodParentFirst = false;
	//HashTableDataStructure fieldsKlase = null; 
	//
	//method
	boolean methodDeclExp = false;
	boolean returnFound = false;
	Obj readObj = null;
	int nVars;
	
	//do-while statement
	boolean entered_do_while = false;
	Stack<Boolean> entered_do_while_stack = new Stack();
	
	
	//DESIGNATOR DOT 
	List<Obj> designatorDotList = new ArrayList<Obj>();
	
	
	//condition statement
	//boolean designator_dot_field = false;
	boolean designator_expr = false;
	boolean designator_dot_global = false;
	//relops
	String relOpString = "";
	
	//actParam
	Obj methodCalled = null;
	int actualsDone = 1;
	int actualsNum = 0;
	Stack<Struct> actualParamStack = new Stack();
	
	public static String arrType(int i){
		
		switch(i){
		case 1 : return "int"; 
		case 2 : return "char";
		case 4: return "Class";
		default: return "bool";
		}
	}
	
	Logger log = Logger.getLogger(getClass());

	public void report_error(String message, SyntaxNode info) {
		errorDetected = true;
		StringBuilder msg = new StringBuilder(message);
		int line = (info == null) ? 0: info.getLine();
		if (line != 0)
			msg.append (" na liniji ").append(line);
		log.error(msg.toString());
	}

	public void report_info(String message, SyntaxNode info) {
		StringBuilder msg = new StringBuilder(message); 
		int line = (info == null) ? 0: info.getLine();
		if (line != 0)
			msg.append (" na liniji ").append(line);
		log.info(msg.toString());
	}
	
	public void visit(Program program) {		
		nVars = Tab.currentScope.getnVars();
		Tab.chainLocalSymbols(program.getProgName().obj); //Ovim se opseg programa koji je otvoren u visit(ProgName progName) ulancava
		
		if(Tab.find("main") == Tab.noObj){
			report_error("Greska: program nema main() metodu!", null);
		}
		
		Tab.closeScope(); // a ovde se on i zatvara
		
		
		
		
	}

	public void visit(ProgName progName) {
		progName.obj = Tab.insert(Obj.Prog, progName.getPName(), Tab.noType);
		Tab.openScope();  //Otvaramo novi opseg jer pocinje opseg Programa   	
		Tab.insert(Obj.Type, "bool", Tab.noType);
	}
	
	

	
	//Obrada deklarisanja promenljivih
	public void visit(VarTypeNameSqParent varTypeName) {
		Obj node = Tab.currentScope.findSymbol(varTypeName.getVarName());
		if(node != null) {
			report_error("Greska na "+ varTypeName.getLine()  +": "+ varTypeName.getVarName() + " vec deklarisano", null);
		}
		else{
		
		if(classDecl == false || methodDeclExp == true){
			currentVarType = Tab.insert(Obj.Var, varTypeName.getVarName(), new Struct(Struct.Array, varTypeName.getType().struct));
			report_info("Deklarisana nizovna promenljiva "+ varTypeName.getVarName()  +" gde su elementi niza tipa "+ arrType(currentVarType.getType().getElemType().getKind()) + ",", varTypeName);
		}
		else{
			currentVarType = Tab.insert(Obj.Fld, varTypeName.getVarName(), new Struct(Struct.Array, varTypeName.getType().struct));
			report_info("Deklarisana klasna nizovna promenljiva "+ varTypeName.getVarName()+ " gde su elementi niza tipa "+ arrType(currentVarType.getType().getElemType().getKind()) + ",", varTypeName);
		}
	  }
	}
	public void visit(VarTypeNameNoSqParent varTypeName) {
		Obj node = Tab.currentScope.findSymbol(varTypeName.getVarName());
		if(node != null) {
			report_error("Greska na "+ varTypeName.getLine()  +": "+ varTypeName.getVarName() + " vec deklarisano", null);
		}
		else{
		if(classDecl == false || methodDeclExp == true){
			report_info("Deklarisana promenljiva "+ varTypeName.getVarName(), varTypeName);
			currentVarType = Tab.insert(Obj.Var, varTypeName.getVarName(), varTypeName.getType().struct);
		}
		else{
			report_info("Deklarisana klasna promenljiva "+ varTypeName.getVarName(), varTypeName);
			currentVarType = Tab.insert(Obj.Fld,  varTypeName.getVarName(), varTypeName.getType().struct);
		 }
		}
	}
	public void visit(VarDeclarationList varDeclList) {
		//Obj varNode = Tab.insert(Obj.Var, varDeclList.getVarName(), varDeclList.getType().struct);
		currentVarType = null;
	}
	
	public void visit(VarDeclarationSqParent varDecl) {
		Struct currStruct = null;
		if(currentVarType.getType().getKind() == Struct.Array){
			currStruct = currentVarType.getType().getElemType(); 
		}
		else{
			currStruct = currentVarType.getType();
		}
		if(classDecl == false || methodDeclExp == true){
			Obj varNode = Tab.insert(Obj.Var, varDecl.getVarDeclName(), new Struct(Struct.Array, currStruct));
		report_info("Deklarisana nizovna promenljiva "+ varDecl.getVarDeclName()  +" gde su elementi niza tipa "+ arrType(currStruct.getKind()) + ",", varDecl.getParent());
		}
		else{
			Obj varNode = null;
			varNode = Tab.insert(Obj.Fld, varDecl.getVarDeclName(), new Struct(Struct.Array,currStruct));
			report_info("Deklarisana klasna nizovna promenljiva "+ varDecl.getVarDeclName() +" gde su elementi niza tipa "+ arrType(currStruct.getKind()) + ",", varDecl.getParent());
		}
	}

	public void visit(VarDeclarationNoSqParent varDecl) {
		if(classDecl == false || methodDeclExp == true){
		Obj varNode = null; 
		if(currentVarType.getType().getKind() == Struct.Array){
		varNode = Tab.insert(Obj.Var, varDecl.getVarDeclName(), currentVarType.getType().getElemType());
		}
		else{
			varNode = Tab.insert(Obj.Var, varDecl.getVarDeclName(), currentVarType.getType());
		}
		report_info("Deklarisana promenljiva "+ varDecl.getVarDeclName(), varDecl.getParent());
		}
		else{
			if(currentVarType.getType().getKind() == Struct.Array){
				Obj varNode = Tab.insert(Obj.Fld, varDecl.getVarDeclName(), currentVarType.getType().getElemType());
				}
			else{
				Tab.insert(Obj.Fld, varDecl.getVarDeclName(), currentVarType.getType());
				}
			report_info("Deklarisana klasna promenljiva "+ varDecl.getVarDeclName(), varDecl.getParent());
		}
	}
	
	
	
	
	//Obrada definisanja konstanti
	public void visit(ConstTypeName constTypeName) {
		Obj node = Tab.currentScope.findSymbol(constTypeName.getConstName());
		if(node != null) {
			report_error("Greska na "+ constTypeName.getLine()  +": "+ constTypeName.getConstName() + " vec deklarisano", null);
		}
		else{	
		report_info("Deklarisana konstanta "+ constTypeName.getConstName(), constTypeName);
		currentConstType = Tab.insert(Obj.Con, constTypeName.getConstName() ,constTypeName.getType().struct);
		currentConstNode = currentConstType;
		line_num = constTypeName.getLine();
		}
	}
	public void visit(ConstDeclaration constDeclaration) {
		//Obj varNode = Tab.insert(Obj.Var, varDeclList.getVarName(), varDeclList.getType().struct);
		
	  if (constDeclaration.getConstTypeList().struct.getKind() != currentConstType.getType().getKind()){
		  report_error("Greska na liniji " + constDeclaration.getLine() + " : " + " nekompatibilni tipovi u dodeli vrednosti ", null);
		}
		
		
	}
	
	public void visit(ConstListConstDecl constDeclaration) {
		currentConstType = null;	
	}
	
	
	public void visit(ConstDeclarationParam constDeclarationParam) {
		String name = constDeclarationParam.getConstDeclarIdent().obj.getName();
		
		 if (constDeclarationParam.getConstTypeList().struct.getKind() != currentConstType.getType().getKind()){
			report_error("Greska na liniji " + constDeclarationParam.getLine() + " : " + " nekompatibilni tipovi u dodeli vrednosti ", null);
		}
	}
	
	public void visit(ConstDeclrIdent cnst){
		Obj node = Tab.currentScope.findSymbol(cnst.getConstDeclParamName());
		if(node != null) {
			report_error("Greska na "+ cnst.getLine()  +": "+ cnst.getConstDeclParamName() + " vec deklarisano", null);
		}else{
		report_info("Definisana konstanta "+ cnst.getConstDeclParamName() + " na liniji " +line_num, null);
		cnst.obj = Tab.insert(Obj.Con, cnst.getConstDeclParamName(), currentConstType.getType());
		currentConstNode = cnst.obj;
		}
	}
		
	
	
	
	//tip koji se dodeljuje konstanti
	public void visit(NumberConst cnst){
		cnst.struct = Tab.intType;    	
		if(currentConstNode != null) {
			currentConstNode.setAdr(cnst.getN1());
		}
	}
	
	public void visit(CharConst cnst){
		if(cnst.getC1().length() > 3){
			report_error("Greska na liniji " + cnst.getLine() + " :  vrednost koja se dodeljuje je tipa string a ne char" , null);
			cnst.struct = Tab.noType;
		}
		else{
			cnst.struct = Tab.charType;    	
			if(currentConstNode != null) {
				char char_const = cnst.getC1().charAt(1); 
				currentConstNode.setAdr(char_const - 0);
			}
		}
	}
	
	public void visit(BooleanConst cnst){
		cnst.struct = (Tab.find("bool")).getType();
		if(currentConstNode != null) {
			if(cnst.getB1() == true){
				currentConstNode.setAdr(1);
			}else{
				currentConstNode.setAdr(0);
			}
		}
	}
	
	
	
	
	
	//Obrada klasa, morao sam da napravim novi strukturni cvor koji ce biti tipa klase
	public void visit(ClassDeclName classDeclName) {
		currentClassType = Tab.insert(Obj.Type, classDeclName.getClassName(), new Struct(Struct.Class));
		classDeclName.obj = currentClassType;
		Tab.openScope();
		Tab.insert(Obj.Fld, "VTP", Tab.noType);
		classDecl = true;
		report_info("Deklarisana klasa "+ classDeclName.getClassName(), classDeclName);
		classDeclName.obj = currentClassType;
	}
	
	public void visit(ClassDeclarationList classDeclarationList) {	
		Tab.chainLocalSymbols(currentClassType.getType());	
		Tab.closeScope();
		
		//fieldsKlase = null;
		currentClassType = null;
		classDecl = false;
		potentialExtends = false;
		classToBeExtended = null;
	}
	
	public void visit(PotentialExtends potExtends){
		potentialExtends = true;
		classToBeExtended = Tab.find(potExtends.getType().getTypeName());
		if(classToBeExtended == Tab.noObj){
			report_error("Greska na liniji "+ potExtends.getLine()+ ": Ime " + potExtends.getType().getTypeName() + " nije klasnog tipa te se ne moze naslediti! ", null);
		
		}else{
			SymbolDataStructure pomExtends = classToBeExtended.getType().getMembers();
			methodParentFirst = true;
			//kopiram polja nadklase
			for (Obj o : pomExtends.symbols()){
				if(o != null && o.getKind() == Obj.Fld){
					Obj node = Tab.insert(o.getKind(), o.getName(), o.getType());
					
				}
			}
		}
	
	}
	
	public void visit(VarDClass vdp){
		if(methodParentFirst == true){
			SymbolDataStructure pomExtends = classToBeExtended.getType().getMembers();
			
			
			//obrada nasledjenih metoda
			for (Obj o : pomExtends.symbols()){
				if(o != null && o.getKind() == Obj.Meth){
					Obj node = Tab.insert(o.getKind(), o.getName(), o.getType());
					node.setLevel(o.getLevel());
					SymbolDataStructure localCurr = SymbolTableFactory.instance().createSymbolTableDataStructure();
					if((o.getLocalSymbols().size()) != 0){
					Tab.openScope();
					for(Obj methLocals : o.getLocalSymbols()){
						Obj localVar = Tab.insert(methLocals.getKind(), methLocals.getName(), methLocals.getType());
						localCurr.insertKey(localVar);
					}
					Tab.chainLocalSymbols(node);
					Tab.closeScope();
					}
			}
		}
		}
		methodParentFirst = false;
	}
	
	// obrada tipa
	public void visit(Type type) {
		Obj typeNode = Tab.find(type.getTypeName());
		if (typeNode == Tab.noObj) {
			report_error("Nije pronadjen tip " + type.getTypeName() + " u tabeli simbola", null);
			type.struct = Tab.noType;
		} 
		else {
			if (Obj.Type == typeNode.getKind()) {
				type.struct = typeNode.getType();
			} 
			else {
				report_error("Greska: Ime " + type.getTypeName() + " ne predstavlja tip ", type);
				type.struct = Tab.noType;
			}
		}  
	}
	
	//obrada metoda
	public void visit(MethodDeclaration methodDecl) {
		if (!returnFound && currentMethod.getType() != Tab.noType) {
			report_error("Semanticka greska na liniji " + methodDecl.getLine() + ": funcija " + currentMethod.getName() + " nema return iskaz!", null);
		}
		
		Tab.chainLocalSymbols(currentMethod);
		Tab.closeScope();
		
		returnFound = false;
		currentMethod = null;
		methodDeclExp = false;
	}
	
	public void visit(MethodDeclarationVoid methodDecl) {
		if (!returnFound && currentMethod.getType() != Tab.noType) {
			report_error("Semanticka greska na liniji " + methodDecl.getLine() + ": funcija " + currentMethod.getName() + " nema return iskaz!", null);
		}
		
		Tab.chainLocalSymbols(currentMethod);
		Tab.closeScope();
		
		returnFound = false;
		currentMethod = null;
		methodDeclExp = false;
	}
	
	
	public void visit(MethodDeclName methodTypeName) {
		currentMethod = Tab.insert(Obj.Meth, methodTypeName.getMethName(), methodTypeName.getType().struct);
		methodTypeName.obj = currentMethod;
		Tab.openScope();
		
		currentMethod .setLevel(0);
		
		//Dodavanje this u metodu
		if(currentClassType != null){
		Tab.insert(Obj.Var, "this", currentClassType.getType());
		currentMethod .setLevel(1);
		}
		//
		
		report_info("Obradjuje se funkcija " + methodTypeName.getMethName() + " ciji je povratni tip " +  methodTypeName.getType().getTypeName() +",", methodTypeName);
		methodDeclExp = true;
	}

	public void visit(VoidType methodTypeName) {
		currentMethod = Tab.insert(Obj.Meth, methodTypeName.getVoidMethName(), Tab.noType);
		methodTypeName.obj = currentMethod;
		
		currentMethod .setLevel(0);
		
		Tab.openScope();
		
		//Dodavanje this u metodu
		if(currentClassType != null){
		Tab.insert(Obj.Var, "this", currentClassType.getType());
		currentMethod .setLevel(1);
		}
		//
		
		report_info("Obradjuje se funkcija " + methodTypeName.getVoidMethName() + " ciji je povratni tip  void,", methodTypeName);
		methodDeclExp = true;
	}
	
	
	
	//obrada formalnih parametara metoda
	public void visit(FormalParamNameSqParent fpName) {
		Obj fpNode = null;
		fpNode = Tab.insert(Obj.Var, fpName.getFpName(), new Struct(Struct.Array, fpName.getType().struct));
		int methodParamNum = currentMethod.getLevel();
		methodParamNum++;
		currentMethod.setLevel(methodParamNum);
		
		report_info("Pronadjen formalni parametar "+ fpName.getFpName() +"[] gde su elementi niza tipa "+ arrType(fpNode.getType().getElemType().getKind()) + ",", fpName);
	}
	
	public void visit(FormalParamNameNoSqParent fpName) {
		Obj fpNode = null;
		fpNode = Tab.insert(Obj.Var, fpName.getFpName(), fpName.getType().struct);
		int methodParamNum = currentMethod.getLevel();
		methodParamNum++;
		currentMethod.setLevel(methodParamNum);
		report_info("Pronadjen formalni parametar "+ arrType(fpNode.getType().getKind()) +" "+ fpName.getFpName() +",", fpName);
	}

	
	
	
	
	
	//Obrada Designator Statement-a
	public void visit(DesignatorStatementAssignment assignment) {
		Obj temp = assignment.getDesignator().obj;
		Struct tempStruct = temp.getType();
		Struct exprStruct = assignment.getExpr().struct;
		
		Struct struct1 = tempStruct;
		Struct struct2 = exprStruct;
		
		if(struct1.getKind() == Struct.Array && struct2.getKind() == Struct.Class){
			if(struct2.getElemType() != null){
				report_error("Greska na liniji " + assignment.getLine() + " : " + " nekompatibilni tipovi u dodeli vrednosti ", null);
			}
		}
		
		else{
		
		if(tempStruct.getKind() == Struct.Array){
			struct1 = tempStruct.getElemType();
		}
		if(exprStruct.getKind() == Struct.Array){
			struct2 = exprStruct.getElemType();
		}
	
		if(!(struct1.compatibleWith(struct2))){
			report_error("Greska na liniji " + assignment.getLine() + " : " + " nekompatibilni tipovi u dodeli vrednosti ", null);
		}
		
		}
		
		//report_info("Pretraga na liniji " + assignment.getLine() + " ("+ assignment.getDesignator().obj.getName() +"), nadjeno " + printObjNode(temp), null);
		
	}

	public void visit(DesignatorStatementInc assignment) {
		Obj temp = assignment.getDesignator().obj;
		Struct struct = assignment.getDesignator().obj.getType();
		
		
		if(struct.getKind() == Struct.Array){
			struct = struct.getElemType(); 
		}
		
		if(!struct.compatibleWith(Tab.intType)){
			report_error("Greska na liniji " + assignment.getLine() + " : " + " operand mora biti tipa int", null);
		}else{
		
		if((temp.getKind() != Obj.Var) && (temp.getKind() != Obj.Fld))
		report_error("Greska na liniji " + assignment.getLine() + " : " + " operand mora biti promenljiva, element niza ili polje unutrasnje klase", null);
		}
		
	}
	
	public void visit(DesignatorStatementDec assignment) {
		Obj temp = assignment.getDesignator().obj;
		Struct struct = assignment.getDesignator().obj.getType();
		
		if(struct.getKind() == Struct.Array){
			struct = struct.getElemType(); 
		}
		
		if(!struct.compatibleWith(Tab.intType)){
			report_error("Greska na liniji " + assignment.getLine() + " : " + " operand mora biti tipa int", null);
		}else{
		
		if((temp.getKind() != Obj.Var) && (temp.getKind() != Obj.Fld))
		report_error("Greska na liniji " + assignment.getLine() + " : " + " operand mora biti promenljiva, element niza ili polje unutrasnje klase", null);
		}
	}
	
	
	public void visit(ReturnExpr returnExpr){
		returnFound = true;
		Struct currMethType = currentMethod.getType();
		if (!currMethType.compatibleWith(returnExpr.getExpr().struct)) {
			report_error("Greska na liniji " + returnExpr.getLine() + " : " + "tip izraza u return naredbi ne slaze se sa tipom povratne vrednosti funkcije " + currentMethod.getName(), null);
		}			  	     	
	}

	public void visit(NoReturnExpr returnExpr){
		returnFound = true;
		Struct currMethType = currentMethod.getType();
		if ((currMethType != Tab.noType)) {
			report_error("Greska na liniji " + returnExpr.getLine() + " : " + "metoda nije void, pa povratna vrednost ne moze biti samo return" + currentMethod.getName(), null);
		}			  	     	
	}
	
	
	// obrada factor neterminala
	public void visit(FactorNumberConst cnst){
		cnst.struct = Tab.intType;    	
	}
	
	public void visit(FactorCharConst cnst){
		cnst.struct = Tab.charType;    	
	}
	
	public void visit(FactorBooleanConst cnst){
		cnst.struct = (Tab.find("bool")).getType();    	
	}
	
	
	public void visit(FactorExpr factExpr) {
		factExpr.struct = factExpr.getExpr().struct;
	}

	public void visit(Var var) {
		var.struct = var.getDesignator().obj.getType();
	}
	
	public void visit(FuncCall funcCall){
		Obj func = funcCall.getDesignator().obj;
		if (Obj.Meth == func.getKind()) { 
			report_info("Pronadjen poziv funkcije " + func.getName() + " na liniji " + funcCall.getLine(), null);
			funcCall.struct = func.getType();
			methodCalled = func;
			
			checkActualParamTypes(funcCall.getLine());
			
			
		} 
		else {
			report_error("Greska na liniji " + funcCall.getLine()+" : ime " + func.getName() + " nije funkcija!", null);
			funcCall.struct = Tab.noType;
			methodCalled = Tab.noObj;
		}

	}
	
	public void visit(WithExpr expr){
		expr.struct = expr.getExpr().struct;
	}
	
	public void visit(WithNoExpr expr){
		expr.struct = Tab.noType;
	}
	public void visit(FactorNewTypeWithExpr var) {
		if(var.getPotentialExpr().struct != Tab.noType){
			if(var.getPotentialExpr().struct.getKind() != Struct.Int){
			report_error("Greska na liniji " + var.getLine()+" : vrednost u [] nije tipa int!", null);
			var.struct = Tab.noObj.getType();
			}
			else {
				Struct pom = var.getType().struct;//ovog tipa su elementi niza koji pravimo
				var.struct = new Struct(Struct.Array, var.getType().struct);
			}
		}else{
			report_error("Greska na liniji " + var.getLine()+" : vrednost u [] je nepoznatog tipa!", null);
			var.struct = Tab.noObj.getType();
		}
	}
	
	public void visit(FactorNewType var) {
		if(!(var.getType().struct.getKind() == Struct.Class)){
			report_error("Greska na liniji " + var.getLine()+" : tip nije tipa unutrasnje klase!", null);
		}
		else{
		var.struct = var.getType().struct;
		}
	}
	
	
	public void visit(ReadStmt readD) {
		Obj temp = readD.getDesignator().obj;
		
		if(temp.getType().getKind() == Struct.Array){
			int num_pom = temp.getType().getElemType().getKind(); 
			if((num_pom != Struct.Int) && (num_pom != Struct.Char) && (num_pom != Struct.None)){
				report_error("Greska na liniji " + readD.getLine() + " : " + " operand mora biti tipa int, char ili bool", null);
			}
		}
		
		else if (((temp.getType().getKind() != Struct.Int) && ((temp.getType().getKind() != Struct.Char)) && ((temp.getType().getKind() != Struct.None)))){
			report_error("Greska na liniji " + readD.getLine() + " : " + " operand mora biti tipa int, char ili bool", null);
			}
		else if((temp.getKind() != Obj.Var) && (temp.getKind() != Obj.Fld))
			report_error("Greska na liniji " + readD.getLine() + " : " + " operand mora biti promenljiva, element niza ili polje unutrasnje klase", null);
	}
	
	public void visit(PrintStmt printE) {
		Struct temp = printE.getExpr().struct;
		if(temp.getKind() == Struct.Array){
			temp = temp.getElemType();
		}
		if (((temp.getKind() != Struct.Int) && ((temp.getKind() != Struct.Char)) && ((temp.getKind() != Struct.None))))
			report_error("Greska na liniji " + printE.getLine() + " : " + " operand instrukcije PRINT mora biti int, char ili bool tipa", null);
		}
	
	
	
	
	
	
	//obrada term
	
	public void visit(SingleTerm term) {
		term.struct = term.getFactor().struct;    	
	}
	
	public void visit(TermList term) {
		Struct struct = term.getFactor().struct;
		if(term.getFactor().struct.getKind() == Struct.Array){
			struct = term.getFactor().struct.getElemType();
		}
		
		if(struct.compatibleWith(Tab.intType)){
			term.struct = struct;
			}
		else {
			report_error("Semanticka greska na liniji " + term.getLine()+" : mnozilac nije tima int!", null);
		}
	}
	
	
	public void visit(TermMulOperation term) {
		term.struct = term.getFactor().struct;
	}
	
	public void visit(FactorMulop term) {
		Struct struct = term.getTermMul().struct;
		if(term.getTermMul().struct.getKind() == Struct.Array){
			struct = term.getTermMul().struct.getElemType();
		}
		
		if(!struct.compatibleWith(Tab.intType)){
			report_error("Semanticka greska na liniji " + term.getLine()+" : mnozilac nije tima int!", null);
		}
	}
	
	public void visit(TermFact term) {
		
		Struct struct = term.getFactor().struct;
		if(term.getFactor().struct.getKind() == Struct.Array){
			struct = term.getFactor().struct.getElemType();
		}
		
		if(!struct.compatibleWith(Tab.intType)){
			report_error("Semanticka greska na liniji " + term.getLine()+" : mnozilac nije tima int!", null);
		}
	}
	
	public void visit(ExprAddOpOperation addExpr){
		addExpr.struct = addExpr.getTerm().struct;
	}
	
	
	public void visit(ExprAddop addExpr) {
		Struct te = ((ExprAddOpOperation)addExpr.getExprAdd()).getExprList().struct;
		Struct t = addExpr.getExprAdd().struct;
		
		if(te.getKind() == Struct.Array){
			te = te.getElemType();
		}
		
		if(t.getKind() == Struct.Array){
			t = t.getElemType();
		}
		
		if(!te.compatibleWith(t) || te.getKind() != Struct.Int){
			report_error("Greska1 na liniji "+ addExpr.getLine()+" : nekompatibilni tipovi u aritmetickom izrazu.", null);
			addExpr.struct = Tab.noType;
		}else{
			addExpr.struct = te;
		}
}
	public void visit(ExprTerm addExpr) {
		Struct node = addExpr.getTerm().struct;
		
		if(node.getKind() == Struct.Array){
			node = node.getElemType();
		}
		
		if(node.getKind() != Struct.Int){
			report_error("Greska4 na liniji "+ addExpr.getLine()+" : nekompatibilni tipovi u aritmetickom izrazu.", null);
			addExpr.struct = Tab.noType;
		}
		else{
			addExpr.struct = addExpr.getTerm().struct;
		}
	}
	
	public void visit(ExprTermMinus addExpr) {
		if(addExpr.getTerm().struct.getKind() != Struct.Int){
			report_error("Greska na liniji "+ addExpr.getLine()+" : tip isped unarnog znaka minus nije int.", null);
		}
		else{
		addExpr.struct = addExpr.getTerm().struct;
		}
	}
	
	public void visit(SingleExpr addExpr) {
		addExpr.struct = addExpr.getTerm().struct;
	}
	
	public void visit(SingleExprMinus addExpr) {
		if(addExpr.getTerm().struct.getKind() != Struct.Int){
			report_error("Greska na liniji "+ addExpr.getLine()+" : tip isped unarnog znaka minus nije int.", null);
		}
		else{
		addExpr.struct = addExpr.getTerm().struct;
		}
	}
	
	public void visit(ExprLists addExpr) {
		Struct te = addExpr.getExprList().struct;
		Struct t = addExpr.getTerm().struct;
		
		if(te.getKind() == Struct.Array){
			te=te.getElemType();
		}
		
		if(t.getKind() == Struct.Array){
			t=t.getElemType();
		}
		
		if (te.compatibleWith(t) && te == Tab.intType)
			addExpr.struct = te;
			
		else {
			report_error("Greska75 na liniji "+ addExpr.getLine()+" : nekompatibilni tipovi u aritmetickom izrazu.", null);
			addExpr.struct = te;
		} 
	}
	
	
	//obrada poziva funkcije
	public void visit(DesignatorStatementProcCall procCall){
		Obj func = procCall.getDesignator().obj;
		if (Obj.Meth == func.getKind()) { 
			report_info("Pretraga na liniji " + procCall.getLine() + " ("+ func.getName() +"), nadjeno " + printObjNode(func), null);
			procCall.obj = func;
			methodCalled = func;
		
			checkActualParamTypes(procCall.getLine());
		} 
		else {
			report_error("Greska na liniji " + procCall.getLine()+" : ime " + func.getName() + " nije funkcija!", null);
			methodCalled = Tab.noObj;
		}     	
	}    
	
	//obrada stvarnih argumenata poziva funkcije
	public void visit(ActualParam actParam){	
		actualParamStack.push(actParam.getExpr().struct);
		designator_expr = false;
		//methodCalled = null;
		
	}
	
	public void checkActualParamTypes(int line){
		int num = 0;
		int index = 0;
		
		if(designator_dot_global == true || classDecl == true){
		num = methodCalled.getLevel()-1;
		}
		else{
			num = methodCalled.getLevel();
		}
		index = methodCalled.getLevel() - 1;
		
		if(actualParamStack.size() != num){
			report_error("Greska na liniji " + line+" : funkciji " + methodCalled.getName() + " je prosledjen nekorektan broj argumenata!", null);	
		}
		else{
			
			while(actualParamStack.empty() == false){
				Struct actParam = actualParamStack.pop(); 
				int i = 0;
				for(Obj o : methodCalled.getLocalSymbols()){
					if(i == index){
						Struct compare = actParam;
						
						if(methodCalled.getName().equals("len")){
							if(actParam.getKind() != o.getType().getKind()){
								report_error("Greska na liniji " + line+" : poziv funkcije " + methodCalled.getName() + ", nekompatibilni tipovi formalnih i stvarnih argumenata!", null);
							}
						}else{
						
						if(actParam.getKind() == Struct.Array){
							compare = actParam.getElemType();
						}
						if(!(compare.compatibleWith(o.getType()))){
							report_error("Greska na liniji " + line+" : poziv funkcije " + methodCalled.getName() + ", nekompatibilni tipovi formalnih i stvarnih argumenata!", null);	
						}
						}	
					}
					i++;
				}
				index--;
			}
		}
		
		
		actualsDone = 1; //uvek ces krenuti ispitivanje od prvog parametra (u listi ce to biti drugi, nulti je this, prvi je ispitan u ActualParam, ovde ispitujem ostale)
		actualsNum = 0;
		designator_dot_global = false;
		methodCalled = null;
		actualParamStack = new Stack();
	}
	
	public void visit(ActualParams actuals){
		actualParamStack.push(actuals.getExpr().struct);
		designator_expr = false;
	}
	
	
	
	//obrada Designator iskaza
	public void visit(DesignatorIdent designator){
		Obj obj = Tab.find(designator.getDName());
	
		if (obj == Tab.noObj) { 
			report_error("Greska na liniji " + designator.getLine()+ " : ime "+designator.getDName()+" nije deklarisano! ", null);
			designator.obj = Tab.noObj;
		}
		else{
			report_info("Pretraga na liniji " + designator.getLine() + " ("+ designator.getDName() +"), nadjeno " + printObjNode(obj), null);
			if(obj.getKind() == Obj.Meth){
				methodCalled = obj;
			}
			designator.obj = obj;
		}	
	}
	
	public void visit(DesignatorArray designator){
		designator.obj = designator.getDesignator().obj;
	}
	public void visit(SingleDesignatorExprArray designator){
		designator.obj = designator.getDesignatorArr().obj;
		//
		designatorDotList.add(designator.obj);
		//
	}
	
	public void visit(DesignatorExpr designator){
		Obj obj = Tab.find(designator.getDesignatorArr().obj.getName());
		if (obj == Tab.noObj) { 
			report_error("Greska na liniji " + designator.getLine()+ " : ime "+ designator.obj.getName()+" nije deklarisano! ", null);
			designator.obj = Tab.noObj;
		}
		else if(Struct.Array != obj.getType().getKind()){
			report_error("Greska na liniji " + designator.getLine()+ " : ime "+ designator.obj.getName() +" nije nizovnog tipa! ", null);
			designator.obj = Tab.noObj;
		}
		//Dodato
		else if(designator.getExpr().struct.getKind() == Struct.Array){
			 if(designator.getExpr().struct.getElemType().getKind() != Struct.Int){
				report_error("Greska na liniji " + designator.getLine()+ " : izraz izmedju [ ] nije tipa int! ", null);
				designator.obj = Tab.noObj;
			}else{
				designator.obj = designator.getDesignatorArr().obj;
				designator_expr = true;
			}
		} 
		//
		else if(designator.getExpr().struct.getKind() != Struct.Int){
			report_error("Greska na liniji " + designator.getLine()+ " : izraz izmedju [ ] nije tipa int! ", null);
			designator.obj = Tab.noObj;
		}
		else {
			//designator.obj = obj;
			designator.obj = designator.getDesignatorArr().obj;
			designator_expr = true;
		}
	}
	
	public void visit(DesignatorDotIdent designator){
		Obj obj = null; 
		//designator_dot_field = true; 
		obj = Tab.find(designator.getField());//Ovako mogu da radim jer ako je classDecl == true onda taj obj cvor jos nije otisao u Fields te klase i moze se naci pomocu Tab.find() a ako je otisao u fields klase onda se ne moze naci sa Tab.find
		
		if(classDecl == false || classAccess == true){
		obj = currentDotStruct.getMembers().searchKey(designator.getField());
		}
		if((designator.getDesignatorDot().obj.getName().equals("this")) && classDecl == true){
			obj = Tab.currentScope.getOuter().findSymbol(designator.getField());
		}
		
		designatorDotList.add(obj);
		
		designator.obj = obj;
		
		if(obj.getKind() == Obj.Meth){
			methodCalled = obj;				
			designator_dot_global = true;
		}else{
			methodCalled = Tab.noObj;
		
		}
		
		for(int i=0; i<designatorDotList.size()-1; i++){
			Obj obj1 = designatorDotList.get(i);
			Obj obj2 = designatorDotList.get(i+1);
			if(obj1.getType().getKind() == Struct.Class){
				
				if(classDecl == true && obj1.getName().equals("this")){
					if(Tab.currentScope.getOuter().getLocals().searchKey(obj2.getName()) == null){
						report_error("Greska11 na liniji " + designator.getLine()+ " : ime "+ obj2.getName()+" nije polje/metoda unutrasnje klase " + obj1.getName(), null);
						break;
					}
				}else{
				if(obj1.getType().getMembers().searchKey(obj2.getName()) == null){
					report_error("Greska10 na liniji " + designator.getLine()+ " : ime "+ obj2.getName()+" nije polje/metoda unutrasnje klase " + obj1.getName(), null);
					break;
				}
				}
			}else if(obj1.getType().getKind() == Struct.Array){
				if(obj1.getType().getElemType().getMembers().searchKey(obj2.getName()) == null){
					report_error("Greska2 na liniji " + designator.getLine()+ " : ime "+ obj2.getName()+" nije polje/metoda unutrasnje klase " + obj1.getName(), null);
					break;
				}
			}
		}

		
		
		
		designatorDotList = new ArrayList();	
		classAccess = false;	
	}

		
	public void visit(DesignatorDots designator){
		Obj obj = null; 
		obj = Tab.find(designator.getField());//Ovako moram da radim jer ako je classDecl == true onda taj obj cvor jos nije otisao u Fields te klase i moze se naci pomocu Tab.find() a ako je otisao u fields klase onda se ne moze naci sa Tab.find
		
		designatorDotList.add(obj);
		
		if(classDecl == false){
		obj = currentDotStruct.getMembers().searchKey(designator.getField());
		}
		if (obj == Tab.noObj) { 
			report_error("Greska na liniji " + designator.getLine()+ " : ime "+ designator.getField()+" nije polje/metoda unutrasnje klase! ", null);
			designator.obj = Tab.noObj;
			currentDotStruct = Tab.noType;
		}
		else 
		{
			Obj node = designator.getDesignatorDot().obj.getType().getMembers().searchKey(obj.getName());
			if(node == null){
			report_error("Greska na liniji " + designator.getLine()+ " : ime "+ designator.getField() +" ne pripada klasi " + designator.getDesignatorDot().obj.getName(), null);
			designator.obj = Tab.noObj;
			currentDotStruct = Tab.noType; 
		}else{
			designator.obj = obj;
			currentDotStruct = obj.getType();
			report_info("Pretraga na liniji " + designator.getLine() + " ("+ designator.getField() +"), nadjeno " + printObjNode(obj), null);
			
		}
			}
		
	} 
	
	public void visit(SingleDesignatorDot designator){
		Obj obj = Tab.find(designator.getField());
		designatorDotList.add(obj);
		
		if (obj == Tab.noObj) { 
			report_error("Greska na liniji " + designator.getLine()+ " : ime "+designator.getField()+" nije deklarisano! ", null);
			designator.obj = Tab.noObj;
			currentDotStruct = Tab.noType;
		}
		else if(Struct.Class != obj.getType().getKind()){
			report_error("Greska na liniji " + designator.getLine()+ " : ime "+designator.getField()+" nije klasnog tipa! ", null);
			designator.obj = Tab.noObj;
			currentDotStruct = Tab.noType;
		}
		else{
		report_info("Pretraga na liniji " + designator.getLine() + " ("+ designator.getField() +"), nadjeno " + printObjNode(obj), null);
		designator.obj = obj;
		currentDotStruct = obj.getType();
		if((classDecl == true) && (!obj.getName().equals("this"))){//pristupam polju ugnjezdene klase
			classAccess = true;
		} 
		}
	} 
	
	
	//do-while statement
	public void visit(DoStmt doStmt){
		entered_do_while = true;
		entered_do_while_stack.push(true);
	}
	
	public void visit(DoWhileExpr doStmt){
		//entered_do_while = false;
		if(entered_do_while_stack.empty() == false){
			entered_do_while_stack.pop();
			if(entered_do_while_stack.empty() == false){ // ako i dalje nismo ispraznili stek, onda smo u nekom ugnjezdenom doStmt
				entered_do_while = true;                // pa je entered_do_while = true			
			}											 
			else{
				entered_do_while = false;
			}
	    }	
	}
	
	public void visit(BreakExpr breakStmt){
		if(entered_do_while == false){
		report_error("Greska na liniji " + breakStmt.getLine()+ " : break se ne moze nalaziti van do-while petlje!", null);		
		}
	}
	
	public void visit(ContinueExpr continuekStmt){
		if(entered_do_while == false){
		report_error("Greska na liniji " + continuekStmt.getLine()+ " : continue se ne moze nalaziti van do-while petlje!", null);		
		}
	}
	
	
	
	
	//conditional statements
	
	public void visit(ConditionFactor cdf){
		
		
	}
	
	public void visit(ConditionFactors cdf){
		if(cdf.getExpr().struct.getKind() == cdf.getExpr1().struct.getKind()){	
		}
		else if(designator_expr == true){
			int tip1;
			int tip2;
			if(cdf.getExpr().struct.getKind() == Struct.Array){
				tip1=cdf.getExpr().struct.getElemType().getKind();
			}
			else{
				tip1 = cdf.getExpr().struct.getKind();
			}
		
		
			if(cdf.getExpr1().struct.getKind() == Struct.Array){
				tip2=cdf.getExpr1().struct.getElemType().getKind();
			}
			else{
				tip2 = cdf.getExpr1().struct.getKind();
			}
			
			if(tip1 != tip2){
				report_error("Greska na liniji " + cdf.getLine()+ " : tipovi operanada primenjeni na relacioni operator nisu kompatabilni!", null);		
			}
			
		}
		else if(!(cdf.getExpr().struct.compatibleWith(cdf.getExpr1().struct))){
			report_error("Greska na liniji " + cdf.getLine()+ " : tipovi operanada primenjeni na relacioni operator nisu kompatabilni!", null);		
		}
		else if((cdf.getExpr().struct.getKind() == Struct.Array && designator_expr == false) || (cdf.getExpr().struct.getKind() == Struct.Class) 
				||(cdf.getExpr1().struct.getKind() == Struct.Array && designator_expr == false) || (cdf.getExpr().struct.getKind() == Struct.Class)){
				if((!(relOpString.equals("BinEquality"))) && (!(relOpString.equals("NotBinEquality")))){
					report_error("Greska na liniji " + cdf.getLine()+ " : tipovi operanada primenjeni na relacioni operator moraju biti tipa niza ili klase!", null);		
					
				}
			
		}
		designator_expr = false;
		//designator_dot_field = false;
		
	}
	
	
	public void visit(MatchedStmt mstmt){
		designator_expr = false;
	}
	
	public void visit(UnmachedStmt umstmt){
		designator_expr = false;
	}
	
	//relop statements
	public void visit(BinEquality beq){
		relOpString = "BinEquality";
	}
	
	public void visit(NotBinEquality nbeq){
		relOpString = "NotBinEquality";
	}
	
	public void visit(GreaterRelOp gr){
		relOpString = "GreaterRelOp";
	}
	
	public void visit(GreaterEqualRelOp ger){
		relOpString = "GreaterEqualRelOp";
	}
	
	public void visit(LowerRelOp lr){
		relOpString = "LowerRelOp";
	}
	
	public void visit(LowerEqualRelOp ler){
		relOpString = "LowerEqualRelOp";
	}
	
	
	
	
	public boolean passed() {
		return !errorDetected;
	}

	
}

