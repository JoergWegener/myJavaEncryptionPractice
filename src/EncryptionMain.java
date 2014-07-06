
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class EncryptionMain {

	public static void main(String[] args) {
		
		String passphrase  = null;
		
		boolean isPassphraseOK = false;
		
		
		// Main process: read a clear text, encrypt it, return it.
		
		do {
			passphrase = getPassphrase( "" );
			isPassphraseOK = checkPassphrase( passphrase );	
		} while ( !isPassphraseOK );
				
		String inputText = getInputtext( "" );
		
		CryptoDirection direction = getDirection( "" );
		
		String outputText = convertString( passphrase, inputText, direction );
		
		printResultText( outputText );

	}
	
	
	// Read a string from the console. NOTE: This could be
	// changed in a later version to allow a GUI input.
	private static String getPassphrase ( String commandlineText ) {
		
		boolean isStringOk = false;
		String s = "";
		
		if ( commandlineText == null || commandlineText == "" ) {
			System.out.println( "Please enter the passphrase:\n" );
			do {

				try {
					BufferedReader bufferread = new BufferedReader( new InputStreamReader( System.in ) );
					s = bufferread.readLine().toString();
				}
				catch ( IOException e ) {
					e.printStackTrace();
				}

				isStringOk = checkStringContent(s);

			} while ( !isStringOk );

		} else {
			s = commandlineText;
		}

		// Remove spaces and convert to upper case
		s = s.toUpperCase();
		s = s.replaceAll( " ", "" );
		
		return s;
	}
	
	
	// Read the direction. Convert to the proper data type
	// "Direction" means whether to encrypt or decrypt.
	private static CryptoDirection getDirection( String commandlineText ) {
			
		String s = "";
		char c; // first character of the string
		
		if ( commandlineText == null || commandlineText == "" ) {
			
			System.out.println( "Please enter the direction: 'E' for encryption, 'D' for decryption:");
			
			do {

				try {
					BufferedReader bufferread = new BufferedReader( new InputStreamReader( System.in ) );
					s = bufferread.readLine().toString();
				}
				catch ( IOException e ) {
					e.printStackTrace();
				}
				c = s.charAt( 0 );

			} while ( (c != 'D') && (c != 'd') && (c != 'E') && (c != 'e') );

		} else { 
			s = commandlineText;
		}
		
		if ( s.charAt( 0 ) == 'D' || s.charAt( 0 ) == 'd')
			return CryptoDirection.DECRYPT;
		else
			return CryptoDirection.ENCRYPT;
	}
	
	
	// Read input text; check that only characters, space, and numbers are used.
	// Repeat until input is correct.
	private static String getInputtext ( String commandlineText ) {
		
		boolean isStringOk = false;
		String s = "";
		
		if ( commandlineText == null || commandlineText == "" ) {
			
			System.out.println( "Please enter the input text:\n" );		
			
			do {

				try {
					BufferedReader bufferread = new BufferedReader( new InputStreamReader( System.in ) );
					s = bufferread.readLine().toString();
				}
				catch ( IOException e ) {
					e.printStackTrace();
				}

				isStringOk = checkStringContent( s );

			} while ( !isStringOk );

		} 
		else 
			s = commandlineText;
		
		// Replace ß before conversion (otherwise it will not work)
		s = s.replaceAll( "ß", "SS" );
		
		// Convert to upper case and remove spaces
		s = s.toUpperCase();
		s = s.replaceAll(" ","");
		//s.replaceAll("\\s", "" );
		
		// Now convert the rest of the umlauts
		s = s.replaceAll( "Ä", "AE" );
		s = s.replaceAll( "Ö", "OE" );
		s = s.replaceAll( "Ü", "UE" );
		
		return s;
	}
	
	
	// Check the passphrase. The only rule in our algorithm is
	// that no character may appear more than once.
	private static boolean checkPassphrase( String passphrase ) {
		
		for ( int i = 0; i < passphrase.length()-1; i++ ) {
			for ( int j = i+1; j < passphrase.length(); j++ ) {
				if ( passphrase.charAt( i ) == passphrase.charAt( j ) ) {
					System.out.println( "The passphrase must contain each character a maximum of one time.\n" );
					return false;
				}
			}
		}
		
		return true;		
	}
	
	
	// Encryption functionality
	private static String convertString( String passphrase, String clearText, CryptoDirection direction ) {
        EncryptionMatrix myMatrix = new EncryptionMatrix( passphrase );
		
		// DEBUG
        myMatrix.printMatrix();
        System.out.println( "Text to be encrypted:\n" + clearText );

		return myMatrix.encryptText( clearText, direction );
	}
	
	
	// Output. Note that this could be changed to provide the output in a GUI.
	private static void printResultText( String cryptotext ) {
		System.out.println( "Result Text:\n" );
		System.out.println( cryptotext );
		System.out.println( "And now in chunks of 5 chars:\n" );
		while ( cryptotext.length() >= 5 ){
			System.out.print( cryptotext.substring( 0, 5 ) + " ");
			cryptotext = cryptotext.substring( 5 );
		}
		System.out.println( cryptotext );
	}

    
	// Only characters, numbers and space allowed in passphrases and cleartexts.
	// Check this and provide a hint to the user if violated.
	private static boolean checkStringContent( String str ) {
        if ( str == null ) {
        	System.out.println( "Please enter at least one character or number!\n" );
            return false;
        }
        for ( int i = 0; i < str.length(); i++ ) {
            if ( ( Character.isLetterOrDigit( str.charAt( i ) ) == false ) && ( str.charAt( i ) != ' ') ) {
            	System.out.println( "Please only use characters and numbers. No symbols or Umlauts allowed.\n" );
                return false;
            }
        }
        return true;
    }    

}
