/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package escalonador;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;
/**
 *
 * @author gabriela
 */

public class Escalonador {
	private int velocidade = 2;// variavel que muda o tempo de clock em segundos
	
	private List<Processo> FE; //fila geral de processos
    private List<Processo> FTR; //fila de processos prontos do tipo tempo-real
    private List<Processo> FU; //fila de processos prontos do tipo usuario
    private List<Processo> FB;// fila de processos bloqueados por entrada e saida
    
    private int quantum = 0;//quantum do processo atual no feedback
    private int timer = 0; 
    private int[] recurso = new int[4];
    private int filafeedBack = 0;
    
    // filas do feedback
    private List<Processo> fila1 = new ArrayList<Processo>();
    private List<Processo> fila2 = new ArrayList<Processo>(); 
    private List<Processo> fila3 = new ArrayList<Processo>();
    
    //quantum de entrada e saída
    private int quantum_imp = 0;
    private int quantum_scan = 0;
    private int quantum_mod = 0;
    private int quantum_CD = 0;
    
    /**
     * @param args the command line arguments
     */
    
    public void setFTR(List<Processo> ftr){
        this.FTR = ftr;
    }
    
    public void setFE(List<Processo> fe){
        this.FE = fe;
    }

    public void setFU(List<Processo> fu){
        this.FU = fu;
    }
    public void setFB(List<Processo> fb){
        this.FB = fb;
    }
    
    public List<Processo> getFTR(){
        return this.FTR;
    }
    
    public List<Processo> getFE(){
        return this.FE;
    }
    
    public List<Processo> getFU(){
        return this.FU;
    }
    public List<Processo> getFB(){
        return this.FB;
    }
    
    public void adicionarEmFTR(Processo novo){
        this.FTR.add(novo);
    }
    
    public void adicionarEmFE(Processo novo){
        this.FE.add(novo);
    }
    
    public void adicionarEmFila1(Processo novo){
        this.fila1.add(novo);
    }
    
    public void adicionarEmFU(Processo novo){
        this.FU.add(novo);
    }
    
    public void adicionarEmFB(Processo novo){
        this.FB.add(novo);
    }
    
    public boolean memoriaFull(Processo p) {
    	int tamanhoFila = 0;
    	for (int i=0; i < FE.size(); i++) {
    		tamanhoFila += FE.get(i).getTamanho();
    	}
    	if ((tamanhoFila + p.getTamanho()) > 512) {
    		return true;
    	}
    	return false;
    }
    
    public void adicionaProcesso(String[] informacoesProcesso, int id){
        Processo processoNovo = new Processo();        
        //pega cada informacao, transforma em int e associa ao novo processo
        
        processoNovo.setID(id);         
        processoNovo.setTempoChegada(Integer.parseInt(informacoesProcesso[0])); 
        processoNovo.setPrioridade(Integer.parseInt(informacoesProcesso[1]));
        processoNovo.setTempoProcessamento(Integer.parseInt(informacoesProcesso[2]));
        processoNovo.setTamanho(Integer.parseInt(informacoesProcesso[3]));
        processoNovo.setQtdImpressoras(Integer.parseInt(informacoesProcesso[4]));
        processoNovo.setQtdScanners(Integer.parseInt(informacoesProcesso[5]));
        processoNovo.setQtdModems(Integer.parseInt(informacoesProcesso[6]));
        processoNovo.setQtdCDs(Integer.parseInt(informacoesProcesso[7]));
        if (memoriaFull(processoNovo)) {
        	System.err.println("O processo " + processoNovo.getID() + " não pode ser inserido. A memória esta cheia");
        }
        else {
            this.adicionarEmFE(processoNovo);
        }
                
    }
    
    public void arquivoParaProcesso(){ //coleta as informações do arquivo, transforma em processos e adiciona no escalonador
        Scanner ler = new Scanner(System.in);
        int id = 1;
 
        System.out.printf(ConsoleColors.BLUE + "Informe o nome de arquivo texto:\n" + ConsoleColors.RESET);
        String nome = ler.nextLine();
        
        System.out.println();
        System.out.printf(ConsoleColors.BLUE + "Processos:   T   P  TP  M  I  S  M  CD\n" + ConsoleColors.RESET);
        try {
            FileReader arq = new FileReader(nome);
            BufferedReader lerArq = new BufferedReader(arq);

            String linha = lerArq.readLine();
            while (linha != null) {
                System.out.printf("Processo %d|  %s\n",id, linha);
                System.out.println();
                String[] informacoesProcesso = linha.split(", ");
                this.adicionaProcesso(informacoesProcesso, id);
                id++;
                linha = lerArq.readLine(); //le a proxima linha
            }
            arq.close();
        Collections.sort(FE);
        } catch (IOException e) {
            System.err.printf("Erro na abertura do arquivo: %s.\n",
            e.getMessage());
        }

        System.out.println();
        
    }
    
