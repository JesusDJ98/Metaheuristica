package Algoritmos;

import Datos.Punto;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Jesus Delgado
 */
public class CHC_Corregir {
    
    public CHC_Corregir(){
    }
    /* Mutimodal, distancia hamming es el radio
    Penalizo inidividuos cercanos (Equilibrio, muy cercano o muy lejanos)
    */
    public ArrayList<Punto> Algoritmo(ArrayList<Punto> c, int semilla, int cantidad){
        //ArrayList<Punto> MejorIndividuo = new ArrayList<>();
        Random R = new Random(semilla);
        //int[][] distancias = MatrizDistancias(c);
        int t = 0;
        int L = c.size();
        int d = L/4;
        ArrayList<ArrayList<Punto>> Poblacion_inicial = GenerarPoblacion(c, R, cantidad);
        //System.out.println("Inicio: "+Poblacion_inicial.size());
        ArrayList<Double> costes = Fitness(Poblacion_inicial);
        int posMejor = SeleccionarMejorIndividuo(costes);
        ArrayList<Punto> MejorIndividuo = Poblacion_inicial.get(posMejor);
        double costeMejor = costes.get(posMejor);
        ArrayList<Punto> MejorPoblacionAnterior = (ArrayList<Punto>) MejorIndividuo.clone();
        
        ArrayList<ArrayList<Punto>> Poblacion_anterior = (ArrayList<ArrayList<Punto>>)Poblacion_inicial.clone();
        int NoMejora = 0;
        /*
        Numero de reinicializaciones: criterio de parada
        */
        while(NoMejora < 100){
            ArrayList<ArrayList<Punto>> C = Select(Poblacion_anterior, R);
            //System.out.println("C: "+C.size());
            ArrayList<ArrayList<Punto>> Cprima = Recombinar(C, d, R);
            //System.out.println("Cprima: "+Cprima.size());
            ArrayList<Double> costes_Cprima = Fitness(Cprima);
            ArrayList<ArrayList<Punto>> Poblacion = SelectR(Cprima, costes_Cprima, Poblacion_anterior);
            //System.out.println("Poblacion: "+Poblacion.size());
            
            
            if(Poblacion.equals(Poblacion_anterior)){
                d--;
                NoMejora++;
            }else{
                //NoMejora = 0;
                costes = Fitness(Poblacion);
                posMejor = SeleccionarMejorIndividuo(costes);
                ArrayList<Punto> MejorIndividuoAux = Poblacion.get(posMejor);
                double costeMejorAux = costes.get(posMejor);
                if(costeMejorAux < costeMejor){
                    costeMejor = costeMejorAux;
                    System.out.println("Coste Mejor: "+costeMejor);
                    MejorIndividuo = (ArrayList<Punto>) MejorIndividuoAux.clone();
                }
                //Actualizamos poblacion anterior
                Poblacion_anterior = (ArrayList<ArrayList<Punto>>)Poblacion.clone();
                MejorPoblacionAnterior = (ArrayList<Punto>)MejorIndividuoAux.clone();
                
            }
            if(d < 0){
                //Estudiar para ver cuanto de rapido reinicializa
                double r = 0.1;
                //d = (int)(r*(1-r)*L);
                d = L/4;
                Poblacion_anterior = Diverge(MejorPoblacionAnterior, R, cantidad);
                //System.out.println("Diverge: "+Poblacion_anterior.size());
            }
            
            t++;
        }
        System.out.print("Generacion: "+t+" --> ");
        return MejorIndividuo;
    }
    
    
    
    private ArrayList<ArrayList<Punto>> GenerarPoblacion(ArrayList<Punto>c, Random r, int cantidad){
        ArrayList<ArrayList<Punto>> Poblacion_Aleatoria = new ArrayList<>();
        for (int i = 0; i < cantidad; i++) {
            Poblacion_Aleatoria.add(GenerarSolucionAleatoria(c, r));
        }
        return Poblacion_Aleatoria;
    }
    private ArrayList<Punto> GenerarSolucionAleatoria(ArrayList<Punto> ciudades, Random r){
        ArrayList<Punto> recorrido = new ArrayList<>();
        ArrayList<Punto> c = (ArrayList<Punto>)ciudades.clone();
        
        while(!c.isEmpty()){
            Punto p = c.remove(r.nextInt(c.size()));
            recorrido.add(recorrido.size(),p);
        }
        
        return recorrido;
    }
    
