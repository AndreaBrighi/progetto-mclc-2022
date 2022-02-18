package compiler;

import java.util.*;
import compiler.lib.*;

public class AST {
	
	public static class ProgLetInNode extends Node {
		final List<DecNode> declist;
		final Node exp;
		ProgLetInNode(List<DecNode> d, Node e) {
			declist = Collections.unmodifiableList(d); 
			exp = e;
		}

		@Override
		public <S,E extends Exception> S accept(BaseASTVisitor<S,E> visitor) throws E {return visitor.visitNode(this);}
	}

	public static class ProgNode extends Node {
		final Node exp;
		ProgNode(Node e) {exp = e;}

		@Override
		public <S,E extends Exception> S accept(BaseASTVisitor<S,E> visitor) throws E {return visitor.visitNode(this);}
	}
	
	public static class FunNode extends DecNode {
		final String id;
		final TypeNode retType;
		final List<ParNode> parlist;
		final List<DecNode> declist; 
		final Node exp;
		FunNode(String i, TypeNode rt, List<ParNode> pl, List<DecNode> dl, Node e) {
	    	id=i; 
	    	retType=rt; 
	    	parlist=Collections.unmodifiableList(pl); 
	    	declist=Collections.unmodifiableList(dl); 
	    	exp=e;
	    }
		
		//void setType(TypeNode t) {type = t;}
		
		@Override
		public <S,E extends Exception> S accept(BaseASTVisitor<S,E> visitor) throws E {return visitor.visitNode(this);}
	}

	public static class ParNode extends DecNode {
		final String id;
		ParNode(String i, TypeNode t) {id = i; type = t;}

		@Override
		public <S,E extends Exception> S accept(BaseASTVisitor<S,E> visitor) throws E {return visitor.visitNode(this);}
	}
	
	public static class VarNode extends DecNode {
		final String id;
		final Node exp;
		VarNode(String i, TypeNode t, Node v) {id = i; type = t; exp = v;}

		@Override
		public <S,E extends Exception> S accept(BaseASTVisitor<S,E> visitor) throws E {return visitor.visitNode(this);}
	}
		
	public static class PrintNode extends Node {
		final Node exp;
		PrintNode(Node e) {exp = e;}

		@Override
		public <S,E extends Exception> S accept(BaseASTVisitor<S,E> visitor) throws E {return visitor.visitNode(this);}
	}
	
	public static class IfNode extends Node {
		final Node cond;
		final Node th;
		final Node el;
		IfNode(Node c, Node t, Node e) {cond = c; th = t; el = e;}

		@Override
		public <S,E extends Exception> S accept(BaseASTVisitor<S,E> visitor) throws E {return visitor.visitNode(this);}
	}
	
	public static class EqualNode extends Node {
		final Node left;
		final Node right;
		EqualNode(Node l, Node r) {left = l; right = r;}

		@Override
		public <S,E extends Exception> S accept(BaseASTVisitor<S,E> visitor) throws E {return visitor.visitNode(this);}
	}
	public static class TimesNode extends Node {
		final Node left;
		final Node right;
		TimesNode(Node l, Node r) {left = l; right = r;}

		@Override
		public <S,E extends Exception> S accept(BaseASTVisitor<S,E> visitor) throws E {return visitor.visitNode(this);}
	}
	
	public static class PlusNode extends Node {
		final Node left;
		final Node right;
		PlusNode(Node l, Node r) {left = l; right = r;}

		@Override
		public <S,E extends Exception> S accept(BaseASTVisitor<S,E> visitor) throws E {return visitor.visitNode(this);}
	}
	
	public static class CallNode extends Node {
		final String id;
		final List<Node> arglist;
		STentry entry;
		int nl;
		CallNode(String i, List<Node> p) {
			id = i; 
			arglist = Collections.unmodifiableList(p);
		}

		@Override
		public <S,E extends Exception> S accept(BaseASTVisitor<S,E> visitor) throws E {return visitor.visitNode(this);}
	}
	
	public static class IdNode extends Node {
		final String id;
		STentry entry;
		int nl;
		IdNode(String i) {id = i;}

		@Override
		public <S,E extends Exception> S accept(BaseASTVisitor<S,E> visitor) throws E {return visitor.visitNode(this);}
	}
	
	public static class BoolNode extends Node {
		final Boolean val;
		BoolNode(boolean n) {val = n;}

