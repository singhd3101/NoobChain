package com;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Block {

	private String hash;
	private String previousHash;
	public String merkleRoot;
	private String data; // our data will be a simple message
	private long timeStamp;
	private int nonce;

	//our data will be a simple message.
	public List<Transaction> transactions = new ArrayList<Transaction>(); 

	//Block Constructor
//	public Block(String data, String previousHash) {
		public Block(String previousHash) {
		//	this.data = data;
		this.previousHash = previousHash;
		this.timeStamp = new Date().getTime();

		//Making sure we do this after we set the other values.
		this.hash = calculateHash();
	}

	//Calculate new hash based on blocks contents
	public String calculateHash() {
		//	return StringUtil.applySha256(
		//			previousHash + Long.toString(timeStamp) + data);

		return StringUtil.applySha256(previousHash +
				Long.toString(timeStamp) +
				Integer.toString(nonce) + 
				merkleRoot);
	}

	//Increases nonce value until hash target is reached.
	public void mineBlock(int difficulty) {

		//Create a string with difficulty * "0" 
		//String target = new String(new char[difficulty]).replace('\0', '0'); 

		merkleRoot = StringUtil.getMerkleRoot(transactions);

		//Create a string with difficulty * "0" 
		String target = StringUtil.getDificultyString(difficulty); 

		while(!hash.substring( 0, difficulty).equals(target)) {
			nonce ++;
			hash = calculateHash();
		}

		System.out.println("Block Mined!!! : " + hash);
	}

	//Add transactions to this block
	public boolean addTransaction(Transaction transaction) {
		//process transaction and check if valid, unless block is genesis block then ignore.
		if(transaction == null) return false;		
		if((previousHash != "0")) {
			if((transaction.processTransaction() != true)) {
				System.out.println("Transaction failed to process. Discarded.");
				return false;
			}
		}
		transactions.add(transaction);
		System.out.println("Transaction Successfully added to Block");
		return true;
	}

	public String getHash() {
		return hash;
	}

	public String getPreviousHash() {
		return previousHash;
	}


}
