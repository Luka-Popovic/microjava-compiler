package rs.ac.bg.etf.pp1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import rs.ac.bg.etf.pp1.CounterVisitor.BreakCounter;
import rs.ac.bg.etf.pp1.CounterVisitor.ConditionFactorsCounter;
import rs.ac.bg.etf.pp1.CounterVisitor.FormParamCounter;
import rs.ac.bg.etf.pp1.CounterVisitor.VarCounter;

import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;

public class CodeGenerator extends VisitorAdaptor {
	
	private int varCount;
	
	private int paramCnt;
	
	private int mainPc;
	
	public int getMainPc() {
		return mainPc;
	}
	
	//stack za if strukturu
	Stack<Integer> stack = new Stack();
	Stack<Integer> stackThen = new Stack();
	Stack<Integer> stackCascade = new Stack();
	Stack<Integer> numberStack = new Stack();
	
	String currentMulOp = ""; 
	String currentAddOp = "";
	String currentRelOp = "";
	
	
	Stack<String> currentAddOperation = new Stack(); //Ako ne radi lepo MulOp i za njega uvedi stack i skidaj sa njega dok moze
	
	//dodela vrednosti array-u
	boolean assignementToArray = false;
	
	//char print
	boolean char_print = false;
	boolean print_len_stmt = false;
	
	//incrementArrayObjNode
	Obj incrementNode = null;
	
	
	//do while stack
	Stack<Integer> doWhileStack= new Stack();
	
	//break stack
	Stack<Integer> breakStack = new Stack();
	Stack<Integer> breakNumberStack = new Stack();
	
	//pravljenje niza tipa char
	boolean newarray_char = false;
	
	//class potential extends
	boolean potential_extends = false;
	
	
	
	//type of jump
	private int jumpType(){
		switch(currentRelOp){
		case "BIN_EQUAL": return Code.eq;
		case "NOT_BIN_EQUAL": return Code.ne;
		case "GREATER": return Code.gt;
		case "GREATER_EQUAL": return Code.ge;
		case "LOWER": return Code.lt;
		case "LOWER_EQUAL": return Code.le;
		default: return 0;
		}
	}
	
	
	//metoda za obradu klasa
	List<Byte> MethodTable = new ArrayList();
	void addWordToStaticData (int value, int address) { 
		MethodTable.add(new Byte((byte)Code.const_)); 
		MethodTable.add(new Byte( (byte)( (value>>16)>>8 ) ) ); 
		MethodTable.add(new Byte((byte)(value>>16)));
		MethodTable.add(new Byte((byte)(value>>8)));
		MethodTable.add(new Byte((byte)value));
		MethodTable.add(new Byte((byte)Code.putstatic));
		MethodTable.add(new Byte((byte)(address>>8)));
		MethodTable.add(new Byte((byte)address)); 
	}
	void addNameTerminator() { 
		addWordToStaticData(-1, Code.dataSize++); 
		} 
	void addTableTerminator() { 
		addWordToStaticData(-2, Code.dataSize++); 
		} 
	void addFunctionAddress(int functionAddress) { 
		addWordToStaticData(functionAddress, Code.dataSize++); 
		} 
	void addFunctionEntry(String name, int functionAddressInCodeBuffer) {
		for (int j=0; j<name.length(); j++) { 
			addWordToStaticData((int)(name.charAt(j)), Code.dataSize++); 
			} 
		addNameTerminator(); 
		addFunctionAddress(functionAddressInCodeBuffer); 
		}
	
	//current class
	Obj current_class = null;
	Obj class_to_be_extended = null;
	boolean newClassFlag = false;
	HashMap<String, Obj> ClassTable = new HashMap<String, Obj>();
	String newClassName = "";
	boolean classMethodCalled = false;
	Obj lastDesignatorForMethodCall = null;
	HashMap<String, Obj> MethodNameList = new HashMap<String, Obj>();
	boolean assignmentToClassField = false;
	//Obj lastDesignatorField = null;
	boolean ClassCreation = false;
	List<Obj> arrayClassMethodCalled = new ArrayList<Obj>();
	boolean arrayClassMethodCalledflag = false;
	
