package com.wyverngame.terraingenerator.noise;

import java.util.Random;

public final class BilinearNoise {
	public static double[][] noise(long seed, int size, int frequency) {
		Random random = new Random(seed);

		double[][] points = new double[size][size];

		for (int x = 0; x < size; x += frequency) {
			for (int y = 0; y < size; y += frequency) {
				points[x][y] = random.nextFloat();
			}
		}

		for (int bx = 0; bx < size - 1; bx += frequency) {
			for (int by = 0; by < size - 1; by += frequency) {
				for (int dx = 0; dx <= frequency; dx++) {
					for (int dy = 0; dy <= frequency; dy++) {
						if ((dx == 0 && dy == 0) ||
							(dx == 0 && dy == frequency) ||
							(dx == frequency && dy == 0) ||
							(dx == frequency && dy == frequency))
							continue;

						float value = 0f;

						double fdx = ((double) dx) / frequency;
						double fdy = ((double) dy) / frequency;

						value += points[bx][by] * (1f - fdx) * (1f - fdy);
						value += points[bx][by + frequency] * (1f - fdx) * fdy;
						value += points[bx + frequency][by] * fdx * (1f - fdy);
						value += points[bx + frequency][by + frequency] * fdx * fdy;

						points[bx + dx][by + dy] = value;
					}
				}
			}
		}

		return points;
	}

	public static double[][] smoothNoise(long seed, int size, int frequency, int aperture) {
		double[][] noise = noise(seed, size, frequency);

		for (int x = aperture; x < size - aperture; x++) {
			for (int y = aperture; y < size - aperture; y++) {
				double avg = 0;
				for (int dx = -aperture; dx <= aperture; dx++) {
					for (int dy = -aperture; dy <= aperture; dy++) {
						avg += noise[x + dx][y + dy];
					}
				}
				avg /= Math.pow((2 * aperture) + 1, 2);

				noise[x][y] = avg;
			}
		}

		return noise;
	}
}
