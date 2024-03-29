package compiler;

import java.util.*;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;

import compiler.AST.*;
import compiler.FOOLParser.*;
import compiler.lib.*;

import static compiler.lib.FOOLlib.*;

public class ASTGenerationSTVisitor extends FOOLBaseVisitor<Node> {

    String indent;
    public boolean print;

    ASTGenerationSTVisitor() {
    }

    ASTGenerationSTVisitor(boolean debug) {
        print = debug;
    }

    private void printVarAndProdName(ParserRuleContext ctx) {
        String prefix = "";
        Class<?> ctxClass = ctx.getClass(), parentClass = ctxClass.getSuperclass();
        if (!parentClass.equals(ParserRuleContext.class)) // parentClass is the var context (and not ctxClass itself)
            prefix = lowerizeFirstChar(extractCtxName(parentClass.getName())) + ": production #";
        System.out.println(indent + prefix + lowerizeFirstChar(extractCtxName(ctxClass.getName())));
    }

    @Override
    public Node visit(ParseTree t) {
        if (t == null) return null;
        String temp = indent;
        indent = (indent == null) ? "" : indent + "  ";
        Node result = super.visit(t);
        indent = temp;
        return result;
    }

    @Override
    public Node visitProg(ProgContext c) {
        if (print) printVarAndProdName(c);
        return visit(c.progbody());
    }

    @Override
    public Node visitLetInProg(LetInProgContext c) {
        if (print) printVarAndProdName(c);
        List<DecNode> declist = new ArrayList<>();
        for (CldecContext dec : c.cldec()) declist.add((DecNode) visit(dec));
        for (DecContext dec : c.dec()) declist.add((DecNode) visit(dec));
        return new ProgLetInNode(declist, visit(c.exp()));
    }

    @Override
    public Node visitNoDecProg(NoDecProgContext c) {
        if (print) printVarAndProdName(c);
        return new ProgNode(visit(c.exp()));
    }

    @Override
    public Node visitTimesDiv(TimesDivContext c) {
        if (print) printVarAndProdName(c);
        Node n;
        Node l = visit(c.exp(0));
        Node r = visit(c.exp(1));
        if (c.TIMES() != null) {
            n = new TimesNode(l, r);
            n.setLine(c.TIMES().getSymbol().getLine());        // setLine added
        } else if (c.DIV() != null) {
            n = new DivNode(l, r);
            n.setLine(c.DIV().getSymbol().getLine());        // setLine added
        } else {
            throw new RuntimeException("TimesDivContext must be times or div");
        }
        return n;
    }

    @Override
    public Node visitPlusMinus(PlusMinusContext c) {
        if (print) printVarAndProdName(c);
        Node n;
        Node l = visit(c.exp(0));
        Node r = visit(c.exp(1));
        if (c.PLUS() != null) {
            n = new PlusNode(l, r);
            n.setLine(c.PLUS().getSymbol().getLine());
        } else if (c.MINUS() != null) {
            n = new MinusNode(l, r);
            n.setLine(c.MINUS().getSymbol().getLine());
        } else {
            throw new RuntimeException("PlusMinusContext must be times or div");
        }
        return n;
    }

    @Override
    public Node visitComp(CompContext c) {
        if (print) printVarAndProdName(c);
        Node n;
        Node l = visit(c.exp(0));
        Node r = visit(c.exp(1));
        if (c.EQ() != null) {
            n = new EqualNode(l, r);
            n.setLine(c.EQ().getSymbol().getLine());
        } else if (c.GE() != null) {
            n = new GreaterEqualNode(l, r);
            n.setLine(c.GE().getSymbol().getLine());
        } else if (c.LE() != null) {
            n = new LessEqualNode(l, r);
            n.setLine(c.LE().getSymbol().getLine());
        } else {
            throw new RuntimeException("CompContext must be equal, greater equal or less equal");
        }
        return n;
    }

    @Override
    public Node visitNot(NotContext c) {
        if (print) printVarAndProdName(c);
        Node n = new NotNode(visit(c.exp()));
        n.setLine(c.NOT().getSymbol().getLine());
        return n;
    }

