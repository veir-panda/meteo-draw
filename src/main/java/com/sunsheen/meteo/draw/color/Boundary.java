package com.sunsheen.meteo.draw.color;

public class Boundary {
	private BoundaryPoint[] line;
	private int size = 0;
	private double left = 1000, right = 0, top = 0, bottom = 1000;

	public Boundary() {
		line = new BoundaryPoint[100];
	}

	public void add(BoundaryPoint bp) {
		if (line.length == size) {
			BoundaryPoint[] newLine = new BoundaryPoint[size + 100];
			System.arraycopy(line, 0, newLine, 0, size);
			line = newLine;
		}

		line[size] = bp;
		double x = bp.x();
		double y = bp.y();

		if (x < left) {
			left = x;
		}
		if (x > right) {
			right = x;
		}
		if (y > top) {
			top = y;
		}
		if (y < bottom) {
			bottom = y;
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

	public BoundaryPoint[] line() {
		return line;
	}

	public BoundaryPoint getPoint(int i) {
		return line[i];
	}

	public void print() {
		String ss = "";
		for (int i = 0; i < this.size; i++) {
			ss += line[i].x() + " " + line[i].y();
			if (i == 0) {
				ss += ",";
			}
		}
		System.out.println(ss);
		System.out.println("left:" + left + "; right:" + right + "; top:" + top
				+ ";  bottom:" + bottom);
	}
}
