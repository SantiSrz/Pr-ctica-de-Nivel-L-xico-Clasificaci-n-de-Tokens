package Trabajo_Clase;

import Trabajo_Clase.MiniLexer.TipoToken;
import Trabajo_Clase.MiniLexer.Token;

public class MiniParser {
	
	String[] nombres;
	String[] tipos;
	boolean[] inicializada;
	int contador;
	Token[] tokens;
	int actual;
	
	// Constructor que inicializa el parser con los tokens
	public MiniParser(Token[] tokens) {
		
		this.tokens = tokens;
		this.actual = 0;
		this.nombres = new String[10];
		this.tipos = new String[10];
		this.inicializada = new boolean[10];
		this.contador = 0;
		
	}
	
	// Verifica que el token actual sea del tipo esperado y avanza
	public void match(TipoToken tipoEsperado) {
		if (this.actual < this.tokens.length) {
		Token t = this.tokens[this.actual];
		if (t.tipo == tipoEsperado) {
		this.actual++;
		}else {
		System.out.println("Error");
		System.exit(1);}
		}
	}
	
	// Procesa una lista de sentencias hasta el final
	public void parseStmtList() {
		while (this.actual < this.tokens.length) {
			parseStmt();
		}
	}
	
	// Procesa una sentencia individual (declaracion, asignacion o print)
	public void parseStmt() {
		Token n = this.tokens[this.actual];
		if (n.lexema.equals("int")) {
			// Procesa declaracion: int nombreVariable ;
			match(TipoToken.PALABRA_CLAVE);
			String nombreVariable = this.tokens[this.actual].lexema;
			match(TipoToken.IDENTIFICADOR);
			registrarDeclaracion(nombreVariable, "int");
			match(TipoToken.DELIMITADOR);
		}else if (n.lexema.equals("print")) {
			// Procesa impresion: print ( expresion ) ;
			match(TipoToken.PALABRA_CLAVE);
			match(TipoToken.DELIMITADOR);
			parseExpr();
			match(TipoToken.DELIMITADOR);
			match(TipoToken.DELIMITADOR);
		}else if (n.tipo == TipoToken.IDENTIFICADOR){
			// Procesa asignacion: variable = expresion ;
			String nombreVariable = n.lexema;
			int indice = buscarVariable(nombreVariable);
			if (indice == -1) {
	            System.out.println("Error, la variable " + nombreVariable + " no ha sido declarada");
	            System.exit(1);
	        } else {
	            this.inicializada[indice] = true;
	        }
			match(TipoToken.IDENTIFICADOR);
			match(TipoToken.OPERADOR);
			parseExpr();
			match(TipoToken.DELIMITADOR);
		}else {
			System.out.println("Se esperaba un " + "PRINT" + " de tipo " + TipoToken.PALABRA_CLAVE + " o un " + TipoToken.IDENTIFICADOR + ", no un " + n);
			System.exit(1);
		}
	}
	
	// Procesa expresiones con suma y resta (precedencia baja)
	public void parseExpr() {
		parseTerm();
		if (this.actual >= this.tokens.length) {
			return;
		}
		while (this.tokens[this.actual].lexema.equals("+") || this.tokens[this.actual].lexema.equals("-")) {
				match(TipoToken.OPERADOR);
				parseTerm();
		}
		if (this.actual >= this.tokens.length) {
			return;
		}
	}
	
	// Procesa terminos con multiplicacion y division (precedencia alta)
	public void parseTerm() {
		parseFactor();
		while (this.actual < this.tokens.length && this.tokens[this.actual].lexema.equals("*") || this.tokens[this.actual].lexema.equals("/")) {
			match(TipoToken.OPERADOR);
			parseFactor();
		}
	}
	
	// Procesa factores: numeros, variables o expresiones entre parentesis
	public void parseFactor() {
		if (this.actual >= this.tokens.length) {
	        System.out.println("Error, se esperaba un " + TipoToken.IDENTIFICADOR + " un " + TipoToken.LITERAL_NUMERICO + " o un '(' ");
	        System.exit(1);
	    }
		Token g = this.tokens[this.actual];
		if (g.tipo == TipoToken.IDENTIFICADOR) {
			// Procesa una variable
			String nombreVar = g.lexema;
			int indice = buscarVariable(nombreVar);
			if (indice == -1) {
	            System.out.println("Error Semántico: La variable '" + nombreVar + "' no ha sido declarada.");
	            System.exit(1);
	        }
			if (this.inicializada[indice] == false) {
	            System.out.println("Error Semántico: La variable '" + nombreVar + "' ha sido declarada pero NO inicializada. No se puede usar aún.");
	            System.exit(1);
	        }
			match(TipoToken.IDENTIFICADOR);
		}else if (g.tipo == TipoToken.LITERAL_NUMERICO) {
			// Procesa un numero
			match(TipoToken.LITERAL_NUMERICO);
		}else if(this.tokens[this.actual].lexema.equals("(")){
			// Procesa una expresion entre parentesis
			match(TipoToken.DELIMITADOR);
			parseExpr();
			match(TipoToken.DELIMITADOR);
		}else {
			System.out.println("Error, se esperaba un " + TipoToken.IDENTIFICADOR + " un " + TipoToken.LITERAL_NUMERICO + " o un '(' ");
	        System.exit(1);
		}
	}
	
	// Guarda la informacion de una variable declarada
	public void registrarDeclaracion(String nombre, String tipo) {
		this.nombres[this.contador] = nombre;
	    this.tipos[this.contador] = tipo;
	    this.inicializada[this.contador] = false;
	    this.contador++;
	}
	
	// Busca una variable por nombre y devuelve su indice o -1 si no existe
	public int buscarVariable(String nombre) {
		for(int i = 0; i < this.contador; i++) {
			if (this.nombres[i].equals(nombre)) {
				return i;
			}
		}
		return -1;
	}
}
