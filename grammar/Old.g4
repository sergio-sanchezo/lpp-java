grammar Old;
//Reglas gramticales
s: declaracionesR declaracionesV declaracionesFP INICIO sentencia* FIN;
declaracionesR: declaracionR*;
declaracionesV:(declaracionV | declaracionArray)* ;
declaracionesFP: (declaracionF|declaracionP)*;
declaracionR: REGISTRO ID atributos FIN REGISTRO;
atributos: declaracionV* ;
decCommaId: (TKN_COMMA ID)*;
declaracionV: tipo ID decCommaId;
declaracionArray: ARREGLO TKN_OPENING_BRA TKN_INTEGER (TKN_COMMA TKN_INTEGER)* TKN_CLOSING_BRA DE tipo ID;
tipo: ENTERO|
       REAL|
       BOOLEANO|
       CARACTER|
       CADENA TKN_OPENING_BRA TKN_INTEGER TKN_CLOSING_BRA|
       ID;

declaracionF: FUNCION ID parametrosFP? TKN_COLON tipoRetorno declaracionV* sentencia* (RETORNE exp)? FIN; //Incluye declaraciones que son iguales a las del main, y puede o no tener parametros, al igual que retorne
declaracionP:PROCEDIMIENTO ID parametrosFP? declaracionV* sentencia* FIN;
parametrosFP: TKN_OPENING_PAR listaParametrosFP TKN_CLOSING_PAR;
listaParametrosFP:   parametro (TKN_COMMA parametro)*; //
parametro: VAR? tipo ID;
tipoRetorno: ENTERO | REAL | BOOLEANO | CARACTER | ID | CADENA TKN_OPENING_BRA TKN_INTEGER TKN_CLOSING_BRA;
sentencia:
      idConIndexYAtributo TKN_ASSIGN exp #sentenceAssign
      |ESCRIBA expEscriba (',' expEscriba)* #sentenceWrite
      |LEA idLectura (TKN_COMMA idLectura)* #sentenceRead
      |SI expCondicional ENTONCES sentencia* sino?  FIN SI  #conditional
      |MIENTRAS expCondicional HAGA sentencia* FIN MIENTRAS#while //Sergio
      |REPITA sentencia* HASTA expCondicional #doWhile //Sergio
      |PARA paraIniCon HAGA sentencia* FIN PARA #for
      |LLAMAR subrutinaLlamada #callFunction //Sergio
      |CASO  idCaso (expLiteral (TKN_COMMA expLiteral)* TKN_COLON sentencia*)+ (sinoCaso colonCaso sentencia*)? FIN CASO  #switch
;

idCaso: idConIndexYAtributo;
idLectura: idConIndexYAtributo; //new
expEscriba: exp;    //new
hastaPara: HASTA;
expLiteral: TKN_INTEGER| TKN_STRING|TKN_CHAR|TKN_REAL| VERDADERO | FALSO;
colonCaso: TKN_COLON;
sinoCaso: SINO;
subrutinaLlamada: ID argumentos?| NUEVA_LINEA;
argumentos: TKN_OPENING_PAR (exp (TKN_COMMA exp)* )? TKN_CLOSING_PAR;
paraIniCon : idConIndexYAtributo TKN_ASSIGN exp hastaPara exp; //Inicialización e incremento
expCondicional: exp; //se usa expresión aparte para facilitar traducción
sino: SINO sentencia*; //Se hizo regla aparte por facilidad de la traducción (corchetes)
idConIndexYAtributo: ID (indexYAtributo|argumentos);
indexYAtributo: indexAcceso* atributo*;
indexAcceso: TKN_OPENING_BRA TKN_INTEGER listaIndex* TKN_CLOSING_BRA; //Se saca aparte para facilitar el proceso de traducción
listaIndex:',' TKN_INTEGER; //Acceso matriz de varias dimensiones (no se usa TKN_COMMA para facilitar la traducción)
atributo: TKN_PERIOD ID indexYAtributo;
exp: expRelacional expAux?; //Completar
expAux: operadorLogico expRelacional expAux?;
expRelacional: expPotencia expRelacionalAux?;
expRelacionalAux:operadorRelacional expPotencia;
expPotencia: expPlusMinus expPotenciaAux?;
expPotenciaAux: TKN_POWER expPlusMinus expPotenciaAux?;
expPlusMinus: expMultiDiv expPlusMinusAux?;
expPlusMinusAux: plusMinus expMultiDiv expPlusMinusAux?;
expMultiDiv: expDivEntera expMultiDivAux?;
expMultiDivAux: multiDiv expDivEntera expMultiDivAux?;
expDivEntera: expMod expDivEnteraAux?;
expDivEnteraAux: DIV expMod expDivEnteraAux?;
expMod: expSign expModAux?;
expModAux: MOD expSign expModAux?;
expSign: TKN_MINUS? expBase;
expBase: TKN_OPENING_PAR exp TKN_CLOSING_PAR|
         TKN_INTEGER|
         TKN_STRING|
         TKN_REAL|
         TKN_CHAR|
         VERDADERO|
         FALSO|
         idConIndexYAtributo
