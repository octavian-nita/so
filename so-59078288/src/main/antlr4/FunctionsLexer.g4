// file: FunctionsLexer.g4

lexer grammar FunctionsLexer;

// ----- Default mode: everything that's not a function

FUNCTION_NAME : '$' ALPHA (ALPHA | DIGIT)* -> pushMode(FUNCTION) ;
fragment ALPHA : [a-zA-Z] ;
fragment DIGIT : [0-9] ;

PLAIN_TEXT : .+? -> skip ;

// ----- Function mode

mode FUNCTION;

STRING_PARAM : '\'' ~[']* '\'' ;

LEFT_PARENTHESIS  : '(' ;
RIGHT_PARENTHESIS : ')' -> popMode ;
