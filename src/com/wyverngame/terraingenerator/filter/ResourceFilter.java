package com.wyverngame.terraingenerator.filter;

import java.util.Random;

import com.wyverngame.terraingenerator.Map;
import com.wyverngame.terraingenerator.SurfaceMesh;
import com.wyverngame.terraingenerator.noise.HashNoise;

public final class ResourceFilter extends Filter {
	private final int type;
	private final int seed;

	public ResourceFilter(int type, int seed) {
		this.type = type;
		this.seed = seed;
	}

	@Override
	public void apply(Map map) {
		SurfaceMesh surface = map.getSurface();
		Random rng = new Random(map.getSeed() + 269463 + seed);
		HashNoise noise = new HashNoise(rng.nextFloat(), rng.nextFloat());

		double scale = type == 6 ? 0.09 : 0.03;

		for (int x = 0; x < map.getSize(); x++) {
			for (int y = 0; y < map.getSize(); y++) {
				if (type == 6) {
					if (surface.getType(x, y) != 5 && surface.getType(x, y) != 1) continue;
					if (surface.getHeight(x, y) < -64 || surface.getHeight(x, y) > 64) continue;

					boolean flag = false;
					for (int dx = -5; dx <= 6; dx++) {
						for (int dy = -5; dy <= 6; dy++) {
							if (x + dx < 0 || y + dy < 0 || x + dx >= map.getSize() || y + dy >= map.getSize()) continue;
							if (surface.getHeight(x + dx, y + dy) < 0) flag = true;
						}
					}

					if (!flag) continue;
				} else {
					if (surface.getType(x, y) != 5) continue;
					if (surface.getHeight(x, y) < 128) continue;
				}

				if (Math.pow(noise.noise(x * scale, y * scale), type == 6 ? 2 : 5) > (type == 6 ? 0.765 : 0.825)) {
					surface.setType(x, y, type);
				}
			}
		}
	}
}
