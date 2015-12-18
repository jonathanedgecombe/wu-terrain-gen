package com.wyverngame.terraingenerator.filter;

import com.wyverngame.terraingenerator.Map;
import com.wyverngame.terraingenerator.SurfaceMesh;

public final class EdgeFilter extends Filter {
	private final float FALLOFF = 128;
	private final float DEPTH = 4096;

	@Override
	public void apply(Map map) {
		SurfaceMesh surface = map.getSurface();

		for (int x = 0; x < map.getSize(); x++) {
			for (int y = 0; y < map.getSize(); y++) {
				double mul = 1;
				mul *= (Math.tanh((x - FALLOFF) / FALLOFF) + 1) / 2;
				mul *= (Math.tanh(((map.getSize() - x) - FALLOFF) / FALLOFF) + 1) / 2;
				mul *= (Math.tanh((y - FALLOFF) / FALLOFF) + 1) / 2;
				mul *= (Math.tanh(((map.getSize() - y) - FALLOFF) / FALLOFF) + 1) / 2;

				double h = surface.getHeight(x, y) * mul;
				h -= DEPTH * (1 - mul);

				surface.setHeight(x, y, (int) h);
			}
		}
	}
}
