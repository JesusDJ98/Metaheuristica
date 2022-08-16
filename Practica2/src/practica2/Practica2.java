package practica2;

import Algoritmos.*;
import Datos.*;
import java.util.ArrayList;

/**
 *
 * @author Jesus Delgado
 */
public class Practica2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Fichero f = new Fichero(); 
        int semilla = 31;
        
                
        // TODOS LOS FICHEROS POR CADA METODO
        
            //Greedy
        System.out.println(" ");
        System.out.println("\t*********************");
        System.out.println("\t  Greddy: ");
        System.out.println("\t*********************");
        
        for(int i = 0; i< f.getArchivos().size(); i++){
            //System.out.println(s);
            Fichero f2 = new Fichero();
            f2.LeerFichero(i);
            System.out.println("Archivo: "+f2.getNombrefich());
            ArrayList<Punto> puntos = f2.getPuntos();
            Greedy g = new Greedy();
            ArrayList<Punto> recorrido = g.Algoritmo(puntos,0);
            System.out.println("El recorrido tiene un coste de "+Evaluacion(recorrido));
        }//*/
        
        System.out.println(" ");
        System.out.println("\t     *********************");
        System.out.println("\t  Busqueda Local Mejor Vecino: ");
        System.out.println("\t     *********************");
        
        for(int i = 0; i< f.getArchivos().size(); i++){
            //System.out.println(s);
            Fichero f2 = new Fichero();
            f2.LeerFichero(i);
            System.out.println("Archivo: "+f2.getNombrefich());
            ArrayList<Punto> puntos = f2.getPuntos();
            BLocales_Mejor_P1 BLM = new BLocales_Mejor_P1();
            ArrayList<Punto> recorrido = BLM.Algoritmo(puntos, semilla);
            System.out.println("El recorrido tiene un coste de "+Evaluacion(recorrido));
        }//*/
        
        
        
        System.out.println(" ");
        System.out.println("\t*********************");
        System.out.println("\t\t GRASP: ");
        System.out.println("\t*********************");
        
        for(int i = 0; i< f.getArchivos().size(); i++){
            //System.out.println(s);
            Fichero f2 = new Fichero();
            f2.LeerFichero(i);
            System.out.println("Archivo: "+f2.getNombrefich());
            ArrayList<Punto> puntos = f2.getPuntos();
            GRASP g = new GRASP();
            ArrayList<Punto> recorrido = g.Algoritmo(puntos, 10, semilla);
            System.out.println("Con la semilla "+semilla+" el recorrido tiene un coste de "+Evaluacion(recorrido));
        }//*/
        
        
        System.out.println(" ");
        System.out.println("\t*********************");
        System.out.println("\t\t ILS: ");
        System.out.println("\t*********************");
        
        for(int i = 0; i< f.getArchivos().size(); i++){
            //System.out.println(s);
            Fichero f2 = new Fichero();
            f2.LeerFichero(i);
            System.out.println("Archivo: "+f2.getNombrefich());
            ArrayList<Punto> puntos = f2.getPuntos();
            ILS g = new ILS();
            ArrayList<Punto> recorrido = g.Algoritmo(puntos, 50, semilla);
            System.out.println("Con la semilla "+semilla+" el recorrido tiene un coste de "+Evaluacion(recorrido));
        }//*/
        
        
        System.out.println(" ");
        System.out.println("\t*********************");
        System.out.println("\t\t VNS: ");
        System.out.println("\t*********************");
        int blmax = 50; //numero de ejecucions de BL
        int kmax = 5;   //Entornos diferentes
        for(int i = 0; i< f.getArchivos().size(); i++){
            //System.out.println(s);
            Fichero f2 = new Fichero();
            f2.LeerFichero(i);
            System.out.println("Archivo: "+f2.getNombrefich());
            ArrayList<Punto> puntos = f2.getPuntos();
            VNS g = new VNS();
            ArrayList<Punto> recorrido = g.Algoritmo(puntos, kmax, blmax, semilla);
            System.out.println("Con la semilla "+semilla+" el recorrido tiene un coste de "+Evaluacion(recorrido));
        }//*/
        
        
    }
    
    
    
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
