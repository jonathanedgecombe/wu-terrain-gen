package com.wyverngame.terraingenerator.filter;

import com.wyverngame.terraingenerator.Map;
import com.wyverngame.terraingenerator.SurfaceMesh;

public final class FeatureMountainFilter extends Filter {
	@Override
	public void apply(Map map) {
		SurfaceMesh surface = map.getSurface();

		int highestX = 0, highestY = 0, highest = Integer.MIN_VALUE;
		int half = map.getSize() / 2;

		for (int x = 0; x < map.getSize(); x++) {
			for (int y = 0; y < map.getSize(); y++) {
				int height = surface.getHeight(x, y);

				height -= (Math.abs(x - half) + Math.abs(y - half)) * 5;

				if (height > highest) {
					highest = height;
					highestX = x;
					highestY = y;
				}
			}
		}

		int radius = 128;

		for (int dx = -radius; dx <= radius; dx++) {
			for (int dy = -radius; dy <= radius; dy++) {
				int tx = highestX + dx;
				int ty = highestY + dy;

				if (tx < 0 || ty < 0 || tx > map.getSize() - 1 || ty > map.getSize() - 1) {
					continue;
				}

				double t = Math.sqrt((dx * dx) + (dy * dy)) / radius;
				if (t > 1) {
					continue;
				}

				int h = surface.getHeight(tx, ty);

				double bicubic = (2 * Math.pow(t, 3)) - (3 * Math.pow(t, 2)) + 1;
				double factor = 1 + (bicubic / 2);

				surface.setHeight(tx, ty, (int) (h * factor));
			}
		}
	}
}
