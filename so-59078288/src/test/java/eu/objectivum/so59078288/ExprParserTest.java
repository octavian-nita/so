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

    final var inputsAndExpectedOutputs = new String[][]{
      { "(account='233AS77') AND (code='SIMP') and (cost=270)",
        "(account.equalsIgnoreCase(\"233AS77\")) and (code.equalsIgnoreCase(\"SIMP\")) and (cost eq 270)" },
      { "(account='233AS77' AND code='SIMP') and '270' = 270",
        "(account.equalsIgnoreCase(\"233AS77\") and code.equalsIgnoreCase(\"SIMP\")) and \"270\" eq 270" }
    };

    final var translatorToTest = new Expr2SpEL(); // our system-under-test

    for (final var inputAndExpectedOutput : inputsAndExpectedOutputs) {
      assertEquals(inputAndExpectedOutput[1], translatorToTest.toSpEL(inputAndExpectedOutput[0]));
    }
  }
}
