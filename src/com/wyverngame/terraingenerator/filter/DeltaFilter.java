package com.wyverngame.terraingenerator.filter;

import com.wyverngame.terraingenerator.Map;
import com.wyverngame.terraingenerator.SurfaceMesh;

public final class DeltaFilter extends Filter {
	private final static int DELTA = 1950;

	@Override
	public void apply(Map map) {
		SurfaceMesh surface = map.getSurface();

		for (int x = 0; x < map.getSize(); x++) {
			for (int y = 0; y < map.getSize(); y++) {
				surface.addHeight(x, y, DELTA);
			}
		}
	}
}
