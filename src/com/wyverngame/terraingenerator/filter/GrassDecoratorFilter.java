package com.wyverngame.terraingenerator.filter;

import java.util.Random;

import com.wyverngame.terraingenerator.Map;
import com.wyverngame.terraingenerator.SurfaceMesh;

public final class GrassDecoratorFilter extends Filter {
	@Override
	public void apply(Map map) {
		SurfaceMesh surface = map.getSurface();
		Random rng = new Random(map.getSeed() + 8471);

		for (int x = 0; x < map.getSize() - 1; x++) {
			loop: for (int y = 0; y < map.getSize() - 1; y++) {
				if (surface.getType(x, y) != 5) continue;

				for (int dx = 0; dx < 2; dx++) {
					for (int dy = 0; dy < 2; dy++) {
						if (surface.getHeight(x + dx, y + dy) < 0) continue loop;
					}
				}

				surface.setType(x, y, 2);

				int flower = 0;
				int chance = rng.nextInt(1000);
				if (rng.nextInt(3) != 0) if (chance > 980) {
					flower = 7;
				} else if (chance > 900) {
					flower = 6;
				} else if (chance > 800) {
					flower = 5;
				} else if (chance > 700) {
					flower = 4;
				} else if (chance > 550) {
					flower = 3;
				} else if (chance > 350) {
					flower = 2;
				} else {
					flower = 1;
				}

				int meta = rng.nextInt(4) << 6 | flower;
				surface.setMeta(x, y, meta);
			}
		}
	}
}
