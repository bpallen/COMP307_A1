package nz.ac.vuw.comp307_2013t1.a1.part4.allenbenj;

import java.util.Random;

public class Feature {

	private int[] row = new int[4];
	private int[] col = new int[4];
	private boolean[] sgn = new boolean[4];
	
	public Feature() {
		// randomly generate, assuming all images are 10 x 10
		Random r = new Random();
		for (int i = 0; i < 4; i++) {
			row[i] = r.nextInt(10);
			col[i] = r.nextInt(10);
			sgn[i] = r.nextBoolean();
		}
	}
	
	public double value(Image img) {
		int sum = 0;
		for (int i = 0; i < 4; i++) {
			if (img.get(row[i], col[i]) == sgn[i]) sum++;
		}
		return sum >= 3 ? 1 : 0;
	}
	
}
