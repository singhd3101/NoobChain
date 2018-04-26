package com;

import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Wallet {

	//Your wallets balance is the sum of all the unspent transaction 
	//outputs - UTXO, addressed to you.

	//Keys are generated as KeyPairs using Elliptic Curve Cryptography
	//https://en.wikipedia.org/wiki/Elliptic-curve_cryptography
	//The private key is used to sign the data and the public key can
	//be used to verify its integrity.

	//used to sign our transactions so that not other owner can use them
	public PrivateKey privateKey;

	//acts as address for NoobCoin..can be shared to receive payments
	//sent along with transaction, used to verify owner's signature and that
	//the data has not been tampered
	public PublicKey publicKey; 

	//Signature = createSign(private key, from, to, value)
	//verifySign(public key, sign, from, to, value)

	//Transaction = public key + private key + value + inputs(references to 
	//              previous transaction that prove the sender has funds to 
	//              send) + outputs(shows the amount relevant addresses 
	//              received in the transaction. These outputs are referenced  
	//              as inputs in new transactions) + cryptographic signature
	//              (that proves that the owner of the address is the one
	//              sending this transaction and that 


	//only UTXOs owned by this wallet.
	public Map<String,TransactionOutput> UTXOs = new HashMap<String,TransactionOutput>(); 

	public Wallet(){
		generateKeyPair();	
	}

	public void generateKeyPair() {
		try {
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA","BC");
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
			ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime192v1");
			// Initialize the key generator and generate a KeyPair
			// 256 bytes provides an acceptable security level
			keyGen.initialize(ecSpec, random);   
			KeyPair keyPair = keyGen.generateKeyPair();
			// Set the public and private keys from the keyPair
			privateKey = keyPair.getPrivate();
			publicKey = keyPair.getPublic();
		}catch(Exception e) {
			e.getMessage();
		}
	}

	//returns balance and stores the UTXO's owned by this wallet in this.UTXOs
	public float getBalance() {
		float total = 0;	
		for (Map.Entry<String, TransactionOutput> item: NoobChain.UTXOs.entrySet()){
			TransactionOutput UTXO = item.getValue();
			if(UTXO.isMine(publicKey)) { //if output belongs to me ( if coins belong to me )
				UTXOs.put(UTXO.id,UTXO); //add it to our list of unspent transactions.
				total += UTXO.value ; 
			}
		}  
		return total;
	}

	//Generates and returns a new transaction from this wallet.
	public Transaction sendFunds(PublicKey _recipient,float value ) {
		if(getBalance() < value) { //gather balance and check funds.
			System.out.println("#Not Enough funds to send transaction. Transaction Discarded.");
			return null;
		}
		//create array list of inputs
		ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();

		float total = 0;
		for (Map.Entry<String, TransactionOutput> item: UTXOs.entrySet()){
			TransactionOutput UTXO = item.getValue();
			total += UTXO.value;
			inputs.add(new TransactionInput(UTXO.id));
			if(total > value) break;
		}

		Transaction newTransaction = new Transaction(publicKey, _recipient , value, inputs);
		newTransaction.generateSignature(privateKey);

		for(TransactionInput input: inputs){
			UTXOs.remove(input.transactionOutputId);
		}
		return newTransaction;
	}
}
