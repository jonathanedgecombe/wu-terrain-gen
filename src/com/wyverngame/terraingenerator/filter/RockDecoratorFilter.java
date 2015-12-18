package com.wyverngame.terraingenerator.filter;

import com.wyverngame.terraingenerator.Map;
import com.wyverngame.terraingenerator.RockMesh;
import com.wyverngame.terraingenerator.SurfaceMesh;

public final class RockDecoratorFilter extends Filter {
	private final int cliffThreshold = 100;

	@Override
	public void apply(Map map) {
		SurfaceMesh surface = map.getSurface();
		RockMesh rock = map.getRock();

		for (int x = 0; x < map.getSize() - 1; x++) {
			loop: for (int y = 0; y < map.getSize() - 1; y++) {
				int max = 0;
				for (int dxa = 0; dxa < 2; dxa++) {
					for (int dya = 0; dya < 2; dya++) {
						if (rock.getHeight(x + dxa, y + dya) < surface.getHeight(x + dxa, y + dya)) {
							continue loop;
						}

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

				if (max > cliffThreshold) {
					surface.setType(x, y, 21);
				} else {
					surface.setType(x, y, 4);
				}
			}
		}
	}
}
