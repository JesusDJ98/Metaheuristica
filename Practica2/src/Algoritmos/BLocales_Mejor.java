package Algoritmos;

import Datos.Punto;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Jesus Delgado
 */
public class BLocales_Mejor {
    
    public BLocales_Mejor(){
        
    }
    
    public ArrayList<Punto> Algoritmo(ArrayList<Punto> c){
        ArrayList<Punto> SolActual = (ArrayList<Punto>)c.clone();
        double solAct = Evaluacion(SolActual);
        
        ArrayList<Punto> SolMejor = SolActual;
        double solMej = solAct;
        int i = 1600*c.size();
        boolean parada = false;
        while(!parada && i>=0){
            ArrayList<Punto> SolVecina = GeneraMejorVecinos(SolMejor, solMej);
            double solVec = Evaluacion(SolVecina);//Estudio mejor vecino (opt-2)
            
            if(solVec < solMej){
                SolMejor = SolVecina;
                solMej = solVec;
            }else parada = true;    //Condicion de parada
            i--;
        }
        //System.out.print(( (1600*c.size())-i )+" veces");
        return SolMejor;
    }
    
    
    public ArrayList<Punto> GeneraMejorVecinos(ArrayList<Punto> c, double costeMej){
        ArrayList<Punto> vecino = (ArrayList<Punto>)c.clone();
        double costeMejor = costeMej;
        for(int i = 0; i< vecino.size()-1; i++){
            for(int j = i+1; j<vecino.size(); j++){
                
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
                        //System.out.println("Mejora "+i+","+j+ " "+costeAct);
                        costeMejor = costeAct;
                        vecino = aux;
                    }
                } //*/
                
                //Muy poco ineficiente
                /*ArrayList<Punto> aux = (ArrayList<Punto>)c.clone();
                aux.set(i, c.get(j));
                aux.set(j, c.get(i));
                double costeAct = Evaluacion(aux);
                
                if(costeAct < costeMejor){
                    //System.out.println("Mejora "+i+","+j+ " "+costeAct);
                    costeMejor = costeAct;
                    vecino = aux;
                }*/
            }
        }
        //System.out.println("Final "+costeMejor);
        
        return vecino;
    }
    
    //NO devuelve el mejor vecino sino el camino con los mejores swap posibles aplicados en cada pasada
    //NO es vecino pues hay mas de un swap
    public ArrayList<Punto> GeneraMejorVecino(ArrayList<Punto> c, double solMej){
        ArrayList<Punto> vecino = (ArrayList<Punto>)c.clone();
        double dist = solMej;
        for(int i = 1; i<c.size()-2; i++){
            for(int j=i+1; j<c.size()-1; j++){
                //Lo que cuesta llegar a mi y lo que cuesta ir al siguiente
//                double x = vecino.get(i-1).distancia(vecino.get(i)) + vecino.get(j).distancia(vecino.get(j+1)); 
//                double y =  vecino.get(i-1).distancia(vecino.get(j)) + vecino.get(i).distancia(vecino.get(j+1));
                double x = c.get(i-1).distancia(c.get(i)) + c.get(j).distancia(c.get(j+1)); 
                double y =  c.get(i-1).distancia(c.get(j)) + c.get(i).distancia(c.get(j+1));
                //System.out.println("Probamos-->"+i+" - "+j+ " con dist "+x+"vs"+y );
                
                if(y < x){
                    ArrayList<Punto> aux = (ArrayList<Punto>)c.clone();
                    aux.set(i, c.get(j));
                    aux.set(j, c.get(i));
                    double sal = Evaluacion(aux);
                    if(sal < dist){
                        vecino = (ArrayList<Punto>)aux.clone();
                        dist = sal;
                    }
                        
                    /*double resta = x-y;
                    if(resta > dist){
                        dist = resta;
                        ArrayList<Punto> aux = (ArrayList<Punto>)c.clone();
                        aux.set(i, c.get(j));
                        aux.set(j, c.get(i));
                        vecino = (ArrayList<Punto>)aux.clone();
                    }//*/
                    //Esto no es vecino, pero resultados chetados
//                    vecino.set(i, c.get(j));
//                    vecino.set(j, c.get(i));
                }
            }
        }
        
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