		@Override
		public <S,E extends Exception> S accept(BaseASTVisitor<S,E> visitor) throws E {return visitor.visitNode(this);}
	}
	
	public static class IntNode extends Node {
		final Integer val;
		IntNode(Integer n) {val = n;}

		@Override
		public <S,E extends Exception> S accept(BaseASTVisitor<S,E> visitor) throws E {return visitor.visitNode(this);}
	}
	
	public static class ArrowTypeNode extends TypeNode {
		final List<TypeNode> parlist;
		final TypeNode ret;
		ArrowTypeNode(List<TypeNode> p, TypeNode r) {
			parlist = Collections.unmodifiableList(p); 
			ret = r;
		}

		@Override
		public <S,E extends Exception> S accept(BaseASTVisitor<S,E> visitor) throws E {return visitor.visitNode(this);}
	}
	
	public static class BoolTypeNode extends TypeNode {

		@Override
		public <S,E extends Exception> S accept(BaseASTVisitor<S,E> visitor) throws E {return visitor.visitNode(this);}
	}

	public static class IntTypeNode extends TypeNode {

		@Override
		public <S,E extends Exception> S accept(BaseASTVisitor<S,E> visitor) throws E {return visitor.visitNode(this);}
	}


	// OPERATOR EXTENSION


	public static class GreaterEqualNode extends Node {
		final Node left;
		final Node right;
		GreaterEqualNode(Node l, Node r) {left = l; right = r;}

		@Override
		public <S,E extends Exception> S accept(BaseASTVisitor<S,E> visitor) throws E {
			return visitor.visitNode(this);
		}
	}
	public static class LessEqualNode extends Node {
		final Node left;
		final Node right;
		LessEqualNode(Node l, Node r) {left = l; right = r;}

		@Override
		public <S,E extends Exception> S accept(BaseASTVisitor<S,E> visitor) throws E {
			return visitor.visitNode(this);
		}
	}
	public static class NotNode extends Node {
		final Node exp;
		NotNode(Node e) {exp = e;}

		@Override
		public <S,E extends Exception> S accept(BaseASTVisitor<S,E> visitor) throws E {
			return visitor.visitNode(this);
		}
	}

	public static class MinusNode extends Node {
		final Node left;
		final Node right;
		MinusNode(Node l, Node r) {left = l; right = r;}

		@Override
		public <S,E extends Exception> S accept(BaseASTVisitor<S,E> visitor) throws E {return visitor.visitNode(this);}
	}
	public static class OrNode extends Node {
		final Node left;
		final Node right;
		OrNode(Node l, Node r) {left = l; right = r;}

		@Override
		public <S,E extends Exception> S accept(BaseASTVisitor<S,E> visitor) throws E {
			return visitor.visitNode(this);
		}
	}
	public static class DivNode extends Node {
		final Node left;
		final Node right;
		DivNode(Node l, Node r) {left = l; right = r;}

		@Override
		public <S,E extends Exception> S accept(BaseASTVisitor<S,E> visitor) throws E {
			return visitor.visitNode(this);
		}
	}
	public static class AndNode extends Node {
		final Node left;
		final Node right;
		AndNode(Node l, Node r) {left = l; right = r;}

		@Override
		public <S,E extends Exception> S accept(BaseASTVisitor<S,E> visitor) throws E {
			return visitor.visitNode(this);
		}
	}

	// OBJECT-ORIENTED EXTENSION

	public static class ClassNode extends DecNode {
		final String superID;
		final String id;
		final List<FieldNode> fields;
		final List<MethodNode> methods;

		STentry superEntry;

		ClassNode(String sID, String i, List<FieldNode> fl, List<MethodNode> ml) {
			superID=sID;
			id=Objects.requireNonNull(i);
			fields=Collections.unmodifiableList(Objects.requireNonNull(fl));
			methods=Collections.unmodifiableList(Objects.requireNonNull(ml));
		}
		void setType(ClassTypeNode t) {
			type = Objects.requireNonNull(t);
		}

		@Override
		public <S,E extends Exception> S accept(BaseASTVisitor<S,E> visitor) throws E {return visitor.visitNode(this);}
	}

	public static class FieldNode extends DecNode {
		final String id;
		int offset;
		FieldNode(String i, TypeNode t) {id = i; type = t;}

