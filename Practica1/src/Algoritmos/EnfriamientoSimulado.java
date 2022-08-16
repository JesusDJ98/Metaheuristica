package Algoritmos;

import Datos.Punto;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Jesus Delgado
 */
public class EnfriamientoSimulado {
    
    public EnfriamientoSimulado(){
    }
    
    public ArrayList<Punto> Algoritmo(ArrayList<Punto> c, int semilla){
        Random r = new Random(semilla);
        ArrayList<Punto> SolActual = GenerarSol(c, r);
        double solAct = Evaluacion(SolActual);      //Costo solucion inicial
        double u = 0.3;
        double o = 0.3;                             //Probabilidad de aceptacion
        double e = Math.E;                          //Numero e
        
        Greedy g = new Greedy();
        ArrayList<Punto> gredy = g.Algoritmo(SolActual);
        double Greedy_Coste = Evaluacion(gredy);    
        
        double Ti = (u/(-Math.log10(o))) * Greedy_Coste;     //Esquema de enfriamiento  --> Temperatura Inicial
        double T = Ti;                
        int L = 20;        //Condicion de enfriamiento --> Soluciones generadas    --> Velocidad de enfriamiento
        double Tf = 80*c.size();  //Condicion de parada       --> Numero de enfriamiento
        int k = 0;
        
        ArrayList<Punto> Mejor  = (ArrayList<Punto>)SolActual.clone();
        double CosteMej = solAct;
        
        while(k < Tf){
            for(int i = 0; i < L; i++){
                ArrayList<Punto> SolCandidata =  SeleccionaSolucion(SolActual, r); //Generacion de nueva solucion
                double solCandi = Evaluacion(SolCandidata);
                double s = solCandi - solAct;         //Calculo de diferencia de coste
                double x = Math.pow(e, (-s/T));
                if( (r.nextDouble() < x) || (s < 0) ){//Criterio de aceptacion --> U numero aleatorio entre 0 y 1
                    solAct = solCandi;
                    SolActual = SolCandidata;
                    if(solAct < CosteMej){            //Para devolver solo el mejor
                        CosteMej = solAct;
                        Mejor = (ArrayList<Punto>)SolActual.clone();
                    }
                }
            }
            k++; //k+=L;
            T  = Ti/ (1+k);                                 //Mecanismo de enfriamiento(alfa) Cauchy
        }
        
        return Mejor;
    }
    
    
    public ArrayList<Punto> SeleccionaSolucion(ArrayList<Punto> actual, Random r){
        ArrayList<Punto> candidata = (ArrayList<Punto>) actual.clone();
        
        //Elijo indices
        int i = 0;
        int j = 0;
        while(i == j){
            i = r.nextInt(actual.size());
            j = r.nextInt(actual.size());
        }
        
        //Intercambio solucion
        candidata.set(i, actual.get(j));
        candidata.set(j, actual.get(i));
        
        return candidata;
    }
    
    
    
    
    
    
    
    
    
    
    
    public ArrayList<Punto> GenerarSol(ArrayList<Punto> ciudades, Random r){
        ArrayList<Punto> recorrido = new ArrayList<>();
        ArrayList<Punto> c = (ArrayList<Punto>)ciudades.clone();
        
        while(!c.isEmpty()){
            Punto p = c.remove(r.nextInt(c.size()));
            recorrido.add(recorrido.size(),p);
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
