package practica3;

import Algoritmos.*;
import Datos.Fichero;
import Datos.Punto;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author Jesus Delgado
 */
public class Practica3 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //GENERACION DE SEMILLAS
        String pi =""+ Math.PI;
        String[] partes = pi.split("\\.");
        pi = partes[0]+ partes[1];
        ArrayList<Integer> semillas = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            String s = ""+pi.charAt(i)+pi.charAt(i+1);
            semillas.add(Integer.parseInt(s));
            //System.out.println("semilla: "+s);
        }
        
        Fichero f = new Fichero(); 
        
        
        /*System.out.println(" ");
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
            ArrayList<Punto> recorrido = g.Algoritmo(puntos);
            System.out.println("El recorrido tiene un coste de "+Evaluacion(recorrido));
        }//*/
        
        
        
        /*System.out.println(" ");
        System.out.println("\t*********************");
        System.out.println("\tAlgoritmo Genetico Basico: ");
        System.out.println("\t*********************");
        
        for(int i = 2; i< f.getArchivos().size(); i++){
            Fichero f2 = new Fichero();
            f2.LeerFichero(i);
            System.out.println("Archivo: "+f2.getNombrefich());
            ArrayList<Punto> puntos = f2.getPuntos();
            GeneticoBasico_Corregido GB = new GeneticoBasico_Corregido();
            for(int j: semillas){
                ArrayList<Punto> recorrido = GB.Algoritmo(puntos, j, 60);
                System.out.println("Con la semilla "+j+" obtengo un coste de "+Evaluacion(recorrido));
                /*System.out.println(" El camino es:");
                int contador = 0;
                Collections.sort(recorrido);
                for (Punto p: recorrido) {
                    System.out.println((contador+1)+"-"+p.getIndice());
                    contador++;
                }//
            }
        }//*/
        
        
        
        /*System.out.println(" ");
        System.out.println("\t*********************");
        System.out.println("\tAlgoritmo Genetico CHC: ");
        System.out.println("\t*********************");
        
        for(int i = 0; i< f.getArchivos().size(); i++){
            Fichero f2 = new Fichero();
            f2.LeerFichero(i);
            System.out.println("Archivo: "+f2.getNombrefich());
            ArrayList<Punto> puntos = f2.getPuntos();
            CHC_Corregido GCHC = new CHC_Corregido();
            for(int j: semillas){
                ArrayList<Punto> recorrido = GCHC.Algoritmo(puntos, j, 60);
                System.out.println("Con la semilla "+j+" obtengo un coste de "+Evaluacion(recorrido));
                /*System.out.println(" El camino es:");
                int contador = 0;
                Collections.sort(recorrido);
                for (Punto p: recorrido) {
                    System.out.println((contador+1)+"-"+p.getIndice());
                    contador++;
                }//
            }
        }//*/
        
        
        
        System.out.println(" ");
        System.out.println("\t*********************");
        System.out.println("\tAlgoritmo Genetico Multimodal: ");
        System.out.println("\t*********************");
        
        for(int i = 0; i< f.getArchivos().size(); i++){
            Fichero f2 = new Fichero();
            f2.LeerFichero(i);
            System.out.println("Archivo: "+f2.getNombrefich());
            ArrayList<Punto> puntos = f2.getPuntos();
            Multimodal GMulti = new Multimodal();
            for(int j: semillas){
                //ArrayList<ArrayList<Punto>> recorridos = GMulti.Algoritmo(puntos, j, 60);
                ArrayList<Punto> recorrido = GMulti.Algoritmo(puntos, j, 60);
                System.out.println("Con la semilla "+j+" obtengo un coste de "+Evaluacion(recorrido));
                /*System.out.println(" El camino es:");
                int contador = 0;
                Collections.sort(recorrido);
                for (Punto p: recorrido) {
                    System.out.println((contador+1)+"-"+p.getIndice());
                    contador++;
                }//*/
                /*for(ArrayList<Punto> r: recorridos){
                    
                    System.out.println(Evaluacion(r)+"");
                }
                
                for(int k = 0; k < recorridos.size(); k++){
                    for(int l = k+1; l < recorridos.size(); l++){
                        int distancia = Hamming(recorridos.get(k),recorridos.get(l));
                        System.out.println(k+"vs"+l+": "+distancia+" ");
                    }
                }*/
            }
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
    
    
    /*
    Cuantos genes diferentes tienen entre ambos cromosomas
    */
    public static int Hamming(ArrayList<Punto> Padre1, ArrayList<Punto> Padre2){
        int distancia = 0;
        
        for(int i = 0; i < Padre1.size(); i++){
            //System.out.println(Padre1.get(i).getIndice()+"vs"+Padre2.get(i).getIndice());
            if(Padre1.get(i).getIndice() != Padre2.get(i).getIndice()){
                distancia++;
            }
        }
        
        return distancia;
    }
}