		@Override
		public <S,E extends Exception> S accept(BaseASTVisitor<S,E> visitor) throws E {return visitor.visitNode(this);}
	}
	public static class MethodNode extends DecNode {
		final String id;
		final TypeNode retType;
		final List<ParNode> parlist;
		final List<DecNode> declist;
		final Node exp;

		String label;
		int offset;

		MethodNode(String i, TypeNode rt, List<ParNode> pl, List<DecNode> dl, Node e) {
			id=i;
			retType=rt;
			parlist=Collections.unmodifiableList(pl);
			declist=Collections.unmodifiableList(dl);
			List<TypeNode> tl = new ArrayList<>();
			for (ParNode p: pl) tl.add(p.getType());
			type = new ArrowTypeNode(tl, rt);
			exp=e;
		}

		//void setType(TypeNode t) {type = t;}

		@Override
		public <S,E extends Exception> S accept(BaseASTVisitor<S,E> visitor) throws E {return visitor.visitNode(this);}
	}

	public static class ClassCallNode extends Node {
		final String id;
		final String methodId;
		final List<Node> arglist;
		STentry entry;
		STentry methodEntry;
		int nl;

		ClassCallNode(String i, String mId, List<Node> p) {
			id = i;
			methodId = mId;
			arglist = Collections.unmodifiableList(p);
		}

		@Override
		public <S,E extends Exception> S accept(BaseASTVisitor<S,E> visitor) throws E {return visitor.visitNode(this);}
	}

	public static class NewNode extends Node {
		final String id;
		STentry entry;
		final List<Node> arglist;

		NewNode(String i, List<Node> a) {
			id = i;
			arglist = a;
		}

		@Override
		public <S,E extends Exception> S accept(BaseASTVisitor<S,E> visitor) throws E {return visitor.visitNode(this);}
	}

	public static class EmptyNode extends Node {

		EmptyNode() {}

		@Override
		public <S,E extends Exception> S accept(BaseASTVisitor<S,E> visitor) throws E {return visitor.visitNode(this);}
	}

	public static class ClassTypeNode extends TypeNode {
		final ArrayList<TypeNode> allFields;
		final ArrayList<ArrowTypeNode> allMethods;

		public ClassTypeNode(ArrayList<TypeNode> allFields, ArrayList<ArrowTypeNode> allMethods) {
			this.allFields = allFields;
			this.allMethods = allMethods;
		}


		@Override
		public <S,E extends Exception> S accept(BaseASTVisitor<S,E> visitor) throws E {return visitor.visitNode(this);}
	}

	public static class MethodTypeNode extends TypeNode {
		final ArrowTypeNode fun;

		public MethodTypeNode(ArrowTypeNode fun) {
			this.fun = fun;
		}


		@Override
		public <S,E extends Exception> S accept(BaseASTVisitor<S,E> visitor) throws E {return visitor.visitNode(this);}
	}

	public static class RefTypeNode extends TypeNode {
		final String id;

		public RefTypeNode(String id) {
			this.id = id;
		}

		@Override
		public <S,E extends Exception> S accept(BaseASTVisitor<S,E> visitor) throws E {return visitor.visitNode(this);}

		@Override
		public String toString() {
			return "RefTypeNode{" +
					"id='" + id + '\'' +
					'}';
		}
	}

	public static class EmptyTypeNode extends TypeNode {

		@Override
		public <S,E extends Exception> S accept(BaseASTVisitor<S,E> visitor) throws E {return visitor.visitNode(this);}
	}

	//KEY ARGUMENT EXTENSION

	public static class NArgNode extends Node {
		final String id;
		STentry entry;
		final Node exp;

		public NArgNode(String id, Node exp) {
			this.id = id;
			this.exp = exp;
		}

		@Override
		public <S,E extends Exception> S accept(BaseASTVisitor<S,E> visitor) throws E {return visitor.visitNode(this);}
	}


	public static class NewKeyNode extends Node {
		final String id;
		STentry entry;
		final List<NArgNode> arglist;

		public NewKeyNode(String id, List<NArgNode> arglist) {
			this.id = id;
			this.arglist = Collections.unmodifiableList(arglist);
		}

		@Override
		public <S,E extends Exception> S accept(BaseASTVisitor<S,E> visitor) throws E {return visitor.visitNode(this);}
	}

}