    public void mostraProcessos(){
        System.out.println("FTR:---------------------------------------");
        for(Processo processo : this.getFTR()){
        	
            System.out.println("Processo " + processo.getID()+": "+processo.getTempoChegada()+", "+processo.getPrioridade()+", "+processo.getTempoProcessamento()+", "+processo.getTamanho()+", "+processo.getQtdImpressoras()+", "+processo.getQtdScanners()+", "+processo.getQtdModems()+", "+processo.getQtdCDs());
            
        }
        
        System.out.println("FE:---------------------------------------");
        for(Processo processo : this.getFE()){

            System.out.println("Processo " + processo.getID()+": "+processo.getTempoChegada()+", "+processo.getPrioridade()+", "+processo.getTempoProcessamento()+", "+processo.getTamanho()+", "+processo.getQtdImpressoras()+", "+processo.getQtdScanners()+", "+processo.getQtdModems()+", "+processo.getQtdCDs());
            
        }
        
        System.out.println("FU:---------------------------------------");
        for(Processo processo : this.getFU()){

            System.out.println("Processo " + processo.getID()+": "+processo.getTempoChegada()+", "+processo.getPrioridade()+", "+processo.getTempoProcessamento()+", "+processo.getTamanho()+", "+processo.getQtdImpressoras()+", "+processo.getQtdScanners()+", "+processo.getQtdModems()+", "+processo.getQtdCDs());
            
        }
        System.out.println("FB:---------------------------------------");
        for(Processo processo : this.getFB()){

            System.out.println("Processo " + processo.getID()+": "+processo.getTempoChegada()+", "+processo.getPrioridade()+", "+processo.getTempoProcessamento()+", "+processo.getTamanho()+", "+processo.getQtdImpressoras()+", "+processo.getQtdScanners()+", "+processo.getQtdModems()+", "+processo.getQtdCDs());
            
        }
        
        System.out.println("------------------FEEDBACK---------------------");
        System.out.println("fila1:---------------------------------------");
        for(Processo processo : this.fila1){

            System.out.println("Processo " + processo.getID()+": "+processo.getTempoChegada()+", "+processo.getPrioridade()+", "+processo.getTempoProcessamento()+", "+processo.getTamanho()+", "+processo.getQtdImpressoras()+", "+processo.getQtdScanners()+", "+processo.getQtdModems()+", "+processo.getQtdCDs());
            
        }
        System.out.println("fila2:---------------------------------------");
        for(Processo processo : this.fila2){

            System.out.println("Processo " + processo.getID()+": "+processo.getTempoChegada()+", "+processo.getPrioridade()+", "+processo.getTempoProcessamento()+", "+processo.getTamanho()+", "+processo.getQtdImpressoras()+", "+processo.getQtdScanners()+", "+processo.getQtdModems()+", "+processo.getQtdCDs());
            
        }
        System.out.println("fila3:---------------------------------------");
        for(Processo processo : this.fila3){

            System.out.println("Processo " + processo.getID()+": "+processo.getTempoChegada()+", "+processo.getPrioridade()+", "+processo.getTempoProcessamento()+", "+processo.getTamanho()+", "+processo.getQtdImpressoras()+", "+processo.getQtdScanners()+", "+processo.getQtdModems()+", "+processo.getQtdCDs());
            
        }
    }
    
    public void entrada_saida(Processo p) {
    	if(!usando_recurso(p.getID())) {
    		if(p.getQtdImpressoras() > 0 && quantum_imp == 0) {
        		quantum_imp = 5;
        		recurso[0] = p.getID();
        		p.setQtdImpressoras(p.getQtdImpressoras() - 1);

        	}else if(p.getQtdScanners() > 0 && quantum_scan == 0) {
        		quantum_scan = 5;
        		recurso[1] = p.getID();
        		p.setQtdScanners(p.getQtdScanners() - 1);
        		
        	} else if(p.getQtdModems() > 0 && quantum_mod == 0) {
        		quantum_mod = 3;
        		recurso[2] = p.getID();
        		p.setQtdModems(p.getQtdModems() - 1);
        			
        	}else if(p.getQtdCDs() > 0 && quantum_CD == 0) {
        		quantum_CD = 3;
        		recurso[3] = p.getID();
        		p.setQtdCDs(p.getQtdCDs() - 1);
        		
        	}   	
    	}    	
    }
    
