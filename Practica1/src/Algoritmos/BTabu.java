package Algoritmos;

import Datos.Punto;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Jesus Delgado
 */
public class BTabu {
    
    public BTabu(){
    }
    
    public ArrayList<Punto> Algoritmo(ArrayList<Punto> c, int semilla){
        Random r = new Random(semilla);
        ArrayList<Punto> SolAct = GenerarSol(c, r); //Generar solucion inicial aleatoria
        double solAct = Evaluacion(SolAct);
        
        int[][] LargoPlazo = new int[c.size()][c.size()];   //Frecuencia de ciudades consecutivas
        LargoPlazo = ActualizarMemoriaLargoPlazo(SolAct, LargoPlazo);
        
        int veces = 40*c.size();
        int cantidad = 40;  //Cantidad de vecinos
        ArrayList<String> tabu = new ArrayList<>(); //Cogere las posiciones
        int tamañoT = c.size()/2;//Si supera el tamaño añado y elimino
        ArrayList<Punto> Mejor_Global = (ArrayList<Punto>)SolAct.clone();   //Solucion mejor del problema
        double Mejor_global = solAct;
        ArrayList<Punto> Mejor_vecino = (ArrayList<Punto>)SolAct.clone();   //Solucion mejor de los vecinos estudiados
        double coste_mejor_vecino = solAct;
               
        for(int i = 0; i<veces; i++){
            ArrayList<String>  movimientos = new ArrayList<>(); //Movimientos ha estudiar
            ArrayList<ArrayList<Punto>> N = GenerarConjuntoVecinos(SolAct, r, cantidad, movimientos);
            ArrayList<Double> Costes = new ArrayList<>();   //Costes de estos vecinos
            ArrayList<String> T = (ArrayList<String>)tabu.clone(); //Movimientos en tabu

            int m = MejorMovimiento(N,Costes);
            ArrayList<Punto> Sprima = (ArrayList<Punto>)N.get(m).clone(); //Mejor camino (inicial)
            double Sprima_valor = Costes.get(m);       //Mejor coste (inicial)
            
            //Miramos los vecinos candidatos
            boolean actualizacion = false;//Prueba de fin
            while(N.size() > 0 && !actualizacion){
                //Si no es Tabu - Es Tabu pero satisface prueba de aspiracion
                if(!T.contains(movimientos.get(m)) || (T.contains(movimientos.get(m)) 
                        && (Sprima_valor < Mejor_global)) ){
                    double coste_vecino = Sprima_valor;     //Evaluamos
                    coste_mejor_vecino = coste_vecino;//Yo siempre cojo el mejor de los vecinos que he generado
                    Mejor_vecino = (ArrayList<Punto>)Sprima.clone();
                    actualizacion = true;
                }else{//Elimino el estudiado, y cojo el siguiente
                    N.remove(m);
                    movimientos.remove(m);
                    Costes.remove(m);
                    //Actualizamos
                    m = MejorMovimiento(N,Costes);
                    Sprima = (ArrayList<Punto>)N.get(m).clone(); 
                    Sprima_valor = Costes.get(m);       
                }
                //Prueba de fin
                //Si se actualiza significa que hay que buscar otros candidatos
                                
            }//Ya tenemos el candidato elegido
            
            //Realizar movimiento
            solAct = coste_mejor_vecino;
            SolAct = (ArrayList<Punto>)Mejor_vecino.clone();
            //Modificar Tabu
            if(!T.contains(movimientos.get(m))) T.add(movimientos.get(m));
            
            if(T.size() >= tamañoT){
                T.remove(0); //Elimino el primero que entro
            }
            tabu = (ArrayList<String>)T.clone();
                        
            //Si es mejor que el global
            if(solAct < Mejor_global){
                Mejor_global = solAct;
                Mejor_Global = (ArrayList<Punto>)SolAct.clone();
            }
            
            //Actualizar Memoria Largo Plazo
            LargoPlazo = ActualizarMemoriaLargoPlazo(SolAct, LargoPlazo);
            
            
            //Reinicializacion
            if(i>0 && i%(veces/5) == 0){
                //Modificamos la lista Tabu
                double modificarTabu = r.nextDouble();
                if(modificarTabu < 0.5) tamañoT/=2; //Disminuimos un 50%
                else tamañoT = tamañoT+(tamañoT/2); //Aumentamos un 50%
                tabu = new ArrayList<>();
                
                double aleatorio = r.nextDouble();
                if(aleatorio < 0.25){//Solucion inicial ALEATORIA
                    SolAct = GenerarSol(SolAct, r);
                }else if(aleatorio < 0.75){ //Largo plazo (GREEDY)
                    SolAct = Greedy(SolAct, LargoPlazo);
                }else{//Mejor solucion obtenida
                    SolAct = (ArrayList<Punto>)Mejor_Global.clone();
                }
                
            }
            
        }
        
        
        return Mejor_Global;
    }
    
    
    public int MejorMovimiento(ArrayList<ArrayList<Punto>> N, ArrayList<Double> Costes){
        int indice = 0;
        if(Costes.isEmpty()){
            double mejor = -1; 
            int i =0;
            for(ArrayList<Punto> camino: N){

                double coste = Evaluacion(camino);
                Costes.add(coste);
                if(mejor == -1 || coste < mejor){
                    mejor = coste;
                    indice = i;
                }i++;
            }
        }else{
            double mejor = Costes.get(indice); 
            for(int i = 0; i<N.size(); i++){
                double coste = Costes.get(indice);
                if(coste < mejor){
                    mejor = coste;
                    indice = i;
                }
            }
        }
        
        
        return indice;
    }
    
    
    public ArrayList<ArrayList<Punto>> GenerarConjuntoVecinos(ArrayList<Punto> ciudades, Random r, int cantidad, ArrayList<String>  movimientos){
        ArrayList<ArrayList<Punto>> Conjunto = new ArrayList<>();
        
        for(int i = 0; i< cantidad; i++){
            ArrayList<Punto> aux = (ArrayList<Punto>)ciudades.clone();
            int a = 0;
            int b = 0;
            String s = "";
            do{         //Realice algun cambio que no este hecho
                a = r.nextInt(aux.size());
                b = r.nextInt(aux.size());
                //Para que sigan todo el mismo criterio y no diferencia entre 3-5 y 5-3
                if(a < b) s = a+"-"+b;
                else s = b+"-"+a;
            }while(a==b && !movimientos.contains(s));   //No sea un movimiento repetido y no sea un cambio tonto
            
            
            
            aux.set(a, ciudades.get(b));
            aux.set(b, ciudades.get(a));
            Conjunto.add((ArrayList<Punto>)aux.clone());
            movimientos.add(s);
        }
        
        return Conjunto;
    }
    
