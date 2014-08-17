import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class Testall {

	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	
	@Before
	public void setUpStreams () {
	    System.setOut(new PrintStream(outContent));
	}
	
	@After
	public void cleanUpStreams () {
		System.setOut(null);
	}
	
	// Test the encryption functionality
	@Test
	public void testEncrypt () {
		String[] args = new String[] {"vase", "E", "testtest"};
	    EncryptionMain.main(args);
	    assertTrue(outContent.toString().toLowerCase().contains("usg0u sg0"));
	}
	
	// Test the decryption functionality
	@Test
	public void testDecrypt () {
		String[] args = new String[] {"vase", "D", "USG0U SG0"};
		EncryptionMain.main(args);
		assertTrue(outContent.toString().toLowerCase().contains("testtest"));
	}

}
