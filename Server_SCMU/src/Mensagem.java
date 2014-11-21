import java.util.ArrayList;


public class Mensagem {

    private int divisao;
    private int elementoAfectado;
    private boolean condicao;
    private ArrayList<AccessPoint> points;

    public Mensagem(int Divisao, int ElementoAfectado, boolean condicao ) {
        this.divisao = Divisao;
        this.elementoAfectado = ElementoAfectado;
        this.condicao = condicao;
    }
    
    public int getDivisao() {
    	return this.divisao; 
    }
    
    public int getElemencto() {
    	return this.elementoAfectado; 
    }
    
    public boolean getCondicao() {
    	return this.condicao; 
    }
    
    public Mensagem(ArrayList<AccessPoint> points){
        this.divisao = 0;
        this.points = points;

    }
}
