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
    //1: int,2:double,  3: boolean, 4: char, 5: string,






    final private Map<String, String> tipos = new HashMap<String, String>(){{
        put("entero","int");
        put("real","double");
        put("booleano","boolean");
        put("caracter","char");
        put("cadena","String");
    }};
    //Crear lista de palabras reservadas en Java para evitar usarlas


    @Override
    public void  enterDeclaracionArray(GramaticaParser.DeclaracionArrayContext ctx){
        printTab();
        /*String var = ctx.tipo().getText();
        if(tipos.containsKey(var)){
            var = tipos.get(var);
        }
        if(ctx.TKN_INTEGER().size()==1){
            System.out.println(var + "[] " + ctx.ID().getText() + "= new " + var + "[" +ctx.TKN_INTEGER(0) +"]"+ ";");
        }else if (ctx.TKN_INTEGER().size()==2){
            System.out.println(var + "[][] " + ctx.ID().getText() + "= new " + var + "[" +ctx.TKN_INTEGER(0) + "]"+ "[" +ctx.TKN_INTEGER(1)+ "]"+ ";");
        }else if (ctx.TKN_INTEGER().size()==3) {
            System.out.println(var + "[][][] " + ctx.ID().getText() + "= new " + var + "[" +ctx.TKN_INTEGER(0)+"]" + "[" +ctx.TKN_INTEGER(1)+ "]" + "[" + ctx.TKN_INTEGER(2) +"]"+ ";");
        }*/
        /*
        * put("entero","int");
        put("real","double");
        put("booleano","boolean");
        put("caracter","char");
        put("cadena","String");*/
        if(ctx.tipo().ID()!=null){            System.out.print(formatId(ctx.tipo().getText()));}
        else if(ctx.tipo().ENTERO()!=null){            System.out.print("int");
        }
        else if(ctx.tipo().REAL()!=null){            System.out.print("double");
        }
        else if(ctx.tipo().CARACTER()!=null){            System.out.print("char");
        }else if(ctx.tipo().CADENA()!=null){            System.out.print("String");
        }else if(ctx.tipo().BOOLEANO()!=null){            System.out.print("boolean");
        }


    }
    @Override
    public void exitDeclaracionArray(GramaticaParser.DeclaracionArrayContext ctx){

        System.out.printf(" %s = new ",formatId(ctx.ID().getText()));
        if(ctx.tipo().ID()!=null){            System.out.print(formatId(ctx.tipo().getText()));}
        else if(ctx.tipo().ENTERO()!=null){            System.out.print("int");
        }
        else if(ctx.tipo().REAL()!=null){            System.out.print("double");
        }
        else if(ctx.tipo().CARACTER()!=null){            System.out.print("char");
        }else if(ctx.tipo().CADENA()!=null){            System.out.print("String");
        }else if(ctx.tipo().BOOLEANO()!=null){            System.out.print("boolean");
        }
        for(GramaticaParser.DeclaracionArrayLoopContext loopCtx:ctx.declaracionArrayLoop()){
            for(TerminalNode numero:loopCtx.TKN_INTEGER()){ //Definicion de cuantas dimensiones tiene la matriz en la declaración (lado izquierdo
                System.out.printf("[%s]",numero.getText());
            }
        }
        System.out.println(";");
    }
    @Override
    public void enterDeclaracionArrayLoop(GramaticaParser.DeclaracionArrayLoopContext ctx){
        for(TerminalNode numero:ctx.TKN_INTEGER()){ //Definicion de cuantas dimensiones tiene la matriz en la declaración (lado izquierdo
            System.out.print("[]");
        }
    }


    @Override
    public void enterDecCommaId( GramaticaParser.DecCommaIdContext ctx) {
        int tam = ctx.ID().size();
        for(int i  = 0; i <tam ; i++){
            System.out.print(", "+formatId(ctx.ID().get(i).getText()));
        }
    }
    @Override
    public void enterDeclaracionF(GramaticaParser.DeclaracionFContext ctx) {
        printTab();
        String var = ctx.tipoRetorno().getText().toLowerCase();
        if(tipos.containsKey(var)){
            var = tipos.get(var);
        } else if (var.contains("cadena[")) {
            var = "String";
        }
        if (ctx.parametrosFP() == null){
            System.out.println("private static " + var + " " + formatId(ctx.ID().getText()) + "()" + "{");
        }else {
            System.out.print("private static " + var + " " + formatId(ctx.ID().getText()));

        }
        tab++;
    }
    @Override
    public void enterParametrosFP(GramaticaParser.ParametrosFPContext ctx) {
        System.out.print("(");
    }
    @Override
    public void exitParametrosFP(GramaticaParser.ParametrosFPContext ctx) {
        System.out.print(")");
        System.out.println("{");

    }
    @Override
    public void  enterListaParametrosFP(GramaticaParser.ListaParametrosFPContext ctx) {
        if (ctx.parametro() != null) {
            int tam = ctx.parametro().size();
            for (int i = 0; i < tam; i++) {
                if (ctx.parametro().get(i).tipo() != null){
                    String tipo = ctx.parametro().get(i).tipo().getText().toLowerCase();
                    if (tipos.containsKey(tipo)) {
                        tipo = tipos.get(tipo);
                    } else if (tipo.contains("cadena[")) {
                        tipo = "String";
                    }
                    if (i == tam - 1) {
                        System.out.print(tipo + " " + ctx.parametro().get(i).ID().getText());
                    } else {
                        System.out.print(tipo + " " + ctx.parametro().get(i).ID().getText() + ", ");
                    }
                }

            }
        }
    }


    @Override
    public void exitDeclaracionF(GramaticaParser.DeclaracionFContext ctx) {

        //System.out.println(ctx.sentencia().toString() + ";");
        tab--;
        printTab();
        System.out.println("}");
    }

    @Override
    public void enterRetorne(GramaticaParser.RetorneContext ctx){

        printTab();
        System.out.print("return " );
    }
    @Override
    public void exitRetorne(GramaticaParser.RetorneContext ctx){
        System.out.println("; " );
    }
    @Override
    public void enterDeclaracionP(GramaticaParser.DeclaracionPContext ctx) {
        printTab();
        System.out.print("private static void "+ formatId(ctx.ID().getText()) );
        tab++;
        if(ctx.parametrosFP()==null){
            System.out.println("(){");
        }
    }
    @Override
    public void exitDeclaracionP(GramaticaParser.DeclaracionPContext ctx) {
        tab--;
        printTab();
        System.out.println("}");
    }

    @Override
    public void enterDeclaracionV(GramaticaParser.DeclaracionVContext ctx) {
        if(!(ctx.parent instanceof GramaticaParser.DeclaracionVMainContext)){
            printTab();
        }

        String var = ctx.tipo().getText().toLowerCase();
        if(ctx.tipo().ID()!=null){
            System.out.printf("%s %s = new %s()",formatId(ctx.tipo().ID().getText()),formatId(ctx.ID().getText()),formatId(ctx.tipo().ID().getText()));

        }else{
            if(tipos.containsKey(var)){
                var = tipos.get(var);
            }else if (var.contains("cadena[")){
                var = "String";
            }
            System.out.print(var + " " + formatId(ctx.ID().getText()));

        }
        if(ctx.tipo().ENTERO()!=null){////1: int,2:double,  3: boolean, 4: char, 5: string,
            datatype.put(formatId(ctx.ID().getText()),1);
        }else if(ctx.tipo().REAL()!=null){
            datatype.put(formatId(ctx.ID().getText()),2);
        }else if(ctx.tipo().BOOLEANO()!=null){
            datatype.put(formatId(ctx.ID().getText()),3);
        }else if(ctx.tipo().CARACTER()!=null){
            datatype.put(formatId(ctx.ID().getText()),4);
        }else if(ctx.tipo().CADENA()!=null){
            datatype.put(formatId(ctx.ID().getText()),5);
        }


    }
    @Override
    public void exitDeclaracionV(GramaticaParser.DeclaracionVContext ctx) {
        System.out.println(";");
    }

    @Override
    public void enterS(GramaticaParser.SContext ctx) {  //Puede variar por temas de declaracin de variables, etc
        System.out.println("import java.util.Scanner;");
        System.out.println("class Main {");
        printTab();
        System.out.println("private static Scanner scanner=new Scanner(System.in);");


        //Falta scanner y demas librerias necesarias
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
    @Override
    public void enterTkn_assign(GramaticaParser.Tkn_assignContext ctx){
        System.out.print("=");
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
    public void enterEntonces(GramaticaParser.EntoncesContext ctx){
        System.out.println("{");
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
    public void enterHaga(GramaticaParser.HagaContext ctx){
        System.out.println("{");
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
        System.out.print("} while (!");
    }

    @Override
    public void exitDoWhile(GramaticaParser.DoWhileContext ctx) {
        tab--;
        System.out.println(");");
    }

    @Override
    public void enterSubrutinaLlamada(GramaticaParser.SubrutinaLlamadaContext ctx){
        printTab();
        if(ctx.ID()!=null){
            System.out.print(formatId(ctx.ID().getText()));
        }
    }
    @Override
    public void exitSubrutinaLlamada(GramaticaParser.SubrutinaLlamadaContext ctx){
        if(ctx.NUEVA_LINEA()!=null){
            System.out.print("System.out.println()");
        }else{
            if(ctx.argumentos()==null){
                System.out.print("()");
            }
        }
        System.out.println(";");
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
    //Handle Id and Index and Atributes
    @Override
    public void enterIdConIndexYAtributo(GramaticaParser.IdConIndexYAtributoContext ctx){
       System.out.print(formatId(ctx.ID().getText()));
    }

    @Override
    public void enterIndexAcceso(GramaticaParser.IndexAccesoContext ctx){
        System.out.print("[");

        if(ctx.TKN_INTEGER()!=null){
            System.out.print(ctx.TKN_INTEGER().getText());
        }
    }
    @Override
    public void enterListaIndex(GramaticaParser.ListaIndexContext ctx){
        if(ctx.TKN_INTEGER()!=null){
            System.out.printf("][%s",ctx.TKN_INTEGER().getText());
        }else{
            System.out.print("][");
        }

    }
    @Override
    public void exitIndexAcceso(GramaticaParser.IndexAccesoContext ctx){
        System.out.print("]");
    }
    @Override
    public void enterAtributo(GramaticaParser.AtributoContext ctx){
        System.out.printf(".%s",ctx.ID().getText());
    }

    //Exp Handle
    @Override
    public void enterOperadorLogico(GramaticaParser.OperadorLogicoContext ctx){
        if(ctx.OR()!=null){
            System.out.print("|");
        }else if(ctx.AND()!=null){
            System.out.print("&");
        }
    }
    @Override
    public void enterOperadorRelacional(GramaticaParser.OperadorRelacionalContext ctx){
//TKN_EQUAL|TKN_NEQ|TKN_LEQ|TKN_LESS|TKN_GEP|TKN_GREATER;
        if(ctx.TKN_NEQ()!=null){
            System.out.print("!=");
        }else if (ctx.TKN_EQUAL()!=null){
            System.out.print("=="); //Imprime tal cual es simbolo
        }else{
            System.out.print(ctx.getText());
        }
    }
    @Override
    public void enterTkn_power(GramaticaParser.Tkn_powerContext ctx){
        System.out.print(",");
    }
    @Override
    public void enterPlusMinus(GramaticaParser.PlusMinusContext ctx){ //Imprime + o -
        System.out.print(ctx.getText());
    }
    @Override
    public void enterMultiDiv(GramaticaParser.MultiDivContext ctx){ //Imprime * o /
        System.out.print(ctx.getText());
    }
    @Override
    public void enterDiv(GramaticaParser.DivContext ctx){
        System.out.print("/"); //Repensar esto
    }
    @Override
    public void enterMod(GramaticaParser.ModContext ctx){
        System.out.print("%");
    }
    @Override
    public void enterTkn_minus(GramaticaParser.Tkn_minusContext ctx){ //Imprime menos solo para expresiones negativas
        System.out.print("-");
    }
    @Override
    public void enterExpParentesis(GramaticaParser.ExpParentesisContext ctx){
        System.out.print("(");
    }
    @Override
    public void exitExpParentesis(GramaticaParser.ExpParentesisContext ctx){
        System.out.print(")");
    }

    @Override
    public void enterExpInt(GramaticaParser.ExpIntContext ctx){
        System.out.print(formatInt(ctx.TKN_INTEGER().getText()));
    }
    @Override
    public void enterExpString(GramaticaParser.ExpStringContext ctx){
        System.out.print(ctx.getText());
    }
    @Override
    public void enterExpDouble(GramaticaParser.ExpDoubleContext ctx){
        System.out.print(formatDouble(ctx.TKN_REAL().getText()));
    }
    @Override
    public void enterExpChar(GramaticaParser.ExpCharContext ctx){
        System.out.print(ctx.getText());
    }
    @Override
    public void enterVerdadero (GramaticaParser.VerdaderoContext ctx){
        System.out.print("true");
    }
    @Override
    public void enterFalso(GramaticaParser.FalsoContext ctx){
        System.out.print("false");
    }
    @Override
    public void enterId(GramaticaParser.IdContext ctx){
        System.out.print(formatId(ctx.ID().getText()));
    }
    @Override
    public void enterTkn_comma(GramaticaParser.Tkn_commaContext ctx){
        System.out.print(",");
    }

    @Override
    public void enterExpMultiDiv(GramaticaParser.ExpMultiDivContext ctx){
        if(ctx.expMultiDivAux()!=null && ctx.expMultiDivAux().div()!=null){ //Tiene una division entera
                System.out.print("(");
        }
    }

    @Override
    public void exitExpMultiDiv(GramaticaParser.ExpMultiDivContext ctx){
        if(ctx.expMultiDivAux()!=null && ctx.expMultiDivAux().div()!=null){ //Tiene una division entera
            System.out.print(")");
        }
    }
    @Override
    public void enterExpPotencia(GramaticaParser.ExpPotenciaContext ctx){
        if(ctx.expPotenciaAux()!=null){
            System.out.print("Math.pow(");
        }

    }
    @Override
    public void exitExpPotencia(GramaticaParser.ExpPotenciaContext ctx){
        if(ctx.expPotenciaAux()!=null){
            System.out.print(")");
        }
    }
    @Override
    public void enterDeclaracionR(GramaticaParser.DeclaracionRContext ctx){
        printTab();
        System.out.printf("public static class %s{\n",formatId(ctx.ID().getText()));
        tab++;
    }
    @Override
    public void exitDeclaracionR(GramaticaParser.DeclaracionRContext ctx){
        tab--;
        printTab();
        System.out.println("}");
    }
    @Override
    public void enterDeclaracionVMain(GramaticaParser.DeclaracionVMainContext ctx){
        printTab();
        System.out.print("static ");
    }
    public int formatInt(String number){
        return Integer.parseInt(number);
    }
    public Double formatDouble(String number){
        return Double.parseDouble(number);
    }
    public String formatId(String id){
        if(!id.isEmpty() && id.charAt(0)=='_'){
            return "a"+id.toLowerCase(); //Evita variables llamadas __... agragando un caracter valido para el nombre
        }
        return id.toLowerCase(); //handle non handle sensitive
    }



}
