package micompi2;

import java.util.ArrayList;

public class Sintaxis {
    
    boolean errorSintaxis = false ;
    
    int  error=0;
    ClaseVariable cabezav;
    String tipo;
    String nombrePrograma;
    ArrayList<String> id= new ArrayList<>();
    String errores[][] = {
                //  0                      1
        /*0*/ {"Se esperaba program",    "400"},
        /*1*/ {"Se esperaba id",         "401"},
        /*2*/ {"Se esperaba ;",          "402"},
        /*3*/ {"Se esperaba .",          "403"},
        /*4*/ {"Se esperaba un type",    "404"},
        /*5*/ {"Se esperaba var",        "405"},
        /*6*/ {"Se esperaba begin",      "406"},
        /*7*/ {"Se esperaba end",        "407"},
        /*8*/ {"Se esperaba (",          "408"},
        /*9*/ {"Se esperaba )",          "409"},
       /*10*/ {"Se esperaba :=",         "410"},
       /*11*/ {"Se esperaba :",          "411"},
       /*12*/ {"Se esperaba constante",  "412"},
       /*13*/ {"Se esperaba then",       "413"},
       /*14*/ {"Se esperaba do",         "414"},
       /*15*/ {"Variable ya declarada",  "501"},
       /*16*/ {"Variable no puede tener el nombre del programa.", "502"},
       /*17*/ {"Variable no declarada.","503"},
    };
    
     private void imprimirMensajeError(int error, int renglon ){
        for (String[] err : errores){
            if (error == Integer.valueOf(err[1])){
                System.out.println("Error encontrado: "+ err[1] + " --- " +err[0]);
                System.out.println("En renglon: "+ renglon); 
            } 
        }
        errorSintaxis = true; 
            if (errorSintaxis = true){
                finPrograma();
            }
    }
     
    private void finPrograma(){
        System.exit(error);
    }
    
    public Nodo sintaxis(Nodo cabeza, Nodo p){
        p = cabeza;
        while (p != null){ 
            if (p.token == 209){ //program
                p=p.sig;
                if (p.token == 100){ //id
                    nombrePrograma = p.lexema;
                    p=p.sig;
                    if (p.token == 114){ //;
                       p= p.sig;
                       if (p==null){
                           break;
                       }
                       p= block(p);
                       if (p.token == 112){ //.
                           return p;
                       }else{
                           imprimirMensajeError(403, p.renglon);
                       }
                    }else{
                        imprimirMensajeError(402, p.renglon);
                    }
                }else{
                    imprimirMensajeError(401, p.renglon);
                }
            }else{
                imprimirMensajeError(400, p.renglon);
            }
            break;
        }
        return p;
    }  
       

    private Nodo block(Nodo p) {
        p= variable_declaration_part(p);
        p=compound_statement(p);
        return p;
    }
    
    private Nodo variable_declaration_part(Nodo p){
        if (p.token == 219){
            p = p.sig;
            p= variable_declaration(p);
        } else if (p.token == 100){
            imprimirMensajeError(405, p.renglon);
        }
        while (p.token != 200){
            p= variable_declaration(p);
        }
        if(p.token != 200){
                    imprimirMensajeError(406, p.renglon);
        }
        return p;
    }
    
    private Nodo variable_declaration(Nodo p) {
        ArrayList<String> id= new ArrayList<>();
        
        ClaseVariable variable= cabezav;
        
        if (p.token == 100){ //id
            id.add(p.lexema);
            p=p.sig;
            while (p.token == 113) { //,
                p=p.sig;
                if(p.token == 100){
                    id.add(p.lexema);
                    p=p.sig;
                }else{
                    imprimirMensajeError(401, p.renglon);
                    return p;
                }
            }
            if (p.token == 119){ //:
                p=p.sig;
                p= tipo(p);
                for (String identif :id){ //error 
                    boolean existev = false;
                    variable = cabezav;
                    while (variable != null) {
                        if (identif.equals(variable.Lexema)){
                            imprimirMensajeError(501, p.renglon);
                            existev = true;
                            break;
                        } if(identif.equals(nombrePrograma)){
                           imprimirMensajeError(502, p.renglon);
                            existev = true;
                            break;
                        }
                        if (variable.sig != null){
                            variable = variable.sig;
                        } else {
                            break;
                        }
                    } 
                    
                    if(existev!=true){
                        ClaseVariable nueva = new ClaseVariable(identif, tipo);
                        if(cabezav == null){
                            cabezav = nueva;
                            variable = cabezav;
                        } else {
                            if(variable!=null){
                                variable.sig = nueva;
                                variable = nueva;
                            }else{
                                break;
                            }
                        }
                        System.out.println("VARIABLE--> "+variable.Lexema+" --> de tipo: "+variable.tipo);
                    
                    }
                    
                }
                
            }  else{
                imprimirMensajeError(411, p.renglon);
                return p;
            }
        }else{
            imprimirMensajeError(401, p.renglon);
            p=p.sig;
            return p;
        } 
           return p;
    }

