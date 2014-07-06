
public enum CryptoDirection {
	ENCRYPT (1), DECRYPT(-1);
	int dirInd;
	CryptoDirection( int i ) {
		this.dirInd = i;
	}
}