	public void visit(ProgName progName) {
		
		Obj node1 = Tab.find("ord");
		if(node1 != Tab.noObj){
			node1.setAdr(Code.pc);
			Code.put(Code.enter);
			Code.put(1);
			Code.put(1);
			Code.put(Code.load_n);
			Code.put(Code.exit);
			Code.put(Code.return_);
		}
		
		Obj node2 = Tab.find("chr");
		if(node2 != Tab.noObj){
			node2.setAdr(Code.pc);
			Code.put(Code.enter);
			Code.put(1);
			Code.put(1);
			Code.put(Code.load_n);
			Code.put(Code.exit);
			Code.put(Code.return_);
		}
		
		Obj node3 = Tab.find("len");
		if(node3 != Tab.noObj){
			node3.setAdr(Code.pc);
			Code.put(Code.enter);
			Code.put(1);
			Code.put(1);
			Code.put(Code.load_n+0);
			Code.put(Code.arraylength);
			Code.put(Code.exit);
			Code.put(Code.return_);
		}
		
	
	}
	
	
	
	
	//Obrada metoda
	@Override
	public void visit(MethodDeclName MethodTypeName) {
		if ("main".equalsIgnoreCase(MethodTypeName.getMethName())) {
			mainPc = Code.pc;
			
			//
			Object ia[]=MethodTable.toArray(); 
			for (int i=0; i<ia.length; i++){ 
				Code.buf[Code.pc++]=((Byte)ia[i]).byteValue(); 
			}
			MethodTable.clear(); 
			
			//
				
		}
		MethodNameList.put(MethodTypeName.obj.getName(), MethodTypeName.obj);
		MethodTypeName.obj.setAdr(Code.pc);
		
		// Collect arguments and local variables.
		SyntaxNode methodNode = MethodTypeName.getParent();
		VarCounter varCnt = new VarCounter();
		methodNode.traverseTopDown(varCnt);
		FormParamCounter fpCnt = new FormParamCounter();
		methodNode.traverseTopDown(fpCnt);		
		int fpCount = fpCnt.getCount();
		
		// Generate the entry.
		Code.put(Code.enter);
		if(ClassCreation == true){
			fpCount++;
		}
		Code.put(fpCount);
		
		Code.put(varCnt.getCount() + fpCount);
	}
	
	@Override
	public void visit(VoidType MethodTypeName) {
		if ("main".equalsIgnoreCase(MethodTypeName.getVoidMethName())) {
			mainPc = Code.pc;
		
			Object ia[]=MethodTable.toArray(); 
			for (int i=0; i<ia.length; i++){ 
				Code.buf[Code.pc++]=((Byte)ia[i]).byteValue(); 
			}
			MethodTable.clear(); 
		
		}
		MethodNameList.put(MethodTypeName.obj.getName(), MethodTypeName.obj);
		MethodTypeName.obj.setAdr(Code.pc);
				
		// Collect arguments and local variables.
		SyntaxNode methodNode = MethodTypeName.getParent();
		VarCounter varCnt = new VarCounter();
		methodNode.traverseTopDown(varCnt);
		FormParamCounter fpCnt = new FormParamCounter();
		methodNode.traverseTopDown(fpCnt);
		int fpCount = fpCnt.getCount();
		
		// Generate the entry.
		Code.put(Code.enter);
		if(ClassCreation == true){
			fpCount++;	
		}
		Code.put(fpCount);
		Code.put(varCnt.getCount() + fpCount);
	}
	
	@Override
	public void visit(MethodDeclaration MethodDecl) {
		//Code.put(Code.exit);
		//Code.put(Code.return_);
	}
	
	@Override
	public void visit(MethodDeclarationVoid	 MethodDeclVoid) {
		Code.put(Code.exit);
		Code.put(Code.return_);
	}
	
	
	
	
	//Obrada Designator iskaza
	@Override
	public void visit(Var designator) {
		SyntaxNode parent = designator.getParent();
		/*if (Assignment.class != parent.getClass() && FuncCall.class != parent.getClass()) {//Ne znam cemu sluzi ovaj if
			Code.load(designator.getDesignator().obj);
		}
		*/
		if(designator.getDesignator().obj.getType().getKind() == Struct.Array && print_len_stmt == false){
			if(designator.getDesignator().obj.getType().getElemType().getKind() == Struct.Char){
				Code.put(Code.baload);
			}
			else{
				Code.put(Code.aload);	
			}
		}else{
			if(designator.getDesignator().obj.getKind() == Obj.Fld){
				Code.put(Code.load_n +0);
			}	
			
			Code.load(designator.getDesignator().obj);
			arrayClassMethodCalled.add(designator.getDesignator().obj);
		}
		lastDesignatorForMethodCall = designator.getDesignator().obj;
	}
	
