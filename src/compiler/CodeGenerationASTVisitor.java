package compiler;

import compiler.AST.*;
import compiler.lib.*;
import compiler.exc.*;
import svm.ExecuteVM;

import java.util.ArrayList;
import java.util.List;

import static compiler.lib.FOOLlib.*;

public class CodeGenerationASTVisitor extends BaseASTVisitor<String, VoidException> {

  CodeGenerationASTVisitor() {}
  CodeGenerationASTVisitor(boolean debug) {super(false,debug);} //enables print for debugging

    private final List<List<String>> dispatchTables = new ArrayList<>();

	@Override
	public String visitNode(ProgLetInNode n) {
		if (print) printNode(n);
		String declCode = null;
		for (Node dec : n.declist) declCode=nlJoin(declCode,visit(dec));
		return nlJoin(
			"push 0",	
			declCode, // generate code for declarations (allocation)			
			visit(n.exp),
			"halt",
			getCode()
		);
	}

	@Override
	public String visitNode(ProgNode n) {
		if (print) printNode(n);
		return nlJoin(
			visit(n.exp),
			"halt"
		);
	}

	@Override
	public String visitNode(FunNode n) {
		if (print) printNode(n,n.id);
		String declCode = null, popDecl = null, popParl = null;
		for (Node dec : n.declist) {
			declCode = nlJoin(declCode,visit(dec));
			popDecl = nlJoin(popDecl,"pop");
		}
		for (int i=0;i<n.parlist.size();i++) popParl = nlJoin(popParl,"pop");
		String funl = freshFunLabel();
		putCode(
			nlJoin(
				funl+":",
				"cfp", // set $fp to $sp value
				"lra", // load $ra value
				declCode, // generate code for local declarations (they use the new $fp!!!)
				visit(n.exp), // generate code for function body expression
				"stm", // set $tm to popped value (function result)
				popDecl, // remove local declarations from stack
				"sra", // set $ra to popped value
				"pop", // remove Access Link from stack
				popParl, // remove parameters from stack
				"sfp", // set $fp to popped value (Control Link)
				"ltm", // load $tm value (function result)
				"lra", // load $ra value
				"js"  // jump to to popped address
			)
		);
		return "push "+funl;		
	}

	@Override
	public String visitNode(VarNode n) {
		if (print) printNode(n,n.id);
		return visit(n.exp);
	}

	@Override
	public String visitNode(PrintNode n) {
		if (print) printNode(n);
		return nlJoin(
			visit(n.exp),
			"print"
		);
	}

	@Override
	public String visitNode(IfNode n) {
		if (print) printNode(n);
	 	String l1 = freshLabel();
	 	String l2 = freshLabel();		
		return nlJoin(
			visit(n.cond),
			"push 1",
			"beq "+l1,
			visit(n.el),
			"b "+l2,
			l1+":",
			visit(n.th),
			l2+":"
		);
	}

	@Override
	public String visitNode(EqualNode n) {
		if (print) printNode(n);
	 	String l1 = freshLabel();
	 	String l2 = freshLabel();
		return nlJoin(
			visit(n.left),
			visit(n.right),
			"beq "+l1,
			"push 0",
			"b "+l2,
			l1+":",
			"push 1",
			l2+":"
		);
	}

	@Override
	public String visitNode(TimesNode n) {
		if (print) printNode(n);
		return nlJoin(
			visit(n.left),
			visit(n.right),
			"mult"
		);	
	}

	@Override
	public String visitNode(PlusNode n) {
		if (print) printNode(n);
		return nlJoin(
			visit(n.left),
			visit(n.right),
			"add"				
		);
	}

