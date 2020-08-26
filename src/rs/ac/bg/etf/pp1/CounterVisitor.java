package rs.ac.bg.etf.pp1;

import rs.ac.bg.etf.pp1.ast.FormalParamDecl;
import rs.ac.bg.etf.pp1.ast.*;
import rs.ac.bg.etf.pp1.ast.VisitorAdaptor;

public class CounterVisitor extends VisitorAdaptor {
	
	protected int count;
	
	public int getCount() {
		return count;
	}
	
	public static class FormParamCounter extends CounterVisitor {

		@Override
		public void visit(FormalParamNameSqParent formParamDecl1) {
			count++;
		}		
	
		public void visit(FormalParamNameNoSqParent formParamDecl1) {
			count++;
		}	
		
	}
	
	public static class VarCounter extends CounterVisitor {		
		@Override
		public void visit(VarTypeNameSqParent VarDecl) {
			count++;
		}
		public void visit(VarTypeNameNoSqParent VarDecl) {
			count++;
		}
		
		public void visit(VarDeclarationSqParent VarDecl) {
			count++;
		}
		
		public void visit(VarDeclarationNoSqParent VarDecl) {
			count++;
		}
	}

	public static class ConditionFactorsCounter extends CounterVisitor {

		@Override
		public void visit(ConditionFactors condFactors) {
			count++;
		}			
	}
	
	public static class BreakCounter extends CounterVisitor {

		@Override
		public void visit(BreakExpr breakExprs) {
			count++;
		}		
		
	}
	
	

}