	public void visit(SingleDesignatorExprArray designator) {
		if(designator.obj.getType().getKind() == Struct.Array){
			if(designator.obj.getType().getElemType().getKind() == Struct.Class){
				Code.put(Code.aload);
				arrayClassMethodCalledflag = true;
			}
			
		}
	}
	
	public void visit(DesignatorIdent designator) {
		if(designator.obj.getName().equals("len")){
			print_len_stmt = true;
		}
		if(designator.obj.getKind() == Obj.Fld){
			Code.put(Code.load_n + 0);
		}
	}
		
	
	@Override
	public void visit(DesignatorStatementInc designator) {
		SyntaxNode parent = designator.getParent();
		//Code.load(designator.getDesignator().obj);
		if(designator.getDesignator().obj.getType().getKind() == Struct.Array){
			//Code.load(designator.getDesignator().obj);
			//Code.load(incrementNode);
			Code.put(Code.dup2);
			Code.put(Code.aload);
		}else
		{
			Code.load(designator.getDesignator().obj);
		}
		Code.put(Code.const_1);
		Code.put(Code.add);
		
		if(designator.getDesignator().obj.getType().getKind() == Struct.Array){
			Code.put(Code.astore);
		}
		else{
		Code.store(designator.getDesignator().obj);
		}
	}
	
	@Override
	public void visit(DesignatorStatementDec designator) {
		SyntaxNode parent = designator.getParent();
		//Code.load(designator.getDesignator().obj);
		if(designator.getDesignator().obj.getType().getKind() == Struct.Array){
			//Code.load(designator.getDesignator().obj);
			//Code.load(incrementNode);
			Code.put(Code.dup2);
			Code.put(Code.aload);
		
		}else
		{
			Code.load(designator.getDesignator().obj);
		}
		Code.put(Code.const_1);
		Code.put(Code.sub);
		
		if(designator.getDesignator().obj.getType().getKind() == Struct.Array){
			Code.put(Code.astore);
		}
		else{
		Code.store(designator.getDesignator().obj);
		}
	}
	
	//Obrada Factor-a, bitan deo
	@Override
	public void visit(FactorNumberConst Const) {
		Obj node = new Obj (Obj.Con, "$", Const.struct, Const.getN1(), 0);
		incrementNode = node;
		arrayClassMethodCalled.add(node);
		Code.load(node);
	}
	
	@Override
	public void visit(FactorCharConst Const) {
		int index = Const.getC1().charAt(1);
		Code.load(new Obj(Obj.Con, "$", Const.struct, index, 0));
	}
	
	@Override
	public void visit(FactorNewTypeWithExpr newTypeAssign) {
		newarray_char = true; //pravim novi niz
		Code.put(Code.newarray);
		if(newTypeAssign.struct.getElemType().getKind() == Struct.Char){
			Code.put(0);
		}
		else {
			Code.put(1);
		}
	}
	
	@Override
	public void visit(FactorBooleanConst Const) {
		int num = -1;
		if(Const.getB1() == true){
			num = 1;
		}else{
			num = 0;
		}
		
		Obj node = new Obj (Obj.Con, "$", Const.struct, num, 0);
		Code.load(node);
	}
	
	
	
	
	//Obrada Term-a
	@Override
	public void visit(TermMulOperation termMul) {
		if(currentMulOp.equals("MUL")){
			Code.put(Code.mul);
		}
		else if(currentMulOp.equals("DIV")){
			Code.put(Code.div);
		}
		else if(currentMulOp.equals("MOD")){
			Code.put(Code.rem);
		}
	}
	
