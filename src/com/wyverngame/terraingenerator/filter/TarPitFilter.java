package com.wyverngame.terraingenerator.filter;

import java.util.Random;

import com.wyverngame.terraingenerator.Map;
import com.wyverngame.terraingenerator.RockMesh;
import com.wyverngame.terraingenerator.SurfaceMesh;

public final class TarPitFilter extends Filter {
	@Override
	public void apply(Map map) {
		Random rng = new Random(map.getSeed() + 3748);
		RockMesh rock = map.getRock();
		SurfaceMesh surface = map.getSurface();

		for (int x = 0; x < map.getSize() - 2; x++) {
			loop: for (int y = 0; y < map.getSize() - 2; y++) {
				if (rng.nextInt(10000) != 0) continue;

				for (int dx = 0; dx <= 2; dx++) {
					for (int dy = 0; dy <= 2; dy++) {
						int h = surface.getHeight(x + dx, y + dy);
						if (h < 128) continue loop;
						int d = h - rock.getHeight(x + dx, y + dy);
						if (d < 20) continue loop;
					}
				}

				for (int dx = 0; dx <= 2; dx++) {
					for (int dy = 0; dy <= 2; dy++) {
						surface.addHeight(x + dx, y + dy, -20);
						if (dx != 2 && dy != 2) {
							surface.setType(x + dx, y + dy, 24);
						}
					}
				}
			}
		}
	}
}
