package eu.objectivum.so59078288;

import org.antlr.v4.runtime.tree.TerminalNode;

import static eu.objectivum.so59078288.ExprParser.*;

/**
 * @author <a href="mailto:Octavian.NITA@ext.ec.europa.eu">Octavian NITA</a>
 * @version $Id$
 */
public class Expr2SpELListener extends ExprBaseListener {

  private StringBuilder output;

  public Expr2SpELListener() {
    setOutput(null);
  }

  public Expr2SpELListener(StringBuilder output) {
    setOutput(output);
  }

  public Expr2SpELListener setOutput(StringBuilder output) {
    this.output = output == null ? new StringBuilder() : output;
    return this;
  }

  public String getOutput() {
    return output.toString();
  }

  @Override
  public String toString() {
    return getOutput();
  }

  @Override
  public void enterRelExpr(RelExprContext ctx) {
    if (ctx.EQ() == null) {
      return; // currently, handle only '='
    }

    var stringCmp = false;

    final var leftTk = ctx.getStart();
    var leftOp = leftTk.getText();
    if (leftTk.getType() == STRING) {
      stringCmp = true;
      leftOp = '"' + leftOp.substring(1, leftOp.length() - 1) + '"';
    }

    final var rightTk = ctx.getStop();
    var rightOp = rightTk.getText();
    if (rightTk.getType() == STRING) {
      stringCmp = true;
      rightOp = '"' + rightOp.substring(1, rightOp.length() - 1) + '"';
    }

    if (stringCmp) {
      output.append(leftOp).append(".equalsIgnoreCase(").append(rightOp).append(")");
    } else {
      output.append(leftOp).append(" eq ").append(rightOp);
    }
  }

  @Override
  public void visitTerminal(TerminalNode node) {
    final var tk = node.getSymbol();
    switch (tk.getType()) {
    case LPAREN:
    case RPAREN:
      output.append(tk.getText());
      break;
    case AND:
      output.append(" and ");
      break;
    }
  }
}
