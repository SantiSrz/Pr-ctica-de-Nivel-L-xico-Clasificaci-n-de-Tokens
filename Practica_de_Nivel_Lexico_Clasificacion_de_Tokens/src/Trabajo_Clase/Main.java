package Trabajo_Clase;

import java.util.Scanner;

import Trabajo_Clase.MiniLexer.TipoToken;
import Trabajo_Clase.MiniLexer.Token;

public class Main {
	
    public static void main(String[] args) {
    	
    	Scanner scanner = new Scanner(System.in);
    	String ejemplo;
    	System.out.println("Inserta tu codigo: ");
        ejemplo = scanner.nextLine();
        scanner.close();
        
        String[] separacion = ejemplo.split(" ");
        Token[] tokens = new Token[separacion.length];
        int index = 0;

        for (String lexema : separacion) {

            lexema = lexema.trim();
            if (lexema.isEmpty()) {
            	continue;
            }

            TipoToken tipo = MiniLexer.clasificarToken(lexema);
            Token t = new Token(tipo, lexema);

            tokens[index] = t;
            index++;

            System.out.println(t);
        }

    } 
}