    @Override
    public Node visitAndOr(AndOrContext c) {
        if (print) printVarAndProdName(c);
        Node n;
        Node l = visit(c.exp(0));
        Node r = visit(c.exp(1));
        if (c.AND() != null) {
            n = new AndNode(l, r);
            n.setLine(c.AND().getSymbol().getLine());
        } else if (c.OR() != null) {
            n = new OrNode(l, r);
            n.setLine(c.OR().getSymbol().getLine());
        } else {
            throw new RuntimeException("AndOrContext must be AND or OR");
        }
        return n;
    }

    @Override
    public Node visitVardec(VardecContext c) {
        if (print) printVarAndProdName(c);
        Node n = null;
        if (c.ID() != null) { //non-incomplete ST
            n = new VarNode(c.ID().getText(), (TypeNode) visit(c.type()), visit(c.exp()));
            n.setLine(c.VAR().getSymbol().getLine());
        }
        return n;
    }

    @Override
    public Node visitFundec(FundecContext c) {
        if (print) printVarAndProdName(c);
        List<ParNode> parList = new ArrayList<>();
        for (int i = 1; i < c.ID().size(); i++) {
            ParNode p = new ParNode(c.ID(i).getText(), (TypeNode) visit(c.type(i)));
            p.setLine(c.ID(i).getSymbol().getLine());
            parList.add(p);
        }
        List<DecNode> decList = new ArrayList<>();
        for (DecContext dec : c.dec()) decList.add((DecNode) visit(dec));
        Node n = null;
        if (c.ID().size() > 0) { //non-incomplete ST
            n = new FunNode(c.ID(0).getText(), (TypeNode) visit(c.type(0)), parList, decList, visit(c.exp()));
            n.setLine(c.FUN().getSymbol().getLine());
        }
        return n;
    }

    @Override
    public Node visitIntType(IntTypeContext c) {
        if (print) printVarAndProdName(c);
        return new IntTypeNode();
    }

    @Override
    public Node visitBoolType(BoolTypeContext c) {
        if (print) printVarAndProdName(c);
        return new BoolTypeNode();
    }

    @Override
    public Node visitInteger(IntegerContext c) {
        if (print) printVarAndProdName(c);
        int v = Integer.parseInt(c.NUM().getText());
        return new IntNode(c.MINUS() == null ? v : -v);
    }

    @Override
    public Node visitTrue(TrueContext c) {
        if (print) printVarAndProdName(c);
        return new BoolNode(true);
    }

    @Override
    public Node visitFalse(FalseContext c) {
        if (print) printVarAndProdName(c);
        return new BoolNode(false);
    }

    @Override
    public Node visitIf(IfContext c) {
        if (print) printVarAndProdName(c);
        Node ifNode = visit(c.exp(0));
        Node thenNode = visit(c.exp(1));
        Node elseNode = visit(c.exp(2));
        Node n = new IfNode(ifNode, thenNode, elseNode);
        n.setLine(c.IF().getSymbol().getLine());
        return n;
    }

    @Override
    public Node visitPrint(PrintContext c) {
        if (print) printVarAndProdName(c);
        Node n = new PrintNode(visit(c.exp()));
        n.setLine(c.PRINT().getSymbol().getLine());
        return n;
    }

    @Override
    public Node visitPars(ParsContext c) {
        if (print) printVarAndProdName(c);
        return visit(c.exp());
    }

    @Override
    public Node visitId(IdContext c) {
        if (print) printVarAndProdName(c);
        Node n = new IdNode(c.ID().getText());
        n.setLine(c.ID().getSymbol().getLine());
        return n;
    }

    @Override
    public Node visitCall(CallContext c) {
        if (print) printVarAndProdName(c);
        List<Node> arglist = new ArrayList<>();
        for (ExpContext arg : c.exp()) arglist.add(visit(arg));
        Node n = new CallNode(c.ID().getText(), arglist);
        n.setLine(c.ID().getSymbol().getLine());
        return n;
    }

