import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class Traductor extends GramaticaBaseListener {
    private void printTab(){
        for(int i=0;i<tab;i++){
            System.out.print("\t");
        }
    }
    private String idFor=""; //Usado para almacenar el valor del id del for
    private int tab=2;
    private Map<String,Integer> datatype=new HashMap<String,Integer>();
    //1: string,2:char,  3: double, 4: int, 5: boolean,



    final private Map<Integer, String> KEYWORDS = new HashMap<Integer, String>(){{
        put(GramaticaLexer.INICIO,"");
        put(GramaticaLexer.FIN,"");
        put(GramaticaLexer.ENTERO,"int");
        put(GramaticaLexer.REAL,"double");
        put(GramaticaLexer.BOOLEANO,"boolean");
        put(GramaticaLexer.CARACTER,"char");
        put(GramaticaLexer.CADENA,"String");
        put(GramaticaLexer.VERDADERO,"true ");
        put(GramaticaLexer.FALSO,"false ");
        put(GramaticaLexer.DIV,"pendiente "); //Pendiente
        put(GramaticaLexer.MOD,"% ");
        put(GramaticaLexer.ESCRIBA,"");
        put(GramaticaLexer.REGISTRO,"");
        put(GramaticaLexer.AND,"& ");
        put(GramaticaLexer.OR,"| ");
        put(GramaticaLexer.LEA,"");
        put(GramaticaLexer.NUEVA_LINEA,"");
        put(GramaticaLexer.SI,"");
        put(GramaticaLexer.SINO,""); //Sujeto a cambios
        put(GramaticaLexer.ENTONCES,"{\n");
        put(GramaticaLexer.CASO,""); //Pendiente de complementar
        put(GramaticaLexer.MIENTRAS,"while");
        put(GramaticaLexer.HAGA,"{\n"); //Pendiente de complementar
        put(GramaticaLexer.REPITA,""); //completar
        put(GramaticaLexer.HASTA,"");
        put(GramaticaLexer.PARA,""); //
        put(GramaticaLexer.PROCEDIMIENTO,"");
        put(GramaticaLexer.VAR,""); //Completar
        put(GramaticaLexer.FUNCION,"");
        put(GramaticaLexer.RETORNE,"return ");
        put(GramaticaLexer.ARREGLO,""); //Completar
        put(GramaticaLexer.DE,"");
        put(GramaticaLexer.LLAMAR,"");
        put(GramaticaLexer.TKN_ASSIGN,"= ");
        put(GramaticaLexer.TKN_PERIOD,".");
        put(GramaticaLexer.TKN_COMMA,""); //No tocar xd
        put(GramaticaLexer.TKN_OPENING_BRA,""); //Por cuestiones de matrices
        put(GramaticaLexer.TKN_CLOSING_BRA,"");
        put(GramaticaLexer.TKN_OPENING_PAR,"");
        put(GramaticaLexer.TKN_CLOSING_PAR,"");
        put(GramaticaLexer.TKN_NEQ,"!=");
        put(GramaticaLexer.TKN_EQUAL,"==");
        put(GramaticaLexer.TKN_COLON,"");

    }};



    final private Map<String, String> tipos = new HashMap<String, String>(){{
        put("entero","int");
        put("real","double");
        put("booleano","boolean");
        put("caracter","char");
        put("cadena","String");
    }};
    //Crear lista de palabras reservadas en Java para evitar usarlas
    //************* REGISTROOOO *************
    /*@Override
    public void enterDeclaracionesR(GramaticaParser.DeclaracionesRContext ctx){
        printTab();
        System.out.println("import java.util.HashMap;");
        System.out.println("import java.util.Map;");
        System.out.println("public class Registro {");
        System.out.println("\tprivate Map<String, Object> atributos;");
        System.out.println("\tpublic Registro() {");
        System.out.println("\t\tatributos = new HashMap<>();");
        System.out.println("\t}");
        System.out.println("\tpublic void agregarAtributo(String nombre, Object valor) {");
        System.out.println("\t\tatributos.put(nombre, valor);");
        System.out.println("\t}");
        System.out.println("\tpublic Object obtenerAtributo(String nombre) {");
        System.out.println("\t\treturn atributos.get(nombre);");
        System.out.println("\t}");
        System.out.println("}");
    }
    @Override
    public void exitDeclaracionR(GramaticaParser.DeclaracionRContext ctx){
        String var = ctx.ID().getText();
        System.out.println("Registro " + var + " = new Registro();");

    }*/

    @Override
    public void  exitExp(GramaticaParser.ExpContext ctx){
        printTab();
        System.out.print(ctx.getText());
    }
    @Override
    public void  enterDeclaracionArray(GramaticaParser.DeclaracionArrayContext ctx){
        printTab();
        String var = ctx.tipo().getText();
        if(tipos.containsKey(var)){
            var = tipos.get(var);
        }
        if(ctx.TKN_INTEGER().size()==1){
            System.out.println(var + "[] " + ctx.ID().getText() + "= new " + var + "[" +ctx.TKN_INTEGER(0) +"]"+ ";");
        }else if (ctx.TKN_INTEGER().size()==2){
            System.out.println(var + "[][] " + ctx.ID().getText() + "= new " + var + "[" +ctx.TKN_INTEGER(0) + "]"+ "[" +ctx.TKN_INTEGER(1)+ "]"+ ";");
        }else if (ctx.TKN_INTEGER().size()==3) {
            System.out.println(var + "[][][] " + ctx.ID().getText() + "= new " + var + "[" +ctx.TKN_INTEGER(0)+"]" + "[" +ctx.TKN_INTEGER(1)+ "]" + "[" + ctx.TKN_INTEGER(2) +"]"+ ";");
        }
    }


    @Override
    public void enterDecCommaId( GramaticaParser.DecCommaIdContext ctx) {
        int tam = ctx.ID().size();
        for(int i  = 0; i <tam ; i++){
            System.out.print(", "+ctx.ID().get(i).getText());
        }
    }
    @Override
    public void enterDeclaracionF(GramaticaParser.DeclaracionFContext ctx) {
        printTab();
        String var = ctx.tipoRetorno().getText();
        if(tipos.containsKey(var)){
            var = tipos.get(var);
        }
        System.out.println("private "+ var + " " + ctx.ID().getText() + "(" + ctx.parametrosFP().getText() + ")" + "{");
    }
    @Override
    public void exitDeclaracionF(GramaticaParser.DeclaracionFContext ctx) {

        //System.out.println(ctx.sentencia().toString() + ";");
        System.out.println("return " + ctx.exp().getText() + ";");
        printTab();
        System.out.println("}");
    }
    @Override
    public void enterDeclaracionP(GramaticaParser.DeclaracionPContext ctx) {
        printTab();
        System.out.println("private void"+ ctx.ID().getText() + ";");
    }

    @Override
    public void enterDeclaracionV(GramaticaParser.DeclaracionVContext ctx) {
        printTab();
        String var = ctx.tipo().getText();
        if(tipos.containsKey(var)){
            var = tipos.get(var);
        }else if (var.contains("cadena[")){
            var = "String";
        }
        System.out.print(var + " " + ctx.ID().getText());
    }
    @Override
    public void exitDeclaracionV(GramaticaParser.DeclaracionVContext ctx) {
        System.out.println(";");
    }

    @Override
    public void enterS(GramaticaParser.SContext ctx) {  //Puede variar por temas de declaración de variables, etc
        System.out.println("import java.util.Scanner;");
        System.out.println("class Main {");
        System.out.println("\tprivate static Scanner scanner=new Scanner(System.in);");
        System.out.println("\tpublic static void main(String[] args) {");
        //Falta scanner y demas librerias necesarias
    }
    @Override
    public void exitS(GramaticaParser.SContext ctx){
        System.out.println("\t}");
        System.out.println("}");
    }
    //Sentences

    //Assign
    @Override
    public void enterSentenceAssign(GramaticaParser.SentenceAssignContext ctx){
        //printTab();
    }
    @Override
    public void exitSentenceAssign(GramaticaParser.SentenceAssignContext ctx){
        System.out.println(";");
    }
    //Write
    @Override
    public void enterSentenceWrite(GramaticaParser.SentenceWriteContext ctx){

    }
    @Override
    public void enterExpEscriba(GramaticaParser.ExpEscribaContext ctx){
        printTab();
        System.out.print("System.out.print(");
    }
    @Override
    public void exitExpEscriba(GramaticaParser.ExpEscribaContext ctx){
        System.out.println(");");
    }
    //Read
    //Es necesario en el flujo inicial definir el scanner para evitar conflictos

    @Override
    public void enterIdLectura(GramaticaParser.IdLecturaContext ctx){
        printTab();
    }
    @Override
    public void exitIdLectura(GramaticaParser.IdLecturaContext ctx){
        int tipo;
        System.out.print("=");
        if(datatype.containsKey(ctx.idConIndexYAtributo().ID().getText())){
            tipo=datatype.get(ctx.idConIndexYAtributo().ID().getText());
            switch (tipo){ //1: int,2:double,  3: boolean, 4: char, 5: string,
                case 1:
                    System.out.print("Integer.nextInt(");
                case 2:
                    System.out.print("scanner.nextDouble()"); //completar
                case 3:
                    System.out.print("scanner.nextBoolean()"); //completar
                case 4:
                    System.out.print("scanner.next()"); //completar
                case 5:
                    System.out.println("scanner.nextLine()");

            }
        }else{
            System.out.println();
        }
    }


    //if statement
    @Override
    public void enterConditional(GramaticaParser.ConditionalContext ctx){
        printTab();
        tab++;
        System.out.print("if "); //Manejo
    }
    @Override
    public void enterExpCondicional(GramaticaParser.ExpCondicionalContext ctx){
        System.out.print("(");
    }
    @Override
    public void exitExpCondicional(GramaticaParser.ExpCondicionalContext ctx){
        System.out.print(")");
    }
    @Override
    public void exitConditional(GramaticaParser.ConditionalContext ctx){
        tab--;
        printTab();
        System.out.println("}");
    }
    @Override
    public void enterSino(GramaticaParser.SinoContext ctx){
        tab--;
        printTab();
        tab++;
        System.out.println("}else{");
    }
    //for statement
    @Override
    public void enterFor(GramaticaParser.ForContext ctx){
        printTab();
        tab++;
        System.out.print("for");
    }
    @Override
    public void enterParaIniCon(GramaticaParser.ParaIniConContext ctx){
        System.out.print("(");
        idFor=ctx.idConIndexYAtributo().getText(); //Provisional, se necesita manejo de index y atributo
    }
    @Override
    public void enterHastaPara(GramaticaParser.HastaParaContext ctx){
        System.out.printf("; %s <=",idFor);
    }
    @Override
    public void exitParaIniCon(GramaticaParser.ParaIniConContext ctx){
        System.out.printf("; %s++)",idFor);
    }
    @Override
    public void exitFor(GramaticaParser.ForContext ctx){
        tab--;
        printTab();
        System.out.println("}");
    }
    //Switch statement
    @Override
    public void enterSwitch(GramaticaParser.SwitchContext ctx){
        printTab();
        System.out.print("switch");
    }
    @Override
    public void enterIdCaso(GramaticaParser.IdCasoContext ctx){
        System.out.print("(");
    }
    @Override
    public void exitIdCaso(GramaticaParser.IdCasoContext ctx){
        System.out.println("){");
        tab+=2;
    }
    @Override
    public void enterExpLiteral(GramaticaParser.ExpLiteralContext ctx){
        //System.out.print("case ");
    }
    /*@Override
    /*public void exitExpLiteral(GramaticaParser.ExpLiteralContext ctx){
        System.out.println(":");
    }
    @Override
    public void enterColonCaso(GramaticaParser.ColonCasoContext ctx){
        System.out.println(":");
    }*/
    @Override
    public void enterSinoCaso(GramaticaParser.SinoCasoContext ctx){
        tab--;
        printTab();
        tab++;
        System.out.print("default");
    }
    @Override
    public void exitSwitch(GramaticaParser.SwitchContext ctx){
        tab-=2;
        printTab();
        System.out.println("}");
    }
    //Terminal handle
    @Override
    public void visitTerminal(TerminalNode node){
        String text=node.getText();
        if(text.charAt(0)=='0'){ //Handle valid java integer and double
            if(text.contains(".")){ //Real-Double
                text=Double.parseDouble(text)+"";
            }else{ //Integer
                text=Integer.parseInt(text)+"";
            }
        }else{ //handle keywords
            if(KEYWORDS.containsKey(node.getSymbol().getType())){text=KEYWORDS.get(node.getSymbol().getType());} //U
        }


        //System.out.print(text); //+,-,*,/,...
    }


}
