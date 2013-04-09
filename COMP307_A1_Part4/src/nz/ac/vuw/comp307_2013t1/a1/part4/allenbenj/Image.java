package nz.ac.vuw.comp307_2013t1.a1.part4.allenbenj;

import java.io.BufferedReader;
import java.io.EOFException;

public class Image {

	private int rows, cols;
	private boolean[][] data;
	private String category;

	public Image(BufferedReader br) throws Exception {
		br.readLine();
		category = br.readLine().substring(1).toLowerCase();
		String[] dim = br.readLine().split("\\s+");
		rows = Integer.parseInt(dim[0]);
		cols = Integer.parseInt(dim[1]);
		data = new boolean[rows][cols];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				int c = br.read();
				while (c != '1' && c != '0') {
					if (c < 0) throw new EOFException();
					c = br.read();
				}
				data[i][j] = c == '1';
			}
		}
		br.readLine();
	}

	public int getRows() {
		return rows;
	}

	public int getCols() {
		return cols;
	}

	public String getCategory() {
		return category;
	}

	public boolean get(int r, int c) {
		return data[r][c];
	}

}
