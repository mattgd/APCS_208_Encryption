/**
 * @(#)APCS_208_Encryption.java
 *
 * @author Matt Dzwonczyk
 * @version 1.00 2015/10/30
 */

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

class Cypher {
	
	private double[][] key = { {1, 2}, {2, 8} };
	private String message = "";
	
	Cypher() {		
		try {
    		Scanner sc = new Scanner(new FileReader("secrets.txt"));
    		
    		while (sc.hasNextLine()) this.message += sc.nextLine();
        	
        	sc.close();
    	} catch (FileNotFoundException e) {
    		e.printStackTrace();
    	}
    	
    	message = decrypt(encrypt(message));
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
		double[][] encryptedArray = multiply(dataArray, (double[][]) key);
		
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
		String validChars = "0123456789- ";
		String selectedChar;
		String nextCode = "";
		int topRow = 0; // Will be used later to keep track of which section the matrix is being multiplied by

		int count = 0;
		for (int i = 0; i < data.length(); i++) {
			if (data.charAt(i) == '-') {
				count++;
			}
		}
		
		double[][] dataArray = new double[count][2];
		count = 0; // Now used to keep track of Array index
		
		for (int i = 0; i < data.length(); i++) {
			selectedChar = "" + data.charAt(i);
			
			// Remove unwated characters in front of number data
			if (validChars.contains(selectedChar)) {
				nextCode += selectedChar;
				data.replaceFirst(selectedChar, "");
				
				if (!validChars.contains("" + data.charAt(i + 1))) {
					dataArray[count][0] = Double.parseDouble(nextCode.substring(0, nextCode.indexOf("-")));
					dataArray[count][1] = Double.parseDouble(nextCode.substring(nextCode.indexOf("-") + 1));
					nextCode = "";
					count++;
				}
			} else {
				data.replaceFirst(selectedChar, "");
			}
		}
		
		// Decrypt
		double[][] inverse = { {2, -.5}, {-.5, .25} };
		double[][] decryptedArray = multiply(dataArray, inverse);
		
		// Convert to letters
		char currentChar;
		data = "";
		
		for (int i = 0; i < decryptedArray.length; i++) {
			for (int j = 0; j < decryptedArray[0].length; j++) {
				if (decryptedArray[i][j] == 27) {
					currentChar = ' ';
				} else {
					currentChar = (char) (decryptedArray[i][j] + 64);
				}
				
				data += currentChar;
			}
		}
		
		data = data.substring(0, data.length() - 1); // Remove end-of-line character
		
		return data;
	}
	
	// return C = A * B
    public static double[][] multiply(double[][] A, double[][] B) {
        int mA = A.length;
        int nA = A[0].length;
        int mB = B.length;
        int nB = B[0].length;
        if (nA != mB) throw new RuntimeException("Illegal matrix dimensions.");
        double[][] C = new double[mA][nB];
        for (int i = 0; i < mA; i++)
            for (int j = 0; j < nB; j++)
                for (int k = 0; k < nA; k++)
                    C[i][j] += A[i][k] * B[k][j];
        return C;
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
    
    public String getMessage() {
    	return message;
    }
}

public class APCS_208_Encryption {
	
    public static void main(String[] args) {
        Cypher cypher = new Cypher();
        System.out.println(cypher.getMessage());
    }
    
}
