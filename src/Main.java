import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import java.util.Scanner;

public class Main {

    private static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) throws Exception {
        try{
            // crear un analizador léxico
            GramaticaLexer lexer;
            if (args.length>0)
                lexer = new GramaticaLexer(CharStreams.fromFileName(args[0]));
            else
                lexer = new GramaticaLexer(CharStreams.fromStream(System.in));

            // Identificar al analizador léxico como fuente de tokens para el sintactico
            CommonTokenStream tokens = new CommonTokenStream(lexer);

            // Crear el analizador sintáctico que se alimenta a partir del buffer de tokens
            GramaticaParser parser = new GramaticaParser(tokens);
            ParseTree tree = parser.s(); // comienza el análisis en la regla inicial

            ParseTreeWalker walker = new ParseTreeWalker();

            walker.walk(new Traductor(), tree);

            //System.out.println(tree.toStringTree(parser)); // imprime el árbol en forma textual
        } catch (Exception e){
            System.err.println("Error (Test): " + e);
        }
    }
}