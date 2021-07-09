
package micompi2;

public class Nodo {
    String lexema;
    int token;
    int renglon;
    Nodo sig = null;
    
   
    
    
    Nodo(String lexema, int token, int renglon){
        this.lexema = lexema;
        this.token = token;
        this.renglon = renglon;
    }
}
