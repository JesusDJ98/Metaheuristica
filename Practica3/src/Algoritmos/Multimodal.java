package Algoritmos;

//import Algoritmos.*;
import Datos.Punto;
import java.util.ArrayList;
import java.util.Random;
import static practica3.Practica3.Evaluacion;

/**
 *
 * @author Jesus Delgado
 */
public class Multimodal {
    
    public Multimodal(){
    }
    
    public ArrayList<Punto> Algoritmo(ArrayList<Punto> c, int semilla, int cantidad){
        int N = c.size();
        Random R = new Random(semilla);
        ArrayList<ArrayList<Punto>> Poblacion = GenerarPoblacionAleatoria(c, R, cantidad);
        ArrayList<Double> costes = Fitness(Poblacion);
        int t = 0;
        int NumeClearing = 0;
        int Cant_padres = 2;
        double prob_Muta = 0.05;                        //Probabilidad de que cada hijo mute
        double prob_Cruce = 0.8;                        //Probabilidad de que crucen
        int GenesHeredados = (int)(N*0.8);              //Un 20% de un padre y un 80% del otro
        int S = N/8;                                    //Lo cambios sean muy(pequenio) o poco(grande) pronunciados
        int k = (int)(Poblacion.size()*0.1);            //10% de poblacion --> Torneo
        int radio = (int)(N*0.2);                       //20% de genes del individuo
        int CantIndi = (int)(Poblacion.size()*0.05);    //5% de poblacion --> Individuos por nichos
        /*System.out.println("Generacion\tMinimo\tMedia\tMaximo");
        System.out.print("    "+t+"   \t");
        Estudio(costes);//*/
        int sinMejoras = 0;
        //sinMejoras < 1000 --> 649612 con 32480 --> 915(18individuos)
        ArrayList<ArrayList<Punto>> ultimoClearing = new ArrayList<>();
        int contador = 1;
        while(NumeClearing < 1000/*nichos[0] > 6*/){
            t++;
            
            if(contador%10 == 0){   //Cada cantidad iteraciones realizamos el clearing --> Suficiente para rellenar y reemplazar
                NumeClearing++;
                ultimoClearing = Clearing(Poblacion, costes, radio, CantIndi);
                Poblacion = (ArrayList<ArrayList<Punto>>) ultimoClearing.clone();
                //System.out.println("clearing "+NumeClearing+"\t"+Poblacion.size());
                contador = 1;
            }//*/
            
            boolean[] mejora = new boolean[1];
            ArrayList<ArrayList<Punto>> Padres = SeleccionarPadres(Poblacion, costes, Cant_padres, R, k);  //Seleccionamos 2 padres
            ArrayList<ArrayList<Punto>> Hijos = Cruce(Padres, R, GenesHeredados, prob_Cruce);   //Generamos dos hijos
            Hijos = Mutacion(Hijos, R, S, prob_Muta);   //Mutan con una probabilidad de prob_Muta

            //Debemos rellenar Pprima hasta tener una poblacion de tamanio cantidad
            boolean entro = false;
            for(int i = 0; i < Hijos.size(); i++){
                if(Poblacion.size() < cantidad){
                    //double media = Media(costes);
                    //double costeH = Evaluacion(Hijos.get(i));
                    //if(costeH < media*1.05) Poblacion.add(Hijos.get(i));
                    Poblacion.add(Hijos.get(i));
                    entro = true;
                    //sinMejoras++;
                }else{
                    if(entro) costes = Fitness(Poblacion);
                    Poblacion = ReemplazoTorneo(Poblacion, Hijos.get(i), costes, k, Cant_padres, R, mejora);
                    /*if(!mejora[0]) sinMejoras++;
                    else sinMejoras = 0;//*/
                }
                
            }
            if(Poblacion.size() == cantidad) contador++;
            if(sinMejoras > max) max = sinMejoras;
            costes = Fitness(Poblacion);
            /*System.out.print("    "+t+"vs"+sinMejoras+"vs"+NumeClearing+" - tam: "+Poblacion.size()+" - Max:"+max+"   \t");
            Estudio(costes);//*/
        }
        System.out.print("Generacion: "+t+" con "+NumeClearing+" numero de clearing --> ");
        return Poblacion.get(SeleccionarMejorIndividuo(costes));
    }
    
    
    
