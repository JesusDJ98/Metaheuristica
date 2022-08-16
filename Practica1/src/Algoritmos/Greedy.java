package Algoritmos;

import Datos.Punto;
import java.util.ArrayList;


/**
 *
 * @author Jesus Delgado
 */
public class Greedy {
    
    public Greedy(){
    }
    
    
    public ArrayList<Punto> Algoritmo(ArrayList<Punto> ar){
        ArrayList<Punto> recorrido = new ArrayList<>();
        ArrayList<Punto> a = (ArrayList<Punto>)ar.clone();
        Punto inicio = a.remove(0);
        recorrido.add(inicio);
        while(!a.isEmpty()){
            inicio=VecinoMasCercano(a, inicio); //Obtengo la siguiente ciudad mas cercana
            recorrido.add(inicio);              //La añado al recorrido
            a.remove(inicio);                   //La elimino de ciudades candidatas
        }
        
        return recorrido;
    }
    
    /**
     * Devolvemos la ciudad mas cercana
     * @param vecinos
     * @return
     */
    private Punto VecinoMasCercano(ArrayList<Punto> vecinos, Punto p){
        
        Punto cercano = p;
        double dist = 999999999.9;
        
        for(Punto p1: vecinos){
            double x = p.distancia(p1);
            if(x < dist){
                dist = x;
                cercano = p1;
            }
        }
        
        return cercano;
    }
    
}
