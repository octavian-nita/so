// file: FunctionsParser.g4

parser grammar FunctionsParser;

options { tokenVocab = FunctionsLexer; }

start
    : function* EOF
    ;

function
    : FUNCTION_NAME LEFT_PARENTHESIS param? RIGHT_PARENTHESIS
    ;

param
    : STRING_PARAM
    | function
    ;
