package Algoritmos;

import Datos.Punto;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Jesus Delgado
 */
public class GeneticoBasico {
    
    public GeneticoBasico(){
    }
    
    public ArrayList<Punto> Algoritmo(ArrayList<Punto> c, int semilla, int cantidad){
        Random r = new Random(semilla);
        //int[][] distancias = MatrizDistancias(c);
        //Inicializar Poblacion
        ArrayList<ArrayList<Punto>> Poblacion_inicial = GenerarPoblacion(c, r, cantidad);
        int[][] porcentajes = Medidas(Poblacion_inicial, c.size()); //Estudiar La seleccion de cantidad
        /*
        Porcentajes durante las iteraciones
        Estudiar diversidad en cada iteracion
        Elegir numero de individuos
        */
        //Mostrar
        /*System.out.print("Ciudades  ");
        for(int i = 0; i<c.size(); i++){
            System.out.print("P"+(i+1)+"\t");
        }System.out.println(" ");
        for(int i = 0; i<porcentajes.length; i++){
            System.out.print(" "+(i+1)+"  \t  ");
            for(int j = 0; j < porcentajes[i].length; j++){
                System.out.print(porcentajes[i][j]+"\t");
            }System.out.println(" ");
        }*/
        double[] costeM = new double[1];
        ArrayList<Punto> MejorIndividuo = SeleccionarMejorIndividuo(Poblacion_inicial, costeM);
        double costeMejor = costeM[0];
        
        //EvaluarPoblacion
        //ArrayList<Double> Fitnes = Fitness(Poblacion_inicial);
        
        //Estacionario
        /*
        En cda generacion un solo individuo
        */
        int t = 0;
        int SinMejoras = 0;
        int Cant_padres = (Poblacion_inicial.get(0).size()/4);   //Cada Generacion modifica una cuarta parte
        ArrayList<ArrayList<Punto>> Poblacion = (ArrayList<ArrayList<Punto>>)Poblacion_inicial.clone();
        while(SinMejoras < 1000){
            ArrayList<ArrayList<Punto>> Padres = SeleccionarPadres(Poblacion, Cant_padres, r);
            //System.out.println("Tenemos: "+Padres.size());
            ArrayList<ArrayList<Punto>> Hijos = Cruce(Padres, r);
            //System.out.println("Nos salen "+Hijos.size());
            int S = Poblacion_inicial.get(0).size()/8;
            ArrayList<ArrayList<Punto>> HijosMutados = Mutacion(Hijos, r, S);   //Probabilidad 0.05
            ArrayList<Double> costesMutados = Fitness(HijosMutados);
            //System.out.print("Generacion "+generacion+" --> Tamanio Sublista "+S+": ");
            //Estudio(costesMutados);   //Ver minimo, max y media
            double[] costeAux = new double[1];
            ArrayList<Punto> MejorIndividuo_aux = SeleccionarMejorIndividuo(HijosMutados, costeAux);
            double d = costeAux[0];
            //System.out.println("CosteMejor: "+costeMejor+" vs CosteAux:"+d);
            
            if(d < costeMejor){ 
                costeMejor = d;
                MejorIndividuo = (ArrayList<Punto>)MejorIndividuo_aux.clone();
                SinMejoras = 0;
            }else SinMejoras++;
            //Crowding deterministico --> Solo cambia al padre si es mejor
            Poblacion = Reemplazo(Poblacion, Padres, HijosMutados);
            
            t++;
        }
        System.out.print("Generacion "+t+" --> ");
        return MejorIndividuo;
    }
    
