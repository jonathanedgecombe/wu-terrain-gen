package com.wyverngame.terraingenerator.filter;

import com.wyverngame.terraingenerator.Map;
import com.wyverngame.terraingenerator.SurfaceMesh;

public final class AverageFilter extends Filter {
	@Override
	public void apply(Map map) {
		SurfaceMesh surface = map.getSurface();

		for (int x = 1; x < map.getSize() - 1; x++) {
			for (int y = 1; y < map.getSize() - 1; y++) {
				float h = 0;

				for (int dx = -1; dx <= 1; dx++) {
					for (int dy = -1; dy <= 1; dy++) {
						h += surface.getHeight(x + dx, y + dy);
					}
				}

				surface.setHeight(x, y, (int) (h / 9));
			}
		}
	}
}