	public void visit(TermList termList) {
		if(currentMulOp.equals("MUL")){
			Code.put(Code.mul);
		}
		else if(currentMulOp.equals("DIV")){
			Code.put(Code.div);
		}
		else if(currentMulOp.equals("MOD")){
			Code.put(Code.rem);
		}
	}
	
	@Override
	public void visit(ExprAddOpOperation exprAdd) {
		if(currentAddOperation.empty() == false){
			currentAddOp = currentAddOperation.pop(); 
		
		if(currentAddOp.equals("PLUS")){
			Code.put(Code.add);
		}
		else if(currentAddOp.equals("MINUS")){
			Code.put(Code.sub);
		}
	 }
	}
	
	@Override
	public void visit(ExprLists exprLists) {
		if(currentAddOperation.empty() == false){
			currentAddOp = currentAddOperation.pop(); 
		
		if(currentAddOp.equals("PLUS")){
			Code.put(Code.add);
		}
		else if(currentAddOp.equals("MINUS")){
			Code.put(Code.sub);
		}
	 }
	}
	@Override
	public void visit(Plus plus) {
	currentAddOp = "PLUS";
	currentAddOperation.push(currentAddOp);
	}
	
	@Override
	public void visit(Minus minus) {
	currentAddOp = "MINUS";
	currentAddOperation.push(currentAddOp);
	}
	
	@Override
	public void visit(Mul mul) {
	currentMulOp = "MUL";
	}
	
	@Override
	public void visit(Div div) {
	currentMulOp = "DIV";
	}
	
	@Override
	public void visit(Mod mod) {
	currentMulOp = "MOD";
	}
	
	@Override
	public void visit(SingleExprMinus singleExprMinus) {
			Code.put(Code.neg);
	}
	
	@Override
	public void visit(ExprTermMinus exprTermMinus) {
			Code.put(Code.neg);
	}
	
	
	@Override
	public void visit(PrintStmt PrintStmt) {
		if(PrintStmt.getExpr().struct.getKind() == Struct.Array){
			if(PrintStmt.getExpr().struct.getElemType().getKind() == Struct.Char){
				Code.put(Code.bprint);	
			}
			else {
				Code.put(Code.print);	
			}
		}else{
			if(PrintStmt.getExpr().struct.getKind() == Struct.Char){
				Code.put(Code.bprint);	
			}else{
				Code.put(Code.print);
			}
		}	
			
	}
	
	public void visit(MatchedStmt mstmt){
		assignementToArray = false;
		print_len_stmt = false;
	}
	
	public void visit(UnmachedStmt umstmt){
		assignementToArray = false;
		print_len_stmt = false;
	}
	
	
	@Override
	public void visit(ReadStmt readStmt) {
		if(readStmt.getDesignator().obj.getType().getKind() == Struct.Array ){
			if(readStmt.getDesignator().obj.getType().getElemType().getKind() == Struct.Char){
				Code.put(Code.bread);	
				Code.put(Code.bastore);
			}
			else {
				Code.put(Code.read);	
				Code.put(Code.astore);
			}
		}else{
			if(readStmt.getDesignator().obj.getType().getKind() == Struct.Char){
				Code.put(Code.bread);	
				Code.store(readStmt.getDesignator().obj);
			}else{
				Code.put(Code.read);
				Code.store(readStmt.getDesignator().obj);
			}
		}	
	}
	
	
	@Override
	public void visit(PotentialNumbers PrintStmt) {
		Code.load(new Obj(Obj.Con, "$", Tab.intType, PrintStmt.getN1(), 0));
	}
	
	@Override
	public void visit(NoPotentialNumbers PrintStmt) {
		Code.put(Code.const_5);
	}
	
	
	@Override
	public void visit(DesignatorStatementAssignment Assignment) {
		if(assignementToArray == true && Assignment.getDesignator().obj.getType().getKind() == Struct.Array){
			if(Assignment.getDesignator().obj.getType().getElemType().getKind() == Struct.Char){
				Code.put(Code.bastore);
			}
			else{
				Code.put(Code.astore);	
			}
		}
		
		else {	
			Code.store(Assignment.getDesignator().obj);
		}
		
		if(newClassFlag == true){
			Obj node = ClassTable.get(newClassName);
			int adr = node.getAdr();
			Code.load(Assignment.getDesignator().obj);
			Code.put(Code.const_);
			Code.put4(adr);
			Code.put(Code.putfield);
			Code.put2(0);
		}
		
		assignementToArray = false;
		newClassFlag = false;
		assignmentToClassField = false;
	}
	
