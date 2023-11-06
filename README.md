# Translator from LPP to Java
## Members
* Juan Camilo Zambrano Lopez
* Gabriel Alejandro Otalora Gomez
* Sergio Alejandro Sanchez Ortiz
## Description
The present project is a **translator** from LPP to **Java**, in this implementation ANTLR4 was
used to generate the lexer and parser from the grammar we designed for LPP.
## Dependencies
* Java 8
* ANTLR4
## Important notes
- **Global Variables**: The translator is capable of handling global variables from the main flow, supporting various common data types in Java. (Except register attributes)
- **Matrix (n-dimensional Arrays)**: The translator can handle matrices defined recursively, such as nested arrays, for all data types except registers.
## Example
### LPP Input
```
entero i
inicio
	escriba "Los numeros pares de 0 a 10"
	llamar nueva_linea
	para i <- 0 hasta 11 haga
	 	si i mod 2=0 entonces
	 	 escriba i
	 	 si i<>10 entonces
	 	  llamar nueva_linea
	 	 fin si
	 	fin si
	fin para
fin

```
### Java Output
```Java
import java.util.Scanner;
class Main {
		private static Scanner scanner=new Scanner(System.in);
		static int i;
		public static void main(String[] args) {
			System.out.print("Los numeros pares de 0 a 10");
			System.out.println();
			for(i=0; i <=11; i++){
				if (i%2==0){
					System.out.print(i);
					if (i!=10){
						System.out.println();
					}
				}
			}
			}
}
```
