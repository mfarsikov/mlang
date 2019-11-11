grammar Lang;

expression:
      literal                               #literal_exp
    | NOT expression                        #not_exp
    | field                                 #field_exp
    | function0                              #function0_exp
    | function1                              #function1_exp
    | function2                              #function2_exp
    | function3                              #function3_exp
    | expression operator expression        #operator_exp
    | '('expression')'                      #parentheses_exp
    | expression boolOperator expression    #bool_exp
;

field: NAME;

literal: NUM
    | STRING
    | BOOL
;

function0: NAME'()';
function1: NAME'('expression')';
function2: NAME'('expression','expression')';
function3: NAME'('expression','expression','expression')';

operator:  GT | LT | GE | LE | EQ;
boolOperator: AND | OR;

GT: '>';
GE: '>=';
LT: '<';
LE: '<=';
EQ: '=' | '==';

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