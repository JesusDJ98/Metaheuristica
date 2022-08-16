package Algoritmos;

import Datos.Punto;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Jesus Delgado
 */
public class CHC_Corregido {
    
    public CHC_Corregido(){
    }
    
    public ArrayList<Punto> Algoritmo(ArrayList<Punto> c, int semilla, int cantidad){
        Random R = new Random(semilla);
        ArrayList<ArrayList<Punto>> Poblacion_anterior = GenerarPoblacion(c, R, cantidad);
        ArrayList<Double> costes = Fitness(Poblacion_anterior);
        
        int t = 0;
        int L = c.size();
        int d = L/4;
        int Reinicializaciones = 0;
        
        /*System.out.println("Generacion\tMinimo\tMedia\tMaximo");
        System.out.print("    "+t+"   \t");
        Estudio(costes);//*/
        
        while(Reinicializaciones < 10){
            t++;
            //System.out.print("    "+t+"   \t");
            ArrayList<ArrayList<Punto>> C = SelectR(Poblacion_anterior, R);
            ArrayList<ArrayList<Punto>> Cprima = Recombinar(C, d, R);
            ArrayList<Double> costes_Cprima = Fitness(Cprima);
            ArrayList<ArrayList<Punto>> Poblacion = SelectS(Cprima, costes_Cprima, Poblacion_anterior, costes);
            costes = Fitness(Poblacion);
            if(Poblacion.equals(Poblacion_anterior)){
                d--;
            }
            if(d < 0){  //Reinicializacion
                //x = true;
                Reinicializaciones++;
                d = L/4;
                int pos = SeleccionarMejorIndividuo(costes);
                Poblacion_anterior = Diverge2(Poblacion.get(pos), R, cantidad);
                costes = Fitness(Poblacion_anterior);
            }else Poblacion_anterior = (ArrayList<ArrayList<Punto>>) Poblacion.clone();
            //Estudio(costes);
            
        }
        int pos = SeleccionarMejorIndividuo(costes);
        System.out.print("Generacion: "+t+" --> ");
        return Poblacion_anterior.get(pos);
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
            recorrido.add(p);
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
        return indice;
    }
    
