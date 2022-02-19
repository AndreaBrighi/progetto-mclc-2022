package compiler;

import compiler.AST.*;
import compiler.lib.*;

import java.util.*;

public class TypeRels {

	public static Map<String, String> superType = new HashMap<>();

	public static boolean isSubtype(TypeNode a, TypeNode b) {
		if ((a instanceof ArrowTypeNode atn) && (b instanceof ArrowTypeNode btn)) {
			if (atn.parlist.size() != btn.parlist.size()) return false;
			for (int i = 0; i < atn.parlist.size(); i++) {
				if (!isSubtype(btn.parlist.get(i), atn.parlist.get(i))) {
					return false;
				}
			}
			return isSubtype(atn.ret, btn.ret);
		} else if ((a instanceof RefTypeNode art) && (b instanceof RefTypeNode brt)) {
			return isSubTypeWithID(art.id , brt.id);
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
	public static String superType(String a) {
		return superType.get(a);
	}
	private static boolean isSubTypeWithID(String a, String b) {
		String superID = a;
		while (superID != null && !superID.equals(b)){
			superID = superType(superID);
		}
		return superID != null;
	}

	public static TypeNode lowestCommonAncestor(TypeNode a, TypeNode b) {
		if ((a instanceof EmptyTypeNode) && (b instanceof RefTypeNode)) {
			return b;
		} else if ((a instanceof RefTypeNode) && (b instanceof EmptyTypeNode)) {
			return a;
		} else if ((a instanceof RefTypeNode art) && (b instanceof RefTypeNode brt)) {
			String superId = art.id;
			while (superId != null) {
				if (isSubTypeWithID(brt.id, superId)) return new RefTypeNode(superId);
				superId = superType(superId);
			}
			return null;
		} else if (((a instanceof IntTypeNode) && ((b instanceof IntTypeNode) || (b instanceof BoolTypeNode)))
				|| ((a instanceof BoolTypeNode) && (b instanceof IntTypeNode))) {
			return new IntTypeNode();
		} else if ((a instanceof BoolTypeNode) && (b instanceof BoolTypeNode)) {
			return new BoolTypeNode();
		} else {
			return null;
		}
	}
}
