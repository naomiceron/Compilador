
package micompi2;

public class MiCompi2 {
    //boolean errorSintaxis = false;
    //Nodo p;
    public static void main(String[] args) {
        Lexico lexico = new Lexico();
        Sintaxis sintaxis = new Sintaxis();
        if(lexico.errorEncontrado!=true){
            System.out.println("Analisis léxico terminado con EXITO");
           if (sintaxis.errorSintaxis !=true){
                System.out.println("Analisis Sintactico terminado");
            }
        } 
    }
    
}
