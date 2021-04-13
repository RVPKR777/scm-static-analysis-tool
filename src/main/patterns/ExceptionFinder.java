package soen6591tutorial1.patterns;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.CompilationUnit;

import soen6591tutorial1.handlers.SampleHandler;
import soen6591tutorial1.visitors.CatchClauseVisitor;

public class ExceptionFinder {

	public void findExceptions(IProject project) throws JavaModelException {
		IPackageFragment[] packages = JavaCore.create(project).getPackageFragments();

		for(IPackageFragment mypackage : packages){
			//System.out.println("******" + mypackage);
			findTargetCatchClauses(mypackage);
		}		
	}

	private void findTargetCatchClauses(IPackageFragment packageFragment) throws JavaModelException {
		for (ICompilationUnit unit : packageFragment.getCompilationUnits()) {
			CompilationUnit parsedCompilationUnit = parse(unit);
			//System.out.println(parsedCompilationUnit.getTypeRoot().getElementName());
			//System.out.println(parsedCompilationUnit.getLineNumber(startPosition));
						
			CatchClauseVisitor exceptionVisitor = new CatchClauseVisitor();
			parsedCompilationUnit.accept(exceptionVisitor);
			
			printExceptions(exceptionVisitor, unit, parsedCompilationUnit, packageFragment);
		}
		
	}

	private void printExceptions(CatchClauseVisitor visitor, ICompilationUnit unit, CompilationUnit parsedCompilationUnit, IPackageFragment packageFragment) throws JavaModelException {
		for(CatchClause statement: visitor.getEmptyCatches()) {
			String catchclause = statement.toString();
			String classname = unit.getElementName();
			String classpathstring = unit.findPrimaryType().toString();
			String[] cc = classpathstring.split("]]]");
			String classpath = cc[0].replace("in", "").replace(" ", "").replace("[", " <- ");
			//System.out.println(cc[0]);
			//String classpathstring = unit.getSource().toString();
			//var array = classpathstring.split('\n',1)[0];
			int line = parsedCompilationUnit.getLineNumber(statement.getStartPosition());
			printdetails(catchclause, classname, line, classpath);
		}
		
	}
	
	public void printdetails(String catchclause, String classname, Integer line, String classpath) {
		//SampleHandler.printMessage("Class Name :"+ classname);
		SampleHandler.printMessage("Trace : "+ classpath);
		SampleHandler.printMessage("Line Number : "+ line);
		SampleHandler.printMessage(catchclause);
	}
	

	private CompilationUnit parse(ICompilationUnit unit) {
		ASTParser parser = ASTParser.newParser(AST.JLS15);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setSource(unit);
		parser.setResolveBindings(true);
		parser.setBindingsRecovery(true);
		parser.setStatementsRecovery(true);
		return (CompilationUnit) parser.createAST(null); // parse
	}

}
