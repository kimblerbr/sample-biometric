package main;

import com.fasterxml.jackson.databind.ObjectMapper;

public class GameRecord {

	public int ownerid;
	public int qtdeconsultas;
	public int qtdeinsercoes;
	public Long dateTransaction;
	

	public GameRecord(){
		
	}
		
	public GameRecord(int ownerid, int qtdeconsultas, int qtdeinsercoes, Long dateTransaction) {
		this.ownerid = ownerid;
		this.qtdeconsultas = qtdeconsultas;
		this.qtdeinsercoes = qtdeinsercoes;
		this.dateTransaction=dateTransaction;
		
	}
	
	@Override
	public String toString() {
	    try {
	        return new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(this);
	    } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
	        e.printStackTrace();
	    }
	    return null;
	}

}