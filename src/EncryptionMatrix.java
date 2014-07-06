
public class EncryptionMatrix {
	
	
	private final short MATRIXDIM = 6; // I hate magic numbers, always use constants
	
	
	// The encryption matrix is a 6x6 matrix. It contains all characters
	// and the numbers 0..9. SPACE is not needed, because it will always
	// be ignored. 
	private char [][] encryptionMatrix = new char [ MATRIXDIM ][ MATRIXDIM ];
	
	// Constructor
	public EncryptionMatrix( String passphrase ) {
		
		String charsForMatrixInsertion = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"; // these still need to be inserted into the matrix
		
		// Write the passphrase into the beginning of the matrix
	    for ( int count = 0; count < passphrase.length(); count++ ) {
	    	// Determine the coordinates for the character in the 
	    	// matrix: X is "count MOD 6" (width of the matrix),
	    	// Y is "count DIV 6" (depth of the matrix).
	    	MatrixPoint myPoint = new MatrixPoint( count, MATRIXDIM );
	    	
	    	// Now we know the square, and we fill it
	    	this.encryptionMatrix[ myPoint.getX() ][ myPoint.getY() ] = passphrase.charAt( count );
	    	
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
	    int count = passphrase.length(); // That number of characters has already been inserted above
	    for ( char c : charsForMatrixInsertion.toCharArray() ) {
	    	MatrixPoint myPoint = new MatrixPoint( count, MATRIXDIM );
	    	this.encryptionMatrix[ myPoint.getX() ][ myPoint.getY() ] = c;
	    	count++;
	    }
	}

	
    // This is the main method. Needs proper documentation.
	public String convertText( String inputText, CryptoDirection direction ) {
		
		String outputText = ""; // This is the result
		
		// Loop over the input string. Build pairs of characters.
		// Determine their position
		for ( int i = 0; i < inputText.length() / 2; i++ ) {
			char c1, c2;      // Characters to be compared
			
			c1 = inputText.charAt( i * 2 );
			c2 = inputText.charAt( ( i * 2 )+1 );
			MatrixPoint point1 = this.findPosition( c1 );
			MatrixPoint point2 = this.findPosition( c2 );
		
			// Now we have to look at the relative positions and 
			// do the encryption.
			outputText += this.createTargetCharPair( point1, point2, direction );
		
		}
		
		// If there is an uneven number of chars, the last one is moved
		// down / up one square
		if ( ( inputText.length() % 2 ) == 1 ) {
			char c;
			c = inputText.charAt( inputText.length() - 1 );
			MatrixPoint point = this.findPosition( c );
			outputText += this.encryptionMatrix[ point.getX() ][ ( point.getY() + direction.dirInd + MATRIXDIM ) % MATRIXDIM ];
		}
		
    	return outputText;
    }
	
	
	// Helper method that returns a string of 2 chars for the clear chars provided.
	// This result has to be added to the encrypted / decrypted string.
	private String createTargetCharPair ( MatrixPoint point1, MatrixPoint point2, CryptoDirection direction ) {	
		String result = "";
		
		// Determine the relative position of the 2 characters
		if ( ( point1.getX() != point2.getX() ) && ( point1.getY() != point2.getY() ) ) {
			// Both axes are different. Swap horizontally
			result += this.encryptionMatrix[ point2.getX() ][ point1.getY() ]; // Move c1 horizontally
			result += this.encryptionMatrix[ point1.getX() ][ point2.getY() ]; // Move c2 horizontally
		} else if ( ( point1.getX() == point2.getX() ) && ( point1.getY() != point2.getY() ) ) { 
			// On the same X axis. Move both chars down / up one space; roll over if needed
			result += this.encryptionMatrix[ point1.getX() ][ ( point1.getY() + direction.dirInd + MATRIXDIM ) % MATRIXDIM ];
			result += this.encryptionMatrix[ point2.getX() ][ ( point2.getY() + direction.dirInd + MATRIXDIM ) % MATRIXDIM ];
		} else if ( ( point1.getX() != point2.getX() ) && ( point1.getY() == point2.getY() ) ) {
			// On the same Y axis. Move right / left one space; roll over if needed
			result += this.encryptionMatrix[ ( point1.getX() + direction.dirInd + MATRIXDIM) % MATRIXDIM ][ point1.getY() ];
			result += this.encryptionMatrix[ ( point2.getX() + direction.dirInd + MATRIXDIM) % MATRIXDIM ][ point2.getY() ];
		} else if ( ( point1.getX() == point2.getX() ) && ( point1.getY() == point2.getY() ) ) {
			// Identical characters: move both one space down / up, roll over if needed
			result += this.encryptionMatrix[ point1.getX() ][ ( point1.getY() + direction.dirInd + MATRIXDIM) % MATRIXDIM ];
			result += this.encryptionMatrix[ point2.getX() ][ ( point2.getY() + direction.dirInd + MATRIXDIM) % MATRIXDIM ];
		} else {
			// SHIT this is not supposed to happen!
			System.out.println( "Clusterfuck; this coding should be completely unreachable!\n" );
		}
		return result;
	}
	
	
	
	// Helper method determines the MatrixPoint of a char.
	private MatrixPoint findPosition ( char c ) {
		MatrixPoint result = null;
		mainloop: 
		for ( int y = 0; y < MATRIXDIM; y++ )
			for ( int x = 0; x < MATRIXDIM; x++ )
				if ( this.encryptionMatrix[x][y] == c ) {
					result = new MatrixPoint( x, y );
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
    		System.out.print("\n");
    	}
		return;
    }

}
