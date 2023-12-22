grammar Expr;

// Parser rules start with a lowercase letter.

/**
 * The (parser) entry/start rule that matches the entire input expression.
 * <p>
 * We need explicit EOF inclusion because we match the entire input.
 *
 * @see <a href="https://stackoverflow.com/questions/17844248/when-is-eof-needed-in-antlr-4">When is EOF needed in ANTLR 4?</a>
 */
expr
  : expr AND expr
  | '(' expr ')'
  | relExpr
  ;

relExpr
  : operand EQ operand
  ;

operand
  : JAVA_ID
  | STRING
  | INT
  ;

// Lexical/lexer/token rules start with an uppercase letter

EQ  : '=' ;
AND : 'AND' ;

INT    : [0-9]+ ;
STRING : '\'' ~['\\\r\n]* '\'' ;

// Identifiers must appear after all keywords like AND, OR, etc.

/**
 * We are generating SpEL expressions, so we allow only Java-valid identifiers, albeit a bit simplified.
 *
 * @see <a href="https://docs.oracle.com/javase/specs/jls/se21/html/jls-3.html#jls-3.8">3.8. Identifiers</a>
 * @see <a href="https://github.com/antlr/grammars-v4/blob/master/java/java20/Java20Lexer.g4#L273C1-L273C1">ANTLR v4 Java Grammar</a>
 */
JAVA_ID : JAVA_ID_START JAVA_ID_PART* ;
fragment JAVA_ID_START : [a-zA-Z$_] ;
fragment JAVA_ID_PART  : [a-zA-Z$_0-9] ; // fragment rules are not tokens but help with token recognition

// Match but toss out whitespace (every possible input char must be matched by at least one lexical rule).
WS : [ \t\r\n\f]+ -> skip ;