    // OBJECT-ORIENTED EXTENSION


    @Override
    public Node visitCldec(CldecContext c) {
        if (print) printVarAndProdName(c);
        List<FieldNode> fieldsList = new ArrayList<>();
        // Start from id 2 if extends
        boolean extend = c.EXTENDS() != null;
        int initialIdIndex = c.EXTENDS() != null ? 2 : 1;
        String superId = c.EXTENDS() != null ? c.ID(1).getText() : null;

        for (int i = initialIdIndex; i < c.ID().size(); i++) {
            FieldNode p = new FieldNode(c.ID(i).getText(), (TypeNode) visit(c.type(i - initialIdIndex)));
            p.setLine(c.ID(i).getSymbol().getLine());
            fieldsList.add(p);
        }
        List<MethodNode> methodsList = new ArrayList<>();
        for (MethdecContext dec : c.methdec()) methodsList.add((MethodNode) visit(dec));
        Node n = null;
        if (((c.ID().size() > 0) && !extend) || ((c.ID().size() > 1) && extend)) { //non-incomplete ST
            n = new ClassNode(superId, c.ID(0).getText(), fieldsList, methodsList);
            n.setLine(c.CLASS().getSymbol().getLine());
        }
        return n;
    }

    @Override
    public Node visitDotCall(DotCallContext c) {
        if (print) printVarAndProdName(c);
        List<Node> arglist = new ArrayList<>();
        for (ExpContext arg : c.exp()) arglist.add(visit(arg));
        Node n = null;
        if (c.ID().size() == 2) {
            n = new ClassCallNode(c.ID(0).getText(), c.ID(1).getText(), arglist);
            n.setLine(c.ID(0).getSymbol().getLine());
        }
        return n;
    }

    @Override
    public Node visitNew(NewContext c) {
        if (print) printVarAndProdName(c);
        List<Node> arglist = new ArrayList<>();
        for (ExpContext arg : c.exp()) arglist.add(visit(arg));
        Node n = null;
        if (c.ID() != null) {
            n = new NewNode(c.ID().getText(), arglist);
            n.setLine(c.ID().getSymbol().getLine());
        }
        return n;
    }

    @Override
    public Node visitNull(NullContext c) {
        if (print) printVarAndProdName(c);
        Node n = new EmptyNode();
        n.setLine(c.NULL().getSymbol().getLine());
        return n;
    }

    @Override
    public Node visitIdType(IdTypeContext c) {
        return new RefTypeNode(c.ID().getText());
    }

    @Override
    public Node visitMethdec(MethdecContext c) {
        if (print) printVarAndProdName(c);
        List<ParNode> parList = new ArrayList<>();
        for (int i = 1; i < c.ID().size(); i++) {
            ParNode p = new ParNode(c.ID(i).getText(), (TypeNode) visit(c.type(i)));
            p.setLine(c.ID(i).getSymbol().getLine());
            parList.add(p);
        }
        List<DecNode> decList = new ArrayList<>();
        for (DecContext dec : c.dec()) decList.add((DecNode) visit(dec));
        Node n = null;
        if (c.ID().size() > 0) { //non-incomplete ST
            n = new MethodNode(c.ID(0).getText(), (TypeNode) visit(c.type(0)), parList, decList, visit(c.exp()));
            n.setLine(c.FUN().getSymbol().getLine());
        }
        return n;
    }

    // NAMED ARGUMENTS EXTENSION


    @Override
    public Node visitNewNamed(NewNamedContext c) {
        if (print) printVarAndProdName(c);
        List<NArgNode> argList = new ArrayList<>();
        for (int i = 1; i < c.ID().size(); i++) {
            NArgNode arg=new NArgNode(c.ID(i).getText(),visit(c.exp(i-1)));
            arg.setLine(c.ID(i).getSymbol().getLine());
            argList.add(arg);
        }

        Node n = null;
        if (c.ID().size() > 0) {
            n= new NNewNode(c.ID(0).getText(),argList);
            n.setLine(c.NEW().getSymbol().getLine());
        }
        return n;
    }
}
