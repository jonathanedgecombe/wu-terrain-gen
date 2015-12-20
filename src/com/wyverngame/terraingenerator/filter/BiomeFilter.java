package com.wyverngame.terraingenerator.filter;

import java.util.Random;

import com.wyverngame.terraingenerator.Map;
import com.wyverngame.terraingenerator.SurfaceMesh;
import com.wyverngame.terraingenerator.noise.HashNoise;

public final class BiomeFilter extends Filter {
	private final int type;
	private final int seed;

	public BiomeFilter(int type, int seed) {
		this.type = type;
		this.seed = seed;
	}

	@Override
	public void apply(Map map) {
		SurfaceMesh surface = map.getSurface();
		Random rng = new Random(map.getSeed() + 6784331 + seed);
		HashNoise noise = new HashNoise(rng.nextFloat(), rng.nextFloat());

		for (int x = 0; x < map.getSize(); x++) {
			for (int y = 0; y < map.getSize(); y++) {
				if (surface.getType(x, y) != 5) continue;
				if (surface.getHeight(x, y) < (type == 1 ? -256 : 128)) continue;

				double n = noise.noise(x * 0.0025, y * 0.0025);
				n = (n * 0.98) + (noise.noise(x * 0.035, y * 0.035) * 0.02);
				n = (n * 0.99) + (noise.noise(x * 0.35, y * 0.35) * 0.01);

				if (Math.pow(n, 2) > 0.725) {
					surface.setType(x, y, type);
				}
			}
		}
	}
}
