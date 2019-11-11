grammar Lang;

expression:
      literal                               #literal_exp
    | NOT expression                        #not_exp
    | field                                 #field_exp
    | function                              #function_exp
    | expression mathOperator expression    #mathOperator_exp
    | expression operator expression        #operator_exp
    | '('expression')'                      #parentheses_exp
    | expression boolOperator expression    #bool_exp
;

field: NAME;

literal: NUM
    | STRING
    | BOOL
;

function: NAME'(' expression? (',' expression)*')';
mathOperator: PLUS | MINUS;
operator:  GT | LT | GE | LE | EQ ;
boolOperator: AND | OR;

GT: '>';
GE: '>=';
LT: '<';
LE: '<=';
EQ: '=' | '==';
PLUS: '+';
MINUS: '-';

AND: 'and' | 'AND' | '&' | '&&';
OR: 'or' | 'OR'| '|' | '||';

NOT: 'not' | 'NOT' | '!';

STRING: '"' .*? '"';
NUM: [0-9]+ ('.' [0-9]+)?;
BOOL: TRUE | FALSE;
TRUE: 'TRUE' | 'true';
FALSE: 'FALSE' | 'false';

NAME: [a-z] ([A-Za-z0-9])*;

WS: [' '\r\t\n] -> skip;

//array: INT_ARRAY | STRING_ARRAY;
//INT_ARRAY: '[' NUM (',' NUM)* ']';
//STRING_ARRAY : '[' STRING (',' STRING)* ']';