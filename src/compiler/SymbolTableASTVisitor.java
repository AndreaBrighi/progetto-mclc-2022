package compiler;

import java.util.*;

import compiler.AST.*;
import compiler.exc.*;
import compiler.lib.*;

public class SymbolTableASTVisitor extends BaseASTVisitor<Void, VoidException> {

    private List<Map<String, STentry>> symTable = new ArrayList<>();
    private Map<String, Map<String, STentry>> classTable = new HashMap<>();
    private Set<String> classFM;
    private int nestingLevel = 0; // current nesting level
    private int decOffset = -2; // counter for offset of local declarations at current nesting level
    int stErrors = 0;

    SymbolTableASTVisitor() {
    }

    SymbolTableASTVisitor(boolean debug) {
        super(debug);
    } // enables print for debugging

    private STentry stLookup(String id) {
        int j = nestingLevel;
        STentry entry = null;
        while (j >= 0 && entry == null)
            entry = symTable.get(j--).get(id);
        return entry;
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
        List<TypeNode> parTypes = new ArrayList<>();
        for (ParNode par : n.parlist) {
            parTypes.add(par.getType());
        }
        STentry entry = new STentry(nestingLevel, new ArrowTypeNode(parTypes, n.retType), decOffset--);
        //inserimento di ID nella symtable
        if (hm.put(n.id, entry) != null) {
            System.out.println("Fun id " + n.id + " at line " + n.getLine() + " already declared");
            stErrors++;
        }
        //creare una nuova hashmap per la symTable
        nestingLevel++;
        Map<String, STentry> hmn = new HashMap<>();
        symTable.add(hmn);
        int prevNLDecOffset = decOffset; // stores counter for offset of declarations at previous nesting level
        decOffset = -2;

        int parOffset = 1;
        for (ParNode par : n.parlist)
            if (hmn.put(par.id, new STentry(nestingLevel, par.getType(), parOffset++)) != null) {
                System.out.println("Par id " + par.id + " at line " + n.getLine() + " already declared");
                stErrors++;
            }
        for (Node dec : n.declist) visit(dec);
        visit(n.exp);
        //rimuovere la hashmap corrente poiche' esco dallo scope
        symTable.remove(nestingLevel--);
        decOffset = prevNLDecOffset; // restores counter for offset of declarations at previous nesting level
        return null;
    }

