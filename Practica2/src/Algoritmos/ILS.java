package Algoritmos;

import Datos.Punto;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Jesus Delgado
 */
public class ILS {
    public ILS(){
    }
    
    public ArrayList<Punto> Algoritmo(ArrayList<Punto> c, int times, int semilla){
        Random r = new Random(semilla);
        BLocales_Mejor BLM = new BLocales_Mejor();
        
        //Greedy g = new Greedy();              //Iniciar con Solucion Greedy
        //ArrayList<Punto> So = g.Algoritmo(c, 0);
        ArrayList<Punto> So = GenerarSol(c, r); //Iniciar solucion aleatoria
        ArrayList<Punto> S = BLM.Algoritmo(So);
        ArrayList<Punto> Mejor = (ArrayList<Punto>)S.clone();
        
        int s = c.size()/4; //Tamaño sublista
        for(int i = 1; i < times; i++){
            ArrayList<Punto> Sprima = Mutacion(S, r, s); //Mutacion a S --> Cambio las posiciones de la sublista de manera
            ArrayList<Punto> S2prima = BLM.Algoritmo(Sprima);//aleatoria (tantos cambios como el tamanio de la sublista)
            S = CriterioAceptacion(S2prima, Sprima);
            Mejor = Actualizar(S, Mejor);
            //System.out.println("Me quedo con "+Evaluacion(Mejor));
        }
        
        
        return Mejor;
    }
    
    private ArrayList<Punto> CriterioAceptacion(ArrayList<Punto> Candidato, ArrayList<Punto> Mejor){
        if(Mejor.size() > 0){
            //System.out.println("Criterio Aceptacion - Comparo Sprima("+Evaluacion(Mejor)+") vs S2prima("+Evaluacion(Candidato)+")");
            if(Evaluacion(Candidato) < Evaluacion(Mejor))return (ArrayList<Punto>)Candidato.clone();
            else return Mejor;
        }else return (ArrayList<Punto>)Candidato.clone();
    }
    
    private ArrayList<Punto> Actualizar(ArrayList<Punto> Candidato, ArrayList<Punto> Mejor){
        if(Mejor.size() > 0){
            //System.out.println("Actualizar -Comparo Mejor("+Evaluacion(Mejor)+") vs Candidato("+Evaluacion(Candidato)+")");
            if(Evaluacion(Candidato) < Evaluacion(Mejor))return (ArrayList<Punto>)Candidato.clone();
            else return Mejor;
        }else return (ArrayList<Punto>)Candidato.clone();
    }
       
    private ArrayList<Punto> Mutacion(ArrayList<Punto> S, Random r, int tama){
        ArrayList<Punto> cambio = new ArrayList<>();
        ArrayList<Punto> sublista = new ArrayList<>();
        int aleatorio = r.nextInt(S.size());
        int fin = tama+aleatorio;
        if(fin > S.size()){ //Que sea fija --> Porfesor dice que NO (aleatorio-size)
            //fin = aleatorio;
            //aleatorio -= tama;
            fin = S.size(); 
        }
        //System.out.println("Tamaño sublista masx("+tama+") real ("+aleatorio+","+fin+") -- "+(fin-aleatorio));
        
            //Rellenamos sublista
        for(int i = aleatorio; i < fin; i++){
            sublista.add(S.get(i));
        }
        
            //Generemos los cambios
        if(sublista.size() > 1){    //Puede darse el caso dq solo haya un Punto...
            for(int i = 0; i < sublista.size(); i++){
                int a;
                int b;
                do{
                    a = r.nextInt(sublista.size());
                    b = r.nextInt(sublista.size());
                }while(a == b);
                Punto A = sublista.get(a);
                Punto B = sublista.get(b);
                sublista.set(a, B);
                sublista.set(b, A);
                //System.out.println("Modificamos "+a+" por "+b+" equivalen "+A.getIndice()+"x"+B.getIndice());
            }
        }
            

            //Recomponemos la solucion
        //Primera parte
        for(int i = 0; i < aleatorio; i++){
            cambio.add(S.get(i));
        }
        //segunda parte
        for(int i = 0; i < sublista.size(); i++){
            cambio.add(sublista.get(i));
        }
        //Tercera parte
        for(int i = fin; i < S.size(); i++){
            cambio.add(S.get(i));
        }
        
        return cambio;
    }
    
    
    
    
    
    
    private ArrayList<Punto> GenerarSol(ArrayList<Punto> ciudades, Random r){
        ArrayList<Punto> recorrido = new ArrayList<>();
        ArrayList<Punto> c = (ArrayList<Punto>)ciudades.clone();
        
        while(!c.isEmpty()){
            Punto p = c.remove(r.nextInt(c.size()));
            recorrido.add(recorrido.size(),p);
        }
        return recorrido;
    }
    
    private double Evaluacion(ArrayList<Punto> camino){
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