    private Nodo tipo(Nodo p) {
        if (p.token == 216 || p.token == 217 || p.token ==218){ //Integer | Real | String
            tipo = p.lexema;
            p=p.sig;
            if (p.token == 114){ // ;
                p=p.sig;
                return p;
            }else{
                imprimirMensajeError(402, p.renglon);
            }
        }
        else{
           imprimirMensajeError(404, p.renglon);
        }
        return p;
    }
    public Nodo verificarExiste(Nodo p){
       ClaseVariable variable = cabezav;
       boolean existe = false;
       while(variable != null){
           
           if (variable.Lexema.equals( p.lexema)){
               existe = true;
               break;
           }
        variable = variable.sig;
       }
       if(existe == false){
            imprimirMensajeError(503, p.renglon);
       }
       return p;
    }
    
    private Nodo compound_statement(Nodo p) {
        if (p.token == 200){ // begin
            p=p.sig;
            p= statement(p);
            while (p.token == 114) {
                p = p.sig;
                p= statement(p);
            }
            if (p.token == 201){
                p=p.sig;
            }else{
                imprimirMensajeError(407, p.renglon);
            }
        }else{
                imprimirMensajeError(406, p.renglon);
        }
        return p;
    }
    
    private Nodo statement(Nodo p) {
        
        p= simple_statement(p);
        p= structured_statement(p);
       
        return p;
    }
    
    private Nodo simple_statement(Nodo p){
        if(p.token==100 ){
            p= assignment_statement(p);
            return p;
        }if(p.token==207){
            p= read_statement(p);
            return p;
        }if(p.token== 208){
            p= write_statement(p);
            return p;
        }
        return p;
    }
    
    private Nodo structured_statement(Nodo p) {
        if(p.token==200){
            p= compound_statement(p);
            return p;
        }else if(p.token==202){
            p= if_statement(p);
            return p;
        }else if(p.token== 205){
            p= while_statement(p);
            return p;
        }
        return p;
    }
    
    private Nodo if_statement(Nodo p) {
        p=p.sig;
        p= expression(p);
        if (p.token==203){ //then
            p=p.sig;
            p= statement(p);
            if(p.token== 204){ //else
                p=p.sig;
                p= statement(p);
            }
        }else{
            imprimirMensajeError(413, p.renglon);
            return p;
        }
        
        return p;
    }
    
    
    private Nodo while_statement(Nodo p) {
        p=p.sig;
        p= expression(p);
        if (p.token == 206){
            p=p.sig;
            p= statement(p);
        }else{
            imprimirMensajeError(414, p.renglon);
        }
        return p;
    }
    
    private Nodo assignment_statement(Nodo p){
        if(p.token==100){
           verificarExiste(p);
            p=p.sig;
            if(p.token==118){ // :=
                p=p.sig;
                if(p.token==114){
                    imprimirMensajeError(412, p.renglon);   
                }
                p= expression(p);
            }else{
                imprimirMensajeError(410, p.renglon);
            }
        }else{
            imprimirMensajeError(401, p.renglon);
        }
        return p;
    }
    
    private Nodo read_statement(Nodo p){
        p=p.sig;
        if (p.token==115){ // (
            p=p.sig;
            p= variable(p);
            while (p.token == 113) { //,
                p=p.sig;
                p= variable(p);
            }
            if(p.token == 116){
                p=p.sig;
            }else{
                imprimirMensajeError(409, p.renglon);
            }
        }else{
            imprimirMensajeError(408, p.renglon);
        }
        return p;
    }
    
    private Nodo write_statement(Nodo p){
        p=p.sig;
        if (p.token==115){ // (
            p=p.sig;
            p= expression(p);
            while (p.token ==113) { //,
                p=p.sig;
                if(p.token==116){
                    imprimirMensajeError(401, p.renglon);
                }
                p= expression(p);
            }
            if (p.token ==116){
                p=p.sig;
            }else{
                imprimirMensajeError(409, p.renglon);
                return p;
            }
        }else{
            imprimirMensajeError(408, p.renglon);
            return p;
        }      
        return p;
    }    

    private Nodo variable(Nodo p) {
        if (p.token!=100){ // id  
            imprimirMensajeError(401, p.renglon);
        }
        verificarExiste(p);
         p=p.sig;
        return p;
    }

    private Nodo expression(Nodo p) {
        p= simple_expression(p);
        if(p.token==106 || p.token==107 || p.token==108 || p.token==109 
                || p.token==110 || p.token==111){ //
             p=p.sig;
             p= simple_expression(p);
        } 
        return p;
    }
    
    private Nodo simple_expression(Nodo p) {
        if (p.token == 103 || p.token ==104){ // + o -
            p=p.sig;
        } 
        p= term(p);
        while (p.token == 103 || p.token ==104 || p.token == 213) {
            p=p.sig;
            p= term(p);
        }
        return p;
    }
    
    private Nodo term(Nodo p) {
        p= factor(p);
        while (p.token == 105 || p.token == 215 || p.token ==212) {
            p=p.sig;
            p= factor(p);
        }
        return p;
    }
    
    private Nodo factor(Nodo p) {
        if (p.token == 100 || p.token == 101 || p.token == 102 || p.token == 117) {
            if(p.token==100){
                verificarExiste(p);
            }
            p=p.sig;
            return p;
        } else if (p.token ==214){ //not
            p=p.sig;
            p= factor(p);
        } else if(p.token ==115){ // (
            p=p.sig;
            p= expression(p);
            if(p.token == 116){ // )
                p=p.sig;
                return p;
            }else{
                imprimirMensajeError(409, p.renglon);
                return p;
            }
        }
        imprimirMensajeError(412, p.renglon);
        return p;
    }
    
}





