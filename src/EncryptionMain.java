
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;



public class EncryptionMain {
	
	// Stuff that can either be read from the console or the command line
	// (or derived, in the case of the direction) is stored here, so that
	// it can be set in different methods.
	private static String          passphrase = null;	
	private static String          inputText  = null;
	private static CryptoDirection direction  = null;

	public static void main( String[] args ) {
		
        // Command line argument processing (if applicable)
		if ( args != null ) {
			if ( !processArgs( args ) ) {
				return; // Failure :(
			}
		} else {
			// No command line => interactive.

			boolean isPassphraseOK = false;


			// Main process: read a clear text, encrypt it, return it.

			do {
				passphrase = getPassphrase( "" );
				isPassphraseOK = checkPassphrase( passphrase );	
			} while ( !isPassphraseOK );

			inputText = getInputtext( "" );

			direction = getDirection( "" );

		}
		
		// Conversion (encryption or decryption) and printout in one step
		printResultText( convertString( passphrase, inputText, direction ), direction );

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

		} else {
			s = commandlineText;
		}
		
		// Replace ß before conversion (otherwise it will not work)
		s = s.replaceAll( "ß", "SS" );
		
		// Convert to upper case and remove spaces
		s = s.toUpperCase();
		s = s.replaceAll(" ","");
		
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
	private static String convertString( String passphrase, String inputText, CryptoDirection direction ) {
        if ( direction == CryptoDirection.ENCRYPT )
        	System.out.println( "Text to be encrypted:\n" + inputText + "\n" );
        else
        	System.out.println( "Text to be decrypted:\n" + inputText + "\n" );

        // Elegant Java: create the matrix, convert the input, catch the output and return
        // in one single go. #iLike
        return new EncryptionMatrix( passphrase ).convertText( inputText, direction );
	}
	
	
	// Output. Note that this could be changed to provide the output in a GUI.
	private static void printResultText( String outputText, CryptoDirection direction ) {
		System.out.println( "Result Text:" );
		System.out.println( getResultText( outputText, direction) );
	}
	
	// Get the result text.
	private static String getResultText( String outputText, CryptoDirection direction ) {
		
		String result = "";
		
		// Output in chunks of 5 characters separated by space; if not enough left, the rest will be printed.
		// We only print the stuff in chunks of 5 if we print encrypted text.
		// Clear text is displayed in one long string.
		if ( direction == CryptoDirection.DECRYPT ) {
			result = outputText;
		} else {
			while ( outputText.length() >= 5 ){
				result += outputText.substring( 0, 5 ) + " ";
				outputText = outputText.substring( 5 );
			}
			result += outputText ;
		}
		return result;
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

	
	// If called from the command line the first three arguments must be passphrase, direction and
	// inputtext. If all is fine, we use this one.
	// Return TRUE if successful, so we can decrypt and print the result.
	private static boolean processArgs( String[] args ) {
		if ( args.length != 3 ) {
			// Wrong call
			printCommandLineUserhelp();
			return false;
		}
		
		if ( checkStringContent( args[ 0 ] ) && checkPassphrase( args[ 0 ] ) ) {
			passphrase = getPassphrase( args[0] );
		} else {
			printCommandLineUserhelp();
			return false;
		}
		if ( ( args[ 1 ].charAt( 0 ) == 'D' ) ||
			 ( args[ 1 ].charAt( 0 ) == 'd' ) ||
			 ( args[ 1 ].charAt( 0 ) == 'E' ) ||
			 ( args[ 1 ].charAt( 0 ) == 'e' ) ) {
			direction  = getDirection( args[1] );
		} else {
			printCommandLineUserhelp();
			return false;
		}
		inputText  = getInputtext( args[2] );
		return true;
	}
	
	
	// Standard user help for command line access.
	private static void printCommandLineUserhelp( ) {
		System.out.println( "Wrong call! Please call the program like that:" );
		System.out.println( "java EncryptionMain <passphrase> <direction> <inputtext>" );
		System.out.println( "where <direction> is D for decryption or E for encryption." );
		System.out.println( "Also note that you have to use \" in case of text with spaces!" );
	}

}
