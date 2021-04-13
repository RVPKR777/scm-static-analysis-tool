package soen6591tutorial1.visitors;

import java.io.File;
import java.util.HashSet;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.InfixExpression;


public class CatchClauseVisitor extends ASTVisitor {
	HashSet<CatchClause> emptyCatches = new HashSet<>();
	HashSet<CatchClause> genericCatches = new HashSet<>();
	//public File f;
	
	
	@Override
	public boolean visit(CatchClause node) {
		
		if(isDestructiveException(node)) {
			emptyCatches.add(node);
			
		}
		
		return super.visit(node);
	}
	
	private boolean isDestructiveException(CatchClause node) {
		
		return node.getBody().statements().toString().contains("throw new");	
	}
	
	private boolean isGenericException(CatchClause node) {
		return node.getException().getType().resolveBinding().getQualifiedName().equals("java.lang.Exception");
	}

	public HashSet<CatchClause> getEmptyCatches() {
		return emptyCatches;
	}

	public HashSet<CatchClause> getGenericCatches() {
		return genericCatches;
	}
	

}
