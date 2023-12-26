package eu.objectivum.so59078288;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author <a href="mailto:octavian.nita@gmail.com">Octavian NITA</a>
 * @version $Id$
 */
class ExprParserTest {

  @Test
  void toSpEL_whenCorrectInput_thenExpectedSpEL() {

    final var translatorToTest = new Expr2SpEL();

    final var exprIn = "(account='233AS77') AND (code='SIMP') and (cost=270)";
    final var spelEx =
      "(account.equalsIgnoreCase(\"233AS77\")) and (code.equalsIgnoreCase(\"SIMP\")) and (cost eq 270)";

    assertEquals(spelEx, translatorToTest.toSpEL(exprIn));
  }
}
