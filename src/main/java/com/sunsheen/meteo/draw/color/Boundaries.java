package com.sunsheen.meteo.draw.color;

public class Boundaries {
	private Boundary[] lines;
	private int size = 0;
	private double left = 1000, right = 0, top = 0, bottom = 1000;

	public Boundaries() {
		lines = new Boundary[1];
	}

	public Boundaries(int size) {
		lines = new Boundary[size];
	}

	public void add(Boundary line) {
		if (lines.length == size) {
			Boundary[] newLine = new Boundary[size + 1];
			System.arraycopy(lines, 0, newLine, 0, size);
			lines = newLine;
		}

		lines[size] = line;
		double x1 = line.left();
		double x2 = line.right();
		double y1 = line.top();
		double y2 = line.bottom();

		if (x1 < left) {
			left = x1;
		}
		if (x2 > right) {
			right = x2;
		}
		if (y1 > top) {
			top = y1;
		}
		if (y2 < bottom) {
			bottom = y2;
		}

		size++;
	}

	public int size() {
		return size;
	}

	public double left() {
		return left;
	}

	public double right() {
		return right;
	}

	public double top() {
		return top;
	}

	public double bottom() {
		return bottom;
	}

	public Boundary[] lines() {
		return lines;
	}

	public Boundary getLine(int i) {
		return lines[i];
	}

	public void print() {
		for (int i = 0; i < this.size; i++) {
			lines[i].print();
		}
		System.out.println("left:" + left + "; right:" + right + "; top:" + top
				+ ";  bottom:" + bottom);
	}
}