	@Override
	public void visit(DesignatorArray designator) {
		Code.load(designator.obj);
		arrayClassMethodCalled.add(designator.obj);
		assignementToArray = true;
	}
	
	//Poziv funkcija - za sada smatram da funkcije koje se pozivaju nemaju parametre
	@Override //Ovo je samo poziv funkcije, koja nece ucestovati ni u jednom izrazu pa moram njenu povratnu vrednost da skinem sa steka jer se nece nigde koristiti
	public void visit(DesignatorStatementProcCall funcCall) {
		if(classMethodCalled == true){// metoda klase se drugacije poziva od globalnih metoda
			String methName = funcCall.obj.getName();
			
			if(arrayClassMethodCalledflag == false){
			Code.load(lastDesignatorForMethodCall);
			}else{
				Obj node2 = arrayClassMethodCalled.get(1);
				Obj node1 = arrayClassMethodCalled.get(0);
			
				Code.load(node1);
				Code.load(node2);
				Code.put(Code.aload);
			}
			Code.put(Code.getfield);
			Code.put2(0);
			
			Code.put(Code.invokevirtual);
			
			for(int i=0; i<methName.length(); i++){
				int charachter = methName.charAt(i);
				Code.put4(charachter);
			}
			Code.put4(-1);
			
		}else{
		
		Obj functionObj = funcCall.getDesignator().obj;
		int offset = functionObj.getAdr() - Code.pc; 
		Code.put(Code.call);
		Code.put2(offset);
		if(funcCall.getDesignator().obj.getType() != Tab.noType){
			Code.put(Code.pop);
		}
	  }
		classMethodCalled = false;
		lastDesignatorForMethodCall = null;
		arrayClassMethodCalledflag = false;
		arrayClassMethodCalled = new ArrayList();
	}

	@Override//Sada ne moram da sklanjam rezultat sa steka jer ce povratna vrednost metode biti iskoriscenja
	public void visit(FuncCall funcCall) {
		
		
		if(classMethodCalled == true){// metoda klase se drugacije poziva od globalnih metoda
			String methName = funcCall.getDesignator().obj.getName();
			
			if(arrayClassMethodCalledflag == false){
			Code.load(lastDesignatorForMethodCall);
			}else{
				Obj node2 = arrayClassMethodCalled.get(1);
				Obj node1 = arrayClassMethodCalled.get(0);
			
				Code.load(node1);
				Code.load(node2);
				Code.put(Code.aload);
			}
			Code.put(Code.getfield);
			Code.put2(0);
			
			Code.put(Code.invokevirtual);
			
			for(int i=0; i<methName.length(); i++){
				int charachter = methName.charAt(i);
				Code.put4(charachter);
			}
			Code.put4(-1);
			
		}else{
		
		Obj functionObj = funcCall.getDesignator().obj;
		int offset = functionObj.getAdr() - Code.pc; 
		Code.put(Code.call);
		Code.put2(offset);
		}
		
		classMethodCalled = false;
		lastDesignatorForMethodCall = null;
		arrayClassMethodCalledflag = false;
		arrayClassMethodCalled = new ArrayList();
		
	}

	@Override
	public void visit(BinEquality be) {
	currentRelOp = "BIN_EQUAL";
	} 
	
	@Override
	public void visit(NotBinEquality nbe) {
	currentRelOp = "NOT_BIN_EQUAL";
	} 
	
	@Override
	public void visit(GreaterRelOp gro) {
	currentRelOp = "GREATER";
	} 

	@Override
	public void visit(GreaterEqualRelOp gero) {
	currentRelOp = "GREATER_EQUAL";
	} 

	@Override
	public void visit(LowerRelOp lro) {
	currentRelOp = "LOWER";
	} 
	
	@Override
	public void visit(LowerEqualRelOp lero) {
	currentRelOp = "LOWER_EQUAL";
	} 

	//if statement
	@Override
	public void visit(ConditionFactors cndfact) {
	Code.putFalseJump(jumpType(), 0);
	stack.push(Code.pc - 2);
	}
	
