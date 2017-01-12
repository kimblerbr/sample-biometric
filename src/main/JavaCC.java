package main;

import java.sql.Timestamp;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hyperledger.fabric.sdk.shim.ChaincodeBase;
import org.hyperledger.fabric.sdk.shim.ChaincodeStub;

import com.fasterxml.jackson.databind.ObjectMapper;




public class JavaCC extends ChaincodeBase {

	private static Log log = LogFactory.getLog(JavaCC.class);

	public String run(ChaincodeStub stub, String function, String[] args) {
		
		log.info("Calling invocation chaincode with function :" + function + " and args :"
				+ org.apache.commons.lang3.StringUtils.join(args, ","));

		switch (function) {
		case "init":
				init(stub, function, args);
				break;
		case "insertBioID":
			String re = insertBioID(stub, args);
			log.info("Return of insert : " + re);
			return re;
		case "insertFraudster":
			String rf = insertFraudster(stub, args);
			log.info("Return of insert : " + rf);
			return rf;
		default:
			String warnMessage = "{\"Error\":\"Error function " + function + " not found\"}";
			log.warn(warnMessage);
			return warnMessage;
		}

		return null;
	}
	
	public String init(ChaincodeStub stub, String function, String[] args) {
		
		if (args.length != 2) {
			return "{\"Error\":\"Incorrect number of arguments. Expecting 2 : CPF and FaceID \"}";
		}
		try {
		//	ContractRecord contractRecord = new ContractRecord(args[0], Integer.parseInt(args[1]),
		//			Integer.parseInt(args[2]));
		//	stub.putState(args[0], contractRecord.toString());
		} catch (NumberFormatException e) {
			return "{\"Error\":\"Expecting values for CPF and FaceID\"}";
		}
		return null;
	}
	
	
	public String insertBioID(ChaincodeStub stub, String[] args) {

		Boolean insertedBioID = false;
		
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        
		if (args.length != 4) {
			return "{\"Error\":\"Incorrect number of arguments. Expecting 4 : CPF, FaceID, BankID, isFraudster \"}";
		}
		String CPF = args[0];
		String faceid = args[1];
	    String ownerid = args[2];
		String isFraudster = args[3];
		
		if (!CPF.isEmpty() || !CPF.equalsIgnoreCase("") || !CPF.equalsIgnoreCase(null)){
			CPF = CheckCPF.removeMask(CPF);	
		} else {
			return "{\"Error\":\"The value of CPF cannot be null\"}";				
		}
		
		int bankid = Integer.parseInt(ownerid);
		int indFraudster = Integer.parseInt(isFraudster);
		
		if (indFraudster !=0 || indFraudster !=1){
			return "{\"Error\":\"Incorrect value for: isFraudster paramater. Expecting: 0 or 1.\"}";		
		}
		
		if (CheckCPF.isCPF(CPF)){
		try {
		 
			      if (faceid.length()==128){
					
					//next step: before recording, query if there is any update < 180 days  
					
					ContractRecord contractRecord = new ContractRecord(CPF,faceid,bankid,indFraudster,timestamp.getTime());
					stub.putState(CPF, contractRecord.toString());
					
					insertedBioID = true;
					
					//next step: before recording, query about the bank query ranking  
					
					String gameTransaction = stub.getState(Integer.toString(bankid));
					if (gameTransaction.isEmpty()){	
						GameRecord gameRecord = new GameRecord(bankid,10,1,timestamp.getTime());
						stub.putState(Integer.toString(bankid), gameRecord.toString());
					}
							
				} else {
					return "{\"Error\":\"Expecting FaceID with 128 characters\"}";
				}
		
		} catch (NumberFormatException e) {
				return "{\"Error\":\"Expecting values for CPF and FaceID\"}";
			}
		} else {
			return "{\"Error\":\"Failed at CPF validation " + args[0] + "\"}";
		}
		return insertedBioID.toString();

	}

