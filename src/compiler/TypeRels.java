package compiler;

import compiler.AST.*;
import compiler.lib.*;

import java.util.*;

public class TypeRels {

	public static Map<String, String> superType = new HashMap<>();
	public static Set<String> classesIds = new HashSet<>();


	public static boolean isTypeDefined(TypeNode type) {
		if (type instanceof BoolTypeNode || type instanceof IntTypeNode) {
			return true;
		} else if (type instanceof RefTypeNode refType){
			return classesIds.contains(refType.id);
		}
		return false;
	}
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
			return art.id.equals(brt.id) || isSubTypeWithID(art.id , brt.id);
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

	private static boolean isSubTypeWithID(String a, String b) {
		String superID = a;
		do {
			superID = superType.get(superID);
		} while (superID != null && !superID.equals(b));
		return superID != null;
	}

}