    /*
    Rellena la matriz completa de las distancias de cada ciudad
    */
    private int[][] MatrizDistancias(ArrayList<Punto> ciudades){
        int[][] matriz = new int[ciudades.size()][ciudades.size()];
        for(int i = 0; i < ciudades.size(); i++){
            for(int j = 0; j < ciudades.size(); j++){
                matriz[i][j] = (int)ciudades.get(i).distancia(ciudades.get(j));
            }
        }
        
        return matriz;
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
    private ArrayList<ArrayList<Punto>> SeleccionarPadres(ArrayList<ArrayList<Punto>> Poblacion, int cantidad, Random r){
        ArrayList<ArrayList<Punto>> Padres = new ArrayList<>();
        //double k1 = Math.sqrt(Poblacion.size());
        int k = (int)Poblacion.size()/10;    //10% de poblacion
        for(int i = 0; i < cantidad; i++){
            //Seleccionamos los candidatos
            ArrayList<ArrayList<Punto>> Candidatos = new ArrayList<>();
            for (int j = 0; j < k; j++) {
                Candidatos.add(Poblacion.get(r.nextInt(Poblacion.size())));
            }
            //Nos quedamos con el mejor
            Padres.add(SeleccionarMejorIndividuo(Candidatos, new double[1]));
        }        
        return Padres;
    }
    /*
    Seleccion al individuo de menor coste
    */
    private ArrayList<Punto> SeleccionarMejorIndividuo(ArrayList<ArrayList<Punto>> Poblacion, double[] coste){
        coste[0] = Evaluacion(Poblacion.get(0));
        int indice = 0;
        for(int i = 1; i<Poblacion.size(); i++){
            double coste_aux = Evaluacion(Poblacion.get(i));
            if(coste_aux < coste[0]){
                indice = i;
                coste[0] = coste_aux;
            }
        }
        return Poblacion.get(indice);//*/
    }
    
    /*
    Cruzamos cada padre con el siguiente, y el ultimo con el primero
    */
    private ArrayList<ArrayList<Punto>> Cruce(ArrayList<ArrayList<Punto>> Poblacion, Random r){
        ArrayList<ArrayList<Punto>> Hijos = new ArrayList<>();
        //Toda la generacion sufrira la misma mutacion
        //Hacerlo circular, darle prioridad a los genes del inicio
        //Un 80% del cromosoma padre
        int inicio = r.nextInt(Poblacion.get(0).size());
        int fin = inicio + Poblacion.get(0).size()/10;
        if(fin > Poblacion.get(0).size()){
            fin = Poblacion.get(0).size();
        }
        
        for (int i = 0; i < Poblacion.size(); i++) {
            ArrayList<Punto> Hijo = new ArrayList<>();
            ArrayList<Punto> HerenciaPadre1 = new ArrayList<>();
            for(int j = inicio; j < fin; j++){
                HerenciaPadre1.add(Poblacion.get(i).get(j));
            }
            //Herencia del otro padre
            int j = 0;
            boolean entra = false;
            while(Hijo.size() < Poblacion.get(i).size()){
                if(j == inicio && !entra){
                    entra = true;
                    for(Punto p: HerenciaPadre1){
                        Hijo.add(p);
                    }
                }else{
                    Punto Gen = new Punto();
                    if(i == Poblacion.size()-1)Gen = Poblacion.get(0).get(j);
                    else Gen = Poblacion.get(i+1).get(j);
                    
                    if(!HerenciaPadre1.contains(Gen)){
                        Hijo.add(Gen);
                    }
                    j++;
                }
            }
            
            
            Hijos.add(Hijo);
        }
        
        return Hijos;
    }
    
    /*
    Cada individuo tiene una probabilidad x de mutar
    */
    private ArrayList<ArrayList<Punto>> Mutacion(ArrayList<ArrayList<Punto>> Poblacion, Random r, int tama){
        double x = 0.05;
        
        ArrayList<ArrayList<Punto>> Mutados = new ArrayList<>();
        for(int j = 0; j< Poblacion.size(); j++){
            ArrayList<Punto> S = Poblacion.get(j);
            
            double prob = r.nextDouble();
            if(prob < x){
                ArrayList<Punto> cambio = new ArrayList<>();
                ArrayList<Punto> sublista = new ArrayList<>();
                int aleatorio = r.nextInt(S.size());
                int fin = tama+aleatorio;
                if(fin > S.size()){ //Que sea fija 
                    fin = S.size(); 
                    aleatorio = fin-tama;
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
                Mutados.add(cambio);
            }else Mutados.add(S);
        }
            
        
        return Mutados;
    }
    
    /*
    Reemplazamos cada padre con mas herencia por su hijo
    Como hay tantos hijos como padre da un poco igual, y cambiamos
    todos los padres por todos los hijos
    */
    private ArrayList<ArrayList<Punto>> Reemplazo(ArrayList<ArrayList<Punto>> Poblacion, 
            ArrayList<ArrayList<Punto>>Padres, ArrayList<ArrayList<Punto>>HijosMutados){
        
        ArrayList<ArrayList<Punto>> NuevaGeneracion = (ArrayList<ArrayList<Punto>>)Poblacion.clone();
        //int contador = 0;
        for(int i = 0; i < Padres.size(); i++){
            int pos = Poblacion.indexOf(Padres.get(i));
            //System.out.println(i+" - El padre2 esta "+pos);
            //System.out.println("Su coste era: "+Evaluacion(NuevaGeneracion.get(pos)));
            NuevaGeneracion.set(pos, HijosMutados.get(i));
            /*System.out.println("Su coste ahora es: "+Evaluacion(NuevaGeneracion.get(pos)));
            System.out.println(" ");//*/
        }
        
        return NuevaGeneracion;
    }
    
    private void Estudio(ArrayList<Double> Datos){
        double min = Datos.get(0);
        double max = Datos.get(0);
        double acumulado = 0;
        
        for(int i = 1; i < Datos.size(); i++){
            double v = Datos.get(i);
            if(v < min) min = v;
            if(v > max) max = v;
            acumulado += v;
        }
        int minimo =(int)min;
        int maximo = (int)max;
        
        //System.out.println("Min("+minimo+"), Media("+(Math.round(acumulado/Datos.size()))+"), Max("+maximo+")");
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