	public String insertFraudster(ChaincodeStub stub, String[] args) {
		Boolean insertedFraudster = false;		
		return insertedFraudster.toString();
	}
	
	
	public String query(ChaincodeStub stub, String function, String[] args) {
		
		log.info("Calling query chaincode with function :" + function + " and args :"
				+ org.apache.commons.lang3.StringUtils.join(args, ","));
		
		if (args.length !=3) {
			return "{\"Error\":\"Incorrect number of arguments. Expecting 3 : CPF, FaceID and BankID\"}";
		}
		
		String CPF = args[0];
		String faceid = args[1];
	    String bankid = args[2];
	
		if (!CPF.isEmpty() || !CPF.equalsIgnoreCase("") || !CPF.equalsIgnoreCase(null)){
			CPF = CheckCPF.removeMask(CPF);	
		} else {
			return "{\"Error\":\"The value of CPF paramater cannot be null\"}";				
		}
	
		if (faceid.length()!=128){
			return "{\"Error\":\"The value of FaceID parameter does not seem right. Expecting: 128bits \"}";							
		}
		
		if (bankid.isEmpty() || bankid.equalsIgnoreCase("") || bankid.equalsIgnoreCase(null)){
			return "{\"Error\":\"The value of BankID cannot be null\"}";									
		}
	    
		switch (function) {
		case "init":
				init(stub, function, args);
				break;
		case "queryByCPF":
			String qcpf = queryByCPF(CPF);
			log.info("Return of query : " + qcpf);
			return qcpf;
		case "queryByFaceID":
			String qface = queryByFaceID(faceid);
			log.info("Return of query : " + qface);
			return qface;
		default:
			String warnMessage = "{\"Error\":\"Error function " + function + " not found\"}";
			log.warn(warnMessage);
			return warnMessage;
		}
		
	//	Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        
		
		String cpf1 = stub.getState(CPF);
		log.info("Calling query for this CPF: " +cpf1+ " .");

		// String faceid1 = stub.getState(args[1]);
	//	String data = stub.getTxTimestamp().toString();
    //  String owner = stub.getUuid().toString;
		
      //  Map<String, String> keysIter = null;
       // if (args.length >= 2) {
      //      keysIter = stub.rangeQueryState(cpf,faceid1);
      //  }else{
      //      keysIter = stub.rangeQueryState("","");
      //  }
        
		try{
		       JavaCC.queryByCPF(CPF);
		       return "";
		//       return keysIter.keySet().toString();
		 
	} catch (Exception e) {
			return "{\"Error\":\"Failed to parse state for faceID " + CPF + " : " + e.getMessage() + "\"}";		
		}
		

	}

     public String queryByFaceID(String faceID) {
 		if (faceID != null && !faceID.isEmpty()) {
 			try {
				ObjectMapper mapper = new ObjectMapper();
				ContractRecord contractRecord = mapper.readValue(faceID, ContractRecord.class);
				return "" + contractRecord.faceid;
			} catch (Exception e) {
				return "{\"Error\":\"Failed to parse state for client " + faceID + " : " + e.getMessage() + "\"}";
			}
 		} else {
			return "2";
		}	 
     }
     
     public static String queryByCPF(String CPF) {
       if (!CPF.isEmpty() || !CPF.equalsIgnoreCase("") || !CPF.equalsIgnoreCase(null)){
        	if (CheckCPF.isCPF(CPF)){
				try {
					ObjectMapper mapper = new ObjectMapper();
					ContractRecord contractRecord = mapper.readValue(CPF, ContractRecord.class);
					return "" + contractRecord.cpf;
				} catch (Exception e) {
					return "{\"Error\":\"Failed to parse state for CPF: " + CPF + " : " + e.getMessage() + "\"}";
				}
			} else {
				return "{\"Error\":\"Invalid CPF number: " + CPF + "\"}";
			}
			
		} else {
			return "{\"Error\":\"The CPF number cannot be null " + CPF + "\"}";
		}
     }
     
     
	@Override
	/**
	 * Just a easiest way to retrieve a contract by its name
	 */
	public String getChaincodeID() {
		return "JavaCC20170106";
	}

	public static void main(String[] args) throws Exception {
		new main.JavaCC().start(args);
	}
}
