
package micompi2;

import java.io.IOException;
import java.io.RandomAccessFile;

public class Lexico {
    Sintaxis sintaxis = new Sintaxis();
    Nodo cabeza = null, p;
    int estado = 0, columna, valorMT, numRenglon = 1;
    int caracter = 0;
    String lexema = "";
    boolean errorEncontrado = false;
    
    String archivo = "C:\\Users\\Naomi\\Documents\\NetBeansProjects\\MiCompi2\\src\\micompi2\\codigo.txt";
    
    int matrizTrans [][] = {
                //L    d   +    -    *    =    .    ,    ;     :    <    >   (     )    "   Eb  tab   NL  EOL  EOF   OC   
                //0    1   2    3    4    5    6    7    8     9    10   11  12   13   14   15   16   17   18   19   20
        /*0*/{    1,   2, 103, 104, 105, 110, 112, 113, 114,   7,   6,   5,   8, 116,  11,   0,   0,   0,   0,   0, 503},
        /*1*/{    1,   1, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100, 100},
        /*2*/{  101,   2, 101, 101, 101, 101,   3, 101, 101, 101, 101, 101, 101, 101, 101, 101, 101, 101, 101, 101, 101},
        /*3*/{  500,   4, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500}, 
        /*4*/{  102,   4, 102, 102, 102, 102, 102, 102, 102, 102, 102, 102, 102, 102, 102, 102, 102, 102, 102, 102, 102},
        /*5*/{  107, 107, 107, 107, 107, 109, 107, 107, 107, 107, 107, 107, 107, 107, 107, 107, 107, 107, 107, 107, 107},
        /*6*/{  106, 106, 106, 106, 106, 108, 106, 106, 106, 106, 106, 111, 106, 106, 106, 106, 106, 106, 106, 106, 106},
        /*7*/{  119, 119, 119, 119, 119, 118, 119, 119, 119, 119, 119, 119, 119, 119, 119, 119, 119, 119, 119, 119, 119},
        /*8*/{  115, 115, 115, 115,   9, 115, 115, 115, 115, 115, 115, 115, 115, 115, 115, 115, 115, 115, 115, 115, 115},
        /*9*/{    9,   9,   9,   9,  10,   9,   9,   9,   9,   9,   9,   9,   9,   9,   9,   9,   9,   9,   9, 501,   9},  
       /*10*/{    9,   9,   9,   9,   9,   9,   9,   9,   9,   9,   9,   9,   9,   0,   9,   9,   9,   9,   9,   9,   9},
       /*11*/{   11,  11,  11,  11,  11,  11,  11,  11,  11,  11,  11,  11,  11,  11, 117,  11,  11,  11, 502, 501,  11}, 
    };
    
    String palabrasReservadas[][] = {
               //  0         1     
        /*0*/  {"begin",   "200"},
        /*1*/  {"end",     "201"},
        /*2*/  {"if",      "202"},
        /*3*/  {"then",    "203"},
        /*4*/  {"else",    "204"},
        /*5*/  {"while",   "205"},
        /*6*/  {"do",      "206"},
        /*7*/  {"read",    "207"},
        /*8*/  {"write",   "208"},
        /*9*/  {"program", "209"},
       /*10*/  {"true",    "210"},
       /*11*/  {"false",   "211"},
       /*12*/  {"and",     "212"},
       /*13*/  {"or",      "213"},
       /*14*/  {"not",     "214"},
       /*15*/  {"div",     "215"},
       /*16*/  {"integer", "216"},
       /*17*/  {"real",    "217"},
       /*18*/  {"string",  "218"},
       /*19*/  {"var",     "219"},
    };
    
    String errores[][] = {
                //  0                   1
        /*0*/ {"Se esperaba digito", "500"},
        /*1*/ {"EoF inesperado",     "501"},
        /*2*/ {"EoL inesprado",      "502"},
        /*3*/ {"Simbolo no valido",  "503"},
      
    };
    
    
    RandomAccessFile file = null;
    
    public Lexico(){
        try{
            file = new RandomAccessFile(archivo, "r");
            while (caracter != -1) {
                caracter = file.read();
                
                if (Character.isLetter(((char) caracter))) {
                    columna = 0;
                } else if (Character.isDigit(((char) caracter))) {
                    columna = 1;
                } else {
                    switch (caracter) {
                        case '+' : columna = 2;   
                            break;
                        case '-' : columna = 3;
                            break;
                        case '*' : columna = 4;
                            break;
                        case '=' : columna = 5;
                            break;
                        case '.' : columna = 6;
                            break;
                        case ',' : columna = 7;
                            break;
                        case ';' : columna = 8;
                            break;
                        case ':' : columna = 9;
                            break;
                        case '<' : columna = 10;
                            break;
                        case '>' : columna = 11;
                            break;
                        case '(' : columna = 12;
                            break;
                        case ')' : columna = 13;
                            break;
                        case '"' : columna = 14;
                            break;
                        case 32 : columna = 15;
                            break;
                        case 9 : columna = 16;
                            break;
                        case 10 : columna = 17;
                            numRenglon = numRenglon + 1;
                            break;
                        case 13 : columna = 18;
                            break;
                        case  -1 : columna = 19;
                            break;
                        default: columna = 20;
                            break;
                    }
                }
                valorMT = matrizTrans[estado][columna];
                
                if (valorMT < 100){
                    estado = valorMT;
                    
                    if(estado == 0){
                        lexema = "";
                    } else {
                        lexema = lexema + (char)caracter;
                    }
                } else if (valorMT >= 100 && valorMT < 500){
                    if (valorMT == 100){
                        validarPalabraReservada();
                        
                    }
                    if (valorMT == 100 || valorMT == 101 || valorMT == 102 ||
                            valorMT == 106 || valorMT == 107 || valorMT == 115|| valorMT >= 200){
                        file.seek(file.getFilePointer()-1);   
                    } else {
                        lexema = lexema + (char)caracter;
                    }
                    
                    insertarNodo();
                    estado = 0;
                    lexema = "";
                } else {
                    imprimirMensajeError();
                    break;
                }
            }
            if (errorEncontrado != true){
                imprimirNodos();
                sintaxis.sintaxis(cabeza, p);
                
            }
        }catch(IOException e){
            System.out.println(e.getMessage());
        }

    }
    
    private void validarPalabraReservada(){
        for (String [] palRe : palabrasReservadas){
            if (lexema.equals(palRe[0])){
                valorMT = Integer.valueOf(palRe[1]);
                
            }
        }
    }
    private void insertarNodo(){
        Nodo nodo = new Nodo(lexema, valorMT, numRenglon);
        if (cabeza == null){
            cabeza = nodo;
            p = cabeza;
        } else {
            p.sig = nodo;
            p = nodo;
        }
    }
    private void imprimirNodos(){
        p = cabeza;
        System.out.println("---------------------------------");
        System.out.println("Lexema   |   Token    |   Renglon");
        System.out.println("---------------------------------");
        while (p != null){
            
            System.out.println(p.lexema + " --- " + p.token 
                    + " --- " + p.renglon);
            
            p = p.sig;
        }
        
            
    }
    private void imprimirMensajeError(){
        if (valorMT >= 500){
            for (String[] err : errores){
                if (valorMT == Integer.valueOf(err[1])){
                    System.out.println("Error encontrado: "
                            + err[1] + " --- " +err[0]);
                    System.out.println("En linea: " + p.renglon);
                }
            }
            errorEncontrado = true;
            
        }
    }
         
}
