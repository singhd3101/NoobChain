package com;

import java.util.ArrayList;
import com.google.gson.GsonBuilder;

public class NoobChain 
{
	
	public static ArrayList<Block> blockchain = new ArrayList<Block>(); 
	public static int difficulty = 1;
	
    public static void main( String[] args )
    {
		blockchain.add(new Block("Genesis Block", "0"));
		System.out.println("Trying to Mine block 1... ");
		blockchain.get(0).mineBlock(difficulty);
		
		blockchain.add(new Block("Second Block", blockchain.get(blockchain.size() - 1).hash));
		System.out.println("Trying to Mine block 2... ");
		blockchain.get(1).mineBlock(difficulty);
		
		blockchain.add(new Block("Third Block", blockchain.get(blockchain.size() - 1).hash));
		System.out.println("Trying to Mine block 3... ");
		blockchain.get(2).mineBlock(difficulty);
		
		System.out.println("\nBlockchain is Valid: " + isChainValid());
		
		String blockchanJson = new GsonBuilder().setPrettyPrinting().create().toJson(blockchain);
		System.out.println("\nThe block chain: ");
		System.out.println(blockchanJson);
    }
    
    public static Boolean isChainValid() {
    	Block currentBlock; 
    	Block previousBlock;
    	
    	//loop through blockchain to check hashes:
    	
    	for(int i=1; i < blockchain.size(); i++) {
    		currentBlock = blockchain.get(i);
    		previousBlock = blockchain.get(i-1);
    		
    		//compare registered hash and calculated hash:
    		
    		if(!currentBlock.hash.equals(currentBlock.calculateHash()) ){
    			System.out.println("Current Hashes not equal");			
    			return false;
    		}
    		
    		//compare previous hash and registered previous hash
    		
    		if(!previousBlock.hash.equals(currentBlock.previousHash) ) {
    			System.out.println("Previous Hashes not equal");
    			return false;
    		}
    	}
    	
    	return true;
    }
}
