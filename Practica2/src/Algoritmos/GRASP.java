package Algoritmos;

import Datos.Punto;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Jesus Delgado
 */
public class GRASP {
    
    public GRASP(){
    }
    
    public ArrayList<Punto> Algoritmo(ArrayList<Punto> c, int times, int semilla){
        Random r = new Random(semilla);
        BLocales_Mejor BL = new BLocales_Mejor();
        int[][] distancias = MatrizDistancias(c);
        int l = c.size()/10;
        
        ArrayList<Punto> MejorSolucion = new ArrayList<>();
        for(int i = 0; i < times; i++){
            ArrayList<Punto> S = GreedyAleatoriezado(c, distancias, l, r);  //Cojo uno aleatorio de los l mejores, 
            ArrayList<Punto> Sprima = BL.Algoritmo(S);                      //empiezo por un Punto aleatorio
            MejorSolucion = Actualizar(Sprima, MejorSolucion);
        }
        
        return MejorSolucion;
    }
    
    private ArrayList<Punto> Actualizar(ArrayList<Punto> Candidato, ArrayList<Punto> Mejor){
        if(Mejor.size() > 0){
            if(Evaluacion(Candidato) < Evaluacion(Mejor))return (ArrayList<Punto>)Candidato.clone();
            else return Mejor;
        }else return (ArrayList<Punto>)Candidato.clone();
    }
    
    //Rellena la matriz completa
    private int[][] MatrizDistancias(ArrayList<Punto> ciudades){
        int[][] matriz = new int[ciudades.size()][ciudades.size()];
        for(int i = 0; i < ciudades.size(); i++){
            for(int j = 0; j < ciudades.size(); j++){
                matriz[i][j] = (int)ciudades.get(i).distancia(ciudades.get(j));
            }
        }
        
        return matriz;
    }
    
    private ArrayList<Punto> GreedyAleatoriezado(ArrayList<Punto> inicial, int[][] distancias, int l, Random r){
        ArrayList<Punto> S = new ArrayList<>();
        Punto p = inicial.get(r.nextInt(inicial.size()));
        S.add(p);  //Añadimos un punto aleatorio como inicial
        while(S.size() < inicial.size()){
            ArrayList<Punto> kCandidatos = LRC(inicial, S, distancias, l);
            Punto s = ElegirAleatorio(kCandidatos, S.get(S.size()-1), distancias, r);
            //Punto s = kCandidatos.get(r.nextInt(kCandidatos.size()));
            S.add(s);
        }
        
        return S;
    }
    
    
    private ArrayList<Punto> LRC(ArrayList<Punto> original, ArrayList<Punto> caminoAct, int[][] distancias, int l){
        ArrayList<Punto> LRC = new ArrayList<>();   //Los k puntos mas cercanos al ultimo
        Punto estudio = caminoAct.get(caminoAct.size()-1);
        int fila = estudio.getIndice()-1;           //Para saber que columnas estudiar de matriz
        
        //Cojo todos los puntos a los que apunta ordenados por distancia al punto bajo estudio
        ArrayList<Punto> PCandidatos = new ArrayList<>();
        ArrayList<Integer> DCandidatos = new ArrayList<>();
        for(int i = 0; i < distancias[fila].length; i++){
            InsertarOrdenado(PCandidatos, DCandidatos, original.get(i), distancias[fila][i]);
        }
        //Cojo los k primeros que no esten en el camino actual
        int i = 0;
        int cantidad = 0;
        while(cantidad < l && i < PCandidatos.size()){
            if(!caminoAct.contains(PCandidatos.get(i))){
                LRC.add(PCandidatos.get(i));
                cantidad++;
            }i++;
        }
            
        
        return LRC;
    }
    
    //Ordena los puntos segun las distanicas
    private void InsertarOrdenado(ArrayList<Punto> candidatos, ArrayList<Integer> distancias, Punto p, int distancia){
        
        if(candidatos.size() > 0){
            int i = 0;
            boolean menor = false;
            while(i < candidatos.size() && !menor){
                if(distancia < distancias.get(i)){
                    menor = true;
                }else i++;
            }
            candidatos.add(i, p);
            distancias.add(i, distancia);
        }else{
            candidatos.add(p);
            distancias.add(distancia);
        }
        
    }
    
    /*
    Elegimos punto con una porbabilidad inversa a su distancia
    */
    private Punto ElegirAleatorio(ArrayList<Punto> Candidatos, Punto origen, int[][] distancias, Random r){
        //System.out.println("Con "+Candidatos.size()+" candidatos");
        if(Candidatos.size() > 1){
            //miro la probabilidad total
            double[] probabilidades = new double[Candidatos.size()];
            int i = origen.getIndice()-1;
            int indice = 0;
            double prob_Acum = 0;
            for(Punto p: Candidatos){
                double v = (double)distancias[i][p.getIndice()-1];
                //if(v == 0) v = 0.001;
                double prob = 1/v;
                prob_Acum += prob;
                probabilidades[indice++] = prob;  
            }
            //System.out.println("El margen es "+prob_Acum);
            
            //Lo paso a margen de 0 a 1
            double anterior = 0.0;
            //System.out.println("La probabilidad de cada uno: ");
            for(int j = 0; j < probabilidades.length; j++){
                anterior += probabilidades[j]/prob_Acum;    //Desde el anterior hasta mi
                //System.out.print(anterior+" ");
                probabilidades[j] = anterior;
            }

            
            double n_aleatorio = r.nextDouble();
            //Busco cual ha tocado
            int posP = 0;
            for(int j = 0; j< probabilidades.length; j++){
                if(n_aleatorio < probabilidades[j]){
                    posP = j;
                    break;
                }
            }
            //System.out.println("\nEl elegido es el "+posP+ " Pues a salido "+n_aleatorio);
            //System.out.println(" ");

            return Candidatos.get(posP);
        }else return Candidatos.get(0);//El unico que hay
            
    }
    
    
    
//    private Punto ElegirAleatorio(ArrayList<Punto> Candidatos, Punto origen, int[][] distancias, Random r){
//        //System.out.println("Con "+Candidatos.size()+" candidatos");
//        if(Candidatos.size() > 1){
//            //miro la probabilidad de cada uno
//            int[] probabilidades = new int[Candidatos.size()];
//            int i = origen.getIndice()-1;
//            int indice = 0;
//            int margen = 0;
//            for(Punto p: Candidatos){
//                double v = (double)distancias[i][p.getIndice()-1];
//                if(v == 0) v = 1;
//                double prob = 1/v;
//                int valor = (int) (prob*1000);    //mil
//                margen += valor;
//                probabilidades[indice++] = valor;  //Para coger los 2 ultimos decimales
//            }
//            //System.out.println("El margen es "+margen);
//
//            int n_aleatorio = r.nextInt(margen);
//            //Busco cual ha tocado
//            int posP = 0;
//            for(int j = 0; j< probabilidades.length; j++){
//                if(probabilidades[j]<n_aleatorio){
//                    posP = j;
//                    break;
//                }
//            }
//            //System.out.println("El elegido es el "+posP);
//
//            return Candidatos.get(posP);
//        }else return Candidatos.get(0);//El unico que hay
//            
//    }
    
    
    
    
    
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
