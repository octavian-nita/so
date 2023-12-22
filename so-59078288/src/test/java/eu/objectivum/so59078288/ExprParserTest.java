package eu.objectivum.so59078288;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.jupiter.api.Test;

/**
 * @author <a href="mailto:octavian.nita@gmail.com">Octavian NITA</a>
 * @version $Id$
 */
class ExprParserTest {

  private static ExprParser parserFor(String exprSrc) {

    final var chars = CharStreams.fromString(exprSrc);

    final var exprLexer = new ExprLexer(chars);

    final var tokens = new CommonTokenStream(exprLexer);

    return new ExprParser(tokens);
  }

  @Test
  void _when_then() {

    final var exprIn = "(account='233AS77') AND (code='SIMP') AND (cost=270)";
    final var spelEx =
      "(account.equalsIgnoreCase(\"233AS77\")) and (code.equalsIgnoreCase(\"SIMP\")) and (cost eq 270)";

    final var exprParser = parserFor(exprIn);
    final var exprTree = exprParser.expr();

    //Assertions.assertEquals(spelEx, ...);
  }
}
