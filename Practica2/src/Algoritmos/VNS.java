package Algoritmos;

import Datos.Punto;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Jesus Delgado
 */
public class VNS {
    public VNS(){
    }
    
    public ArrayList<Punto> Algoritmo(ArrayList<Punto> c, int kmax, int blmax, int semilla){
        Random r = new Random(semilla);
        BLocales_Mejor BLM = new BLocales_Mejor();

        //1
        ArrayList<Punto> Sact = GenerarSol(c, r);   //Esta es ya la mejor solucion
        int k = 1;
        int bl = 0;
        boolean fin = false;
        while(!fin){
            //2
            if(k > kmax) k = 1;
            while(k <= kmax && !fin){
                //3
                int s = c.size()/(9-k);
                ArrayList<Punto> Svec = GenerarVecino(Sact, r, s);
                //4
                ArrayList<Punto> Sprima = BLM.Algoritmo(Svec);
                bl++;
                //5
                if(Evaluacion(Sprima) < Evaluacion(Sact)){
                    Sact = (ArrayList<Punto>)Sprima.clone();
                    k = 1;
                }else k++;
            }
            //6
            if(bl >= blmax) fin = true;
        }
        
        return Sact;
    }
    
    private ArrayList<Punto> GenerarVecino(ArrayList<Punto> S, Random r, int tama){
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
