grammar Expr;

expr
  : JAVA_ID+
  ;

// Identifiers must appear after all keywords like AND, OR, etc.
JAVA_ID : JAVA_ID_START JAVA_ID_PART* ;
fragment JAVA_ID_START : [a-zA-Z$_] ;
fragment JAVA_ID_PART : [a-zA-Z0-9$_] ;

WS : [ \t\r\n\u000C]+ -> skip; // toss out whitespace
