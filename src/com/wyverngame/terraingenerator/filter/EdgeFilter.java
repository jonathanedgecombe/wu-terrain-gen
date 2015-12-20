package com.wyverngame.terraingenerator.filter;

import com.wyverngame.terraingenerator.Map;
import com.wyverngame.terraingenerator.SurfaceMesh;

public final class EdgeFilter extends Filter {
	private final static float DEPTH = 2000;
	private final float falloff;

	public EdgeFilter(float falloff) {
		this.falloff = falloff;
	}

	@Override
	public void apply(Map map) {
		SurfaceMesh surface = map.getSurface();

		for (int x = 0; x < map.getSize(); x++) {
			for (int y = 0; y < map.getSize(); y++) {
				double mul = 1;
				mul *= (Math.tanh((x - falloff) / (falloff / 2)) + 1) / 2;
				mul *= (Math.tanh(((map.getSize() - x) - falloff) / (falloff / 2)) + 1) / 2;
				mul *= (Math.tanh((y - falloff) / (falloff / 2)) + 1) / 2;
				mul *= (Math.tanh(((map.getSize() - y) - falloff) / (falloff / 2)) + 1) / 2;

				double h = surface.getHeight(x, y) * mul;
				h -= DEPTH * (1 - mul);

				surface.setHeight(x, y, (int) h);
			}
		}
	}
}
