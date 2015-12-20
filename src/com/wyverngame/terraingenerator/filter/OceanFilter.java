package com.wyverngame.terraingenerator.filter;

import java.util.Random;

import com.wyverngame.terraingenerator.Map;
import com.wyverngame.terraingenerator.SurfaceMesh;
import com.wyverngame.terraingenerator.noise.HashNoise;

public final class OceanFilter extends Filter {
	private final boolean scale;

	public OceanFilter(boolean scale) {
		this.scale = scale;
	}

	@Override
	public void apply(Map map) {
		//double[][] noise = BilinearNoise.smoothNoise(map.getSeed() + 3479, map.getSize() + 1, 256, 8);
		Random rng = new Random(map.getSeed() + 8493);
		HashNoise hn = new HashNoise(rng.nextFloat(), rng.nextFloat());
		SurfaceMesh surface = map.getSurface();

		double factor = scale ? 0.0035 : 0.0045;
		double delta = scale ? 1300 : 1000;

		for (int x = 0; x < map.getSize(); x++) {
			for (int y = 0; y < map.getSize(); y++) {
				//surface.addHeight(x, y, (int) (noise[x][y] * 2048));
				//double s = Math.pow(noise[x][y], 2);
				double s = Math.pow(hn.noise(x * factor, y * factor), 2);
				double h = surface.getHeight(x, y);
				surface.setHeight(x, y, (int) (((1 - s) * h) + (s * -delta)));
			}
		}
	}
}
