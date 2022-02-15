package compiler;

import java.util.*;
import compiler.AST.*;
import compiler.exc.*;
import compiler.lib.*;

public class SymbolTableASTVisitor extends BaseASTVisitor<Void,VoidException> {
	
	private List<Map<String, STentry>> symTable = new ArrayList<>();
	private Map<String, Map<String, STentry>> classTable = new HashMap<>();
	private int nestingLevel=0; // current nesting level
	private int decOffset=-2; // counter for offset of local declarations at current nesting level 
	int stErrors=0;

	SymbolTableASTVisitor() {}
	SymbolTableASTVisitor(boolean debug) {super(debug);} // enables print for debugging

	private STentry stLookup(String id) {
		int j = nestingLevel;
		STentry entry = null;
		while (j >= 0 && entry == null) 
			entry = symTable.get(j--).get(id);	
		return entry;
	}
	private boolean checkTypeDefined(TypeNode type) {
		if (type instanceof BoolTypeNode || type instanceof IntTypeNode) {
			return true;
		} else if (type instanceof RefTypeNode refType){
			return classTable.containsKey(refType.id);
		}
		return false;
	}
	@Override
	public Void visitNode(ProgLetInNode n) {
		if (print) printNode(n);
		Map<String, STentry> hm = new HashMap<>();
		symTable.add(hm);
	    for (Node dec : n.declist) visit(dec);
		visit(n.exp);
		symTable.remove(0);
		return null;
	}

	@Override
	public Void visitNode(ProgNode n) {
		if (print) printNode(n);
		visit(n.exp);
		return null;
	}
	
	@Override
	public Void visitNode(FunNode n) {
		if (print) printNode(n);
		Map<String, STentry> hm = symTable.get(nestingLevel);
		if(!checkTypeDefined(n.retType)) {
			System.out.println("Fun " + n.id + ": has unknown return type " + n.retType);
			stErrors++;
		}
		List<TypeNode> parTypes = new ArrayList<>();  
		for (ParNode par : n.parlist) {
			if(!checkTypeDefined(par.getType())) {
				System.out.println("Fun " + n.id + ": par id " + par.id + "has unknown type " + par.getType());
				stErrors++;
			}
			parTypes.add(par.getType());
		}
		STentry entry = new STentry(nestingLevel, new ArrowTypeNode(parTypes,n.retType),decOffset--);
		//inserimento di ID nella symtable
		if (hm.put(n.id, entry) != null) {
			System.out.println("Fun id " + n.id + " at line "+ n.getLine() +" already declared");
			stErrors++;
		} 
		//creare una nuova hashmap per la symTable
		nestingLevel++;
		Map<String, STentry> hmn = new HashMap<>();
		symTable.add(hmn);
		int prevNLDecOffset=decOffset; // stores counter for offset of declarations at previous nesting level 
		decOffset=-2;
		
		int parOffset=1;
		for (ParNode par : n.parlist)
			if (hmn.put(par.id, new STentry(nestingLevel,par.getType(),parOffset++)) != null) {
				System.out.println("Par id " + par.id + " at line "+ n.getLine() +" already declared");
				stErrors++;
			}
		for (Node dec : n.declist) visit(dec);
		visit(n.exp);
		//rimuovere la hashmap corrente poiche' esco dallo scope               
		symTable.remove(nestingLevel--);
		decOffset=prevNLDecOffset; // restores counter for offset of declarations at previous nesting level 
		return null;
	}
	
	@Override
	public Void visitNode(VarNode n) {
		if (print) printNode(n);
		visit(n.exp);
		Map<String, STentry> hm = symTable.get(nestingLevel);
		STentry entry = new STentry(nestingLevel,n.getType(),decOffset--);
		if(!checkTypeDefined(n.getType())) {
			System.out.println("Var id " + n.id + "has unknown type " + n.getType());
			stErrors++;
		}
		//inserimento di ID nella symtable
		if (hm.put(n.id, entry) != null) {
			System.out.println("Var id " + n.id + " at line "+ n.getLine() +" already declared");
			stErrors++;
		}
		return null;
	}