    /*  Memoria de frecuencias de ciudades consecutivas de los movimientos realizados
    indice de la ciudad
            1   2   3   4   5                                           1-4-...
        [1  -   0   2   1   4   veces que estan consecutivos (la ciudad 1 esta antes que la ciudad 4, 1 vez)
        [2      -
        [3          -
        [4  3   0   2   -   3       (la ciudad 4 esta antes que la ciudad 1, 3 veces) 4-1-...
        [5                  -
        */
    public int[][] ActualizarMemoriaLargoPlazo(ArrayList<Punto> actual, int[][] memoria){
        int[][] memorialargoplazo = memoria;
        
        for(int i = 0; i< actual.size(); i++){
            int a = actual.get(i).getIndice()-1;
            int b;
            if((i+1) >= actual.size()){
                b = actual.get(0).getIndice()-1;
            }else b = actual.get(i+1).getIndice()-1;
            memorialargoplazo[a][b]++; //Incrementamos el valor de la frecuencia
        }
        
        return memorialargoplazo;
    }
    
    /*
    Desde la posicion de inicio de la posicion actual, partimos y cogemos los destinos
    menos visitados(frecuencia) de la memoria a largo plazo, en caso de empate cogemos
    el que primero hemos encontrado
    */
    public ArrayList<Punto> Greedy(ArrayList<Punto> actual, int[][] memoriaLP){
        ArrayList<Punto> caminoGreedy = new ArrayList<>();
        
        Punto Pact = actual.remove(0);
        caminoGreedy.add(Pact);
        //System.out.println("Empiezo por "+Pact.toString() + " con un camino restante "+actual.size());
        while(actual.size() > 0 ){
            int indice = Pact.getIndice()-1;
            int pos = 0;
            int minvalor = 99999999;
            for(int i = 0; i < memoriaLP[indice].length; i++){
                if(indice != i){
                    int valor = memoriaLP[indice][i];   //Estudiamos toda mi fila
                    if(valor < minvalor && !EstaCogido(caminoGreedy, i+1)){
                        pos = i;
                        minvalor = valor;
                    }
                }
            }
            //System.out.println("El siguiente menos visitado es "+(pos+1)+" con un total de "+minvalor+" veces");
            
            //Buscamos ese punto
            int j = 0;
            boolean encontrado = false;
            while(j < actual.size() && !encontrado){
                if(actual.get(j).getIndice() == (pos+1))encontrado = true;
                else j++;
            }
            //System.out.println("El punto esta en la pos "+j+" de "+actual.size());
            //Lo añado
            Pact = actual.remove(j);
            caminoGreedy.add(Pact);
            
            /*System.out.println("Por ahora mi camino es:");
            for(Punto p: caminoGreedy){
                System.out.println(p.toString());
            }
            System.out.println("--------------------");
            System.out.println(" ");*/
            
        }
        
        
        return caminoGreedy;
    }
    
    /*
    Comprueba que un indice de ciudad esta ya estudiado
    */
    public boolean EstaCogido(ArrayList<Punto> camino, int indice){
        boolean esta = false;
        int i = 0;
        while(i < camino.size() && !esta){
            Punto aux = camino.get(i);
            if(aux.getIndice() == indice) esta = true;
            i++;
        }
        
        return esta;
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
