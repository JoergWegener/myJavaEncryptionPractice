/**
 * This class just represents a point. A point has an x and and a y axis.
 * That's it, folks!
 * 
 */

/**
 * @author joergw
 *
 */
public final class MatrixPoint {
	private int x;
	private int y;
	
	
	// Possibly the most trivial constructor in IT history.
	public MatrixPoint (int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	
	// We can also calculate a MatrixPoint from a counter (running number) 
	// and the matrix dimensions
	public MatrixPoint (int count, short matrixDimension) {
		this.x = count % matrixDimension;
		this.y = count / matrixDimension;
	}
	
	
	// Embarrassing, the primitive coding, but hey...
	public int getX () {
		return this.x;
	}
	
	
	public int getY () {
		return this.y;
	}

}
