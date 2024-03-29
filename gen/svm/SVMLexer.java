// Generated from /Users/gzaccaroni/DocumentiLocali/Università/MCLC/progetto/src/svm/SVM.g4 by ANTLR 4.9.2
package svm;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class SVMLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.9.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		PUSH=1, POP=2, ADD=3, SUB=4, MULT=5, DIV=6, STOREW=7, LOADW=8, BRANCH=9, 
		BRANCHEQ=10, BRANCHLESSEQ=11, JS=12, LOADRA=13, STORERA=14, LOADTM=15, 
		STORETM=16, LOADFP=17, STOREFP=18, COPYFP=19, LOADHP=20, STOREHP=21, PRINT=22, 
		HALT=23, COL=24, LABEL=25, INTEGER=26, COMMENT=27, WHITESP=28, ERR=29;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"PUSH", "POP", "ADD", "SUB", "MULT", "DIV", "STOREW", "LOADW", "BRANCH", 
			"BRANCHEQ", "BRANCHLESSEQ", "JS", "LOADRA", "STORERA", "LOADTM", "STORETM", 
			"LOADFP", "STOREFP", "COPYFP", "LOADHP", "STOREHP", "PRINT", "HALT", 
			"COL", "LABEL", "INTEGER", "COMMENT", "WHITESP", "ERR"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'push'", "'pop'", "'add'", "'sub'", "'mult'", "'div'", "'sw'", 
			"'lw'", "'b'", "'beq'", "'bleq'", "'js'", "'lra'", "'sra'", "'ltm'", 
			"'stm'", "'lfp'", "'sfp'", "'cfp'", "'lhp'", "'shp'", "'print'", "'halt'", 
			"':'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "PUSH", "POP", "ADD", "SUB", "MULT", "DIV", "STOREW", "LOADW", 
			"BRANCH", "BRANCHEQ", "BRANCHLESSEQ", "JS", "LOADRA", "STORERA", "LOADTM", 
			"STORETM", "LOADFP", "STOREFP", "COPYFP", "LOADHP", "STOREHP", "PRINT", 
			"HALT", "COL", "LABEL", "INTEGER", "COMMENT", "WHITESP", "ERR"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}


	public int lexicalErrors=0;


	public SVMLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "SVM.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	@Override
	public void action(RuleContext _localctx, int ruleIndex, int actionIndex) {
		switch (ruleIndex) {
		case 28:
			ERR_action((RuleContext)_localctx, actionIndex);
			break;
		}
	}
	private void ERR_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 0:
			 System.out.println("Invalid char: "+getText()+" at line "+getLine()); lexicalErrors++; 
			break;
		}
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\37\u00ca\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31"+
		"\t\31\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\3\2\3\2\3\2\3"+
		"\2\3\2\3\3\3\3\3\3\3\3\3\4\3\4\3\4\3\4\3\5\3\5\3\5\3\5\3\6\3\6\3\6\3\6"+
		"\3\6\3\7\3\7\3\7\3\7\3\b\3\b\3\b\3\t\3\t\3\t\3\n\3\n\3\13\3\13\3\13\3"+
		"\13\3\f\3\f\3\f\3\f\3\f\3\r\3\r\3\r\3\16\3\16\3\16\3\16\3\17\3\17\3\17"+
		"\3\17\3\20\3\20\3\20\3\20\3\21\3\21\3\21\3\21\3\22\3\22\3\22\3\22\3\23"+
		"\3\23\3\23\3\23\3\24\3\24\3\24\3\24\3\25\3\25\3\25\3\25\3\26\3\26\3\26"+
		"\3\26\3\27\3\27\3\27\3\27\3\27\3\27\3\30\3\30\3\30\3\30\3\30\3\31\3\31"+
		"\3\32\3\32\7\32\u009f\n\32\f\32\16\32\u00a2\13\32\3\33\3\33\5\33\u00a6"+
		"\n\33\3\33\3\33\7\33\u00aa\n\33\f\33\16\33\u00ad\13\33\5\33\u00af\n\33"+
		"\3\34\3\34\3\34\3\34\7\34\u00b5\n\34\f\34\16\34\u00b8\13\34\3\34\3\34"+
		"\3\34\3\34\3\34\3\35\6\35\u00c0\n\35\r\35\16\35\u00c1\3\35\3\35\3\36\3"+
		"\36\3\36\3\36\3\36\3\u00b6\2\37\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23"+
		"\13\25\f\27\r\31\16\33\17\35\20\37\21!\22#\23%\24\'\25)\26+\27-\30/\31"+
		"\61\32\63\33\65\34\67\359\36;\37\3\2\5\4\2C\\c|\5\2\62;C\\c|\5\2\13\f"+
		"\17\17\"\"\2\u00cf\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13"+
		"\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2"+
		"\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2"+
		"!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3"+
		"\2\2\2\2/\3\2\2\2\2\61\3\2\2\2\2\63\3\2\2\2\2\65\3\2\2\2\2\67\3\2\2\2"+
		"\29\3\2\2\2\2;\3\2\2\2\3=\3\2\2\2\5B\3\2\2\2\7F\3\2\2\2\tJ\3\2\2\2\13"+
		"N\3\2\2\2\rS\3\2\2\2\17W\3\2\2\2\21Z\3\2\2\2\23]\3\2\2\2\25_\3\2\2\2\27"+
		"c\3\2\2\2\31h\3\2\2\2\33k\3\2\2\2\35o\3\2\2\2\37s\3\2\2\2!w\3\2\2\2#{"+
		"\3\2\2\2%\177\3\2\2\2\'\u0083\3\2\2\2)\u0087\3\2\2\2+\u008b\3\2\2\2-\u008f"+
		"\3\2\2\2/\u0095\3\2\2\2\61\u009a\3\2\2\2\63\u009c\3\2\2\2\65\u00ae\3\2"+
		"\2\2\67\u00b0\3\2\2\29\u00bf\3\2\2\2;\u00c5\3\2\2\2=>\7r\2\2>?\7w\2\2"+
		"?@\7u\2\2@A\7j\2\2A\4\3\2\2\2BC\7r\2\2CD\7q\2\2DE\7r\2\2E\6\3\2\2\2FG"+
		"\7c\2\2GH\7f\2\2HI\7f\2\2I\b\3\2\2\2JK\7u\2\2KL\7w\2\2LM\7d\2\2M\n\3\2"+
		"\2\2NO\7o\2\2OP\7w\2\2PQ\7n\2\2QR\7v\2\2R\f\3\2\2\2ST\7f\2\2TU\7k\2\2"+
		"UV\7x\2\2V\16\3\2\2\2WX\7u\2\2XY\7y\2\2Y\20\3\2\2\2Z[\7n\2\2[\\\7y\2\2"+
		"\\\22\3\2\2\2]^\7d\2\2^\24\3\2\2\2_`\7d\2\2`a\7g\2\2ab\7s\2\2b\26\3\2"+
		"\2\2cd\7d\2\2de\7n\2\2ef\7g\2\2fg\7s\2\2g\30\3\2\2\2hi\7l\2\2ij\7u\2\2"+
		"j\32\3\2\2\2kl\7n\2\2lm\7t\2\2mn\7c\2\2n\34\3\2\2\2op\7u\2\2pq\7t\2\2"+
		"qr\7c\2\2r\36\3\2\2\2st\7n\2\2tu\7v\2\2uv\7o\2\2v \3\2\2\2wx\7u\2\2xy"+
		"\7v\2\2yz\7o\2\2z\"\3\2\2\2{|\7n\2\2|}\7h\2\2}~\7r\2\2~$\3\2\2\2\177\u0080"+
		"\7u\2\2\u0080\u0081\7h\2\2\u0081\u0082\7r\2\2\u0082&\3\2\2\2\u0083\u0084"+
		"\7e\2\2\u0084\u0085\7h\2\2\u0085\u0086\7r\2\2\u0086(\3\2\2\2\u0087\u0088"+
		"\7n\2\2\u0088\u0089\7j\2\2\u0089\u008a\7r\2\2\u008a*\3\2\2\2\u008b\u008c"+
		"\7u\2\2\u008c\u008d\7j\2\2\u008d\u008e\7r\2\2\u008e,\3\2\2\2\u008f\u0090"+
		"\7r\2\2\u0090\u0091\7t\2\2\u0091\u0092\7k\2\2\u0092\u0093\7p\2\2\u0093"+
		"\u0094\7v\2\2\u0094.\3\2\2\2\u0095\u0096\7j\2\2\u0096\u0097\7c\2\2\u0097"+
		"\u0098\7n\2\2\u0098\u0099\7v\2\2\u0099\60\3\2\2\2\u009a\u009b\7<\2\2\u009b"+
		"\62\3\2\2\2\u009c\u00a0\t\2\2\2\u009d\u009f\t\3\2\2\u009e\u009d\3\2\2"+
		"\2\u009f\u00a2\3\2\2\2\u00a0\u009e\3\2\2\2\u00a0\u00a1\3\2\2\2\u00a1\64"+
		"\3\2\2\2\u00a2\u00a0\3\2\2\2\u00a3\u00af\7\62\2\2\u00a4\u00a6\7/\2\2\u00a5"+
		"\u00a4\3\2\2\2\u00a5\u00a6\3\2\2\2\u00a6\u00a7\3\2\2\2\u00a7\u00ab\4\63"+
		";\2\u00a8\u00aa\4\62;\2\u00a9\u00a8\3\2\2\2\u00aa\u00ad\3\2\2\2\u00ab"+
		"\u00a9\3\2\2\2\u00ab\u00ac\3\2\2\2\u00ac\u00af\3\2\2\2\u00ad\u00ab\3\2"+
		"\2\2\u00ae\u00a3\3\2\2\2\u00ae\u00a5\3\2\2\2\u00af\66\3\2\2\2\u00b0\u00b1"+
		"\7\61\2\2\u00b1\u00b2\7,\2\2\u00b2\u00b6\3\2\2\2\u00b3\u00b5\13\2\2\2"+
		"\u00b4\u00b3\3\2\2\2\u00b5\u00b8\3\2\2\2\u00b6\u00b7\3\2\2\2\u00b6\u00b4"+
		"\3\2\2\2\u00b7\u00b9\3\2\2\2\u00b8\u00b6\3\2\2\2\u00b9\u00ba\7,\2\2\u00ba"+
		"\u00bb\7\61\2\2\u00bb\u00bc\3\2\2\2\u00bc\u00bd\b\34\2\2\u00bd8\3\2\2"+
		"\2\u00be\u00c0\t\4\2\2\u00bf\u00be\3\2\2\2\u00c0\u00c1\3\2\2\2\u00c1\u00bf"+
		"\3\2\2\2\u00c1\u00c2\3\2\2\2\u00c2\u00c3\3\2\2\2\u00c3\u00c4\b\35\2\2"+
		"\u00c4:\3\2\2\2\u00c5\u00c6\13\2\2\2\u00c6\u00c7\b\36\3\2\u00c7\u00c8"+
		"\3\2\2\2\u00c8\u00c9\b\36\2\2\u00c9<\3\2\2\2\t\2\u00a0\u00a5\u00ab\u00ae"+
		"\u00b6\u00c1\4\2\3\2\3\36\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}