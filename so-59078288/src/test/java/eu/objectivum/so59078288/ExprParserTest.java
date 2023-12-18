package eu.objectivum.so59078288;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.jupiter.api.Test;

/**
 * @author <a href="mailto:octavian.nita@gmail.com">Octavian NITA</a>
 * @version $Id$
 */
class ExprParserTest {

  private static ExprParser createParser(String exprSrc, String srcName) {
    final var charStream = CharStreams.fromString(exprSrc, srcName);
    final var exprLexer = new ExprLexer(charStream);
    final var tokenStream = new CommonTokenStream(exprLexer);
    return new ExprParser(tokenStream);
  }

  @Test
  void _when_then() {

    final var inputExpr = "(account='233AS77') AND (code='SIMP') AND (cost=270)";
    final var expectedSpEL =
      "(account.equalsIgnoreCase(\"233AS77\")) and (code.equalsIgnoreCase(\"SIMP\")) and (cost eq 270)";

    final var exprParser = createParser(inputExpr, "so59078288");

    //Assertions.assertEquals(expectedSpEL, ...);
  }
}
