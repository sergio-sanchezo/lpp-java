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
        put(GramaticaLexer.ENTERO,"int ");
        put(GramaticaLexer.REAL,"double ");
        put(GramaticaLexer.BOOLEANO,"boolean ");
        put(GramaticaLexer.CARACTER,"char ");
        put(GramaticaLexer.CADENA,"String ");
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
        put(GramaticaLexer.MIENTRAS,"");
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
    //Crear lista de palabras reservadas en Java para evitar usarlas
    @Override
    public void enterS(GramaticaParser.SContext ctx) {  //Puede variar por temas de declaración de variables, etc
        System.out.println("import java.util.Scanner;");
        System.out.println("class Main {");
        printTab();
        System.out.println("private static Scanner scanner=new Scanner(System.in);");
        //Falta scanner y demas librerias necesarias

    }

    @Override
    public void enterDeclaracionF(GramaticaParser.DeclaracionFContext ctx){
        printTab();
        System.out.print("public static ");
    }

    @Override
    public void enterDeclaracionP(GramaticaParser.DeclaracionPContext ctx){
        printTab();
        System.out.print("public static ");
    }

    @Override
    public void enterSubPF(GramaticaParser.SubPFContext ctx){
        System.out.print("{");
    }

    @Override
    public void exitSubPF(GramaticaParser.SubPFContext ctx){
        System.out.println("}");
    }

    @Override
    public void enterSubPP(GramaticaParser.SubPPContext ctx){
        System.out.print("{");
    }

    @Override
    public void exitSubPP(GramaticaParser.SubPPContext ctx){
        System.out.println("}");
    }

    @Override
    public void enterRetornoAux(GramaticaParser.RetornoAuxContext ctx){
        System.out.print(": ");
    }

    @Override
    public void enterParametrosFP(GramaticaParser.ParametrosFPContext ctx){
        System.out.print("(");
    }

    @Override
    public void exitParametrosFP(GramaticaParser.ParametrosFPContext ctx){
        System.out.print(")");
        tab++;
    }

    @Override
    public void exitS(GramaticaParser.SContext ctx){
        System.out.println("}");
    }

    @Override
    public void enterMain(GramaticaParser.MainContext ctx){
        printTab();
        System.out.println("public static void main(String[] args) {");
        tab++;
    }

    @Override
    public void exitMain(GramaticaParser.MainContext ctx) {
        printTab();
        System.out.println("}");
        tab--;
    }


    //Sentences

    //Assign
    @Override
    public void enterSentenceAssign(GramaticaParser.SentenceAssignContext ctx){
        printTab();
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
        datatype.put("a",1);
        if(datatype.containsKey(ctx.idConIndexYAtributo().ID().getText())){
            tipo=datatype.get(ctx.idConIndexYAtributo().ID().getText());
            switch (tipo){ //1: int,2:double,  3: boolean, 4: char, 5: string,
                case 1: //Inte input
                    System.out.println("scanner.nextInt();");
                    break;
                case 2: //Real input
                    System.out.println("scanner.nextDouble();"); //completar
                    break;
                case 3: //Boolean input
                    System.out.println("scanner.nextBoolean();"); //completar
                    break;
                case 4: //Character input
                    System.out.println("scanner.nextLine().charAt(0);"); //completar
                    break;
                case 5: //String input
                    System.out.println("scanner.nextLine();");
                    break;
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
    //while statement
    @Override
    public void enterWhile(GramaticaParser.WhileContext ctx){
        printTab();
        tab++;
        System.out.print("while");
    }

    @Override
    public void exitWhile(GramaticaParser.WhileContext ctx){
        tab--;
        printTab();
        System.out.println("}");
    }

    //do while statement
    @Override
    public void enterDoWhile(GramaticaParser.DoWhileContext ctx) {
        printTab();
        tab++;
        System.out.println("do {");
    }

    @Override
    public void enterDoWhileCon(GramaticaParser.DoWhileConContext ctx) {
        printTab();
        System.out.print("} while (");
    }

    @Override
    public void exitDoWhile(GramaticaParser.DoWhileContext ctx) {
        tab--;
        System.out.println(");");
    }

    @Override
    public void enterCallFunction(GramaticaParser.CallFunctionContext ctx){
        printTab();
    }

    @Override
    public void exitCallFunction(GramaticaParser.CallFunctionContext ctx){
        tab--;
        // check if NUEVA_LINEA is not null
        if(ctx.subrutinaLlamada().NUEVA_LINEA() != null) {
            System.out.println("System.out.println();");
        } else {
            // check if there are argumentos in ctx
            if (ctx.subrutinaLlamada().argumentos() == null) {
                System.out.println("();");
            } else {
                System.out.println(";");

            }
        }
        tab++;
    }

    @Override
    public void enterArgumentos(GramaticaParser.ArgumentosContext ctx){
        System.out.print("(");
    }

    @Override
    public void exitArgumentos(GramaticaParser.ArgumentosContext ctx){
        System.out.print(")");
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
        tab--;
        printTab();
        tab++;
        System.out.print("case ");
    }
    @Override
    public void exitExpLiteral(GramaticaParser.ExpLiteralContext ctx){
        System.out.println(":");
    }
    @Override
    public void enterColonCaso(GramaticaParser.ColonCasoContext ctx){
        System.out.println(":");
    }
    @Override
    public void exitCuerpoCaso(GramaticaParser.CuerpoCasoContext ctx){
        printTab();
        System.out.println("break;");
    }
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
        System.out.print(text); //+,-,*,/,...
    }


}
