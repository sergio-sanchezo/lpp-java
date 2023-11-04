grammar prueba;
listaExpr
    : expr ( ',' expr )*
    ;
expLiteral: TKN_INTEGER| TKN_STRING|TKN_CHAR|TKN_REAL| VERDADERO | FALSO;

expr
    : '(' expr ')'
    | expLiteral
    | ID
    | expr '.' ID
    | expr '[' listaExpr ']'
    | ID '(' listaExpr? ')'
    | '-' expr
    |<assoc=right> expr '^' expr
    | expr ( TKN_TIMES | TKN_DIV | DIV| MOD ) expr
    | expr ( TKN_PLUS | TKN_MINUS ) expr
    | expr ( TKN_EQUAL | TKN_NEQ | TKN_LEQ | TKN_GEP | TKN_LESS |TKN_GREATER ) expr
    | expr AND expr
    | expr OR expr
    ;