    /*
    Devolvemos la poblacion en orden aleatorio
    */
    private ArrayList<ArrayList<Punto>> SelectR(ArrayList<ArrayList<Punto>> Poblacion, Random r){
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
                ArrayList<ArrayList<Punto>> Aux = Cruce(Padre1, Padre2, r, distancia/2);
                for(ArrayList<Punto> p: Aux){
                    Hijos.add(p);
                }
            }//else los eliminamos
        }
        
        return Hijos;
    }
    /*
    Cuantos genes diferentes tienen entre ambos cromosomas(individuos)
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
    Cruzamos ambos progenitores para dar dos individuos de descendencia
    cada uno se parece mas a uno de los progenitores
    */
    private ArrayList<ArrayList<Punto>> Cruce(ArrayList<Punto> Padre1, ArrayList<Punto> Padre2, Random r, int cantidad){
        ArrayList<ArrayList<Punto>> Hijos = new ArrayList<>();
        int inicio = r.nextInt(Padre1.size());
        int fin = (inicio + cantidad)%Padre1.size();
        //System.out.println("El cruce del padre menos predominante va: "+inicio+"-"+fin+" --> deben ser "+cantidad);
        //Relleno la sublista
        ArrayList<Punto> Sublista1 = new ArrayList<>();
        ArrayList<Punto> Sublista2 = new ArrayList<>();
        for(int i = 0; i < cantidad; i++){
            int pos = (i+inicio)%Padre1.size();
            Punto aux2 = Padre1.get(pos);
            Punto aux1 = Padre2.get(pos);
            Sublista1.add(aux1);
            Sublista2.add(aux2);
        }
        //System.out.println("Tamanio sublista: "+Sublista1.size()+"-"+Sublista2.size());
        
        //Relleno los hijos
        ArrayList<Punto> Hijo1 = new ArrayList<>(); //Hereda la mayoria de genes del primer progenitor
        ArrayList<Punto> Hijo2 = new ArrayList<>(); //Hereda la mayoria de genes del segundo progenitor
        
        int diferencia = Padre1.size()-inicio;   //Util para la ciclica
        int empieza = 0;
        for(int i = 0; i < Padre1.size(); i++){ //Estudio todos los genes de cada padre para cada hijo
            if(fin < inicio){   
                if(i == inicio){    //Relleno el hijo con los valores de la sublista del padre menos predominante
                    //System.out.println("Aniado la sublista 1: "+i);
                    inicio--;   //Para que no repita
                    i--;        //Para que no se salte ningun valor del padre predominante
                    for(int j = 0; j < fin; j++){
                        Hijo1.add(Sublista1.get(j+diferencia));
                        Hijo2.add(Sublista2.get(j+diferencia));
                    }
                }else if(i == empieza){
                    //System.out.println("Aniado la sublista 2: "+i);
                    i--;
                    empieza--;
                    for(int j = 0; j < diferencia; j++){
                        Hijo1.add(Sublista1.get(j));
                        Hijo2.add(Sublista2.get(j));
                    }
                }else{  //Rellenamos con el padre predominante
                    //System.out.println("Aniado padre predominate 1: "+i);
                    if(!Sublista1.contains(Padre1.get(i))) Hijo1.add(Padre1.get(i));
                    if(!Sublista2.contains(Padre2.get(i))) Hijo2.add(Padre2.get(i));
                }
            }else{
                if(i == inicio){    //Rellenamos con los valores de la sublista
                    //System.out.println("Aniado la sublista 3: "+i);
                    inicio--;
                    i--;
                    for(int j = 0; j < cantidad; j++){
                        Hijo1.add(Sublista1.get(j));
                        Hijo2.add(Sublista2.get(j));
                    }
                }else{
                    //System.out.println("Aniado padre predominate 3: "+i);
                    if(!Sublista1.contains(Padre1.get(i))) Hijo1.add(Padre1.get(i));
                    if(!Sublista2.contains(Padre2.get(i))) Hijo2.add(Padre2.get(i));
                }
            }
            //System.out.println("Tamanio Hijos: "+Hijo1.size()+"-"+Hijo2.size());
        }
        Hijos.add(Hijo1);
        Hijos.add(Hijo2);
        
        return Hijos;
    }
    
    /*
    Elimino los peores elementos de la poblacion anterior
    y los sustituyo por los mejores de la poblacion candidata
    */
    private ArrayList<ArrayList<Punto>> SelectS(ArrayList<ArrayList<Punto>> Cprima, ArrayList<Double> Costeprima,
            ArrayList<ArrayList<Punto>> Poblacion_anterior, ArrayList<Double> CostePobAnt){
        
        ArrayList<ArrayList<Punto>> Poblacion = (ArrayList<ArrayList<Punto>>)Poblacion_anterior.clone();
        ArrayList<ArrayList<Punto>> prima = (ArrayList<ArrayList<Punto>>)Cprima.clone();
        ArrayList<Double> cPri = (ArrayList<Double>) Costeprima.clone();
        ArrayList<Double> cPob = (ArrayList<Double>) CostePobAnt.clone();
        //System.out.println("SelectR: "+prima.size());
        while(prima.size() > 0){
            int peorP_1 = Peor(cPob);
            int mejorC = Mejor(cPri);
            Poblacion.set(peorP_1, prima.get(mejorC));
            //Los eliminamos de posibles candidatos
            prima.remove(mejorC);
            cPri.remove(mejorC);
            cPob = Fitness(Poblacion);
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
        //System.out.println("Diverge");
        
        //Generamos los individuos que componen la poblacion
        for(int i = 1; i < cantidad; i++){
            //Determino que genes se modificaran
            ArrayList<Integer> GenesCambio = new ArrayList<>(); //Posicion de los genes que cambio
            ArrayList<Punto> GenesCambioRestante = new ArrayList<>(); //Genes que quedan por cambiar
            int max = (MejorPoblacionAnterior.size()*35)/100;
            //System.out.println("Max: "+max);
            while(GenesCambio.size() < max){
                int pos = r.nextInt(MejorPoblacionAnterior.size());
                int indice = MejorPoblacionAnterior.get(pos).getIndice();
                if(!GenesCambio.contains(indice)){
                    GenesCambio.add(indice);    //Añado el inidice del gen a cambiar
                    GenesCambioRestante.add(MejorPoblacionAnterior.get(pos));   //Añado el gen de cambio
                }
            }
            
            //Genero el individuo
            ArrayList<Punto> Individuo = new ArrayList<>();
            for(int j = 0; j < MejorPoblacionAnterior.size(); j++){
                //System.out.println("Quedan por cambiar: "+GenesCambioRestante.size()+" - j: "+j+" - Indice: "+MejorPoblacionAnterior.get(j).getIndice());
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
    
    
    private ArrayList<ArrayList<Punto>> Diverge2(ArrayList<Punto> MejorPoblacionAnterior, Random r, int cantidad){
        ArrayList<ArrayList<Punto>> Poblacion = new ArrayList<>();
        Poblacion.add(MejorPoblacionAnterior);
        int max = (MejorPoblacionAnterior.size()*35)/100;
        
        for(int i = 1; i < cantidad; i++){
            ArrayList<Punto> C = Mutacion(MejorPoblacionAnterior, r, max);
            Poblacion.add(C);
        }
        
        return Poblacion;
    }
    
    
    /*
    Cada individuo tiene una probabilidad x de mutar
    */
    private ArrayList<Punto> Mutacion(ArrayList<Punto> Hijo, Random r, int tama){
        ArrayList<ArrayList<Punto>> Mutados = new ArrayList<>();
        
        ArrayList<Punto> S = (ArrayList<Punto>) Hijo.clone();
        
        ArrayList<Punto> cambio = new ArrayList<>();
        ArrayList<Punto> sublista = new ArrayList<>();
        int inicio = r.nextInt(S.size());
        int fin = (tama+inicio)%S.size();//Fija, circular
        
        //Rellenamos sublista
        for(int i = 0; i < S.size(); i++){
            if( ( (inicio > fin) && (i >= inicio || i < fin) ) ||  
            ( (inicio < fin) && (i >= inicio && i < fin) ) ){
                sublista.add(S.get(i));
            }
        }
        //System.out.println("Tamanio sublista: "+sublista.size());

        //Desordenamos la sublista
        for(int i = 0; i < sublista.size(); i++){   //Hacemos n cambios
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

        //Recomponemos la solucion
        int diferencia = S.size()-inicio;
        //System.out.println("La diferencia es "+diferencia);
        for(int i = 0; i < S.size(); i++){

            if( (inicio < fin) && (i >= inicio && i < fin) ){
                //System.out.println("La i es: "+i+" y deberia ser: "+(i-inicio));
                cambio.add(sublista.get(i-inicio));
            }else if(inicio > fin) {
                if(i >= inicio){
                    //System.out.print("La i es: "+i+" y deberia ser: "+(i-inicio));
                    cambio.add(sublista.get(i-inicio));
                }else if(i < fin){
                    //System.out.print("La i es: "+i+" y deberia ser: "+(i+diferencia));
                    cambio.add(sublista.get(i+diferencia));
                }else cambio.add(S.get(i));

            }else cambio.add(S.get(i));
        }
        
        return cambio;
    }
    
    
    private void Estudio(ArrayList<Double> Datos){
        double min = Datos.get(0);
        double max = Datos.get(0);
        double acumulado = min;
        for(int i = 1; i < Datos.size(); i++){
            double v = Datos.get(i);
            if(v < min) min = v;
            if(v > max) max = v;
            acumulado += v;
        }
        int minimo =(int)min;
        int maximo = (int)max;
        //System.out.println(minimo+"(min) "+maximo+"(max)\t"+acumulado+"/"+Datos.size()+"= "+(Math.round(acumulado/Datos.size()))+"(Media)");
        System.out.println(minimo+"\t"+(Math.round(acumulado/Datos.size()))+"\t"+maximo);
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
