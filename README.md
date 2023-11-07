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
## Examples
A basic LPP program and its translation to Java will be shown below, as well as some extreme cases that the translator can handle.
### Basic Example
#### LPP Input
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
#### Java Output
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
### Extreme cases

#### Case 1

The following example shows the handling of matrices and nested registers
It includes registers with matrices as attributes (which in turn can be of another type of register ...)

##### LPP Input
```
registro r5
entero num
cadena[90] texto
fin registro
registro r4
r5 registrointerno4
fin registro
registro r3
r4 registrointerno3
fin registro
registro r2
r3 registrointerno2
arreglo[4,5] de arreglo[2] de r5 arrayInterno
fin registro
registro r
r2 registrointerno
fin registro
r registroExterno
inicio

escriba "Ingrese un numero entero, es el atributo num de un registro tipo r5 dentro de un registro r4 dentro de un registro r3..."
llamar nueva_linea
lea registroExterno.registrointerno.registrointerno2.registrointerno3.registrointerno4.num
escriba "El entero ingresado fue: ",registroExterno.registrointerno.registrointerno2.registrointerno3.registrointerno4.num
llamar nueva_linea
escriba "ahora ingrese una cadena, ese será un valor del atributo texto un elemento de un arreglo de tipo r5 al interior de un registro r2 (atributo arrayInterno), r2 que es atributo a su vez de r1"
llamar nueva_linea
lea registroExterno.registrointerno.arrayInterno[2,2][0].texto
escriba "La cadena escrita fue ", registroExterno.registrointerno.arrayInterno[2,2][0].texto
fin
```

##### Java Output
```Java
import java.util.Scanner;
class Main {
		private static Scanner scanner=new Scanner(System.in);
		//Declaracion de Registros (convertidos en clases)
		public static class r5{
			int num;
			String texto;
		}
		public static class r4{
			r5 registrointerno4 = new r5();
		}
		public static class r3{
			r4 registrointerno3 = new r4();
		}
		public static class r2{
			r3 registrointerno2 = new r3();
			r5[][][] arrayinterno = new r5[4][5][2];
			public r2(){
				for(int auxArrayRegister0=0;auxArrayRegister0<4;auxArrayRegister0++){
					for(int auxArrayRegister1=0;auxArrayRegister1<5;auxArrayRegister1++){
						for(int auxArrayRegister2=0;auxArrayRegister2<2;auxArrayRegister2++){
							arrayinterno[auxArrayRegister0][auxArrayRegister1][auxArrayRegister2]=new r5();
						}
					}
				}
			}
		}
		public static class r{
			r2 registrointerno = new r2();
		}
		//Declaracion de variables globales (flujo principal)
		static r registroexterno = new r();
		//Declaracion de funciones y procedimientos (convertidos en funciones static)
		public static void main(String[] args) {
			System.out.print("Ingrese un numero entero, es el atributo num de un registro tipo r5 dentro de un registro r4 dentro de un registro r3...");
			System.out.println();
			registroexterno.registrointerno.registrointerno2.registrointerno3.registrointerno4.num=scanner.nextInt();
			scanner.nextLine();
			System.out.print("El entero ingresado fue: ");
			System.out.print(registroexterno.registrointerno.registrointerno2.registrointerno3.registrointerno4.num);
			System.out.println();
			System.out.print("ahora ingrese una cadena, ese será un valor del atributo texto un elemento de un arreglo de tipo r5 al interior de un registro r2 (atributo arrayInterno), r2 que es atributo a su vez de r1");
			System.out.println();
			registroexterno.registrointerno.arrayinterno[2][2][0].texto=scanner.nextLine();
			System.out.print("La cadena escrita fue ");
			System.out.print(registroexterno.registrointerno.arrayinterno[2][2][0].texto);
		}
}
```

#### Case 2
In the following example there are variables inside registers and functions that are called x,
showing that the translator makes a correct distinction of the variables inside registers, functions and main flow


##### LPP Input
```

registro r
cadena[3] x
fin registro
registro r2
booleano x
fin registro
//variables globales
real x
r2 instanciaRegistro2
procedimiento p
entero x
inicio
escriba "bienvenido a la funcion p, digite un entero"
llamar nueva_linea
lea x
escriba "El entero digitado fue: ",x
llamar nueva_linea
fin
inicio
llamar p
escriba "Digite un numero real"
llamar nueva_linea
lea x
escriba "El numero real digitado fue ",x

llamar nueva_linea
escriba "Ingrese un valor booleano (verdadero o falso, 1 o 0) que sera el valor del atributo x de la instancia de registro r2"
llamar nueva_linea
lea instanciaRegistro2.x
llamar nueva_linea
escriba "El valor booleano ingresado fue: ",instanciaRegistro2.x

fin
```

##### Java Output
```Java
import java.util.Scanner;
class Main {
		private static Scanner scanner=new Scanner(System.in);
		//Declaracion de Registros (convertidos en clases)
		public static class r{
			String x;
		}
		public static class r2{
			boolean x;
		}
		//Declaracion de variables globales (flujo principal)
		static double x;
		static r2 instanciaregistro2 = new r2();
		//Declaracion de funciones y procedimientos (convertidos en funciones static)
		private static void p(){
			int x;
			System.out.print("bienvenido a la funcion p, digite un entero");
			System.out.println();
			x=scanner.nextInt();
scanner.nextLine();
			System.out.print("El entero digitado fue: ");
			System.out.print(x);
			System.out.println();
		}
		public static void main(String[] args) {
			p();
			System.out.print("Digite un numero real");
			System.out.println();
			x=scanner.nextDouble();
			scanner.nextLine();
			System.out.print("El numero real digitado fue ");
			System.out.print(x);
			System.out.println();
			System.out.print("Ingrese un valor booleano (verdadero o falso, 1 o 0) que sera el valor del atributo x de la instancia de registro r2");
			System.out.println();
			instanciaregistro2.x=scanner.nextBoolean();
			scanner.nextLine();
			System.out.println();
			System.out.print("El valor booleano ingresado fue: ");
			System.out.print(instanciaregistro2.x);
		}
}
```

#### Case 3
Here we can see how the translator handles the nesting of matrices, as well as the reading of data from it

##### LPP Input
```
arreglo[4,5] de arreglo[4] de arreglo[89] de entero arr
inicio
arr[0,1][3][80]<-10
lea arr[0,1][3][81]
escriba arr[0,1][3][80]+arr[0,1][3][81]
fin
```

##### Java Output
```Java
import java.util.Scanner;
class Main {
		private static Scanner scanner=new Scanner(System.in);
		//Declaracion de Registros (convertidos en clases)
		//Declaracion de variables globales (flujo principal)
		static int[][][][] arr = new int[4][5][4][89];
		//Declaracion de funciones y procedimientos (convertidos en funciones static)
		public static void main(String[] args) {
			arr[0][1][3][80]=10;
			arr[0][1][3][81]=scanner.nextInt();
scanner.nextLine();
			System.out.print(arr[0][1][3][80]+arr[0][1][3][81]);
		}
}
```