    private ArrayList<ArrayList<Punto>> GenerarPoblacionAleatoria(ArrayList<Punto>c, Random r, int cantidad){
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
    
    /*
    Calcula el coste de cada individuo de una poblacion
    */
    private ArrayList<Double> Fitness(ArrayList<ArrayList<Punto>> Poblacion){
        ArrayList<Double> fit = new ArrayList<>();
        for(ArrayList<Punto> p: Poblacion){
            fit.add(Evaluacion(p));
        }
        return fit;
    }
    
    /*
    Clearing
    Obtiene los individuos(Kapa) dominantes de cada nicho que estan a una distancia (omega)
    */
    private ArrayList<ArrayList<Punto>> Clearing(ArrayList<ArrayList<Punto>> Poblacion, ArrayList<Double> Costes, 
            int omega, int kappa){
        
        ArrayList<ArrayList<Punto>> Aux = OrdenarDesc(Poblacion, Costes);
        ArrayList<Double> costes = Fitness(Aux);
        ArrayList<ArrayList<Punto>> Pprima = new ArrayList<>();
        for(int j = 0; j < Aux.size()-1; j++){
            if(costes.get(j) != 0.0){
                int NumGanadores = 1;   //Nuevo nicho
                for(int i = j+1; i < Aux.size(); i++){
                    int disH = Hamming(Aux.get(j), Aux.get(i));
                    if(disH < omega){ //Es igual al mejor, lo intentamos coger
                        if(NumGanadores < kappa) NumGanadores++;
                        else costes.set(i, 0.0);
                    }
                }
            }
        }
        
        for(int i = 0; i < Aux.size(); i++){
            if(costes.get(i) != 0.0) Pprima.add(Aux.get(i));
        }
        
        return Pprima;
    }
    /*
    Ordena de mayor a menor la poblacion segun su coste
    */
    private ArrayList<ArrayList<Punto>> OrdenarDesc(ArrayList<ArrayList<Punto>> P, ArrayList<Double> Costes){
        ArrayList<ArrayList<Punto>> Aux = (ArrayList<ArrayList<Punto>>) P.clone();
        ArrayList<Double> costes = (ArrayList<Double>) Costes.clone();
        ArrayList<ArrayList<Punto>> Ordenado = new ArrayList<>();
        
        while(Aux.size() > 0){
            int pos = SeleccionarMejorIndividuo(costes);
            Ordenado.add(Aux.remove(pos));
            costes.remove(pos);
        }
        
        return Ordenado;
    }
    
        
    /*
    Seleccionamos una cantidad determinada de padres
    Mediante el metodo de torneo
    */
    private ArrayList<ArrayList<Punto>> SeleccionarPadres(ArrayList<ArrayList<Punto>> Poblacion, ArrayList<Double> Fitnes, int cantidad, Random r, int k){
        ArrayList<ArrayList<Punto>> Padres = new ArrayList<>();
        
        ArrayList<Integer> Indices = new ArrayList<>();
        for(int i = 0; i < cantidad; i++){
            //Seleccionamos los candidatos
            ArrayList<ArrayList<Punto>> Candidatos = new ArrayList<>();
            ArrayList<Double> Costes = new ArrayList<>();
            ArrayList<Integer> posiciones = new ArrayList<>();
            for (int j = 0; j < k; j++) {
                int pos = r.nextInt(Poblacion.size());
                Candidatos.add(Poblacion.get(pos));
                Costes.add(Fitnes.get(pos));
                posiciones.add(pos);
            }
            //Nos quedamos con el mejor de cada torneo
            int ind = SeleccionarMejorIndividuo(Costes);
            ArrayList<Punto> aux = (ArrayList<Punto>) Candidatos.get(ind).clone();
            if(Indices.isEmpty() || (Indices.size() > 0 && !Indices.contains(posiciones.get(ind)))){
                Padres.add(aux);
                Indices.add(posiciones.get(ind));
            }else i--;
        }
        return Padres;
    }
    
    /*
    Cruzamos ambos progenitores para dar dos individuos de descendencia
    cada uno se parece mas a uno de los progenitores
    */
    private ArrayList<ArrayList<Punto>> Cruce(ArrayList<ArrayList<Punto>> Padres, Random r, int cantidad, double prob){
        ArrayList<ArrayList<Punto>> Hijos = new ArrayList<>();
        int tam = Padres.get(0).size();
        for(int i = 0; i < 2; i++){
            ArrayList<Punto> H = (ArrayList<Punto>) Padres.get(i).clone();
            if(r.nextDouble() < prob){
                int inicio = r.nextInt(tam);
                int fin = (inicio + cantidad)%tam;
                //ArrayList<Punto> H = (ArrayList<Punto>) Padres.get(i).clone();
                ArrayList<Punto> SubL = new ArrayList<>();
                int ind = inicio;
                while(ind != fin){
                    SubL.add(H.get(ind));
                    ind = (ind+1)%tam;
                }
                
                int otroIndice = 0;
                if(i == 0) otroIndice = 1;
                ArrayList<Punto> OtroPadre = (ArrayList<Punto>) Padres.get(otroIndice).clone();
                int j = fin;
                int k = fin;
                while(j != inicio){
                    boolean encontrado = false;
                    do{
                        if(!SubL.contains(OtroPadre.get(k))){
                            encontrado = true;
                            H.set(j, OtroPadre.get(k));
                        }
                        k= (k+1)%tam;
                    }while(k != fin && !encontrado);
                    j = (j+1)%tam;
                }
            }//else se queda copiado 
            
            Hijos.add((ArrayList<Punto>)H.clone());
        }
               
        return Hijos;
    }
    
    /*
    Cada individuo tiene una probabilidad x de mutar
    */
    private ArrayList<ArrayList<Punto>> Mutacion(ArrayList<ArrayList<Punto>> Hijos, Random r, int tama, double x){
        ArrayList<ArrayList<Punto>> Mutados = new ArrayList<>();
        for(int j = 0; j< Hijos.size(); j++){
            ArrayList<Punto> S = Hijos.get(j);
            double prob = r.nextDouble();
            if(prob < x){
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
                }
                
                //Recomponemos la solucion
                int diferencia = S.size()-inicio;
                for(int i = 0; i < S.size(); i++){
                    if( (inicio < fin) && (i >= inicio && i < fin) ) cambio.add(sublista.get(i-inicio));
                    else if(inicio > fin) {
                        if(i >= inicio) cambio.add(sublista.get(i-inicio));
                        else if(i < fin) cambio.add(sublista.get(i+diferencia));
                        else cambio.add(S.get(i));
                        
                    }else cambio.add(S.get(i));
                }
                Mutados.add(cambio);
            }else Mutados.add(S);
        }
        
        return Mutados;
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
    Cuantos genes diferentes tienen entre ambos cromosomas
    */
    private int Hamming(ArrayList<Punto> Padre1, ArrayList<Punto> Padre2){
        int distancia = 0;
        
        for(int i = 0; i < Padre1.size(); i++){
            //System.out.println(Padre1.get(i).getIndice()+"vs"+Padre2.get(i).getIndice());
            if(Padre1.get(i).getIndice() != Padre2.get(i).getIndice()){
                distancia++;
            }
        }
        
        return distancia;
    }
    
    /* Metodo de torneo
    Reemplazamos cada Candidato elegido en el torneo por un hijo siempre
    que el hijo se mejor que este candidato
    */
    private ArrayList<ArrayList<Punto>> ReemplazoTorneo(ArrayList<ArrayList<Punto>> Poblacion, 
            ArrayList<Punto>HijoMutados, ArrayList<Double> Fitnes, int k, int padres, Random r, boolean[] mejora){
        
        ArrayList<ArrayList<Punto>>Padres = Torneo(Poblacion, Fitnes,  k, padres, r);
        double costeHijo = Evaluacion(HijoMutados);
        ArrayList<ArrayList<Punto>> NuevaGeneracion = (ArrayList<ArrayList<Punto>>)Poblacion.clone();
        int posP = SeleccionarPeorIndividuo(Padres, Fitness(Padres));
        double costePadre = Evaluacion(Padres.get(posP));

        if(costeHijo < costePadre){
            mejora[0] = true;
            int pos = Poblacion.indexOf(Padres.get(posP));
            NuevaGeneracion.set(pos, HijoMutados);
        }
        return NuevaGeneracion;
    }
    
    
    /*
    Seleccionamos una cantidad determinada de Candidatos a eliminar
    Mediante el metodo de torneo
    */
    private ArrayList<ArrayList<Punto>> Torneo(ArrayList<ArrayList<Punto>> Poblacion, ArrayList<Double> Fitnes, int k, int cantidad, Random r){
        ArrayList<ArrayList<Punto>> Padres = new ArrayList<>();
        
        ArrayList<Integer> Indices = new ArrayList<>();
        for(int i = 0; i < cantidad; i++){
            //Seleccionamos los candidatos
            ArrayList<ArrayList<Punto>> Candidatos = new ArrayList<>();
            ArrayList<Double> Costes = new ArrayList<>();
            ArrayList<Integer> posiciones = new ArrayList<>();
            for (int j = 0; j < k; j++) {
                int pos = r.nextInt(Poblacion.size());
                Candidatos.add(Poblacion.get(pos));
                Costes.add(Fitnes.get(pos));
                posiciones.add(pos);
            }
            //Nos quedamos con el peor de cada torneo
            int ind = SeleccionarPeorIndividuo(Candidatos, Costes);
            ArrayList<Punto> aux = (ArrayList<Punto>) Candidatos.get(ind).clone();
            if(Indices.isEmpty() || (Indices.size() > 0 && !Indices.contains(posiciones.get(ind)))){
                Padres.add(aux);
                Indices.add(posiciones.get(ind));
            }else i--;
            
        }        
        //System.out.println("Cambios: "+Evaluacion(Padres.get(0))+" : "+Poblacion.indexOf(Padres.get(0))+"-"+Evaluacion(Padres.get(1))+" : "+Poblacion.indexOf(Padres.get(1))+" ");
        return Padres;
    }
    /*
    Seleccion al individuo de mayor coste
    */
    private int SeleccionarPeorIndividuo(ArrayList<ArrayList<Punto>> Poblacion, ArrayList<Double> Fitnes){
        //System.out.println("Seleccion peor candidato");
        double max = Fitnes.get(0);
        int indice = 0;
        for(int i = 1; i<Poblacion.size(); i++){
            
            double coste_aux = Evaluacion(Poblacion.get(i));
            //System.out.println(i+": "+Fitnes.get(i)+"-"+coste_aux+" --> "+max+" : "+indice);
            if(coste_aux > max){
                indice = i;
                max = coste_aux;
            }
        }
        return indice;
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
    
    private double Media(ArrayList<Double> Datos){
        double acumulado = 0;
        for(double d: Datos){
            acumulado += d;
        }
        acumulado = Math.round(acumulado/Datos.size());
        return acumulado;
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
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    /*
    Cruzamos ambos progenitores para dar dos individuos de descendencia
    cada uno se parece mas a uno de los progenitores
    */
    private ArrayList<ArrayList<Punto>> Cruce2(ArrayList<ArrayList<Punto>> Padres, Random r, int cantidad, double prob){
        ArrayList<ArrayList<Punto>> Hijos = new ArrayList<>();
        //Hacerlo circular, sino es darle prioridad a los genes del final
        //Un 80% del cromosoma padre
        
        if(r.nextDouble() < prob){
            int inicio = r.nextInt(Padres.get(0).size());
            int fin = (inicio + cantidad)%Padres.get(0).size(); //Cojo un 20% del cromosoma
            //System.out.println("El cruce del padre menos predominante va: "+inicio+"-"+fin+" --> deben ser "+cantidad);
            //Relleno la sublista
            ArrayList<Punto> Sublista1 = new ArrayList<>();
            ArrayList<Punto> Sublista2 = new ArrayList<>();
            for(int i = 0; i < cantidad; i++){
                int pos = (i+inicio)%Padres.get(0).size();
                Punto aux2 = Padres.get(0).get(pos);
                Punto aux1 = Padres.get(1).get(pos);
                Sublista1.add(aux1);
                Sublista2.add(aux2);
            }
            //System.out.println("Tamanio sublista: "+Sublista1.size()+"-"+Sublista2.size());

            //Relleno los hijos
            ArrayList<Punto> Hijo1 = new ArrayList<>(); //Hereda el 80% del primer progenitor
            ArrayList<Punto> Hijo2 = new ArrayList<>(); //Hereda el 80% del segundo progenitor

            int diferencia = Padres.get(0).size()-inicio;   //Util para la ciclica
            int empieza = 0;
            //System.out.print(" Tam: "+Padres.get(0).size()+" - ");
            for(int i = 0; i < Padres.get(0).size(); i++){
                if(fin < inicio){
                    if(i == inicio){
                        //System.out.println("Aniado la sublista 1: "+i);
                        inicio--;   //Para que no repita
                        i--;        //Para que no se salte ningun valor
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
                        if(!Sublista1.contains(Padres.get(0).get(i))) Hijo1.add(Padres.get(0).get(i));
                        if(!Sublista2.contains(Padres.get(1).get(i))) Hijo2.add(Padres.get(1).get(i));
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
                        if(!Sublista1.contains(Padres.get(0).get(i))) Hijo1.add(Padres.get(0).get(i));
                        if(!Sublista2.contains(Padres.get(1).get(i))) Hijo2.add(Padres.get(1).get(i));
                    }
                }

            }
            //System.out.print("  Tamanio Hijos: "+Hijo1.size()+"-"+Hijo2.size()+"  - ");
            Hijos.add(Hijo1);
            Hijos.add(Hijo2);
        }else{
            Hijos.add((ArrayList<Punto>)Padres.get(0).clone());
            Hijos.add((ArrayList<Punto>)Padres.get(1).clone());
        }
        
        return Hijos;
    }
    
}
