package com.wyverngame.terraingenerator.noise;

public final class Noise {
	private final float a, b;

	public Noise(float a, float b) {
		this.a = a;
		this.b = b;
	}

	public double noise(double x, double y) {
		double px = Math.floor(x);
		double py = Math.floor(y);

		double fx = fract(x);
		double fy = fract(y);

		fx *= fx * (3 - (2 * fx));
		fy *= fy * (3 - (2 * fy));

		double rx = mix(hash(px, py), hash(px + 1f, py), fx);
		double ry = mix(hash(px, py + 1f), hash(px + 1f, py + 1f), fx);

		return mix(rx, ry, fy);
	}

	private double hash(double x, double y) {
		double px = fract(x / (3.07965 + a));
		double py = fract(y / (7.4235 + b));

		double dot = ((px * (py + 201.21)) + (py * (px + 201.21)));

		px += dot;
		py += dot;

		return fract(px * py);
	}

	private double mix(double x, double y, double a) {
		return (x * (1 - a)) + (y * a);
	}

	private double fract(double value) {
		return value - Math.floor(value);
	}
}
