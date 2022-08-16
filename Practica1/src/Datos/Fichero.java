package Datos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

/**
 *
 * @author Jesus Delgado
 */
public class Fichero {
    
    private ArrayList<String> archivos;
    private ArrayList<Punto> puntos;
    private String nombrefich;
    
    /**
     * Constructor en el que pasamos el nombre del fichero
     */
    public Fichero(){
        this.archivos = ObtenerFicheros();
        this.puntos = new ArrayList<>();
        this.nombrefich = "";
    }
    
    private ArrayList<String> ObtenerFicheros(){
        
        ArrayList<String> MisArchivos = new ArrayList();
        //Obtengo el path de los ficheros de datos
        String actual = DirAct();
        String carpeta = "\\Files";
        String path = actual+carpeta;
        
        //Obtengo los files del directorio
        File f = new File(path);
        
        if(f.isDirectory()){ //Si es un directorio
            File[] arr_content = f.listFiles();
            
            for(int i=0; i<arr_content.length; i++){
                if( arr_content[i].isFile()){ //Si es un archivo
                    String aux = arr_content[i].getName();
                    //Si es un tsp lo añado
                    if(aux.contains(".tsp")) MisArchivos.add(path+"\\"+aux);
                }
            }
        }else{
            System.out.println("Directorio incorrecto");
        }
        
        return MisArchivos;
    }
    
    
    /**
     * Devuelve el directorio de trabajo
     * @return
     */
    private String DirAct(){
        //Ver en que directorio estamos trabajando
        String dir = "";
        try {
            dir = new java.io.File( "." ).getCanonicalPath();
        } catch (Exception ex) {
            System.out.println("Excepcion en  DirAct: "+ex);
        }
        return dir;
    }
    
       
    /**
     * Ruta del fichero
     * @param ind
     */
    public void LeerFichero(/*String ruta*/ int ind){ 
        File archivo;
        FileReader fr=null;
        BufferedReader bf=null;
        try{
            archivo=new File(this.archivos.get(ind));
            fr= new FileReader(archivo);//necesitamos try por si no puede abrir el archivo
            bf= new BufferedReader(fr);
            
             //Leemos el archivo
            String s=bf.readLine();
            int i=0;    //Posicion de linea del fichero
            boolean intro = true;
            while(!s.equals("EOF")){
//                System.out.println(""+s);
                
                if(intro){        //Informacion del fichero
                    if(i==0){
                        String[] partes = s.split(": ");
                        this.nombrefich = partes[1];//+".tsp";
                    }
                    //System.out.println("Linea: "+i+": "+s);
                }else{          //Los puntos
                    
                    String[] partes = s.split(" ");
                    double[] datos = new double[3];
                    int k = 0;
                    for(int j =0; j<partes.length; j++){
//                        System.out.println(partes[j]);
                        if(!partes[j].equals("") && !partes[j].equals(" ")) 
                            
                            datos[k++]=Double.parseDouble(partes[j]);
                    }
                    
//                    double indice = Double.parseDouble(""+s.charAt(1)+s.charAt(2)+s.charAt(3));
//                    double x = Double.parseDouble(""+s.charAt(4)+s.charAt(5)+s.charAt(6));
//                    double y = Double.parseDouble(""+s.charAt(8)+s.charAt(9)+s.charAt(10));
                    double indice = datos[0];
                    double x = datos[1];
                    double y = datos[2];
                    
                    //Punto p = new Punto(x,y,i-6);
                    Punto p = new Punto(x,y,(int)indice);
                    //System.out.println("El punto es: "+p.toString());
                    this.puntos.add(p);
                    
                    /*String[] partes=s.split(" ");
                            //0-9
                    //[0] --> vacio     || [1] --> vacio    || [2] --> indice   || [3] --> X   || [4] --> Y
                            //10-99
                    //[0] --> vacio     || [1] --> indice   || [2] --> X        || [3] --> Y
                            //100-999
                    //[0] --> indice    || [1] --> X        || [2] --> Y
                    System.out.println("Cantidad de partes: "+partes.length);*/
                    
                    /*Punto p;
                    switch(partes.length){
                        case 3: 
                            p = new Punto(Double.parseDouble(partes[1]), Double.parseDouble(partes[2]));
                            break;
                        case 4:
                            p = new Punto(Double.parseDouble(partes[2]), Double.parseDouble(partes[3]));
                            break;
                        case 5:
                            p = new Punto(Double.parseDouble(partes[3]), Double.parseDouble(partes[4]));
                            break;
                        default: p = new Punto();
                    }
                    
                    
                    System.out.println("El punto es: "+p.toString());
                    this.puntos.add(p);*/
                    
                }
                if(s.equals("NODE_COORD_SECTION")) intro = false;
                i++;//Incrementamos i
                s=bf.readLine();//Renovamos la info de s
            }
            
            
        }catch(Exception ex){
            System.out.println("Error abriendo");   
        }finally{
            try{
                bf.close();
            }catch(Exception ex){
                System.out.println("Error cerrando");   
            }
            
        }
    }
    
    //Aqui quiero guardarlo en excel
    public boolean EscribirFichero(ArrayList<Punto> camino, double distancia){
        boolean guardado = false;
        
        
        return guardado;
    }
    
    

    //Getters y Setters
    public ArrayList<String> getArchivos() {
        return archivos;
    }

    public void setArchivos(ArrayList<String> archivos) {
        this.archivos = archivos;
    }
    
    public ArrayList<Punto> getPuntos() {
        return puntos;
    }

    public void setPuntos(ArrayList<Punto> puntos) {
        this.puntos = puntos;
    }

    public String getNombrefich() {
        return nombrefich;
    }

    public void setNombrefich(String nombrefich) {
        this.nombrefich = nombrefich;
    }
    
}
