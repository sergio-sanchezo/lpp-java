grammar Gramatica;
//Reglas gramticales
s: declaracionesR declaracionesV declaracionesFP main;
main: INICIO sentencia* FIN;
declaracionesR: declaracionR*;
declaracionesV:declaracionVMain* ;
declaracionVMain: (declaracionV | declaracionArray);
declaracionesFP: (declaracionF|declaracionP)*;
declaracionR: REGISTRO ID atributos FIN REGISTRO;
atributos: (declaracionV| declaracionArray)* ;
decCommaId: (TKN_COMMA ID)*;
declaracionV: tipo ID decCommaId;
declaracionArray: (declaracionArrayLoop)+ tipo ID;
declaracionArrayLoop: ARREGLO TKN_OPENING_BRA TKN_INTEGER (TKN_COMMA TKN_INTEGER)* TKN_CLOSING_BRA DE;
tipo: ENTERO|
       REAL|
       BOOLEANO|
       CARACTER|
       CADENA TKN_OPENING_BRA TKN_INTEGER TKN_CLOSING_BRA|
       ID;

declaracionF: FUNCION ID parametrosFP? TKN_COLON tipoRetorno declaracionV* INICIO sentencia* retorne? FIN; //Incluye declaraciones que son iguales a las del main, y puede o no tener parametros, al igual que retorne
retorne: RETORNE exp;
declaracionP:PROCEDIMIENTO ID parametrosFP? declaracionV* INICIO sentencia* FIN;
parametrosFP: TKN_OPENING_PAR listaParametrosFP TKN_CLOSING_PAR;
listaParametrosFP:   parametro (TKN_COMMA parametro)*; //
parametro: VAR? tipo ID;
tipoRetorno: ENTERO | REAL | BOOLEANO | CARACTER | ID | CADENA TKN_OPENING_BRA TKN_INTEGER TKN_CLOSING_BRA;
sentencia:
      idConIndexYAtributo tkn_assign exp #sentenceAssign
      |ESCRIBA expEscriba (',' expEscriba)* #sentenceWrite
      |LEA idLectura (TKN_COMMA idLectura)* #sentenceRead
      |SI expCondicional entonces sentencia* sino?  FIN SI  #conditional
      |MIENTRAS expCondicional haga sentencia* FIN MIENTRAS#while //Sergio
      |REPITA sentencia* HASTA doWhileCon #doWhile //Sergio
      |PARA paraIniCon haga sentencia* FIN PARA #for
      |LLAMAR subrutinaLlamada #callFunction //Sergio
      |CASO  idCaso (cuerpoCaso)+ (sinoCaso colonCaso sentencia*)? FIN CASO  #switch
;
tkn_assign: TKN_ASSIGN;
entonces: ENTONCES;
haga: HAGA;
idCaso: idConIndexYAtributo;
cuerpoCaso: expLiteral (TKN_COMMA expLiteral)* TKN_COLON sentencia*;
idLectura: idConIndexYAtributo; //new
expEscriba: exp;    //new
hastaPara: HASTA;
expLiteral: TKN_INTEGER| TKN_STRING|TKN_CHAR|TKN_REAL| VERDADERO | FALSO;
colonCaso: TKN_COLON;
sinoCaso: SINO;
subrutinaLlamada: ID argumentos?| NUEVA_LINEA;
argumentos: TKN_OPENING_PAR (exp (tkn_comma exp)* )? TKN_CLOSING_PAR;
tkn_comma:TKN_COMMA;
paraIniCon : idConIndexYAtributo tkn_assign exp hastaPara exp; //Inicialización e incremento
expCondicional: exp; //se usa expresión aparte para facilitar traducción
sino: SINO sentencia*; //Se hizo regla aparte por facilidad de la traducción (corchetes)
idConIndexYAtributo: ID indexYAtributo;
indexYAtributo: indexAcceso* atributo*;
indexAcceso: TKN_OPENING_BRA (TKN_INTEGER|idConIndexYAtributo) listaIndex* TKN_CLOSING_BRA; //Se saca aparte para facilitar el proceso de traducción
listaIndex:',' (TKN_INTEGER|idConIndexYAtributo); //Acceso matriz de varias dimensiones (no se usa TKN_COMMA para facilitar la traducción)
atributo: TKN_PERIOD ID indexYAtributo;
doWhileCon: exp;
exp: expRelacional expAux?; //Completar
expAux: operadorLogico expRelacional expAux?;
expRelacional: expPotencia expRelacionalAux?;
expRelacionalAux:operadorRelacional expPotencia;
expPotencia: expPlusMinus expPotenciaAux?;
expPotenciaAux: tkn_power expPlusMinus expPotenciaAux?;
expPlusMinus: expMultiDiv expPlusMinusAux?;
expPlusMinusAux: plusMinus expMultiDiv expPlusMinusAux?;
/*expMultiDiv: expDivEntera expMultiDivAux?;
expMultiDivAux: multiDiv expDivEntera expMultiDivAux?;
expDivEntera: expMod expDivEnteraAux?;
expDivEnteraAux: div expMod expDivEnteraAux?;
expMod: expSign expModAux?;
expModAux: mod expSign expModAux?;*/
expMultiDiv: expSign expMultiDivAux?;
expMultiDivAux: (div|mod|multiDiv) expSign expMultiDivAux?;
expSign: tkn_minus? expBase;
expBase: TKN_OPENING_PAR exp TKN_CLOSING_PAR #expParentesis
        |TKN_INTEGER #expInt
         |TKN_STRING #expString
         |TKN_REAL #expDouble
         |TKN_CHAR #expChar
         |VERDADERO #verdadero
         |FALSO #falso
         |id #expId
;
id: ID (indexYAtributo|argumentos);
tkn_power:TKN_POWER;
div: DIV;
mod:MOD;
tkn_minus: TKN_MINUS;
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