	@Override
	public void visit(ConditionFactor cndfact) {
	Code.put(Code.const_n+0);
	Code.putFalseJump(Code.gt, 0);
	stack.push(Code.pc - 2);
	
	}
	
	
	public void visit(CondTermOr cndor) {
		Code.putJump(0);
		int adr2 = Code.pc - 2;
		while(stack.empty() == false){
			Code.fixup(stack.pop());
		}
		stackThen.push(adr2);	//Ako je sve u redu onda ce skociti tu
	
	}
	
	public void visit(ThenPart thenpart) {//oni sto su prosli izraz skacu ovde
			while(stackThen.empty() == false){
			Code.fixup(stackThen.pop());
			}
	}
	
	public void visit(Conditions cond) {
		int number = 0;
		while(stack.empty() == false){
			Integer num = stack.pop();
			number ++;
			stackCascade.push(num);
		}
		numberStack.push(number);
	}
	
	//Dodato
/*	public void visit(SingleCondition cond) {
		int number = 0;
		while(stack.empty() == false){
			Integer num = stack.pop();
			number ++;
			stackCascade.push(num);
		}
		System.out.println("Zapamcen broj na steku je:" + number);
		numberStack.push(number);
	}
	//
	*/
	/*public void visit(SingleCondition cond) {
		int number = 0;
		while(stack.empty() == false){
			Integer num = stack.pop();
			number ++;
			stackCascade.push(num);
		}
		System.out.println("Zapamcen broj na SingleCondition steku je:" + number);
		numberStack.push(number);
	}
	*/
	
	
	public void visit(IfPart ifPart) {
		Code.putJump(0);
		int adr2 = Code.pc - 2;
		int number = 0;
		if(numberStack.empty() == false){
			number = numberStack.pop();
		}
		//Ovako moram da radim ako nemam izraze sa || pa mi smena ide preko SingleCondition
		if(stack.empty() == true){
		while((number != 0) && (stackCascade.empty() == false)){
			Code.fixup(stackCascade.pop());
			number--;
		}	
		}
		else{
			while(stack.empty() == false){
				Code.fixup(stack.pop());
			}
		}
		stackCascade.push(adr2);
		numberStack.push(1);
		
	}

	public void visit(MatchedIf matchedIf) {
		if(numberStack.empty() == false){
		int number = numberStack.pop();
		while((number != 0) && (stackCascade.empty() == false)){
			Code.fixup(stackCascade.pop());
			number--;
		}	
		}else {
			while(stack.empty() == false){
				Code.fixup(stack.pop());
			}
		}
	}
		
	public void visit(UnmatchedIfElse UnmatchedifPart) {
		if(numberStack.empty() == false){
		int number = numberStack.pop();
		while((number != 0) && (stackCascade.empty() == false)){
			Code.fixup(stackCascade.pop());
			number--;
		}	
		}else {
			while(stack.empty() == false){
				Code.fixup(stack.pop());
			}
		}
	}

	public void visit(UnmatchedIf UnmatchedifPart) {
		if(numberStack.empty() == false){
		int number = numberStack.pop();
		while((number != 0) && (stackCascade.empty() == false)){
			Code.fixup(stackCascade.pop());
			number--;
		}		
		}else {
			while(stack.empty() == false){
				Code.fixup(stack.pop());
			}
		}
	}
	
	//obrada do while izraza
	@Override
	public void visit(DoStmt doStmt) {
		doWhileStack.push(Code.pc);
	}
	
	@Override
	public void visit(DoWhileExpr doStmt) {
		if(doWhileStack.empty() == false){
			int address = doWhileStack.pop();
			if(numberStack.empty() == false){//slucaj kada imamo OR sturkuru Condition-a
				
				while(stackThen.empty() == false){
					Code.fixup(stackThen.pop());
				}
				Code.putJump(address);
				int number = numberStack.pop();
				while((number != 0) && (stackCascade.empty() == false)){
					Code.fixup(stackCascade.pop());
					number--;
				}
			}else{
				
				while(stackThen.empty() == false){
					Code.fixup(stackThen.pop());
				}
				Code.putJump(address);
				while(stack.empty() == false){
					Code.fixup(stack.pop());
				}
				
			}
				
				// brojanje break izraza
				BreakCounter breakCnt = new BreakCounter();
				doStmt.traverseTopDown(breakCnt);
				int number = breakCnt.getCount();
				if(breakNumberStack.empty() ==  false){
					int temp = breakNumberStack.pop();
					number = breakCnt.getCount() - temp;
				}
				breakNumberStack.push(breakCnt.getCount());
				
				while((breakStack.empty() == false) && (number != 0)){ // dovoljno je skinuti samo jedan break, jer samo jedan break se moze naci u
					Code.fixup(breakStack.pop());
					number--;
				}
		}
	}

