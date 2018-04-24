package com;

import java.security.MessageDigest;

public class StringUtil {
	
	private StringUtil() {
	    throw new IllegalStateException("Utility class");
	  }
	
	//Applies SHA256 to a string and returns a result.
	
	public static String applySha256(String input) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			
			//Applies SHA256 to our input.
			
			byte[] hash = digest.digest(input.getBytes("UTF-8"));
			
			StringBuilder hexString = new StringBuilder(); // This will contain has as hexadecimal
			
			for(int i = 0; i < hash.length ; i++) {
				String hex = Integer.toHexString(0xff & hash[i]);
				if(hex.length()==1) {
					hexString.append('0');
				}
				hexString.append(hex);
			}
			
			return hexString.toString();
		}
		catch(Exception e) {
			return e.getMessage();
		}
	}

}
