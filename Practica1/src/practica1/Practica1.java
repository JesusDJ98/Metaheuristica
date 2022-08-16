package practica1;

import Datos.*;
import Algoritmos.*;
import java.util.ArrayList;

/**
 *
 * @author Jesus Delgado
 */
public class Practica1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //GENERACION DE SEMILLAS
        String pi =""+ Math.PI;
        String[] partes = pi.split("\\.");
        pi = partes[0]+ partes[1];
        ArrayList<Integer> semillas = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            String s = ""+pi.charAt(i)+pi.charAt(i+1);
            semillas.add(Integer.parseInt(s));
            //System.out.println("semilla: "+s);
        }
        
        Fichero f = new Fichero(); 
        
                
        // TODOS LOS FICHEROS POR CADA METODO
        
            //Greedy
        System.out.println(" ");
        System.out.println("*********************");
        System.out.println("Greddy: ");
        System.out.println("*********************");
        
        for(int i = 0; i< f.getArchivos().size(); i++){
            //System.out.println(s);
            Fichero f2 = new Fichero();
            f2.LeerFichero(i);
            System.out.println("Archivo: "+f2.getNombrefich());
            ArrayList<Punto> puntos = f2.getPuntos();
            Greedy g = new Greedy();
            ArrayList<Punto> recorrido = g.Algoritmo(puntos);
            System.out.println("El recorrido tiene un coste de "+Evaluacion(recorrido));
        }//*/
        

            //Busqueda aleatoria
        System.out.println(" ");
        System.out.println("*********************");
        System.out.println("Busqueda Aleatoria: ");
        System.out.println("*********************");
        
        for(int i = 0; i< f.getArchivos().size(); i++){
            Fichero f2 = new Fichero();
            f2.LeerFichero(i);
            System.out.println("Archivo: "+f2.getNombrefich());
            ArrayList<Punto> puntos = f2.getPuntos();
            BAleatoria ba = new BAleatoria();
            ArrayList<Punto> recorrido = puntos;
            for(int j: semillas){
                recorrido = ba.Algoritmo(recorrido, j);
                System.out.println("Con la semilla "+j+" obtengo un coste de "+Evaluacion(recorrido));
            }
        } //*/
        
        
        
        
            //Busqueda Locales Basicas
        System.out.println(" ");
        System.out.println("*********************");
        System.out.println("Busqueda Locales"); 
        System.out.println("*********************");
            
        //Busqueda Local el mejor vecino
        System.out.println(" ");
        System.out.println("\t*********************");
        System.out.println("\tEL Mejor Vecino"); 
        System.out.println("\t*********************");
        
        for(int i = 0; i< f.getArchivos().size(); i++){
            //System.out.println(s);
            Fichero f2 = new Fichero();
            f2.LeerFichero(i);
            System.out.println("Archivo: "+f2.getNombrefich());
            ArrayList<Punto> puntos = f2.getPuntos();
            BLocales_Mejor BLM = new BLocales_Mejor();
            //ArrayList<Punto> recorrido = puntos;
            for(int s: semillas){
                System.out.print("Con la semilla "+s+" el algoritmo se ejecuta ");
                ArrayList<Punto> recorrido = BLM.Algoritmo(puntos, s);
                System.out.println(" y se obtiene un valor de "+Evaluacion(recorrido));
            }
        } //*/
        
        
        //Busqueda Local El primer mejor vecino
        System.out.println(" ");
        System.out.println("\t*********************");
        System.out.println("\tEl Primer Mejor Vecino"); 
        System.out.println("\t*********************");
        
        for(int i = 0; i< f.getArchivos().size(); i++){
            Fichero f2 = new Fichero();
            //System.out.println(s);
            f2.LeerFichero(i);
            System.out.println("Archivo: "+f2.getNombrefich());
            ArrayList<Punto> puntos = f2.getPuntos();
            //ArrayList<Punto> recorrido = puntos;
            BLocales_PrimerMejor BLPM = new BLocales_PrimerMejor();
            for(int s: semillas){
                System.out.print("Con la semilla "+s+" el algoritmo se ejecuta ");
                ArrayList<Punto> recorrido = BLPM.Algoritmo(puntos, s);
                System.out.println(" y se obtiene un valor de "+Evaluacion(recorrido));
            }
        } //*/
        
        
        
        System.out.println(" ");
        System.out.println("\t*********************");
        System.out.println("\tEnfriamiento Simulado"); 
        System.out.println("\t*********************");
        
               
        for(int i = 0; i< f.getArchivos().size(); i++){
            //System.out.println(s);
            Fichero f2 = new Fichero();
            f2.LeerFichero(i);
            System.out.println("Archivo: "+f2.getNombrefich());
            ArrayList<Punto> puntos = f2.getPuntos();
            
            EnfriamientoSimulado ES = new EnfriamientoSimulado();
            for(int s: semillas){
                ArrayList<Punto> recorrido = ES.Algoritmo(puntos, s);
                System.out.println("Con la semilla "+s+" obtengo un valor de "+Evaluacion(recorrido));
            }
        } //*/
        
        
        
        
        
        System.out.println(" ");
        System.out.println("\t*********************");
        System.out.println("\t   Busqueda Tabu"); 
        System.out.println("\t*********************");
        
                
        for(int i = 0; i< f.getArchivos().size(); i++){
            //System.out.println(s);
            Fichero f2 = new Fichero();
            f2.LeerFichero(i);
            System.out.println("Archivo: "+f2.getNombrefich());
            ArrayList<Punto> puntos = f2.getPuntos();
            BTabu Tabu = new BTabu();
            for(int s: semillas){
                ArrayList<Punto> recorrido = Tabu.Algoritmo(puntos, s);
                System.out.println("Con la semilla "+s+" obtengo un valor de "+Evaluacion(recorrido));
            }
        } //*/
        
        
        //Guardar camino y evaluacion
        
    }
    
    
     /*
    Lo añado en una clase que todos deben heredar para poder llamarla sin problemas
    */
    public static double Evaluacion(ArrayList<Punto> camino){
        double km = 0;
        if(camino.size() > 0){
            for(int i = 0; i<camino.size()-1; i++){
                Punto p1 = camino.get(i);
                Punto p2 = camino.get(i+1);
                km += p1.distancia(p2);
            }

            km += camino.get(0).distancia(camino.get(camino.size()-1));
        }
        
        
        return km;
    }
    
}