	//Obrada Break izraza
	@Override
	public void visit(BreakExpr breakExpr) {
		Code.putJump(0);
		breakStack.push(Code.pc - 2);
	}

	@Override
	public void visit(ContinueExpr continueExpr) {
		if(doWhileStack.empty() == false){
			int address = doWhileStack.peek();
			Code.putJump(address);
		}
	}
	
	
	//Retrurn Expr
	@Override
	public void visit(ReturnExpr ReturnExpr) {
		Code.put(Code.exit);
		Code.put(Code.return_);
	}
	
	@Override
	public void visit(NoReturnExpr NoReturnExpr) {
		Code.put(Code.exit);
		Code.put(Code.return_);
	}
	
	////////////////////////////////////
	//Obrada klasa
	
	public void visit(PotentialExtends pte){
		potential_extends = true;
		class_to_be_extended = ClassTable.get(pte.getType().getTypeName());
	}
	
	public void visit(NoPotentialExtends npte){
		potential_extends = false;
	}
	
	
	public void visit(ClassDeclarationFirstPart classDecl){			
		current_class = classDecl.getClassDeclName().obj;
		ClassTable.put(classDecl.getClassDeclName().getClassName(), current_class);
		current_class.setAdr(Code.dataSize);
		ClassCreation = true;
	}
	
	public void visit(ClassDeclarationList classDecl){			
		if(potential_extends == false){//radi se o osnovoj klasi
			for(Obj ob: current_class.getType().getMembers().symbols()){
				if(ob.getKind() == Obj.Meth){
					addFunctionEntry (ob.getName(), ob.getAdr());
				}
			}
			addTableTerminator();
		}
		else{//radi se o izvedenoj klasi, prolazim kroz metode roditeljske klase i gledam ako nije redefinisana u potklasi da joj onda ima istu adresu kao i metoda iz natklase
			if(class_to_be_extended != null){
				for(Obj obj_super: class_to_be_extended.getType().getMembers().symbols()){
					if(obj_super.getKind() == Obj.Meth){
						Obj obj_derived = current_class.getType().getMembers().searchKey(obj_super.getName());
						if(obj_derived != null){
							if(obj_derived.getAdr() != obj_super.getAdr() && obj_derived.getAdr() == 0){
								obj_derived.setAdr(obj_super.getAdr() );
							}
						}
					}
				}
			}
		
			for(Obj ob: current_class.getType().getMembers().symbols()){
				if(ob.getKind() == Obj.Meth){
					addFunctionEntry (ob.getName(), ob.getAdr());
				}
			}
				addTableTerminator();
			
		
		
		}
		
	
		potential_extends = false;
		class_to_be_extended = null;
		ClassCreation = false;
	}
	
	

	@Override
	public void visit(FactorNewType newTypeAssign) {
			newClassFlag = true;
			newClassName = newTypeAssign.getType().getTypeName();
		
			Code.put(Code.new_);
			int num = newTypeAssign.struct.getNumberOfFields();
			Code.put2(num*4);
			
	}
	
	
	@Override
	public void visit(SingleDesignatorDot designator) {
			Code.load(designator.obj);
			assignmentToClassField = true;
	}

	public void visit(DesignatorDotIdent designator) {
			if(MethodNameList.containsKey(designator.obj.getName()) == true){
			classMethodCalled = true;
			lastDesignatorForMethodCall = designator.getDesignatorDot().obj;
			}
			else{
				classMethodCalled = false;
				lastDesignatorForMethodCall = Tab.noObj;
			}
	
			//lastDesignatorField = designator.obj;
	}
	
	
}
