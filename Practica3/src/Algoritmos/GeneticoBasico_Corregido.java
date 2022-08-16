package Algoritmos;

import Datos.Punto;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Jesus Delgado
 */
public class GeneticoBasico_Corregido {
    
    public GeneticoBasico_Corregido(){
    }
    
    public ArrayList<Punto> Algoritmo(ArrayList<Punto> c, int semilla, int cantidad){
        Random r = new Random(semilla);
        //Inicializar Poblacion
        ArrayList<ArrayList<Punto>> Poblacion_inicial = GenerarPoblacion(c, r, cantidad);
        //EvaluarPoblacion
        ArrayList<Double> Fitnes = Fitness(Poblacion_inicial);
                
        //Estacionario
        ArrayList<ArrayList<Punto>> Poblacion = (ArrayList<ArrayList<Punto>>)Poblacion_inicial.clone();
        int t = 0;
        int SinMejoras = 0;
        int Cant_padres = 2;                                        //Seleccionamos 2 padres
        int S = Poblacion.get(0).size()/8;                          //Lo cambios sean muy(pequenio) o poco(grande) pronunciados
        double prob_Muta = 0.05;                                    //Probabilidad de que cad hijo mute
        int GenesHeredados = (int)(Poblacion.get(0).size()*0.8);    //Un 20% de un padre y un 80% del otro
        int k = (int)(Poblacion.size()*0.1);                        //10% de poblacion
        System.out.println("Generacion\tMinimo\tMedia\tMaximo");
        System.out.print("    "+t+"   \t");
        Estudio(Fitnes);//*/
        while(SinMejoras < 1000){
            t++;
            
            ArrayList<ArrayList<Punto>> Padres = SeleccionarPadres(Poblacion, Fitnes, Cant_padres, r, k);  
            ArrayList<ArrayList<Punto>> Hijos = Cruce(Padres, r, GenesHeredados);   //Generamos dos hijos
            ArrayList<ArrayList<Punto>> HijosMutados = Mutacion(Hijos, r, S,prob_Muta);            
                        
            boolean[] mejora = new boolean[1];
            //Poblacion = Reemplazo(Poblacion, Padres, HijosMutados, mejora); //Crowding deterministico --> Solo cambia al padre si es mejor
            Poblacion = ReemplazoTorneo(Poblacion, HijosMutados, Fitnes, r, mejora);
            Fitnes = Fitness(Poblacion);
            
            System.out.print("    "+t+"   \t");
            Estudio(Fitnes);
            
            if(mejora[0]) SinMejoras = 0;
            else SinMejoras++;
        }
        //Devolvemos el mejor individuo de la poblacion
        System.out.print("Generacion "+t+" --> ");
        int pos = SeleccionarMejorIndividuo(Poblacion, Fitnes);
        ArrayList<Punto> MejorIndividuo = Poblacion.get(pos);
        
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
    
    /*  
    La frecuencia con la que la misma ciudad aparece en la misma posicion 
    Memoria --> Filas(Ciudades) - Columnas(Posicion)
                Pos1    Pos2    Pos3 ....  PosN
    Ciudad1      2       4       6
    Ciudad2      0       10      
    */
    private int[][] Medidas(ArrayList<ArrayList<Punto>> Poblacion, int n){
        int[][] memoria = new int[n][n];
        for(ArrayList<Punto> p: Poblacion){ //Estudiamos la poblacion
            for(int i = 0; i < p.size(); i++){  //Cada uno de los individuos
                int indice = p.get(i).getIndice()-1;
                memoria[indice][i]++;   
            }
        }
        
        return memoria;
    }
    
    /*
    Evalua el coste de cada uno de los individuos de la poblacion
    */
    private ArrayList<Double> Fitness(ArrayList<ArrayList<Punto>> Poblacion){
        ArrayList<Double> fit = new ArrayList<>();
        for(ArrayList<Punto> p: Poblacion){
            fit.add(Evaluacion(p));
        }
        return fit;
    }
    
    /*
    Seleccionamos una cantidad determinada de padres
    Mediante el metodo de torneo
    */
    private ArrayList<ArrayList<Punto>> SeleccionarPadres(ArrayList<ArrayList<Punto>> Poblacion, ArrayList<Double> Fitnes, int cantidad, Random r, int k){
        ArrayList<ArrayList<Punto>> Padres = new ArrayList<>();
        
        ArrayList<Integer> Indices = new ArrayList<>();
        //Seleccionamos un 10% de la poblacion como candidatos para el torneo
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
            int ind = SeleccionarMejorIndividuo(Candidatos, Costes);
            ArrayList<Punto> aux = (ArrayList<Punto>) Candidatos.get(ind).clone();
            if(Indices.isEmpty() || (Indices.size() > 0 && !Indices.contains(posiciones.get(ind)))){
                Padres.add(aux);
                Indices.add(posiciones.get(ind));
            }else i--;
            //Padres.add(Candidatos.get(SeleccionarMejorIndividuo(Candidatos, Costes)));
        }
        //System.out.println("\nPadres: "+Evaluacion(Padres.get(0))+" : "+Poblacion.indexOf(Padres.get(0))+"-"+Evaluacion(Padres.get(1))+" : "+Poblacion.indexOf(Padres.get(1))+" ");

        
        return Padres;
    }
    /*
    Seleccion al individuo de menor coste
    */
    private int SeleccionarMejorIndividuo(ArrayList<ArrayList<Punto>> Poblacion, ArrayList<Double> Fitnes){
        double min = Fitnes.get(0);
        int indice = 0;
        for(int i = 1; i<Poblacion.size(); i++){
            double coste_aux = Evaluacion(Poblacion.get(i));
            if(coste_aux < min){
                indice = i;
                min = coste_aux;
            }
        }
        return indice;
    }
    
