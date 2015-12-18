package com.wyverngame.terraingenerator.filter;

import java.util.Random;

import com.wyverngame.terraingenerator.Map;
import com.wyverngame.terraingenerator.SurfaceMesh;
import com.wyverngame.terraingenerator.noise.Noise;

public final class ForestFilter extends Filter {
	private final static int[] TYPES = new int[] {100, 101, 103, 105, 106, 107, 108, 109, 110, 111, 112, 113};

	@Override
	public void apply(Map map) {
		SurfaceMesh surface = map.getSurface();

		Random rng = new Random(map.getSeed() + 128943);
		Noise[] noises = new Noise[TYPES.length];
		for (int i = 0; i < noises.length; i++) noises[i] = new Noise(rng.nextFloat(), rng.nextFloat());

		Noise base = new Noise(rng.nextFloat(), rng.nextFloat());

		for (int x = 0; x < map.getSize(); x++) {
			for (int y = 0; y < map.getSize(); y++) {
				if (surface.getType(x, y) != 2) continue;
				if (rng.nextInt(3) != 0) continue;

				int type = 0;

				if (Math.pow(base.noise(x * 0.006, y * 0.006), 2.45) < rng.nextFloat() + 0.08f) {
					if (surface.getHeight(x, y) < 48 && rng.nextInt(40) == 0) {
						type = 104;
					} else continue;
				}

				if (type == 0) {
					double max = 0;
					for (int index = 0; index < TYPES.length; index++) {
						double n = noises[index].noise(x * 0.02, y * 0.02);
						if (n > max) {
							max = n;
							type = TYPES[index];
						}
					}
				}

				if (rng.nextInt(46) == 0) {
					type = 102;
				}

				if (rng.nextInt(16) == 0) {
					int r = rng.nextInt(5);
					type = 143 + r;
				}

				surface.setType(x, y, type);

				int age = rng.nextInt(16);
				int grass = rng.nextInt(4);
				boolean fruit = rng.nextInt(2) == 0;

				int meta = (age << 4) | grass;
				//if (center) meta |= 0x04;
				if (fruit) meta |= 0x08;

				surface.setMeta(x, y, meta);
			}
		}
	}
}
