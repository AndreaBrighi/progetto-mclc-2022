package compiler;

import compiler.AST.*;
import compiler.lib.*;

public class TypeRels {

	// valuta se il tipo "a" e' <= al tipo "b", dove "a" e "b" sono tipi di base: IntTypeNode o BoolTypeNode
	public static boolean isSubtype(TypeNode a, TypeNode b) {
		if ((a instanceof RefTypeNode art) && (b instanceof RefTypeNode brt)) {
			return art.id.equals(brt.id);
		} else if (a.getClass().equals(b.getClass())) {
			return true;
		} else if ((a instanceof BoolTypeNode) && (b instanceof IntTypeNode)) {
			return true;
		} else if ((a instanceof EmptyTypeNode) && (b instanceof RefTypeNode)) {
			return true;
		} else {
			return false;
		}
	}

}