	@Override
	public Void visitNode(PrintNode n) {
		if (print) printNode(n);
		visit(n.exp);
		return null;
	}

	@Override
	public Void visitNode(IfNode n) {
		if (print) printNode(n);
		visit(n.cond);
		visit(n.th);
		visit(n.el);
		return null;
	}
	
	@Override
	public Void visitNode(EqualNode n) {
		if (print) printNode(n);
		visit(n.left);
		visit(n.right);
		return null;
	}
	
	@Override
	public Void visitNode(TimesNode n) {
		if (print) printNode(n);
		visit(n.left);
		visit(n.right);
		return null;
	}
	
	@Override
	public Void visitNode(PlusNode n) {
		if (print) printNode(n);
		visit(n.left);
		visit(n.right);
		return null;
	}

	@Override
	public Void visitNode(CallNode n) {
		if (print) printNode(n);
		STentry entry = stLookup(n.id);
		if (entry == null) {
			System.out.println("Fun id " + n.id + " at line "+ n.getLine() + " not declared");
			stErrors++;
		} else {
			n.entry = entry;
			n.nl = nestingLevel;
		}
		for (Node arg : n.arglist) visit(arg);
		return null;
	}

	@Override
	public Void visitNode(IdNode n) {
		if (print) printNode(n);
		STentry entry = stLookup(n.id);
		if (entry == null) {
			System.out.println("Var or Par id " + n.id + " at line "+ n.getLine() + " not declared");
			stErrors++;
		} else {
			n.entry = entry;
			n.nl = nestingLevel;
		}
		return null;
	}

	@Override
	public Void visitNode(BoolNode n) {
		if (print) printNode(n, n.val.toString());
		return null;
	}

	@Override
	public Void visitNode(IntNode n) {
		if (print) printNode(n, n.val.toString());
		return null;
	}

	// OPERATOR EXTENSION


	@Override
	public Void visitNode(GreaterEqualNode n) {
		if (print) printNode(n);
		visit(n.left);
		visit(n.right);
		return null;
	}

	@Override
	public Void visitNode(LessEqualNode n) {
		if (print) printNode(n);
		visit(n.left);
		visit(n.right);
		return null;
	}

	@Override
	public Void visitNode(NotNode n) throws VoidException {
		if (print) printNode(n);
		visit(n.exp);
		return null;
	}

	@Override
	public Void visitNode(MinusNode n) {
		if (print) printNode(n);
		visit(n.left);
		visit(n.right);
		return null;
	}

	@Override
	public Void visitNode(OrNode n) {
		if (print) printNode(n);
		visit(n.left);
		visit(n.right);
		return null;
	}

	@Override
	public Void visitNode(DivNode n) {
		if (print) printNode(n);
		visit(n.left);
		visit(n.right);
		return null;
	}

	@Override
	public Void visitNode(AndNode n) {
		if (print) printNode(n);
		visit(n.left);
		visit(n.right);
		return null;
	}

	// OBJECT-ORIENTED EXTENSION

	@Override
	public Void visitNode(ClassNode n) throws VoidException {
		if (print) printNode(n);
		Map<String, STentry> hm = symTable.get(nestingLevel);
		Map<String, STentry> hmn = new HashMap<>();
		classTable.put(n.id, hmn);
		ArrayList<TypeNode> parTypes = new ArrayList<>();
		for (FieldNode field : n.fields) {
			if(!checkTypeDefined(field.getType())) {
				System.out.println("Method " + n.id + ": par id " + field.id + "has unknown type " + field.getType());
				stErrors++;
			}
			parTypes.add(field.getType());
		}
		ArrayList<ArrowTypeNode> methodTypes = new ArrayList<>();
		for (MethodNode method : n.methods) methodTypes.add((ArrowTypeNode) method.getType());

		STentry entry = new STentry(nestingLevel, new ClassTypeNode(parTypes, methodTypes),decOffset--);
		//inserimento di ID nella symtable
		if (hm.put(n.id, entry) != null) {
			System.out.println("Class id " + n.id + " at line "+ n.getLine() +" already declared");
			stErrors++;
		}
		//creare una nuova hashmap per la symTable
		nestingLevel++;
		symTable.add(hmn);
		int prevNLDecOffset=decOffset; // stores counter for offset of declarations at previous nesting level
		decOffset=0;

		int parOffset=-1;
		for (FieldNode field : n.fields)
			if (hmn.put(field.id, new STentry(nestingLevel,field.getType(),parOffset--)) != null) {
				System.out.println("Field id " + field.id + " at line "+ n.getLine() +" already declared");
				stErrors++;
			}
		for (MethodNode dec : n.methods) visit(dec);
		//rimuovere la hashmap corrente poiche' esco dallo scope
		symTable.remove(nestingLevel--);
		decOffset=prevNLDecOffset; // restores counter for offset of declarations at previous nesting level
		return null;
	}

