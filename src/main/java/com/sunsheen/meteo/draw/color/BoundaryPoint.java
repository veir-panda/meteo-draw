package com.sunsheen.meteo.draw.color;

public class BoundaryPoint {
	private double x;
	private double y;

	public BoundaryPoint(String s) {
		String ss[] = s.split(" ");
		x = Double.valueOf(ss[0]);
		y = Double.valueOf(ss[1]);
	}

	public double x() {
		return x;
	}

	public double y() {
		return y;
	}
}
