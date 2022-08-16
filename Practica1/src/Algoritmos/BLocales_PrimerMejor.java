package Algoritmos;

import Datos.Punto;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Jesus Delgado
 */
public class BLocales_PrimerMejor {
    
    public BLocales_PrimerMejor(){
    }
    
    public ArrayList<Punto> Algoritmo(ArrayList<Punto> c, int semilla){
        Random r = new Random(semilla);
        ArrayList<Punto> SolActual = GenerarSol(c, r);
        double solAct = Evaluacion(SolActual);
        
        ArrayList<Punto> SolMejor = SolActual;
        double solMej = solAct;
        
        
        int i = 1600*c.size();
        boolean parada = false;
        while(!parada && i>=0){
            //Estudio mejor vecino (opt-2)
            ArrayList<Punto> SolVecina = GeneraPrimerMejorVecinos(SolMejor);
            double solVec = Evaluacion(SolVecina);
            if(solVec < solMej){
                SolMejor = SolVecina;
                solMej = solVec;
            }else parada = true;    //Condicion de parada
            i--;
        }
        System.out.print(( (1600*c.size())-i )+" veces");
        return SolMejor;
    }
    
    public ArrayList<Punto> GeneraPrimerMejorVecinos(ArrayList<Punto> c){
        ArrayList<Punto> vecino = (ArrayList<Punto>)c.clone();
        double costeMejor = Evaluacion(vecino);
        //System.out.println("Iniacial "+costeMejor);
        boolean mejor = false;
        int i = 0;
        while(i<vecino.size()-1 && !mejor){
            int j = i+1;
            while(j<vecino.size() && !mejor){
                
                double dist_i_ant = 0;
                if(i != 0) dist_i_ant = c.get(i-1).distancia(c.get(i));
                else dist_i_ant = c.get(c.size()-1).distancia(c.get(i));
                double dist_j_sig = 0;
                if(j != vecino.size()-1) dist_j_sig = c.get(j).distancia(c.get(j+1));
                else dist_j_sig = c.get(j).distancia(c.get(0));
                //De llegar ami y de salir de aqui
                double d1 = dist_i_ant + c.get(i).distancia(c.get(i+1));
                double d2 = c.get(j-1).distancia(c.get(j)) + dist_j_sig;
                double x = d1+d2;   //Coste normal
                
                
                double dist_i_ant2 = 0;
                if(i != 0)dist_i_ant2 = c.get(i-1).distancia(c.get(j));
                double dist_j_sig2 = 0;
                if(j != vecino.size()-1) dist_j_sig2 = c.get(i).distancia(c.get(j+1));
                else dist_j_sig2 = c.get(i).distancia(c.get(0));
                
                double d3 = dist_i_ant2 + c.get(j).distancia(c.get(i+1));
                double d4 = c.get(j-1).distancia(c.get(i)) + dist_j_sig2;
                double y = d3+d4;   //Coste cambiado
                
                 //Empeora pero es mucho MUCHISIMO mas eficiente
                if(y<x){
                    ArrayList<Punto> aux = (ArrayList<Punto>)c.clone();
                    aux.set(i, c.get(j));
                    aux.set(j, c.get(i));
                    double costeAct = Evaluacion(aux);

                    if(costeAct < costeMejor){
                        costeMejor = costeAct;
                        vecino = aux;
                        mejor = true;
                    }
                }
                
                //Muy poco ineficiente
                /*ArrayList<Punto> aux = (ArrayList<Punto>)c.clone();
                aux.set(i, c.get(j));
                aux.set(j, c.get(i));
                double costeAct = Evaluacion(aux);
                //System.out.println("Salida "+i+","+j+ " "+costeAct);
                if(costeAct < costeMejor){
                    costeMejor = costeAct;
                    vecino = aux;
                    mejor = true;
                }*/
                j++;
            }i++;
        }
        //System.out.println("Final "+costeMejor);
        
        return vecino;
    }
    
    
    
   /*public ArrayList<Punto> GeneraVecinos(ArrayList<Punto> c, Punto estudio){
        ArrayList<Punto> vecino = (ArrayList<Punto>)c.clone();
        
        int i = c.indexOf(estudio);
        double dist = 99999999999.99;   //Un numero muy grande
        int min = i;                    //Indice del que cambio
        //Genero los vecinos del punto de estudio
        for(int j=0; j<c.size(); j++){
            if(j != i){
                double x = estudio.distancia(c.get(j));
                if(x < dist){
                    min = j;
                    dist = x;
                }
            }
        }
        //Intercambiamos posicion
        if(min != i){   //Hemos encontrado algo
            //System.out.println("Hemos intercambiado el punto: "+estudio+" por el punto: "+c.get(min));
            vecino.set(i, c.get(min));
            vecino.set(min, estudio);
            j = c.size()
        }//else System.out.println("No modifico nada");
        
        return vecino;
    }//*/
    
    
    
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