    public void FB_manutencao () {
    	
    	if(quantum_imp > 0) {
    		quantum_imp--;
    	}else {
    		recurso[0] = -1;
    	}
    	if(quantum_scan > 0) {
    		quantum_scan--;
    	}else {
    		recurso[1] = -1;
    	}
    	if(quantum_mod > 0) {
    		quantum_mod--;
    	}else {
    		recurso[2] = -1;
    	}
    	if(quantum_CD > 0) {
    		quantum_CD--;
    	}else {
    		recurso[3] = -1;
    	}
    	
    	
    	for(int i = 0; i < FB.size(); i++) {
			if(!usa_recurso(FB.get(i)) && !usando_recurso(FB.get(i).getID())) {
				if(FB.get(i).getPrioridade() == 0) {
					adicionarEmFTR(FB.get(i));
					FB.remove(i);
				}else {
					adicionarEmFila1(FB.get(i));
					FB.remove(i);
				}
			}else {
				entrada_saida(FB.get(i));
			}
		}
    }
    
    public boolean usa_recurso (Processo p) {
    	return (p.getQtdImpressoras()+p.getQtdCDs()+p.getQtdModems()+p.getQtdScanners()) > 0;
    }
    
    public boolean usando_recurso(int id){
    	for (int i = 0; i < 4; i++) {
			if(recurso[i] == id) {
				return true;
			}
		}
    	return false;
    }

    public void escalonamento() throws InterruptedException{
        while(this.FTR.size() != 0 || this.FU.size() != 0 || this.FE.size() != 0 || this.FB.size() != 0 || this.fila1.size() != 0|| this.fila2.size() != 0|| this.fila3.size() != 0){
        	
        	TimeUnit.SECONDS.sleep(velocidade);
        	System.out.println();
                System.out.println(ConsoleColors.BLACK_BOLD+"⌚: "+timer+"\n"+ConsoleColors.RESET);
        	
        	int cont = 0;
        	for(Processo processo: this.getFE()) {// separa os tipos de processo pela prioridade
        		if(processo.getPrioridade() == 0 && processo.getTempoChegada() == timer) {
            		adicionarEmFTR(processo);
            		cont++;
            	} 
            	if (processo.getPrioridade() > 0 && processo.getTempoChegada() == timer){
            		adicionarEmFU(processo);
            		adicionarEmFila1(processo);// adiciona logo na fila1 do feedback quando é adicionado em FU
            		cont++;
            	}
        	}
        	for(int i = 0; i < cont; i++) {
        		this.FE.remove(0);
        	}
        	
        	FB_manutencao();// método que faz a manutenção da fila de entrada e saida
        	System.out.println("Impressoras: "+quantum_imp+" Scanners: "+quantum_scan+" Modems: "+quantum_mod+" CDs: "+quantum_CD+"\n");
        	System.out.println("P: "+recurso[0]+"  P: "+recurso[1]+" P: "+recurso[2]+"  P: "+recurso[3]+"\n");
        	
        	mostraProcessos();
        	
            if(this.FTR.size() != 0 && this.quantum == 0){ //se a FTR nao estiver vazia
                
            	if(!usa_recurso(FTR.get(0))) {
            		System.out.println("processo: "+this.FTR.get(0).getID()+" Tempo restante: " + this.FTR.get(0).getTempoProcessamento());
                    this.FTR.get(0).setTempoProcessamento(this.FTR.get(0).getTempoProcessamento()- 1);// decrementa o tempo de processamento
          
                    if(this.FTR.get(0).getTempoProcessamento() == 0) {
                    	this.FTR.remove(0);
                    }
            	} else {
            		adicionarEmFB(FTR.get(0));
            		FTR.remove(0);
            	}
            }
            else{
                //this.FU = organizaPorPrioridade(this.FU);
                if(FU.size() > 0) {
	            	  if(!usa_recurso(FU.get(0))) {
	                  	this.feedback();
	                  }else {
	                  	adicionarEmFB(FU.get(0));
	                  	fila1.remove(0);
	                  	FU.remove(0);
	                  }
                }else {
                	this.feedback();
                }
            }
            this.timer++;
        }
        mostraProcessos();
    }
    
