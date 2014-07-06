
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class EncryptionMain {

	public static void main(String[] args) {
		
		String passphrase  = null;
		String cleartext   = null;
		String cryptotext  = null;
		
		boolean isPassphraseOK = false;
		
		
		// Main process: read a clear text, encrypt it, return it.
		// TODO: Branch, so that the method can both encrypt and decrypt!
		// TODO: In order to do so, this functionality needs to be encapsulated, and
		// TODO: the main method must query the intend of the caller.
		
		do {
			passphrase = getPassphrase();
			isPassphraseOK = checkPassphrase( passphrase );	
		} while ( !isPassphraseOK );
				
		cleartext = getCleartext();
		
		cryptotext = encryptString( passphrase, cleartext );
		
		printCryptotext( cryptotext );

	}
	
	
	// Read a string from the console. NOTE: This could be
	// changed in a later version to allow a GUI input.
	private static String getPassphrase () {
		
		boolean isStringOk = false;
		String s = "";
		
		System.out.println( "Bitte das Kennwort eingeben:\n" );
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

		// Remove spaces and convert to upper case
		s = s.toUpperCase();
		s.replaceAll( " ", "" );
		
		return s;
	}
	
	
	// Read input text; check that only characters, space, and numbers are used.
	// Repeat until input is correct.
	private static String getCleartext () {
		
		boolean isStringOk = false;
		String s = "";
		
		System.out.println( "Bitte den Klartext eingeben:\n" );
		
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
		
		// Replace ß before conversion (otherwise it will not work)
		s.replaceAll( "ß", "SS" );
		
		// Convert to upper case and remove spaces
		s = s.toUpperCase();
		s.replaceAll( " ", "" );
		
		// Now convert the rest of the umlauts
		s.replaceAll( "Ä", "AE" );
		s.replaceAll( "Ö", "OE" );
		s.replaceAll( "Ü", "UE" );
		
		return s;
	}
	
	
	// Check the passphrase. The only rule in our algorithm is
	// that no character may appear more than once.
	private static boolean checkPassphrase( String passphrase ) {
		
		for ( int i = 0; i < passphrase.length()-1; i++ ) {
			for ( int j = i+1; j < passphrase.length(); j++ ) {
				if ( passphrase.charAt( i ) == passphrase.charAt( j ) ) {
					System.out.println( "Das Kennwort muss aus unterschiedlichen Zeichen bestehen.\n" );
					return false;
				}
			}
		}
		
		return true;		
	}
	
	
	// Encryption functionality
	private static String encryptString( String passphrase, String clearText ) {
        EncryptionMatrix myMatrix = new EncryptionMatrix( passphrase );
		
		// DEBUG
        myMatrix.printMatrix();

		return myMatrix.encryptText( clearText );
	}
	
	
	// Output. Note that this could be changed to provide the output in a GUI.
	private static void printCryptotext( String cryptotext ) {
		System.out.println( "Verschlüsselter Text:\n" );
		System.out.println( cryptotext );
		System.out.println( "Und jetzt in 5er Gruppen:\n" );
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
        	System.out.println( "Bitte mindestens 1 Zeichen oder Zahl eingeben!\n" );
            return false;
        }
        for ( int i = 0; i < str.length(); i++ ) {
            if ( ( Character.isLetterOrDigit( str.charAt( i ) ) == false ) && ( str.charAt( i ) != ' ') ) {
            	System.out.println( "Bitte lediglich Zeichen, Zahlen und Leerzeichen eingeben! Umlaute sind nicht zulässig\n" );
                return false;
            }
        }
        return true;
    }    

}