;
//Operadores logicos y relacionales
operadorRelacional: TKN_EQUAL|TKN_NEQ|TKN_LEQ|TKN_LESS|TKN_GEP|TKN_GREATER;
operadorLogico: AND|OR;
//Operadores artimeticos:
plusMinus:TKN_PLUS|TKN_MINUS;
multiDiv: TKN_TIMES|TKN_DIV;
//Reglas lexicas (tokens):
//KEYWORDS (VAN PRIMERO PARA QUE NO SE TOKENICE ANTES COMO ID

ESPACIOS: [\t\r\n ]+ -> skip; //Ignore spaces
COMMENT: '//' ~[\r\n]*->skip;
COMMENT_MULTI: '/*' .*? '*/' -> skip ;
INICIO: I N I C I O;
FIN: F I N;
ENTERO: E N T E R O;
REAL: R E A L;
BOOLEANO: B O O L E A N O;
CARACTER: C A R A C T E R;
CADENA: C A D E N A;
VERDADERO: V E R D A D E R O;
FALSO: F A L S O;
DIV: D I V;
MOD: M O D;
ESCRIBA: E S C R I B A;
REGISTRO: R E G I S T R O;
AND: Y; //Llamados así para evitar conflictos de noombre con los fragments
OR: O;
LEA: L E A;
NUEVA_LINEA: N U E V A '_' L I N E A;
SI: S I;
SINO: S I N O;
ENTONCES: E N T O N C E S;
CASO: C A S O;
MIENTRAS: M I E N T R A S;
HAGA: H A G A;
REPITA: R E P I T A;
HASTA: H A S T A;
PARA: P A R A;
PROCEDIMIENTO: P R O C E D I M I E N T O;
VAR: V A R;
FUNCION: F U N C I O N;
RETORNE: R E T O R N E;
ARREGLO: A R R E G L O;
DE: D E;
LLAMAR: L L A M A R;


TKN_STRING: ["] (~["\n])* ["];
TKN_CHAR: [']  (~['\n])?  ['] ;
ID: [a-zA-Z_][a-zA-Z_0-9]*;

TKN_REAL:[0-9]+'.'[0-9]+;
TKN_INTEGER: [0-9]+;


//Fragments para los "alfabetos" de los tokens
//Fragments ara que no sea sensible a mayusculas, pero las letras sigan siendo terminales
fragment A:('a'|'A');
fragment B:('b'|'B');
fragment C:('c'|'C');
fragment D:('d'|'D');
fragment E:('e'|'E');
fragment F:('f'|'F');
fragment G:('g'|'G');
fragment H:('h'|'H');
fragment I:('i'|'I');
fragment J:('j'|'J');
fragment K:('k'|'K');
fragment L:('l'|'L');
fragment M:('m'|'M');
fragment N:('n'|'N');
fragment O:('o'|'O');
fragment P:('p'|'P');
fragment Q:('q'|'Q');
fragment R:('r'|'R');
fragment S:('s'|'S');
fragment T:('t'|'T');
fragment U:('u'|'U');
fragment V:('v'|'V');
fragment W:('w'|'W');
fragment X:('x'|'X');
fragment Y:('y'|'Y');
fragment Z:('z'|'Z');
//Simbolos:
TKN_ASSIGN: '<-';
TKN_PERIOD: '.';
TKN_COMMA: ',';
TKN_COLON: ':';
TKN_OPENING_PAR: '(';
TKN_CLOSING_PAR: ')';
TKN_OPENING_BRA: '[';
TKN_CLOSING_BRA: ']';
TKN_PLUS: '+';
TKN_MINUS: '-';
TKN_TIMES: '*';
TKN_POWER: '^';
TKN_EQUAL: '=';
TKN_NEQ: '<>';
TKN_LEQ: '<=';
TKN_LESS: '<';
TKN_GEP: '>=';
TKN_GREATER: '>';
TKN_DIV:'/';