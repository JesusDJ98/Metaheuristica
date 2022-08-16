package Algoritmos;

import Datos.Punto;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Jesus Delgado
 */
public class BAleatoria {
    
    public BAleatoria(){
    }
    
    public ArrayList<Punto> Algoritmo(ArrayList<Punto> c, int semilla){
        Random r = new Random(semilla);
        ArrayList<Punto> SolInicial = GenerarSol(c, r);
        ArrayList<Punto> SolActual = SolInicial;
        ArrayList<Punto> SolMejor = SolActual;
        double solMejor = Evaluacion(SolMejor);
        int i = 1600*c.size();
        while(i > 0){
            SolActual = GenerarSol(c, r);   //Genera una solucion aleatoria
            double x = Evaluacion(SolActual);
            if(x  < solMejor){
                SolMejor = SolActual;
                solMejor = x;
            }
            i--;
        }
        
        return SolMejor;
    }
    
    public ArrayList<Punto> GenerarSol(ArrayList<Punto> ciudades, Random r){
        ArrayList<Punto> recorrido = new ArrayList<>();
        ArrayList<Punto> c = (ArrayList<Punto>)ciudades.clone();
        
        while(!c.isEmpty()){
            Punto p = c.remove(r.nextInt(c.size()));
            recorrido.add(p);
        }
        return recorrido;
    }
    
    
    public double Evaluacion(ArrayList<Punto> camino){
        double km = 0;
        for(int i = 0; i<camino.size()-1; i++){
            Punto p1 = camino.get(i);
            Punto p2 = camino.get(i+1);
            km += p1.distancia(p2);
        }
        
        km += camino.get(0).distancia(camino.get(camino.size()-1));
        
        return km;
    }
}
