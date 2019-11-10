grammar Lang;

expression:
      literal                           #exp_literal
    | NOT expression                    #exp_not
    | field                             #exp_field
    | expression operator expression    #exp_operator
    | '('expression')'                  #exp_parentheses
    | function                          #exp_function
;

field: NAME;

literal: NUM
    | STRING
    | BOOL
    | array
;

array: INT_ARRAY | STRING_ARRAY;

function: NAME'('expression')';

INT_ARRAY: '[' NUM (',' NUM)* ']';
STRING_ARRAY : '[' STRING (',' STRING)* ']';

operator:  cmp | EQ | IN | AND | OR;
GT: '>';
GE: '>=';
LT: '<';
LE: '<=';
cmp: GT | LT | GE | LE;
AND: 'and' | 'AND' | '&' | '&&';
OR: 'or' | 'OR'| '|' | '||';
IN: 'in' | 'IN';
EQ: '=' | '==';

NOT: 'not' | 'NOT' | '!';

STRING: '"' .*? '"';
NUM: [0-9]+ ('.' [0-9]+)?;
BOOL: TRUE | FALSE;
TRUE: 'TRUE' | 'true';
FALSE: 'FALSE' | 'false';

NAME: [a-z] ([A-Za-z0-9])*;

WS: [' '\r\t\n] -> skip;
