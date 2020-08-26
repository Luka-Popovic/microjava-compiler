package rs.ac.bg.etf.pp1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import java_cup.runtime.Symbol;
import rs.ac.bg.etf.pp1.ast.*;
import rs.ac.bg.etf.pp1.util.Log4JUtils;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Scope;
import rs.etf.pp1.symboltable.concepts.Struct;
import rs.etf.pp1.symboltable.visitors.DumpSymbolTableVisitor;

public class Compiler {

	static {
		DOMConfigurator.configure(Log4JUtils.instance().findLoggerConfigFile());
		Log4JUtils.instance().prepareLogFile(Logger.getRootLogger());
	}
	
	
	 private StringBuilder output = new StringBuilder();
	 private final String indent = "   ";
	 private StringBuilder currentIndent = new StringBuilder();
		
	 void nextIndentationLevel() {
			currentIndent.append(indent);
		}
		
		void previousIndentationLevel() {
			if (currentIndent.length() > 0)
				currentIndent.setLength(currentIndent.length()-indent.length());
		}
	
	void tsdump(){
		
		System.out.println("=====================SYMBOL TABLE DUMP=========================");
			for (Scope s = Tab.currentScope; s != null; s = s.getOuter()) {
			this.visitScopeNode(s);
		}
		System.out.println(output.toString());
	}
		

		
		
		public void visitObjNode(Obj objToVisit) {
			//output.append("[");
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
			
			if ((Obj.Var == objToVisit.getKind()) && "this".equalsIgnoreCase(objToVisit.getName()))
				output.append("");
			else{
				if(objToVisit.getKind() == Obj.Prog){
					output.append("notype");
				}else if(objToVisit.getKind() == Obj.Meth && objToVisit.getType().getKind() == Struct.None){
					output.append("void");
				}
				else{
				this.visitStructNode(objToVisit.getType());
				}
			}
			output.append(", ");
			output.append(objToVisit.getAdr());
			output.append(", ");
			output.append(objToVisit.getLevel() + " ");
					
			if (objToVisit.getKind() == Obj.Prog || objToVisit.getKind() == Obj.Meth) {
				output.append("\n");
				nextIndentationLevel();
			}
			

			for (Obj o : objToVisit.getLocalSymbols()) {
				output.append(currentIndent.toString());
				this.visitObjNode(o);
				output.append("\n");
			}
			
			if (objToVisit.getKind() == Obj.Prog || objToVisit.getKind() == Obj.Meth) 
				previousIndentationLevel();

			//output.append("]");
			
		}
	
	public void visitScopeNode(Scope scope) {
		for (Obj o : scope.values()) {
			this.visitObjNode(o);
			output.append("\n");
		}
	}
	
	public void visitStructNode(Struct structToVisit) {
		switch (structToVisit.getKind()) {
		case Struct.None:
			output.append("bool");
			break;
		case Struct.Int:
			output.append("int");
			break;
		case Struct.Char:
			output.append("char");
			break;
		case Struct.Array:
			output.append("Arr of ");
			
			switch (structToVisit.getElemType().getKind()) {
			case Struct.None:
				output.append("notype");
				break;
			case Struct.Int:
				output.append("int");
				break;
			case Struct.Char:
				output.append("char");
				break;
			case Struct.Class:
				output.append("Class");
				break;
			}
			break;
		case Struct.Class:
			output.append("Class [");
			for (Obj obj : structToVisit.getMembers().symbols()) {
				this.visitObjNode(obj);
			}
			output.append("]");
			break;
		}

	}

	
	public String getOutput() {
		return output.toString();
	}
	
	
	///
	
	
	
	
	public static void main(String[] args) throws Exception {
		Logger log = Logger.getLogger(Compiler.class);
		if (args.length < 2) {
			log.error("Not enough arguments supplied! Usage: MJParser <source-file> <obj-file> ");
			return;
		}
		
		File sourceCode = new File(args[0]);
		if (!sourceCode.exists()) {
			log.error("Source file [" + sourceCode.getAbsolutePath() + "] not found!");
			return;
		}
			
		log.info("Compiling source file: " + sourceCode.getAbsolutePath());
		
		try (BufferedReader br = new BufferedReader(new FileReader(sourceCode))) {
			Yylex lexer = new Yylex(br);
			MJParser p = new MJParser(lexer);
	        Symbol s = p.parse();  //pocetak parsiranja
	        SyntaxNode prog = (SyntaxNode)(s.value);
	   
	        Program program = (Program) (s.value);
	        
	        // ispis sintaksnog stabla
	        System.out.println(program.toString(""));
			System.out.println("===================================");

			/*
			 
		 	ispis prepoznatih programskih konstrukcija
			RuleVisitor v = new RuleVisitor();
	  		prog.traverseBottomUp(v);  
			  
			  */
			
			if(p.errorDetected == false){
			
	           
			Tab.init(); // Universe scope
			SemanticAnalyzer semanticCheck = new SemanticAnalyzer();
			prog.traverseBottomUp(semanticCheck);
			
	       // log.info("Print calls = " + semanticCheck.printCallCount);
	       
	        Compiler comp = new Compiler();
	        comp.tsdump();
	        //Tab.dump();
	        if (!p.errorDetected && semanticCheck.passed()) {
	        	File objFile = new File(args[1]);
	        	log.info("Generating bytecode file: " + objFile.getAbsolutePath());
	        	if (objFile.exists())
	        		objFile.delete();
	        	
	        	// Code generation...
	        	CodeGenerator codeGenerator = new CodeGenerator();
	        	prog.traverseBottomUp(codeGenerator);
	        	Code.dataSize = Code.dataSize + semanticCheck.nVars;
	        	Code.mainPc = codeGenerator.getMainPc();
	        	Code.write(new FileOutputStream(objFile));
	        	log.info("Parsiranje uspesno zavrseno!");
	        }
	        else {
	        	log.error("Parsiranje NIJE uspesno zavrseno!");
	        }
		}else{
			log.error("Sintaksna analiza NIJE uspesno zavrsena!");
		}
		}
	}
}