    private ArrayList<Double> Fitness(ArrayList<ArrayList<Punto>> Poblacion){
        ArrayList<Double> fit = new ArrayList<>();
        for(ArrayList<Punto> p: Poblacion){
            fit.add(Evaluacion(p));
        }
        return fit;
    }
    
    /*
    Seleccion al individuo de menor coste
    */
    private int SeleccionarMejorIndividuo(ArrayList<Double> costes){
        double coste = costes.get(0);
        int indice = 0;
        for(int i = 1; i<costes.size(); i++){
            if(costes.get(i) < coste){
                indice = i;
                coste = costes.get(i);
            }
        }
        return indice;//*/
    }
    
    /*
    Devolvemos la poblacion en orden aleatorio
    */
    private ArrayList<ArrayList<Punto>> Select(ArrayList<ArrayList<Punto>> Poblacion, Random r){
        ArrayList<ArrayList<Punto>> C = new ArrayList<>();
        ArrayList<ArrayList<Punto>> Copy = (ArrayList<ArrayList<Punto>>) Poblacion.clone();
        
        while(Copy.size() > 0){
            C.add(Copy.remove(r.nextInt(Copy.size())));
        }
        
        return C;
    }
    
    private ArrayList<ArrayList<Punto>> Recombinar(ArrayList<ArrayList<Punto>> C, int d, Random r){
        ArrayList<ArrayList<Punto>> Hijos = new ArrayList<>();
        
        for(int i = 0; i < C.size(); i+=2){
            ArrayList<Punto> Padre1 = C.get(i);
            ArrayList<Punto> Padre2 = C.get(i+1);
            int distancia = Hamming(Padre1, Padre2);
            if(distancia/2 > d){
                //Cruce OX
                ArrayList<ArrayList<Punto>> Aux = Swap(Padre1, Padre2, distancia, r);
                for(ArrayList<Punto> p: Aux){
                    Hijos.add(p);
                }
            }//else los eliminamos
        }
        
        return Hijos;
    }
    /*
    Cuantos genes diferentes tienen entre ambos cromosomas
    */
    private int Hamming(ArrayList<Punto> Padre1, ArrayList<Punto> Padre2){
        int distancia = 0;
        
        for(int i = 0; i < Padre1.size(); i++){
            if(Padre1.get(i).getIndice() != Padre2.get(i).getIndice()){
                distancia++;
            }
        }
        
        return distancia;
    }
    /*
    Intercambiamos la mitad de los genes diferentes
    de manera aleatoria, generando un hijo por cada padre
    */
    private ArrayList<ArrayList<Punto>> Swap(ArrayList<Punto> Padre1, ArrayList<Punto> Padre2, int d, Random r){
        ArrayList<ArrayList<Punto>> Hijos = new ArrayList();
        int diferentes = 0;
        //Determinamos que genes mutaran
        ArrayList<Integer> GenesElegidos = new ArrayList<>();
        while(GenesElegidos.size() < d/2){
            for(int i = 0; i < Padre1.size(); i++){ //Recorremos todos los puntos(Genes)
                if(Padre1.get(i).getIndice() != Padre2.get(i).getIndice()){ //Gen diferente
                    double aleatorio = r.nextDouble();
                    if(aleatorio < 0.5 && !GenesElegidos.contains(i)){  //Lo elegimos
                        GenesElegidos.add(i);
                    }
                    if(GenesElegidos.size() == d/2) i = Padre1.size();  //Terminamos
                } 
            }
        }
        //Generamos los 2 hijos con los genes elegidos mutados 
        ArrayList<Punto> Hijo1 = new ArrayList<>();
        ArrayList<Punto> Hijo2 = new ArrayList<>();
        for(int i = 0; i < Padre1.size(); i++){
            
            if(Padre1.get(i).getIndice() != Padre2.get(i).getIndice()){
                
                if(GenesElegidos.contains(diferentes)){ //Gen que cambia
                    Hijo1.add(Padre2.get(i));
                    Hijo2.add(Padre1.get(i));
                }else{
                    Hijo1.add(Padre1.get(i));
                    Hijo2.add(Padre2.get(i));
                }
                
                diferentes++;
            }else{
                Hijo1.add(Padre1.get(i));
                Hijo2.add(Padre2.get(i));
            }
        }
        //Devolvemos los hijos
        Hijos.add(Hijo1);
        Hijos.add(Hijo2);
        return Hijos;
    }
    /*
    Elimino los peores elementos de la poblacion anterior
    y los sustituyo por los mejores de la poblacion candidata
    */
    private ArrayList<ArrayList<Punto>> SelectR(ArrayList<ArrayList<Punto>> Cprima, ArrayList<Double> Costeprima,
            ArrayList<ArrayList<Punto>> Poblacion_anterior){
        
        ArrayList<ArrayList<Punto>> Poblacion = (ArrayList<ArrayList<Punto>>)Poblacion_anterior.clone();
        ArrayList<ArrayList<Punto>> prima = (ArrayList<ArrayList<Punto>>)Cprima.clone();
        ArrayList<Double> cPri = (ArrayList<Double>) Costeprima.clone();
        
        while(prima.size() > 0){
            ArrayList<Double> cPob = Fitness(Poblacion);
            int peorP_1 = Peor(cPob);
            int mejorC = Mejor(cPri);
            Poblacion.set(peorP_1, prima.get(mejorC));
            //Los eliminamos de posibles candidatos
            prima.remove(mejorC);
            cPri.remove(mejorC);
        }
        
        
        return Poblacion;
    }
    
