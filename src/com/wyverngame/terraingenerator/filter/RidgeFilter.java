package com.wyverngame.terraingenerator.filter;

import java.util.Random;

import com.wyverngame.terraingenerator.Map;
import com.wyverngame.terraingenerator.SurfaceMesh;
import com.wyverngame.terraingenerator.noise.HashNoise;

public final class RidgeFilter extends Filter {
	private final int delta;

	public RidgeFilter(boolean large) {
		delta = large ? 250 : 215;
	}

	@Override
	public void apply(Map map) {
		Random rng = new Random(map.getSeed() + 34571);
		HashNoise noise = new HashNoise(rng.nextFloat(), rng.nextFloat());

		float[][] params = new float[2][3];
		for (int x = 0; x < 2; x++) {
			for (int y = 0; y < 3; y++) {
				params[x][y] = rng.nextFloat();
			}
		}

		SurfaceMesh surface = map.getSurface();

		for (int x = 0; x < map.getSize(); x++) {
			for (int y = 0; y < map.getSize(); y++) {
				int m = 0;
				for (int pass = 0; pass < 3; pass++) {
					m += (int) gen(x, y, noise, params[0][pass], params[1][pass], 1.2f / (1f + pass), 5f, 16f / (1f + pass));
				}

				surface.setHigher(x, y, (int) Math.pow(m, 1.05) - 300);
			}
		}
	}

	private double gen(double x, double y, HashNoise noise, float a, float b, float scale, float power, float height) {
		x *= 0.03 / scale;
		y *= 0.03 / scale;

		for (int i = 0; i < 5; i++) {
			double nx = (1.2323f * x) + (1.999231f * y);
			double ny = (-1.999231f * x) + (1.22f * y);

			x = nx;
			y = ny;

			x += a * 919.19;
			y += b * 191.91;
		}

		double ff = Math.pow(noise.noise(x * 0.002f, y * 0.002f), 6) * 2 - 1;
		return (delta - Math.abs(Math.abs(ff) * 170)) * height * scale * (1 - Math.pow(noise.noise(x * 0.008f, y * 0.008f), 1.85));
	}
}
