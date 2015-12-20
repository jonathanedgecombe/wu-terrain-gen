package com.wyverngame.terraingenerator.filter;

import java.util.Random;

import com.wyverngame.terraingenerator.Map;
import com.wyverngame.terraingenerator.SurfaceMesh;
import com.wyverngame.terraingenerator.noise.HashNoise;

public final class MountainFilter extends Filter {
	private final static float POWER = 8;
	private final float height;
	private final float scale;

	public MountainFilter(boolean large) {
		height = large ? 52 : 48;
		scale = large ? 1.05f : 1;
	}

	@Override
	public void apply(Map map) {
		Random rng = new Random(map.getSeed());
		SurfaceMesh surface = map.getSurface();

		for (int pass = 0; pass < 3; pass++) {
			float a = rng.nextFloat();
			float b = rng.nextFloat();
			HashNoise noise = new HashNoise(a, b);

			for (int x = 0; x < map.getSize(); x++) {
				for (int y = 0; y < map.getSize(); y++) {
					surface.addHeight(x, y, (int) gen(x, y, noise, a, b, scale / ((float) pass + 1), POWER / ((float) pass + 1), height / ((float) pass + 1)));
				}
			}
		}
	}

	private double gen(double x, double y, HashNoise noise, float a, float b, float scale, float power, float height) {
		x *= 0.03 / scale;
		y *= 0.03 / scale;

		for (int i = 0; i < 5; i++) {
			double nx = (1.3623f * x) + (1.7531f * y);
			double ny = (-1.7131f * x) + (1.4623f * y);

			x = nx;
			y = ny;

			x += a * 919.19;
			y += b * 191.91;
		}

		double ff = noise.noise(x * 0.002f, y * 0.002f);
		return ((Math.pow(Math.abs(ff), power) * 275) - 5) * height * scale;
	}
}
