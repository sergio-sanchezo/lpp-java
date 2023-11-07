import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.*;

public class Traductor extends GramaticaBaseListener {
    public  class DatatypeContext{
        DatatypeContext parent;

        public DatatypeContext(){

        }
    }
    private void printTab(){
        for(int i=0;i<tab;i++){
            System.out.print("\t");
        }
    }
    private String idFor=""; //Usado para almacenar el valor del id del for
    private int tab=2;
    private Map<String,String> datatype=new HashMap<String,String>();
    static String currentFP="";
    static String currentR="";
    static private Map<String,String> temporalDataType=new HashMap<String,String>(); //Uso dentro de funciones
    //1: int,2:double,  3: boolean, 4: char, 5: string,
    static private Map<String,Map<String,String>> datatypeRegister=new HashMap<String,Map<String,String>>();
    static private Queue<GramaticaParser.DeclaracionArrayContext> initArrayRegQueueTemporal =new LinkedList<GramaticaParser.DeclaracionArrayContext>();
    static private Queue<GramaticaParser.DeclaracionArrayContext> initArrayRegQueue =new LinkedList<GramaticaParser.DeclaracionArrayContext>();




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
        if(!(ctx.parent instanceof GramaticaParser.DeclaracionVMainContext)){
            printTab();
        }
        Map<String,String> currentDataType;
        if(ctx.parent instanceof GramaticaParser.AtributosContext){ //Declarada al interior de un registro
            currentDataType=datatypeRegister.get(currentR);
        }else{
            if(ctx.parent instanceof GramaticaParser.DeclaracionVMainContext){
                currentDataType=datatype;
            }else{ //Is instance of DeclaracionesV_FPContext
                currentDataType=temporalDataType;
            }

        }
        if(ctx.tipo().ID()!=null){
            currentDataType.put(formatId(ctx.ID().getText()),formatId(ctx.tipo().getText()));
            System.out.print(formatId(ctx.tipo().getText()));}
        else if(ctx.tipo().ENTERO()!=null){////1: int,2:double,  3: boolean, 4: char, 5: string,
            System.out.print("int");
            currentDataType.put(formatId(ctx.ID().getText()),"int");
        }
        else if(ctx.tipo().REAL()!=null){
            currentDataType.put(formatId(ctx.ID().getText()),"double");
            System.out.print("double");
        }
        else if(ctx.tipo().CARACTER()!=null){
            currentDataType.put(formatId(ctx.ID().getText()),"char");
            System.out.print("char");
        }else if(ctx.tipo().CADENA()!=null){
            currentDataType.put(formatId(ctx.ID().getText()),"String");
            System.out.print("String");
        }else if(ctx.tipo().BOOLEANO()!=null){
            currentDataType.put(formatId(ctx.ID().getText()),"boolean");
            System.out.print("boolean");
        }


    }
    @Override
    public void exitDeclaracionArray(GramaticaParser.DeclaracionArrayContext ctx){

        System.out.printf(" %s = new ",formatId(ctx.ID().getText()));
        if(ctx.tipo().ID()!=null){
            System.out.print(formatId(ctx.tipo().getText()));
        }
        else if(ctx.tipo().ENTERO()!=null){            System.out.print("int");
        }
        else if(ctx.tipo().REAL()!=null){            System.out.print("double");
        }
        else if(ctx.tipo().CARACTER()!=null){            System.out.print("char");
        }else if(ctx.tipo().CADENA()!=null){            System.out.print("String");
        }else if(ctx.tipo().BOOLEANO()!=null){            System.out.print("boolean");
        }
        for(GramaticaParser.DeclaracionArrayLoopContext loopCtx:ctx.declaracionArrayLoop()){
            for(TerminalNode numero:loopCtx.TKN_INTEGER()){ //Definicion de cuantas dimensiones tiene la matriz en la declaracion (lado izquierdo
                System.out.printf("[%s]",numero.getText());
            }
        }
        System.out.println(";");

        //En caso de ser un arreglo de registros (array de clases), se deben inicializar:
        if(ctx.tipo().ID()!=null){ //Si es un arreglo de una clase (registro
            if(ctx.parent instanceof GramaticaParser.DeclaracionVMainContext){
                initArrayRegQueue.add(ctx);
            }else{
                initArrayRegQueueTemporal.add(ctx); //Registros o funciones
            }

        }

    }
    public void initRegArray(GramaticaParser.DeclaracionArrayContext ctx){ //Inicializar arreglos de registros dentro del flujo principal

        int i=0;
        for(GramaticaParser.DeclaracionArrayLoopContext loopCtx:ctx.declaracionArrayLoop()){
            for(TerminalNode numero:loopCtx.TKN_INTEGER()){ //Definicion de cuantas dimensiones tiene la matriz en la declaracion (lado izquierdo
                printTab();
                System.out.printf("for(int auxArrayRegister%d=0;auxArrayRegister%d<%s;auxArrayRegister%d++){\n",i,i,numero.getText(),i);
                tab++;
                i++;
            }
        }
        printTab();
        System.out.printf("%s",formatId(ctx.ID().getText()));
        for(int j=0;j<i;j++){
            System.out.printf("[auxArrayRegister%d]",j);
        }

        System.out.printf("=new %s();\n",formatId(ctx.tipo().ID().getText()));
        while(i>0){
            tab--;
            printTab();
            System.out.print("}\n");
            i--;
        }
    }


    @Override
    public void enterDeclaracionArrayLoop(GramaticaParser.DeclaracionArrayLoopContext ctx){
        for(TerminalNode numero:ctx.TKN_INTEGER()){ //Definicion de cuantas dimensiones tiene la matriz en la declaracion (lado izquierdo
            System.out.print("[]");
        }
    }



    @Override
    public void enterDeclaracionesV(GramaticaParser.DeclaracionesVContext ctx){
        printTab();
        System.out.println("//Declaracion de variables globales (flujo principal)");
    }
    @Override
    public void enterDeclaracionesR(GramaticaParser.DeclaracionesRContext ctx){
        printTab();
        System.out.println("//Declaracion de Registros (convertidos en clases)");
    }
    @Override
    public void enterDeclaracionesFP(GramaticaParser.DeclaracionesFPContext ctx){
        printTab();
        System.out.println("//Declaracion de funciones y procedimientos (convertidos en funciones static)");
    }
    @Override
    public void enterDeclaracionF(GramaticaParser.DeclaracionFContext ctx) {
        currentFP=formatId(ctx.ID().getText());
        temporalDataType=new HashMap<String,String>();
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
                        System.out.print(tipo + " " + formatId(ctx.parametro().get(i).ID().getText()));
                    } else {
                        System.out.print(tipo + " " + formatId(ctx.parametro().get(i).ID().getText()) + ", ");
                    }
                }

            }
        }
    }
    @Override
    public void exitDeclaracionesV_FP(GramaticaParser.DeclaracionesV_FPContext ctx){
        if(!initArrayRegQueueTemporal.isEmpty()){ //Se declararon arreglos de registros (clases al interior del registro
            printTab();
            System.out.println("//Inicializacion de elementos en matriz o arreglo de registros (al interior de la funcion)");
            while(!initArrayRegQueueTemporal.isEmpty()){
                initRegArray(initArrayRegQueueTemporal.poll());
            }
        }
    }

    @Override
    public void exitDeclaracionF(GramaticaParser.DeclaracionFContext ctx) {

        //System.out.println(ctx.sentencia().toString() + ";");
        currentFP="";

        tab--;
        printTab();
        System.out.println("}");
    }

    @Override
    public void enterRetorne(GramaticaParser.RetorneContext ctx){
        if(!initArrayRegQueueTemporal.isEmpty()){ //Se declararon arreglos de registros (clases al interior del registro

            while(!initArrayRegQueueTemporal.isEmpty()){
                initRegArray(initArrayRegQueueTemporal.poll());
            }
        }
        printTab();
        System.out.print("return " );
    }
    @Override
    public void exitRetorne(GramaticaParser.RetorneContext ctx){
        System.out.println("; " );
    }
    @Override
    public void enterDeclaracionP(GramaticaParser.DeclaracionPContext ctx) {
        currentFP=formatId(ctx.ID().getText());
        temporalDataType=new HashMap<String,String>();
        printTab();
        System.out.print("private static void "+ formatId(ctx.ID().getText()) );
        tab++;
        if(ctx.parametrosFP()==null){
            System.out.println("(){");
        }
    }
    @Override
    public void exitDeclaracionP(GramaticaParser.DeclaracionPContext ctx) {
        currentFP="";
        tab--;
        printTab();
        System.out.println("}");
    }

    @Override
    public void enterDeclaracionV(GramaticaParser.DeclaracionVContext ctx) {
        if(!(ctx.parent instanceof GramaticaParser.DeclaracionVMainContext)){
            printTab();
        }
        Map<String,String>  currentDataType;

        if(ctx.parent instanceof GramaticaParser.AtributosContext){ //Declarada al interior de un registro
            currentDataType=datatypeRegister.get(currentR);
        }else{
            if(ctx.parent instanceof GramaticaParser.DeclaracionVMainContext){
                currentDataType=datatype;
            }else{ //Is instance of DeclaracionesV_FPContext
                currentDataType=temporalDataType;
            }
        }
        String var = ctx.tipo().getText().toLowerCase();

        if(ctx.tipo().ID()!=null){
            System.out.printf("%s %s = new %s();\n",formatId(ctx.tipo().ID().getText()),formatId(ctx.ID(0).getText()),formatId(ctx.tipo().ID().getText()));
            currentDataType.put(formatId(ctx.ID(0).getText()),formatId(ctx.tipo().ID().getText())); //Agrega a la declaracion de tipo distinto (nueva clase)
            for(int idxID=1;idxID<ctx.ID().size();idxID++){
                printTab();
                if(ctx.parent instanceof GramaticaParser.DeclaracionVMainContext){
                    System.out.print("static ");
                }
                System.out.printf("%s %s = new %s();\n",formatId(ctx.tipo().ID().getText()),formatId(ctx.ID(idxID).getText()),formatId(ctx.tipo().ID().getText()));
                currentDataType.put(formatId(ctx.ID(idxID).getText()),formatId(ctx.tipo().ID().getText())); //Agrega a la declaracion de tipo distinto (nueva clase)
            }
            for(int idxID=0;idxID<ctx.ID().size();idxID++){

            }

        }else{
            if (tipos.containsKey(var)) {
                var = tipos.get(var);
            } else if (var.contains("cadena[")) {
                var = "String";
            }
            System.out.printf("%s ",var);
            for(int idxID=0;idxID<ctx.ID().size();idxID++) {

                if(idxID>0){
                    System.out.print(",");
                }
                System.out.printf("%s ",formatId(ctx.ID(idxID).getText()));


                String idFormateado=formatId(ctx.ID(idxID).getText());
                if(ctx.tipo().ENTERO()!=null){//1: int,2:double,  3: boolean, 4: char, 5: string,
                    currentDataType.put(idFormateado,"int");
                }else if(ctx.tipo().REAL()!=null){
                    currentDataType.put(idFormateado,"double");
                }else if(ctx.tipo().BOOLEANO()!=null){
                    currentDataType.put(idFormateado,"boolean");
                }else if(ctx.tipo().CARACTER()!=null){
                    currentDataType.put(idFormateado,"char");
                }else if(ctx.tipo().CADENA()!=null){
                    currentDataType.put(idFormateado,"String");
                }
            }
            System.out.println(";");
        }











    }
    @Override
    public void exitDeclaracionV(GramaticaParser.DeclaracionVContext ctx) {

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

        if(!initArrayRegQueue.isEmpty()){ //Se declararon arreglos de registros (clases al interior del registro
            printTab();
            System.out.println("//Inicializacion de elementos en matriz o arreglo de registros (al interior de la funcion)");
            while(!initArrayRegQueue.isEmpty()){
                initRegArray(initArrayRegQueue.poll());
            }
        }

    }

    @Override
    public void exitMain(GramaticaParser.MainContext ctx) {
        tab--;
        printTab();
        System.out.println("}");


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
        String tipo;
        String id="";
        System.out.print("=");
        id=formatId(ctx.idConIndexYAtributo().ID().getText());
        Map<String,String>  currentDataType;
        if(currentFP.equals("")){

            currentDataType=datatype;
        }else{
            currentDataType=temporalDataType;
        }
        if(currentDataType.containsKey(id)){
            tipo=currentDataType.get(id);
            switch (tipo){ //1: int,2:double,  3: boolean, 4: char, 5: string,
                case "int": //Inte input
                    System.out.println("scanner.nextInt();");
                    printTab();
                    System.out.println("scanner.nextLine();");
                    break;
                case "double": //Real input
                    System.out.println("scanner.nextDouble();");
                    printTab();
                    System.out.println("scanner.nextLine();"); //Limpiar Scanner
                    break;
                case "boolean": //Boolean input
                    System.out.println("scanner.nextBoolean();");
                    printTab();
                    System.out.println("scanner.nextLine();"); //Limpiar Scanner
                    break;
                case "char": //Character input
                    System.out.println("scanner.nextLine().charAt(0);");
                    break;
                case "String": //String input
                    System.out.println("scanner.nextLine();");
                    break;
                default:
                    buscarDataType(tipo,ctx.idConIndexYAtributo().indexYAtributo().atributo(0)); //Se le pasa el atributo

            }
        }
    }

    private void buscarDataType(String id, GramaticaParser.AtributoContext atributoContext) {

        Map<String,String> datatypeContext= datatypeRegister.get(id); //Aqui hay que buscar
        String tipo;
        String atributeId=formatId(atributoContext.ID().getText());
        if(datatypeContext.containsKey(atributeId)){
            tipo=datatypeContext.get(atributeId);
            switch (tipo){ //1: int,2:double,  3: boolean, 4: char, 5: string,
                case "int": //Inte input
                    System.out.println("scanner.nextInt();");
                    printTab();
                    System.out.println("scanner.nextLine();"); //Limpiar Scanner
                    break;
                case "double": //Real input

                    System.out.println("scanner.nextDouble();");
                    printTab();
                    System.out.println("scanner.nextLine();"); //Limpiar Scanner
                    break;
                case "boolean": //Boolean input
                    System.out.println("scanner.nextBoolean();");
                    printTab();
                    System.out.println("scanner.nextLine();"); //Limpiar Scanner
                    break;
                case "char": //Character input
                    System.out.println("scanner.nextLine().charAt(0);");
                    break;
                case "String": //String input
                    System.out.println("scanner.nextLine();");
                    break;
                default:
                    buscarDataType(tipo,atributoContext.indexYAtributo().atributo(0));

            }
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
        if(ctx.TKN_INTEGER()!=null){
            System.out.print(formatInt(ctx.TKN_INTEGER().getText()));
        }else if(ctx.TKN_REAL()!=null){
            System.out.print(formatDouble(ctx.TKN_REAL().getText()));
        }else{
            System.out.print(ctx.getText());
        }
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
            System.out.print(formatInt(ctx.TKN_INTEGER().getText()));
        }
    }
    @Override
    public void enterListaIndex(GramaticaParser.ListaIndexContext ctx){
        if(ctx.TKN_INTEGER()!=null){
            System.out.printf("][%s",formatInt(ctx.TKN_INTEGER().getText()));
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
        System.out.printf(".%s",formatId(ctx.ID().getText()));
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
        currentR=formatId(ctx.ID().getText());
        if(!datatypeRegister.containsKey(currentR)){
            datatypeRegister.put(currentR,new HashMap<String,String>());
        }
        tab++;
    }
    @Override
    public void exitDeclaracionR(GramaticaParser.DeclaracionRContext ctx){
        if(!initArrayRegQueueTemporal.isEmpty()){ //Se declararon arreglos de registros (clases al interior del registro
            printTab();
            System.out.printf("public %s(){\n",formatId(ctx.ID().getText()));
            tab++;
            while(!initArrayRegQueueTemporal.isEmpty()){
                initRegArray(initArrayRegQueueTemporal.poll());
            }
            tab--;
            printTab();
            System.out.println("}");
        }
        tab--;
        printTab();
        currentR="";
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