	@Override
	public Void visitNode(MethodNode n) {
		if (print) printNode(n);
		Map<String, STentry> hm = symTable.get(nestingLevel);
		if(!checkTypeDefined(n.retType)) {
			System.out.println("Method " + n.id + ": has unknown return type " + n.retType);
			stErrors++;
		}
		List<TypeNode> parTypes = new ArrayList<>();
		for (ParNode par : n.parlist) {
			if(!checkTypeDefined(par.getType())) {
				System.out.println("Method " + n.id + ": par id " + par.id + "has unknown type " + par.getType());
				stErrors++;
			}
			parTypes.add(par.getType());
		}
		STentry entry = new STentry(nestingLevel, new MethodTypeNode(new ArrowTypeNode(parTypes,n.retType)),decOffset++);
		//inserimento di ID nella symtable
		if (hm.put(n.id, entry) != null) {
			System.out.println("Method id " + n.id + " at line "+ n.getLine() +" already declared");
			stErrors++;
		}
		n.offset = entry.offset;
		//creare una nuova hashmap per la symTable
		nestingLevel++;
		Map<String, STentry> hmn = new HashMap<>();
		symTable.add(hmn);
		int prevNLDecOffset=decOffset; // stores counter for offset of declarations at previous nesting level
		decOffset=-2;

		int parOffset=1;
		for (ParNode par : n.parlist)
			if (hmn.put(par.id, new STentry(nestingLevel,par.getType(),parOffset++)) != null) {
				System.out.println("Par id " + par.id + " at line "+ n.getLine() +" already declared");
				stErrors++;
			}
		for (Node dec : n.declist) visit(dec);
		visit(n.exp);
		//rimuovere la hashmap corrente poiche' esco dallo scope
		symTable.remove(nestingLevel--);
		decOffset=prevNLDecOffset; // restores counter for offset of declarations at previous nesting level
		return null;
	}

	@Override
	public Void visitNode(ClassCallNode n) throws VoidException {
		if (print) printNode(n);
		STentry entry = stLookup(n.id);
		if (entry == null || !(entry.type instanceof RefTypeNode)) {
			System.out.println("Var id " + n.id + " at line "+ n.getLine() + " not declared");
			stErrors++;
		} else {
			RefTypeNode cl = (RefTypeNode) entry.type;
			n.entry = entry;
			n.nl = nestingLevel;

			STentry methodEntry = classTable.get(cl.id).get(n.methodId);
			if (methodEntry == null) {
				System.out.println("Method id " + n.methodId + " at line "+ n.getLine() + " not declared");
				stErrors++;
			} else {
				n.methodEntry = methodEntry;
			}
		}
		for (Node arg : n.arglist) visit(arg);
		return null;
	}

	@Override
	public Void visitNode(NewNode n) throws VoidException {
		if (print) printNode(n);
		STentry classEntry = stLookup(n.id);
		if (classEntry == null) {
			System.out.println("Class id " + n.id + " at line "+ n.getLine() + " not declared");
			stErrors++;
		} else {
			n.entry = classEntry;
		}
		for (Node arg : n.arglist) visit(arg);
		return null;
	}

	@Override
	public Void visitNode(EmptyNode n) throws VoidException {
		return null;
	}
}
