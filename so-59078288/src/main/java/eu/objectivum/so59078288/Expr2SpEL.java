package eu.objectivum.so59078288;

import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import static java.lang.System.exit;
import static java.lang.System.out;

/**
 * @author <a href="mailto:octavian.nita@gmail.com">Octavian NITA</a>
 * @version $Id$
 */
public class Expr2SpEL {

  public String toSpEL(String expr) {

    // Set up lexical analysis (tokenization)
    final var chars = CharStreams.fromString(expr);
    final var exprLexer = new ExprLexer(chars);

    // Set up syntax analysis (parsing)
    final var tokens = new CommonTokenStream(exprLexer);
    final var exprParser = new ExprParser(tokens);
    // Cancel parsing as soon as we get a syntax error
    exprParser.setErrorHandler(new BailErrorStrategy());

    // Parse by invoking the grammar start rule
    final var exprTree = exprParser.expr();

    // Set up translation to SpEL
    final var toSpELListener = new Expr2SpELListener(); // our expression tree listener

    // Translate
    ParseTreeWalker.DEFAULT.walk(toSpELListener, exprTree);

    return toSpELListener.getOutput();
  }

  public static void main(String[] args) {
    if (args.length == 0) {
      out.printf("%nUsage: java %s [expr1] [expr2] [...]%n", Expr2SpEL.class.getName());
      exit(1);
    }

    final var translator = new Expr2SpEL();
    for (final var arg : args) {
      out.printf("%n%s%n", translator.toSpEL(arg));
    }
  }
}
