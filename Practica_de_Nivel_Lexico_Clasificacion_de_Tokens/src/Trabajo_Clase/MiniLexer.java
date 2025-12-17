package Trabajo_Clase;

public class MiniLexer {

    public enum TipoToken {
        PALABRA_CLAVE,
        IDENTIFICADOR,
        LITERAL_NUMERICO,
        OPERADOR,
        DELIMITADOR
    }

    public static class Token {
        TipoToken tipo;
        String lexema;

        public Token(TipoToken tipo, String lexema) {
            this.tipo = tipo;
            this.lexema = lexema;
        }

        public String toString() {
            return "Token: '" + lexema +"' -> de tipo: " + tipo;
        }
    }

    public static void main(String[] args) {

        String ejemplo = "int valor = 3 ; if ( valor == 5 ) valor = 0 ;";
        String[] separacion = ejemplo.split(" ");

        Token[] tokens = new Token[separacion.length];

        int index = 0;

        for (String lexema : separacion) {

            lexema = lexema.trim();
            if (lexema.isEmpty()) {
            	continue;
            }

            TipoToken tipo = clasificarToken(lexema);
            Token t = new Token(tipo, lexema);

            tokens[index] = t;
            index++;

            System.out.println(t);
        }

    }

    public static TipoToken clasificarToken(String lexema) {

        if (lexema.equals("int") || lexema.equals("if") || lexema.equals("for") || lexema.equals("var") || lexema.equals("String") || lexema.equals("double")) {
            return TipoToken.PALABRA_CLAVE;
        }

        if (lexema.equals("=") || lexema.equals("==") || lexema.equals("+") || lexema.equals("-") || lexema.equals("*") || lexema.equals("/") || lexema.equals("%")) {
            return TipoToken.OPERADOR;
        }

        if (lexema.equals(";") || lexema.equals("(") || lexema.equals(")") || lexema.equals(",")) {
            return TipoToken.DELIMITADOR;
        }

        if (lexema.matches("[0-9]+")) {
            return TipoToken.LITERAL_NUMERICO;
        }

        return TipoToken.IDENTIFICADOR;
    }
}