    public void feedback(){
    	
       // if(fila1.size() != 0 || fila2.size() != 0 || fila3.size() != 0){
    	
    	
            if(fila1.size() != 0 && (filafeedBack == 1 || filafeedBack == 0)){// verifica se pode executar processos da fila 1
            	if(this.quantum == 0) {
            		this.quantum = 2;
            		this.filafeedBack = 1;
            	}
                int tempo_restante = processa(fila1.get(0));
                if(tempo_restante != 0 && this.quantum == 0){
                    Processo aux = fila1.get(0);
                    fila1.remove(0);
                    fila2.add(aux); //vai pra a segunda fila
                    this.filafeedBack = 0;
                    System.out.println("PROCESSO"+aux.getID()+" NÃO TERMINOU, PASSOU PRA A FILA 2");
                    
                }
                else if (tempo_restante == 0){
                	System.out.println("PROCESSO"+fila1.get(0).getID()+" FINALIZADO E REMOVIDO");
                	quantum = 0;
                	this.filafeedBack = 0;
                	this.FU.remove(fila1.get(0));
                    fila1.remove(0);
                    
                }
                
            }
            else if(fila2.size() != 0 && (filafeedBack == 2 || filafeedBack == 0)){// verifica se pode executar processos da fila 2
                	if(this.quantum == 0) {
                		this.quantum = 4;
                		this.filafeedBack = 2;
                	}
                    int tempo_restante = processa(fila2.get(0));
                    if(tempo_restante != 0 && this.quantum == 0){
                        Processo aux = fila2.get(0);
                        fila2.remove(0);
                        fila3.add(aux); //vai pra a terceira fila
                        this.filafeedBack = 0;
                        System.out.println("PROCESSO"+aux.getID()+" NÃO TERMINOU, PASSOU PRA A FILA 3");
                        
                    }
                    else if (tempo_restante == 0){
                    	System.out.println("PROCESSO"+fila2.get(0).getID()+" FINALIZADO E REMOVIDO");
                    	quantum = 0;
                    	this.filafeedBack = 0;
                    	this.FU.remove(fila2.get(0));
                        fila2.remove(0);
                        
                    }
            }
        	else if(fila3.size() != 0 && (filafeedBack == 3 || filafeedBack == 0)){// verifica se pode executar processos da fila 3
            	if(this.quantum == 0) {
            		this.quantum = 8;
            		this.filafeedBack = 3;
            	}
                int tempo_restante = processa(fila3.get(0));
                if(tempo_restante != 0 && this.quantum == 0){
                    Processo aux = fila3.get(0);
                    fila3.remove(0);
                    fila1.add(aux); //volta pra a primeira fila
                    this.filafeedBack = 0;
                    System.out.println("PROCESSO"+aux.getID()+" NÃO TERMINOU, VOLTOU PRA A FILA 1");
                    
                }
                else if (tempo_restante == 0){
                	System.out.println("PROCESSO"+fila3.get(0).getID()+" FINALIZADO E REMOVIDO");
                	quantum = 0;
                	this.filafeedBack = 0;
                	this.FU.remove(fila3.get(0));
                    fila3.remove(0);
                    
                }
            }
        //} 
    }
    
    
    public int processa(Processo processo){
        int tempo = processo.getTempoProcessamento();
        System.out.println("\nINICIANDO PROCESSAMENTO "+ processo.getID());
        System.out.println("QUANTUM: " + this.quantum);
        if(this.quantum > 0){
            if(tempo > 0){ //se nao tiver acabado o processamento
                System.out.println("processo: "+processo.getID()+" Tempo restante: " + (tempo-1));
                processo.setTempoProcessamento(tempo-1);// decrementa o tempo de processamento
                tempo = processo.getTempoProcessamento();
            }
            else{
                System.out.println("PROCESSO TERMINADO "+ processo.getID());
                this.quantum = 0;
                this.filafeedBack = 0;
                return 0;
            }
        } 
        this.quantum --;
        System.out.println("PROCESSAMENTO FINALIZADO "+ processo.getID());
        return tempo;
    }
    
    
    public static List<Processo> organizaPorPrioridade(List<Processo> fila){
        List<Processo> aux = new ArrayList<Processo>();
        int i;
        int prioridade;
        for(prioridade = 0; prioridade <= 3; prioridade++){
            for(i = 0; i < fila.size(); i++){
                if(fila.get(i).getPrioridade() == prioridade){
                    aux.add(fila.get(i));
                }
            }
        }
        return aux;
    }
    
    public static void main(String[] args) {
        Escalonador escalonador = new Escalonador();
        
        List<Processo> ftr = new ArrayList<Processo>();
        escalonador.setFTR(ftr);
        
        List<Processo> fe = new ArrayList<Processo>();
        escalonador.setFE(fe);
        
        List<Processo> fu = new ArrayList<Processo>();
        escalonador.setFU(fu);
        
        List<Processo> fb = new ArrayList<Processo>();
        escalonador.setFB(fb);
        
        escalonador.arquivoParaProcesso();
        //meu exemplo: C:\Users\gabriela\Documents\NetBeansProjects\Escalonador\src\escalonador\arquivo.txt
        
        //escalonador.mostraProcessos();
        try {
        	escalonador.escalonamento();
        }catch(Exception e) {
        	System.out.println("deu ruim aqui! "+ e);
        }

        
    }
}