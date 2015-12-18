package com.wyverngame.terraingenerator.filter;

import java.util.Random;

import com.wyverngame.terraingenerator.CaveMesh;
import com.wyverngame.terraingenerator.Map;
import com.wyverngame.terraingenerator.ResourceMesh;
import com.wyverngame.terraingenerator.noise.Noise;

public final class OreFilter extends Filter {
	private final static int[] TYPES = new int[] {205, 206, 220, 220, 221, 221, 221, 221, 223, 223, 223, 223, 224, 224, 224, 224, 225, 225, 225, 225, 226, 226, 226, 226};

	@Override
	public void apply(Map map) {
		CaveMesh cave = map.getCave();
		ResourceMesh resource = map.getResource();

		Random rng = new Random(map.getSeed() + 1289);

		Noise[] noises = new Noise[TYPES.length];
		for (int i = 0; i < noises.length; i++) noises[i] = new Noise(rng.nextFloat(), rng.nextFloat());

		for (int x = 0; x < map.getSize(); x++) {
			for (int y = 0; y < map.getSize(); y++) {
				if (rng.nextInt(64) != 0) continue;

				int type = 222;
				double max = 0;

				for (int i = 0; i < TYPES.length; i++) {
					double n = noises[i].noise(x * 0.033, y * 0.033);
					if (n > max) {
						max = n;
						type = TYPES[i];
					}
				}

				cave.setType(x, y, type);
				resource.setOre(x, y, rng.nextInt(65435) + 100);
			}
		}

		for (int x = 0; x < map.getSize() - 7; x += 7) {
			for (int y = 0; y < map.getSize() - 7; y+= 7) {
				int count = rng.nextInt(2) + 1;
				for (int i = 0; i < count; i++) {
					int dx = rng.nextInt(7);
					int dy = rng.nextInt(7);

					cave.setType(x + dx, y + dy, 222);
					resource.setOre(x + dx, y + dy, rng.nextInt(65435) + 100);
				}
			}
		}
	}
}
