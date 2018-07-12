package escalonador;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author gabriela
 */
public class Processo implements Comparable<Processo>{
	private int id;
    private int tempo_chegada;
    private int prioridade; //0 = tempo real     1, 2 ou 3 = prioridades normais para processos de usuarios
    private int tempo_processamento;
    private int tamanho; //em Mbytes
    private int qtd_impressoras, qtd_cds, qtd_modems, qtd_scanners;
    
    @Override
    public int compareTo(Processo outroProcesso) {
         if (this.tempo_chegada < outroProcesso.getTempoChegada()) {
              return -1;
         }
         if (this.tempo_chegada > outroProcesso.getTempoChegada()) {
              return 1;
         }
         return 0;
    }
   
    //setters
    public void setID(int id){
        this.id = id;
    }
    
    public void setTempoChegada(int tempo){
        this.tempo_chegada = tempo;
    }
    
    public void setPrioridade(int prioridade){
        this.prioridade = prioridade;
    }
    
    public void setTempoProcessamento(int tempo){
        this.tempo_processamento = tempo;
    }
    
    public void setTamanho(int tamanho){
        this.tamanho = tamanho;
    }
    
    public void setQtdImpressoras(int qtd_impressoras){
        this.qtd_impressoras = qtd_impressoras;
    }
    
    public void setQtdCDs(int qtd_cds){
        this.qtd_cds = qtd_cds;
    }
    
    public void setQtdModems(int qtd_modems){
        this.qtd_modems = qtd_modems;
    }
    
    public void setQtdScanners(int qtd_scanners){
        this.qtd_scanners = qtd_scanners;
    }
    
    //getters
    
    public int getID(){
        return this.id;
    }
    
    public int getTempoChegada(){
        return this.tempo_chegada;
    }
    
    public int getPrioridade(){
        return this.prioridade;
    }
    
    public int getTempoProcessamento(){
        return this.tempo_processamento;
    }
    
    public int getTamanho(){
        return this.tamanho;
    }  
    public int getQtdImpressoras(){
        return this.qtd_impressoras;
    }
    
    public int getQtdCDs(){
        return this.qtd_cds;
    }
    
    public int getQtdModems(){
        return this.qtd_modems;
    }
    
    public int getQtdScanners(){
        return this.qtd_scanners;
    }
}
