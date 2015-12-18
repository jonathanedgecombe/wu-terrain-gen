package com.wyverngame.terraingenerator.filter;

import java.util.Random;

import com.wyverngame.terraingenerator.Map;
import com.wyverngame.terraingenerator.SurfaceMesh;
import com.wyverngame.terraingenerator.noise.Noise;

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
		Noise noise = new Noise(rng.nextFloat(), rng.nextFloat());

		for (int x = 0; x < map.getSize(); x++) {
			for (int y = 0; y < map.getSize(); y++) {
				if (surface.getType(x, y) != 5) continue;
				if (surface.getHeight(x, y) < (type == 1 ? -512 : 128)) continue;

				if (Math.pow(noise.noise(x * 0.003, y * 0.003), 2) > 0.65) {
					surface.setType(x, y, type);
				}
			}
		}
	}
}
