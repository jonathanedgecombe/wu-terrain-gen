package com.wyverngame.terraingenerator.filter;

import java.util.Random;

import com.wyverngame.terraingenerator.Map;
import com.wyverngame.terraingenerator.SurfaceMesh;
import com.wyverngame.terraingenerator.noise.HashNoise;

public final class NoiseFilter extends Filter {
	private final static double POW = 1.1;
	private final static double MUL = 50;
	private final static double RED = 0.5;
	private final static int ITERATIONS = 5;

	@Override
	public void apply(Map map) {
		Random rng = new Random(map.getSeed());
		float a = rng.nextFloat();
		float b = rng.nextFloat();
		HashNoise noise = new HashNoise(a, b);

		SurfaceMesh surface = map.getSurface();

		for (int x = 0; x < map.getSize(); x++) {
			for (int y = 0; y < map.getSize(); y++) {
				surface.addHeight(x, y, (int) gen(x, y, noise, a, b));
			}
		}
	}

	private double gen(double x, double y, HashNoise noise, float a, float b) {
		x *= 0.03;
		y *= 0.03;

		double w = (noise.noise(x * 0.25, y * 0.25) * 0.75) + 0.15;

		w *= Math.pow(w, POW) * MUL;

		double f = 0;

		for (int i = 0; i < ITERATIONS; i++) {
			if (i != 0) f += w * noise.noise(x, y);
			w *= -RED;

			double nx = (1.3623f * x) + (1.7531f * y);
			double ny = (-1.7131f * x) + (1.4623f * y);

			x = nx;
			y = ny;

			x += a * 919.19;
			y += b * 191.91;
		}

		return f * 32;
	}
}