	@Override
	public String visitNode(CallNode n) {
		if (print) printNode(n,n.id);
		String argCode = null, getAR = null;
		for (int i=n.arglist.size()-1;i>=0;i--) argCode=nlJoin(argCode,visit(n.arglist.get(i)));
		for (int i = 0;i<n.nl-n.entry.nl;i++) getAR=nlJoin(getAR,"lw");

		if (n.entry.type instanceof MethodTypeNode) {
			return nlJoin(
					"lfp", // load Control Link (pointer to frame of function "id" caller)
					argCode, // generate code for argument expressions in reversed order
					"lfp", getAR, // retrieve address of frame containing "id" declaration
					// by following the static chain (of Access Links)
					// DA QUI DIFFERENZA (l'access link è l'object pointer)
					"stm", // set $tm to popped value (with the aim of duplicating top of stack)
					"ltm", // load object pointer
					"ltm", // duplicate top of stack (object pointer address)
					"lw", // load value of dispatch pointer
					"push "+n.entry.offset, "add", // compute address of method declaration
					"lw", // load address of "id" function
					"js"  // jump to popped address (saving address of subsequent instruction in $ra)
			);
		} else {
			return nlJoin(
					"lfp", // load Control Link (pointer to frame of function "id" caller)
					argCode, // generate code for argument expressions in reversed order
					"lfp", getAR, // retrieve address of frame containing "id" declaration
					// by following the static chain (of Access Links)
					"stm", // set $tm to popped value (with the aim of duplicating top of stack)
					"ltm", // load Access Link (pointer to frame of function "id" declaration)
					"ltm", // duplicate top of stack
					"push "+n.entry.offset, "add", // compute address of "id" declaration
					"lw", // load address of "id" function
					"js"  // jump to popped address (saving address of subsequent instruction in $ra)
			);
		}
	}

	@Override
	public String visitNode(IdNode n) {
		if (print) printNode(n,n.id);
		String getAR = null;
		for (int i = 0;i<n.nl-n.entry.nl;i++) getAR=nlJoin(getAR,"lw");
		return nlJoin(
			"lfp", getAR, // retrieve address of frame containing "id" declaration
			              // by following the static chain (of Access Links)
			"push "+n.entry.offset, "add", // compute address of "id" declaration
			"lw" // load value of "id" variable
		);
	}

	@Override
	public String visitNode(BoolNode n) {
		if (print) printNode(n,n.val.toString());
		return "push "+(n.val?1:0);
	}

	@Override
	public String visitNode(IntNode n) {
		if (print) printNode(n,n.val.toString());
		return "push "+n.val;
	}

	// OPERATOR EXTENSION

	@Override
	public String visitNode(GreaterEqualNode n) throws VoidException {
		if (print) printNode(n);
		String l1 = freshLabel();
		String l2 = freshLabel();
		return nlJoin(
				visit(n.right),
				visit(n.left),
				"bleq "+l1,
				"push 0",
				"b "+l2,
				l1+":",
				"push 1",
				l2+":"
		);
	}
	@Override
	public String visitNode(LessEqualNode n) throws VoidException {
		if (print) printNode(n);
		String l1 = freshLabel();
		String l2 = freshLabel();
		return nlJoin(
				visit(n.left),
				visit(n.right),
				"bleq "+l1,
				"push 0",
				"b "+l2,
				l1+":",
				"push 1",
				l2+":"
		);
	}

	@Override
	public String visitNode(NotNode n) throws VoidException {
		if (print) printNode(n);
		return nlJoin(
				"push 1",
				visit(n.exp),
				"sub"
		);
  	}
	@Override
	public String visitNode(MinusNode n) {
		if (print) printNode(n);
		return nlJoin(
				visit(n.left),
				visit(n.right),
				"sub"
		);
	}
	@Override
	public String visitNode(OrNode n) {
		if (print) printNode(n);
		String l1 = freshLabel();
		String l2 = freshLabel();
		// If sum of n.left + n.right equal 0 push 0 else push 1
		return nlJoin(
				visit(n.left),
				visit(n.right),
				"add",
				"push 0",
				"beq "+l1,
				"push 1",
				"b "+l2,
				l1+":",
				"push 0",
				l2+":"
		);
	}
	@Override
	public String visitNode(DivNode n) {
		if (print) printNode(n);
		return nlJoin(
				visit(n.left),
				visit(n.right),
				"div"
		);
	}
	@Override
	public String visitNode(AndNode n) {
	  	// Equal to equal node
		if (print) printNode(n);
		return nlJoin(
				visit(n.left),
				visit(n.right),
				"mult"
		);
	}

	// OBJECT-ORIENTED EXTENSION