    /*
    Cruzamos ambos progenitores para dar dos individuos de descendencia
    cada uno se parece mas a uno de los progenitores
    */
    private ArrayList<ArrayList<Punto>> Cruce(ArrayList<ArrayList<Punto>> Padres, Random r, int cantidad){
        ArrayList<ArrayList<Punto>> Hijos = new ArrayList<>();
        //Hacerlo circular, sino es darle prioridad a los genes del final
        //Un 80% del cromosoma padre
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
                //System.out.print(" "+j+" Mutamos -");
                ArrayList<Punto> cambio = new ArrayList<>();
                ArrayList<Punto> sublista = new ArrayList<>();
                int inicio = r.nextInt(S.size());
                int fin = (tama+inicio)%S.size();//Fija, circular
                //System.out.println("Tamaño sublista max("+tama+") real ("+inicio+","+fin+") -- "+(fin-inicio));

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
                Mutados.add(cambio);
            }else{
                //System.out.print(" "+j+" NO Mutamos - ");
                Mutados.add(S);
            }
        }
        //System.out.println(" ");
        /*System.out.print("  Tamanio HijosMutados: ");
        for(ArrayList<Punto> p: Mutados){
            System.out.print(p.size()+"-");
        }System.out.print("  -  ");//*/
        return Mutados;
    }
    
    /* Crowding deterministico
    Reemplazamos cada padre con mas herencia por su hijo
    Siempre y cuando su hijo sea mejor que el progenitor predominante
    */
    private ArrayList<ArrayList<Punto>> Reemplazo(ArrayList<ArrayList<Punto>> Poblacion, 
            ArrayList<ArrayList<Punto>>Padres, ArrayList<ArrayList<Punto>>HijosMutados, boolean[] mejora){
        //System.out.print(" | ");
        ArrayList<ArrayList<Punto>> NuevaGeneracion = (ArrayList<ArrayList<Punto>>)Poblacion.clone();
        for(int i = 0; i < Padres.size(); i++){
            double costePadre = Evaluacion(Padres.get(i));
            double costeHijo = Evaluacion(HijosMutados.get(i));
            if(costeHijo < costePadre){
                mejora[0] = true;
                //System.out.print(" "+i+" es Mejor que el padre ("+costePadre+"vs"+costeHijo+") ");
                int pos = Poblacion.indexOf(Padres.get(i));
                NuevaGeneracion.set(pos, HijosMutados.get(i));
            }//else System.out.print(" "+i+" NO es Mejor que el padre ("+costePadre+"vs"+costeHijo+") ");
        }
        //System.out.println(" ");
        return NuevaGeneracion;
    }
    
    
    /* Metodo de torneo
    Reemplazamos cada Candidato elegido en el torneo por un hijo siempre
    que el hijo se mejor que este candidato
    */
    private ArrayList<ArrayList<Punto>> ReemplazoTorneo(ArrayList<ArrayList<Punto>> Poblacion, 
            ArrayList<ArrayList<Punto>>HijosMutados, ArrayList<Double> Fitnes, Random r, boolean[] mejora){
        
        ArrayList<ArrayList<Punto>>Padres = Torneo(Poblacion, Fitnes, 2, r);
        
        
        //System.out.print(" | ");
        ArrayList<ArrayList<Punto>> NuevaGeneracion = (ArrayList<ArrayList<Punto>>)Poblacion.clone();
        while(HijosMutados.size() > 0){
            int posP = SeleccionarPeorIndividuo(Padres, Fitness(Padres));
            int posH = SeleccionarMejorIndividuo(HijosMutados, Fitness(HijosMutados));
            double costePadre = Evaluacion(Padres.get(posP));
            double costeHijo = Evaluacion(HijosMutados.get(posH));
            if(costeHijo < costePadre){
                mejora[0] = true;
                int pos = Poblacion.indexOf(Padres.get(posP));
                NuevaGeneracion.set(pos, HijosMutados.get(posH));
                HijosMutados.remove(posH);  //Ya esta insertado
                Padres.remove(posP);        //Ya esta eliminado
            }else HijosMutados = new ArrayList<>();//Eliminamos los hijos
        }
        //System.out.println(" ");
        return NuevaGeneracion;
    }
    
    
    /*
    Seleccionamos una cantidad determinada de Candidatos a eliminar
    Mediante el metodo de torneo
    */
    private ArrayList<ArrayList<Punto>> Torneo(ArrayList<ArrayList<Punto>> Poblacion, ArrayList<Double> Fitnes, int cantidad, Random r){
        ArrayList<ArrayList<Punto>> Padres = new ArrayList<>();
        //System.out.println(" ");
        int k = (int)Poblacion.size()/10;    //10% de poblacion
        //Seleccionamos un 10% de la poblacion como candidatos para el torneo
        
        
        ArrayList<Integer> Indices = new ArrayList<>();
        for(int i = 0; i < cantidad; i++){
            //Seleccionamos los candidatos
            ArrayList<ArrayList<Punto>> Candidatos = new ArrayList<>();
            ArrayList<Double> Costes = new ArrayList<>();
            ArrayList<Integer> posiciones = new ArrayList<>();
            for (int j = 0; j < k; j++) {
                int pos = r.nextInt(Poblacion.size());
                //System.out.println("Torneo "+j+": "+pos+" - "+Fitnes.get(pos));
                Candidatos.add(Poblacion.get(pos));
                Costes.add(Fitnes.get(pos));
                posiciones.add(pos);
            }
            //System.out.println(" "+Evaluacion(Candidatos.get(SeleccionarPeorIndividuo(Candidatos, Costes))));
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
}
