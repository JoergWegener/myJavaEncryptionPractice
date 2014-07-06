
public class EncryptionMatrix {
	
	
	private final int MATRIXDIM = 6; // I hate magic numbers, always use constants
	
	
	// The encryption matrix is a 6x6 matrix. It contains all characters
	// and the numbers 0..9. SPACE is not needed, because it will always
	// be translated into SPACE. Yes, this is a weakness, but hey, this
	// encryption algorithm is not designed to withstand the NSA. It is
	// meant for me to practice my Java, nothing more, nothing less.
	private char [][] encryptionMatrix = new char [MATRIXDIM][MATRIXDIM];
	
	// Constructor
	public EncryptionMatrix( String passphrase ) {
		
		String charsForMatrixInsertion = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"; // these still need to be inserted into the matrix
		
		// Write the passphrase into the beginning of the matrix
	    for ( int count = 0; count < passphrase.length(); count++ ) {
	    	// Determine the coordinates for the character in the 
	    	// matrix: X is "count MOD 6" (width of the matrix),
	    	// Y is "count DIV 6" (depth of the matrix).
	    	int x = count % MATRIXDIM;
	    	int y = count / MATRIXDIM;
	    	
	    	// Now we know the square, and we fill it
	    	this.encryptionMatrix[x][y] = passphrase.charAt(count);
	    	
	    	// The character is already used, so we remove it from the string that
	    	// contains the still-to-be-inserted characters:
	    	for ( int i = 0; i < charsForMatrixInsertion.length(); i++ ) {
	    		if ( charsForMatrixInsertion.charAt( i ) == passphrase.charAt( count ) ) {
	    			charsForMatrixInsertion = charsForMatrixInsertion.substring( 0,  i ) + charsForMatrixInsertion.substring( i+1 );
	    			break; // There is only ever one hit, so we can leave the loop here
	    		}
	    	}
	    }
	    
	    // Now we add all the characters and numbers that were NOT part of 
	    // the passphrase
	    int count = passphrase.length();
	    for ( char c : charsForMatrixInsertion.toCharArray() ) {
	    	int x = count % MATRIXDIM;
	    	int y = count / MATRIXDIM;
	    	this.encryptionMatrix[x][y] = c;
	    	count++;
	    }
	}

	
    // This is the main method. Needs proper documentation.
	public String encryptText( String clearText, CryptoDirection direction ) {
		
		String cryptoText = ""; // This is the result
		
		// Loop over the input string. Build pairs of characters.
		// Determine their position
		for ( int i = 0; i < clearText.length() / 2; i++ ) {
			char c1, c2;      // Characters to be compared
			int x1, y1;       // Coordinates of the first character
			int x2, y2;       // Coordinates of the second character
			int temp1, temp2; // temps for the determination of the coordinates
			
			c1 = clearText.charAt( i * 2 );
			c2 = clearText.charAt( ( i * 2 )+1 );
			temp1 = this.findPosition( c1 );
			temp2 = this.findPosition( c2 );
			x1 = temp1 / 10; y1 = temp1 % 10;
			x2 = temp2 / 10; y2 = temp2 % 10;
		
			// Now we have to look at the relative positions and 
			// do the encryption.
			cryptoText += this.createTargetCharPair( x1, y1, x2, y2, direction );
		
		}
		
		// If there is an uneven number of chars, the last one is moved
		// down / up one square
		if ( ( clearText.length() % 2 ) == 1 ) {
			char c;
			int temp;
			c = clearText.charAt( clearText.length() - 1 );
			temp = this.findPosition( c );
			cryptoText += this.encryptionMatrix[ temp / 10 ][ ( ( temp % 10 ) + direction.dirInd + MATRIXDIM ) % MATRIXDIM ];
		}
		
    	return cryptoText;
    }
	
	
	// Helper method that returns a string of 2 chars for the clear chars provided.
	// This result has to be added to the encrypted / decrypted string.
	private String createTargetCharPair ( int x1, int y1, int x2, int y2, CryptoDirection direction ) {
		String result = "";
		
		// Determine the relative position of the 2 characters
		if ( ( x1 != x2 ) && ( y1 != y2 ) ) {
			// Both axes are different. Swap horizontally
			result += this.encryptionMatrix[ x2 ][ y1 ]; // Move c1 horizontally
			result += this.encryptionMatrix[ x1 ][ y2 ]; // Move c2 horizontally
		} else if ( ( x1 == x2 ) && ( y1 != y2) ) { 
			// On the same X axis. Move both chars down / up one space; roll over if needed
			result += this.encryptionMatrix[ x1 ][ (y1 + direction.dirInd + MATRIXDIM) % MATRIXDIM ];
			result += this.encryptionMatrix[ x2 ][ (y2 + direction.dirInd + MATRIXDIM) % MATRIXDIM ];
		} else if ( (x1 != x2 ) && ( y1 == y2 ) ) {
			// On the same Y axis. Move right / left one space; roll over if needed
			result += this.encryptionMatrix[ (x1 + direction.dirInd + MATRIXDIM) % MATRIXDIM ][ y1 ];
			result += this.encryptionMatrix[ (x2 + direction.dirInd + MATRIXDIM) % MATRIXDIM ][ y2 ];
		} else if ( ( x1 == x2 ) && ( y1 == y2 ) ) {
			// Identical characters: move both one space down / up, roll over if needed
			result += this.encryptionMatrix[ x1 ][ (y1 + direction.dirInd + MATRIXDIM) % MATRIXDIM ];
			result += this.encryptionMatrix[ x2 ][ (y2 + direction.dirInd + MATRIXDIM) % MATRIXDIM ];
		} else {
			// SHIT this is not supposed to happen!
			System.out.println( "Clusterfuck; this coding should be completely unreachable!\n" );
		}
		return result;
	}
	
	
	
	// Helper method. The tens determine the X position, the Ones the Y position
	// (How I HATE it that Java does not have structs, or export parameters!)
	private int findPosition ( char c ) {
		int result = 0;
		mainloop: 
		for ( int y = 0; y < MATRIXDIM; y++ )
			for ( int x = 0; x < MATRIXDIM; x++ )
				if ( this.encryptionMatrix[x][y] == c ) {
					result = ( 10*x ) + y;
					break mainloop; // Only one hit possible, so we can leave the loop
				}
		return result;
	}
    
	// For testing purposes only!
	public void printMatrix () {
    	for ( int y = 0; y < MATRIXDIM; y++ ) {
    		for ( int x = 0; x < MATRIXDIM; x++ ) {
    			System.out.print( this.encryptionMatrix[x][y] );
    		}
    		System.out.println("\n");
    	}
		return;
    }

}
