package com.wyverngame.terraingenerator.filter;

import java.util.Random;

import com.wyverngame.terraingenerator.Map;
import com.wyverngame.terraingenerator.SurfaceMesh;
import com.wyverngame.terraingenerator.noise.Noise;

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
		Noise noise = new Noise(rng.nextFloat(), rng.nextFloat());

		for (int x = 0; x < map.getSize(); x++) {
			for (int y = 0; y < map.getSize(); y++) {
				if (type == 6) {
					if (surface.getType(x, y) != 5 && surface.getType(x, y) != 1) continue;
					if (surface.getHeight(x, y) < -64 || surface.getHeight(x, y) > 64) continue;
				} else {
					if (surface.getType(x, y) != 5) continue;
					if (surface.getHeight(x, y) < 128) continue;
				}

				if (Math.pow(noise.noise(x * 0.09, y * 0.09), type == 6 ? 2 : 5) > (type == 6 ? 0.765 : 0.825)) {
					surface.setType(x, y, type);
				}
			}
		}
	}
}