    @Override
    public Void visitNode(VarNode n) {
        if (print) printNode(n);
        visit(n.exp);
        Map<String, STentry> hm = symTable.get(nestingLevel);
        STentry entry = new STentry(nestingLevel, n.getType(), decOffset--);
        //inserimento di ID nella symtable
        if (hm.put(n.id, entry) != null) {
            System.out.println("Var id " + n.id + " at line " + n.getLine() + " already declared");
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
            System.out.println("Fun id " + n.id + " at line " + n.getLine() + " not declared");
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
            System.out.println("Var or Par id " + n.id + " at line " + n.getLine() + " not declared");
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
        classFM = new HashSet<>();
        Map<String, STentry> hm = symTable.get(nestingLevel);

        ClassTypeNode classTypeN = null;
        Map<String, STentry> hmn = null;
        if (n.superID != null) {
            STentry superEntry = hm.get(n.superID);
            if (superEntry == null) {
                System.out.println("Super class " + n.superID + " at line " + n.getLine() + " not declared");
                stErrors++;
            } else {
                ClassTypeNode superNode = (ClassTypeNode) superEntry.type;
                classTypeN = new ClassTypeNode(new ArrayList<>(superNode.allFields), new ArrayList<>(superNode.allMethods));

                // Set super entry
                n.superEntry = superEntry;
                hmn = new HashMap<>(classTable.get(n.superID));
            }
        } else {
            classTypeN = new ClassTypeNode(new ArrayList<>(), new ArrayList<>());
            hmn = new HashMap<>();
        }
        if (classTypeN != null /*&& hmn != null */) {
            n.setType(classTypeN);
            STentry entry = new STentry(nestingLevel, classTypeN, decOffset--);
            classTable.put(n.id, hmn);
            //inserimento di ID nella symtable
            if (hm.put(n.id, entry) != null) {
                System.out.println("Class id " + n.id + " at line " + n.getLine() + " already declared");
                stErrors++;
            }

            nestingLevel++;
            symTable.add(hmn);
            int prevNLDecOffset = decOffset; // stores counter for offset of declarations at previous nesting level

            // Method offset starts at superclass method count
            decOffset = (classTypeN.allMethods.size());

            // Par offset starts at superclass field count +1
            int parOffset = -(classTypeN.allFields.size() + 1);
            for (FieldNode field : n.fields) {
                STentry currentSTEntry = hmn.get(field.id);
                if (!classFM.add(field.id)) {
                    System.out.println("Field id " + field.id + " at line " + n.getLine() + " already declared");
                    stErrors++;
                }
                int offset;
                if (currentSTEntry != null) {
                    if (currentSTEntry.type instanceof MethodTypeNode) {
                        System.out.println("Can't override method " + field.id + " with a field");
                        stErrors++;
                    }
                    offset = currentSTEntry.offset;
                } else {
                    offset = parOffset--;
                }
                hmn.put(field.id, new STentry(nestingLevel, field.getType(), offset));

                field.offset = offset;
                int fieldIndex = Math.abs(offset) - 1;
                if (fieldIndex < classTypeN.allFields.size()) {
                    classTypeN.allFields.set(fieldIndex, field.getType());
                } else {
                    classTypeN.allFields.add(field.getType());
                }
            }
            for (MethodNode dec : n.methods) {
                visit(dec);
                int methodIndex = dec.offset;
                ArrowTypeNode methodType = (ArrowTypeNode) dec.getType();
                if (methodIndex < classTypeN.allMethods.size()) {
                    classTypeN.allMethods.set(methodIndex, methodType);
                } else {
                    classTypeN.allMethods.add(methodType);
                }
            }
            //rimuovere la hashmap corrente poiche' esco dallo scope
            symTable.remove(nestingLevel--);
            decOffset = prevNLDecOffset; // restores counter for offset of declarations at previous nesting level
        }
        return null;
    }

    @Override
    public Void visitNode(MethodNode n) {
        if (print) printNode(n);
        Map<String, STentry> hm = symTable.get(nestingLevel);
        List<TypeNode> parTypes = new ArrayList<>();
        for (ParNode par : n.parlist) {
            parTypes.add(par.getType());
        }
        //inserimento di ID nella symtable
        STentry currentSTEntry = hm.get(n.id);
        STentry entry;
        if (!classFM.add(n.id)) {
            System.out.println("Method id " + n.id + " at line " + n.getLine() + " already declared");
            stErrors++;
        }
        int offset;
        if (currentSTEntry != null) {
            if (!(currentSTEntry.type instanceof MethodTypeNode)) {
                System.out.println("Can't override field " + n.id + " with a method");
                stErrors++;
            }
            offset = currentSTEntry.offset;
        } else {
            offset = decOffset++;
        }
        entry = new STentry(nestingLevel, new MethodTypeNode(new ArrowTypeNode(parTypes, n.retType)), offset);
        hm.put(n.id, entry);
        n.offset = entry.offset;
        //creare una nuova hashmap per la symTable
        nestingLevel++;
        Map<String, STentry> hmn = new HashMap<>();
        symTable.add(hmn);
        int prevNLDecOffset = decOffset; // stores counter for offset of declarations at previous nesting level
        decOffset = -2;

        int parOffset = 1;
        for (ParNode par : n.parlist)
            if (hmn.put(par.id, new STentry(nestingLevel, par.getType(), parOffset++)) != null) {
                System.out.println("Par id " + par.id + " at line " + n.getLine() + " already declared");
                stErrors++;
            }
        for (Node dec : n.declist) visit(dec);
        visit(n.exp);
        //rimuovere la hashmap corrente poiche' esco dallo scope
        symTable.remove(nestingLevel--);
        decOffset = prevNLDecOffset; // restores counter for offset of declarations at previous nesting level
        return null;
    }

    @Override
    public Void visitNode(ClassCallNode n) throws VoidException {
        if (print) printNode(n);
        STentry entry = stLookup(n.id);
        if (entry == null || !(entry.type instanceof RefTypeNode)) {
            System.out.println("Var id " + n.id + " at line " + n.getLine() + " not declared");
            stErrors++;
        } else {
            RefTypeNode cl = (RefTypeNode) entry.type;
            n.entry = entry;
            n.nl = nestingLevel;

            STentry methodEntry = classTable.get(cl.id).get(n.methodId);
            if (methodEntry == null) {
                System.out.println("Method id " + n.methodId + " at line " + n.getLine() + " not declared");
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
        if (classEntry == null || !classTable.containsKey(n.id)) {
            System.out.println("Class id " + n.id + " at line " + n.getLine() + " not declared");
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

    // NAMED ARGUMENTS EXTENSION


    @Override
    public Void visitNode(NNewNode n) throws VoidException {
        if (print) printNode(n);
        STentry classEntry = stLookup(n.id);
        Map<String, STentry> clT = classTable.get(n.id);
        if (classEntry == null || clT == null) {
            System.out.println("Class id " + n.id + " at line " + n.getLine() + " not declared");
            stErrors++;
        } else {
            n.entry = classEntry;

            clT = new HashMap<>(clT); // Copy class Table
            for (NArgNode arg : n.arglist) {
                STentry argST = clT.get(arg.id);
                if (argST == null) {
                    System.out.println("Arg named " + arg.id + " at line " + arg.getLine() + " not declared");
                    stErrors++;
                } else {
                    arg.entry = argST;
                }
                visit(arg.exp);
                clT.remove(arg.id);
            }
            List<String> argsLeft = clT.entrySet().stream()
                    .filter(el -> !(el.getValue().type instanceof MethodTypeNode))
                    .map(Map.Entry::getKey)
                    .toList();
            if(!argsLeft.isEmpty()) {
                String undefinedArgs = String.join(", ",   argsLeft);
                System.out.println("Args named " + undefinedArgs + " at line " + n.getLine() + " not declared");
                stErrors++;
            }
        }
        return null;
    }
}
