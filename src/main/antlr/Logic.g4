grammar Logic;

expression:
      VARIABLE                  #expression_var
    | constant                  #expression_const
    | expression AND expression #expression_and
    | expression OR expression  #expression_or
    | '(' expression ')'        #expression_group
;

constant: TRUE | FALSE #const
;

VARIABLE: [a-z] /*(([a-z] | [A-Z])?)+*/;
OR: '|';
AND: '&';


TRUE: 'true';
FALSE: 'false';
WS: ' ' -> skip;