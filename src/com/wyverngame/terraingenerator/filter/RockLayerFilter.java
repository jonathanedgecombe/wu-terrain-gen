package com.wyverngame.terraingenerator.filter;

import com.wyverngame.terraingenerator.Map;
import com.wyverngame.terraingenerator.RockMesh;
import com.wyverngame.terraingenerator.SurfaceMesh;

public final class RockLayerFilter extends Filter {
	private final int threshold = 57;
	private final float depthMultiplier = 1.25f;

	@Override
	public void apply(Map map) {
		SurfaceMesh surface = map.getSurface();
		RockMesh rock = map.getRock();

		for (int x = 0; x < map.getSize(); x++) {
			for (int y = 0; y < map.getSize(); y++) {
				int height = surface.getHeight(x, y);

				if (x == map.getSize() - 1 || y == map.getSize() - 1) {
					rock.setHeight(x, y, height);
					continue;
				}

				int max = 0;
				for (int dxa = 0; dxa < 2; dxa++) {
					for (int dya = 0; dya < 2; dya++) {
						for (int dxb = 0; dxb < 2; dxb++) {
							for (int dyb = 0; dyb < 2; dyb++) {
								if (dxa == dxb || dya == dyb) continue;

								int delta = Math.abs(surface.getHeight(x + dxa, y + dya) - surface.getHeight(x + dxb, y + dyb));
								if (delta > max) {
									max = delta;
								}
							}
						}
					}
				}

				int delta = (int) (Math.max(0, threshold - max) * depthMultiplier);
				rock.setHeight(x, y, height - delta);
			}
		}
	}
}
