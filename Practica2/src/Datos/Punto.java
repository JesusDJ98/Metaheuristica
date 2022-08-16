package Datos;

/**
 *
 * @author Jesus Delgado
 */
public class Punto {
    private double X;
    private double Y;
    private int indice;
    
    public Punto(){
        this.X=-1;
        this.Y=-1;
        this.indice = -1;
    }
    
    /**
     * Constructor
     * @param X
     * @param Y
     * @param i
     */
    public Punto(double X, double Y, int i){
        this.X=X;
        this.Y=Y;
        this.indice = i;
    }
    
    @Override
    public String toString(){
        String s=""+this.indice+":("+this.X+","+this.Y+")";
        return s;
    }
    
    /**
     *
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        Punto p = (Punto)obj;
        return this.indice == p.getIndice() && this.X == p.getX() && this.Y == p.getY();
    }

    @Override
    public int hashCode() {
        //              1..n            da igual    da igual                
        String n = ""+this.indice + (int)this.X + (int)this.Y;
        int hash = Integer.parseInt(n);
        return hash;
    }

    public double distancia(Punto p){
        
        double distancia = Math.round(Math.sqrt( Math.pow(X-p.getX(),2) + Math.pow(Y-p.getY(),2) ));
        //double distancia = Math.sqrt( Math.pow(X-p.getX(),2) + Math.pow(Y-p.getY(),2) );
        return distancia;
    }
    
    //Getter y setter
    public double getX() {
        return X;
    }

    public void setX(double X) {
        this.X = X;
    }

    public double getY() {
        return Y;
    }

    public void setY(double Y) {
        this.Y = Y;
    }

    public int getIndice() {
        return indice;
    }

    public void setIndice(int indice) {
        this.indice = indice;
    }
    
}
