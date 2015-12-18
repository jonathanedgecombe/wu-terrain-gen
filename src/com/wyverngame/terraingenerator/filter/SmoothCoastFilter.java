package com.wyverngame.terraingenerator.filter;

import com.wyverngame.terraingenerator.Map;
import com.wyverngame.terraingenerator.SurfaceMesh;

public final class SmoothCoastFilter extends Filter {
	@Override
	public void apply(Map map) {
		SurfaceMesh surface = map.getSurface();

		for (int x = 0; x < map.getSize(); x++) {
			for (int y = 0; y < map.getSize(); y++) {
				float height = surface.getHeight(x, y);
				float mul = 0.66666f - ((float) Math.tanh((100 - height) / 500) * 0.33333f);
				surface.setHeight(x, y, (int) (mul * height));
			}
		}
	}
}
