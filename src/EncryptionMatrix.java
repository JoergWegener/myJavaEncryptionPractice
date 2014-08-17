
public class EncryptionMatrix {
	
	
	private final short MATRIXDIM = 6; // I hate magic numbers, always use constants
	
	
	// The encryption matrix is a 6x6 matrix. It contains all characters
	// and the numbers 0..9. SPACE is not needed, because it will always
	// be ignored. 
	// Note that this is the MENTAL representation of the algorithm! The technical
	// one is one where there is one linear string, and the X and Y coordinates are
	// calculated from the one-dimensional position when needed on the basis of the
	// position and the matrix dimension.
	private String encryptionString = null;
	
	// Constructor
	public EncryptionMatrix (String passphrase) {
		
		String charsForMatrixInsertion = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"; // these still need to be inserted into the matrix
		
		// Write the passphrase into the beginning of the matrix
		this.encryptionString = passphrase;
		
		// Delete the characters from the passphrase from the string of 
		// characters yet to be added (they have already been added, at the start).
		for (char c : passphrase.toCharArray()) {
			charsForMatrixInsertion = charsForMatrixInsertion.replaceAll(String.valueOf(c), "");
		}
		
		// Now add the rest of the characters NOT contained in the passphrase to the
		// encryption string.
		this.encryptionString += charsForMatrixInsertion;
		
	}
	
	
	// Method that reads a char from a MatrixPoint. Useful for encryption.
	private char getChar (MatrixPoint point) {
		return encryptionString.charAt(point.getY() * MATRIXDIM + point.getX());
	}

	
    // This is the main method. Needs proper documentation.
	public String convertText (String inputText, CryptoDirection direction) {
		
		String outputText = ""; // This is the result
		
		// Loop over the input string. Build pairs of characters.
		// Determine their position
		for (int i = 0; i < inputText.length() / 2; i++) {
			char c1, c2;      // Characters to be compared
			
			c1 = inputText.charAt(i * 2);
			c2 = inputText.charAt((i * 2)+1);
			MatrixPoint point1 = this.findPosition(c1);
			MatrixPoint point2 = this.findPosition(c2);
		
			// Now we have to look at the relative positions and 
			// do the encryption.
			outputText += this.createTargetCharPair(point1, point2, direction);
		
		}
		
		// If there is an uneven number of chars, the last one is moved
		// down / up one square
		if ((inputText.length() % 2) == 1) {
			char c;
			c = inputText.charAt(inputText.length() - 1);
			MatrixPoint point = this.findPosition(c);
			MatrixPoint newPoint = new MatrixPoint(point.getX(), ( point.getY() + direction.dirInd + MATRIXDIM ) % MATRIXDIM);
			outputText += String.valueOf(getChar( newPoint));
		}
		
    	return outputText;
    }
	
	
	// Helper method that returns a string of 2 chars for the clear chars provided.
	// This result has to be added to the encrypted / decrypted string.
	private String createTargetCharPair (MatrixPoint point1, MatrixPoint point2, CryptoDirection direction) {	
		String result = "";
		// Determine the relative position of the 2 characters
		if ((point1.getX() != point2.getX()) && (point1.getY() != point2.getY())) {
			// Both axes are different. Swap horizontally
			result += String.valueOf(this.getChar(new MatrixPoint( point2.getX(), point1.getY()))); // move c1 horizontally
            result += String.valueOf(this.getChar(new MatrixPoint( point1.getX(), point2.getY() ) ) ); // move c2 horizontally
		} else if ((point1.getX() == point2.getX()) && (point1.getY() != point2.getY())) { 
			// On the same X axis. Move both chars down / up one space; roll over if needed
			result += String.valueOf(this.getChar(new MatrixPoint(point1.getX(), (point1.getY() + direction.dirInd + MATRIXDIM) % MATRIXDIM)));
			result += String.valueOf(this.getChar(new MatrixPoint(point2.getX(), (point2.getY() + direction.dirInd + MATRIXDIM) % MATRIXDIM)));
		} else if ((point1.getX() != point2.getX()) && (point1.getY() == point2.getY())) {
			// On the same Y axis. Move right / left one space; roll over if needed
			result += String.valueOf(this.getChar(new MatrixPoint(( point1.getX() + direction.dirInd + MATRIXDIM) % MATRIXDIM, point1.getY())));
			result += String.valueOf(this.getChar(new MatrixPoint(( point2.getX() + direction.dirInd + MATRIXDIM) % MATRIXDIM, point2.getY())));
		} else if ((point1.getX() == point2.getX()) && (point1.getY() == point2.getY())) {
			// Identical characters: move both one space down / up, roll over if needed
			result += String.valueOf(this.getChar(new MatrixPoint(point1.getX(), (point1.getY() + direction.dirInd + MATRIXDIM) % MATRIXDIM)));
			result += String.valueOf(this.getChar(new MatrixPoint(point2.getX(), (point2.getY() + direction.dirInd + MATRIXDIM) % MATRIXDIM)));
		} else {
			// SHIT this is not supposed to happen!
			System.out.println("Clusterfuck; this coding should be completely unreachable!\n");
		}		
		return result;
	}
	
	
	
	// Helper method determines the MatrixPoint of a char.
	private MatrixPoint findPosition (char c) {
		int position = encryptionString.indexOf( String.valueOf(c));
		return new MatrixPoint(position, MATRIXDIM);
	}
    
	// For testing purposes only!
	public void printMatrix () {
		for (int y = 0; y < MATRIXDIM; y++) {
			System.out.println(encryptionString.substring(y * MATRIXDIM, y * MATRIXDIM + MATRIXDIM - 1) + "\n");
		}
		return;
    }

}