    private int Mejor(ArrayList<Double> Coste){
        double mejorC = Coste.get(0);
        int pos = 0;
        for(int i = 1; i < Coste.size(); i++){
            if(Coste.get(i) < mejorC){
                mejorC = Coste.get(i);
                pos = i;
            }
        }
        return pos;
    }
    private int Peor(ArrayList<Double> Coste){
        double mejorC = Coste.get(0);
        int pos = 0;
        for(int i = 1; i < Coste.size(); i++){
            if(Coste.get(i) > mejorC){
                mejorC = Coste.get(i);
                pos = i;
            }
        }
        return pos;
    }
    
    
    /*
    Usamos el mejor individuo como referencia
    lo copiamos y luego sus compatriotas seran el con un 35% de diferencia
    */
    private ArrayList<ArrayList<Punto>> Diverge(ArrayList<Punto> MejorPoblacionAnterior, Random r, int cantidad){
        ArrayList<ArrayList<Punto>> Poblacion = new ArrayList<>();
        Poblacion.add(MejorPoblacionAnterior);
        
        
        //Generamos los individuos que componen la poblacion
        for(int i = 1; i < cantidad; i++){
            //Determino que genes se modificaran
            ArrayList<Integer> GenesCambio = new ArrayList<>(); //Posicion de los genes que cambio
            ArrayList<Punto> GenesCambioRestante = new ArrayList<>(); //Genes que quedan por cambiar
            int max = (MejorPoblacionAnterior.size()*35)/100;
            System.out.println("Max: "+max);
            while(GenesCambio.size() < max){
                int pos = r.nextInt(MejorPoblacionAnterior.size());
                int indice = MejorPoblacionAnterior.get(pos).getIndice();
                if(!GenesCambio.contains(indice)){
                    GenesCambio.add(indice);    //Añado el inidice del gen a cambiar
                    GenesCambioRestante.add(MejorPoblacionAnterior.get(pos));   //Añado el gen de cambio
                }
            }
            System.out.println("Los genes que voy a cambiar: ");
            for(Integer ind: GenesCambio){
                System.out.print(ind+" - ");
            }System.out.println(" ");
            //Genero el individuo
            ArrayList<Punto> Individuo = new ArrayList<>();
            for(int j = 0; j < MejorPoblacionAnterior.size(); j++){
                System.out.println("Quedan por cambiar: "+GenesCambioRestante.size()+" - j: "+j+" - Indice: "+MejorPoblacionAnterior.get(j).getIndice());
                if(GenesCambio.contains(MejorPoblacionAnterior.get(j).getIndice())){    //Es un gen que debo cambiar
                    int posElegida = r.nextInt(GenesCambioRestante.size()); //Elijo un gen aleatorio que se debe modificar
                    Individuo.add(GenesCambioRestante.remove(posElegida));
                    //GenesCambioRestante.remove(posElegida);                 //La elimino como candidata
                }else Individuo.add(MejorPoblacionAnterior.get(j));
                
            }
            Poblacion.add(Individuo);
        }
        
        return Poblacion;
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