	@Override
	public String visitNode(MethodNode n) {
		if (print) printNode(n,n.id);
		String declCode = null, popDecl = null, popParl = null;
		for (Node dec : n.declist) {
			declCode = nlJoin(declCode,visit(dec));
			popDecl = nlJoin(popDecl,"pop");
		}
		for (int i=0;i<n.parlist.size();i++) popParl = nlJoin(popParl,"pop");
		String methl = freshFunLabel();
		n.label = methl;
		putCode(
				nlJoin(
						"/*MethodNode*/" + methl+":",
						"cfp", // set $fp to $sp value
						"lra", // load $ra value
						declCode, // generate code for local declarations (they use the new $fp!!!)
						visit(n.exp), // generate code for function body expression
						"stm", // set $tm to popped value (function result)
						popDecl, // remove local declarations from stack
						"sra", // set $ra to popped value
						"pop", // remove Access Link from stack
						popParl, // remove parameters from stack
						"sfp", // set $fp to popped value (Control Link)
						"ltm", // load $tm value (function result)
						"lra", // load $ra value
						"js"  // jump to to popped address
				)
		);
		return null;
	}

	@Override
	public String visitNode(ClassNode n) throws VoidException {
		// se eredito devo andare a prendere la dispatch table della classe da cui eredito
		// e copiarne il contenuto
		// Per trovarla usa offset classe da cui erediti
		ArrayList<String> dispatchTable;
		if (n.superID != null) {
			int superPos = -n.superEntry.offset-2;
			dispatchTable = new ArrayList<>(dispatchTables.get(superPos));
		} else {
			dispatchTable = new ArrayList<>();
		}
		dispatchTables.add(dispatchTable);
		for (MethodNode dec : n.methods) {
			visit(dec);
			dispatchTable.add(dec.offset, dec.label);
		}

		String hpContent = null;
		for (String label : dispatchTable) {
			hpContent = nlJoin(hpContent,
					"push " + label, // Push label
					"lhp", // Load heap pointer
					"sw",  // Store label at heap pointer
					"lhp",
					"push 1",
					"add", // increment heap pointer,
					"shp" // store incremented heap pointer
			);
		}

		return nlJoin(
				"/*ClassNode*/" + "lhp",
				hpContent
		);
	}

	@Override
	public String visitNode(EmptyNode n) throws VoidException {
		return nlJoin(
				"/*EmptyNode*/" + "push -1"
		);
	}

	@Override
	public String visitNode(ClassCallNode n) throws VoidException {
		// ID1.ID2()
		if (print) printNode(n,n.id);
		String argCode = null, getAR = null;
		for (int i=n.arglist.size()-1;i>=0;i--) argCode=nlJoin(argCode,visit(n.arglist.get(i)));
		for (int i = 0;i<n.nl-n.entry.nl;i++) getAR=nlJoin(getAR,"lw");
		return "/*ClassCallNode*/" + nlJoin(
				"lfp", // load Control Link (pointer to frame of function "id" caller)
				argCode, // generate code for argument expressions in reversed order
				"lfp", getAR, // retrieve address of frame containing "ID1" declaration
				// by following the static chain (of Access Links)
				"push "+n.entry.offset, "add", // compute address of "ID1"
				"lw", // load object pointer of "ID1"
				"stm", // set $tm to popped value (with the aim of duplicating top of stack)
				"ltm", // load object pointer of "ID1"
				"ltm", // duplicate top of stack
				"lw",	// load dispatch pointer
				"push "+n.methodEntry.offset, "add", // compute address of dispatch table of "ID1"
				"lw", // load address of "ID2" method
				"js"  // jump to popped address (saving address of subsequent instruction in $ra)
		);
	}

	@Override
	public String visitNode(NewNode n) throws VoidException {
		if (print) printNode(n,n.id);
		String argCode = null, copyArgCode = null;
		for (int i=0; i< n.arglist.size(); i++) argCode=nlJoin(argCode,visit(n.arglist.get(i)));
		for (int i=0; i< n.arglist.size(); i++)
			copyArgCode=nlJoin(copyArgCode,
					"lhp",
					"sw",
					"lhp",
					"push 1",
					"add",
					"shp"
			);
		int pos = ExecuteVM.MEMSIZE+n.entry.offset;
		return nlJoin(
				"/*NewNode*/" + argCode, // generate code for argument expressions in reversed order
				copyArgCode,
				"push " + pos,
				"lw",
				"lhp",
				"sw",
				"lhp", // Duplicate lhp
				"lhp", // Increment lhp
				"push 1",
				"add",
				"shp" // Store incremented lhp
		);
	}
}