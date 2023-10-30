import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class Traductor extends GramaticaBaseListener {
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
        put(GramaticaLexer.CASO,"switch"); //Pendiente de complementar
        put(GramaticaLexer.MIENTRAS,"while");
        put(GramaticaLexer.HAGA,""); //Pendiente de complementar
        put(GramaticaLexer.REPITA,""); //completar
        put(GramaticaLexer.HASTA,"");
        put(GramaticaLexer.PARA,"for"); //completar
        put(GramaticaLexer.PROCEDIMIENTO,"");
        put(GramaticaLexer.VAR,""); //Completar
        put(GramaticaLexer.FUNCION,"");
        put(GramaticaLexer.RETORNE,"return ");
        put(GramaticaLexer.ARREGLO,""); //Completar
        put(GramaticaLexer.DE,"");
        put(GramaticaLexer.LLAMAR,"");
        put(GramaticaLexer.TKN_ASSIGN,"= ");
        put(GramaticaLexer.TKN_PERIOD,".");
        put(GramaticaLexer.TKN_COMMA,""); //Sujeto a modificaciones
        put(GramaticaLexer.TKN_OPENING_BRA,""); //Por cuestiones de matrices
        put(GramaticaLexer.TKN_CLOSING_BRA,"");
        put(GramaticaLexer.TKN_OPENING_PAR,"");
        put(GramaticaLexer.TKN_CLOSING_PAR,"");
        put(GramaticaLexer.TKN_NEQ,"!=");
        put(GramaticaLexer.TKN_EQUAL,"==");

    }};
    //Crear lista de palabras reservadas en Java para evitar usarlas
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
    //Write
    @Override
    public void enterSentenceWrite(GramaticaParser.SentenceWriteContext ctx){

    }
    @Override
    public void enterExpEscriba(GramaticaParser.ExpEscribaContext ctx){
        System.out.print("System.out.print(");
    }
    public void exitExpEscriba(GramaticaParser.ExpEscribaContext ctx){
        System.out.println(");");
    }
    //Read
    //Es necesario en el flujo inicial definir el scanner para evitar conflictos
    @Override
    public void enterSentenceRead(GramaticaParser.SentenceReadContext ctx){

    }
    @Override
    public void exitIdLectura(GramaticaParser.IdLecturaContext ctx){
        boolean exist;
        int tipo=5;
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
        }
    }

    //if statement
    @Override
    public void enterConditional(GramaticaParser.ConditionalContext ctx){
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
        System.out.println("}");
    }
    @Override
    public void enterSino(GramaticaParser.SinoContext ctx){
        System.out.println("}else{");
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
