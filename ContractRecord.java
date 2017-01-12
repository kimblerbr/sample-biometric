package main;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ContractRecord {

	public String cpf;
	public String faceid;
	public int ownerid;
	public int isFraudster;
	public Long dateTransaction;
	

	public ContractRecord(){
		
	}	
	
	public ContractRecord(String faceid, int ownerid, int isFraudster, Long dateTransaction) {
		this.faceid = faceid;
		this.ownerid = ownerid;
		this.isFraudster = isFraudster;
		this.dateTransaction=dateTransaction;
	}
		
	public ContractRecord(String cpf, String faceid, int ownerid, int isFraudster, Long dateTransaction) {
		this.cpf = cpf;
		this.faceid = faceid;
		this.ownerid = ownerid;
		this.isFraudster = isFraudster;
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