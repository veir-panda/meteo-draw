package com.sunsheen.meteo.draw.color;

import java.awt.Color;

public class ColorCode {
	private double[] values;
	private Color[] colors;
	private int colorsize;
	private int valuesize;
	private String unit;

	public ColorCode() {
		values = new double[2];
		colors = new Color[1];
	}

	public ColorCode(int size) {
		values = new double[size + 1];
		colors = new Color[size];
	}

	public void addFirst(double v1, double v2, Color c) {
		values[0] = v1;
		values[1] = v2;
		colors[0] = c;

		colorsize = 1;
		valuesize = 2;
	}

	public void add(double v, Color c) {
		if (colors.length == colorsize) {
			Color[] newc = new Color[colorsize + 10];
			System.arraycopy(colors, 0, newc, 0, colorsize);
			colors = newc;
		}

		if (values.length == valuesize) {
			double[] newv = new double[valuesize + 10];
			System.arraycopy(values, 0, newv, 0, valuesize);
			values = newv;
		}

		values[valuesize] = v;
		colors[colorsize] = c;
		colorsize++;
		valuesize++;
	}

	public void add(String v, String c) {
		if (colors.length == colorsize) {
			Color[] newc = new Color[colorsize + 10];
			System.arraycopy(colors, 0, newc, 0, colorsize);
			colors = newc;
		}

		if (values.length == valuesize) {
			double[] newv = new double[valuesize + 10];
			System.arraycopy(values, 0, newv, 0, valuesize);
			values = newv;
		}

		values[valuesize] = Double.valueOf(v);

		String cc[] = c.split(",");
		colors[colorsize] = new Color(Integer.valueOf(cc[0]),
				Integer.valueOf(cc[1]), Integer.valueOf(cc[2]));

		valuesize++;
		colorsize++;
	}

	public void addLast(double v) {
		if (values.length == valuesize) {
			double[] newv = new double[valuesize + 10];
			System.arraycopy(values, 0, newv, 0, valuesize);
			values = newv;
		}

		values[valuesize] = Double.valueOf(v);
		valuesize++;
	}

	public void addList() {
		if (values.length == valuesize) {
			double[] newv = new double[valuesize + 10];
			System.arraycopy(values, 0, newv, 0, valuesize);
			values = newv;
		}

		values[valuesize] = values[valuesize - 1] + 10000;
		valuesize++;
	}

	public int getSize() {
		return colorsize;
	}

	public int getValueSize() {
		return valuesize;
	}

	public double getValue(int i) {
		return values[i];
	}

	public Color getColor(int i) {
		return colors[i];
	}

	public double[] getValues() {
		return values;
	}

	public Color[] getColors() {
		return colors;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getUnit() {
		return unit;
	}

	public void print() {
		System.out.println("Unit:" + this.unit);
		for (int i = 0; i < colorsize; i++) {
			System.out.println(values[i] + "    " + colors[i]);
		}
		if (valuesize > colorsize)
			System.out.println(values[valuesize - 1]);
	}
}
