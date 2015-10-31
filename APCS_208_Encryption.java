/**
 * @(#)APCS_208_Encryption.java
 *
 * @author mattgd
 * @version 1.00 2015/10/30
 */

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

class Cypher {
	
	private int[][] key = { {1, 2}, {2, 8} };
	private String message = "";
	
	Cypher() {		
		try {
    		Scanner sc = new Scanner(new FileReader("secrets.txt"));
    		
    		while (sc.hasNextLine()) this.message += sc.nextLine();
        	
        	sc.close();
    	} catch (FileNotFoundException e) {
    		e.printStackTrace();
    	}
    	
    	encrypt(message);
	}
	
	public String encrypt(String data) {
		data = data.toUpperCase();
		char currentChar;
		int charValue, dataLength = data.length();
		int topRow = 0; // Will be used later to keep track of which section the matrix is being multiplied by
		
		// Create an array of proper size for the unencrypted numbers
		if (dataLength % 2 != 0) dataLength++;
		double[][] dataArray = new double[dataLength / 2][2];
		
		// Convert the String to an Array of numbers
		int count = 0;
		for (int i = 0; i < data.length(); i++) {
			currentChar = data.charAt(i);
			
			// Convert the character value
			if (currentChar == ' ') {
				charValue = 27;
			} else {
				charValue = ((int) currentChar) - 64;
			}
			
			dataArray[i / 2][count] = charValue;
			
			count++;
			if (count == 2) count = 0;
		}
		
		// Create an array of proper size for the encrypted numbers
		double[][] encryptedArray = new double[dataArray.length][2];
		
		count = 0;
		for (int i = 0; i < dataArray.length; i++) {
			for (int j = 0; j < 2; j++) {
				// This prints out all of the information for testing purposes
				//System.out.printf("{ TOPROW: %d, I: %d, J: %d, COUNT: %d, ENCRYPTED ROW INDEX: %d, DATA ARRAY ROW INDEX: %d, DATA ARRAY: %.2f, KEY ROW INDEX: %d, DATAARRAY NEW: %.2f }%n", topRow, i, j, count, i + topRow % 2, i, dataArray[i * 2][j], key[i % 2][j], dataArray[i * 2][j] * key[i % 2][j]);

				if (isEven(j)) {
					encryptedArray[i + j + topRow % 2][j] = dataArray[i][j] * key[i % 2][j];
				} else {
					encryptedArray[i + topRow % 2][j] = dataArray[i][j] * key[i % 2][j];
				}
			}
			
			// Increment the topRow
			count++;
			if (count == 2) {
				count = 0;
				topRow += 2;
			}
		}
		
		// Turn the array into a "coded" String
		data = "";
		for (int i = 0; i < encryptedArray.length; i++) {
			for (int j = 0; j < encryptedArray[0].length; j++) {
				if (j == 0) {
					data += (int) encryptedArray[i][j] + "-";
				} else {
					data += (int) encryptedArray[i][j];
				}
			}
			data += "\t";
		}
		
		return data;
	}
	
	public String decrypt(String data) {
		
	}
    
    public void printArray(double[][] dataArray) {
    	String test = "[";
    	int count = 1;
		for (int i = 0; i < dataArray.length; i++) {
			for (int j = 0; j < dataArray[0].length; j++) {
				test += dataArray[i][j];
				
				if (count == 1) {
					count = 0;
					test += "\t";
				}
			}
			test += "]\n[";
			
			count++;
		}
		
		test = test.substring(0, test.length() - 2);
		
		System.out.println(test);
    }
    
    public boolean isEven(int i) {
    	if (i % 2 == 0) return true;
    	return false;
    }
}

public class APCS_208_Encryption {
	
    public static void main(String[] args) {
        Cypher cypher = new Cypher();
    }
    
}
