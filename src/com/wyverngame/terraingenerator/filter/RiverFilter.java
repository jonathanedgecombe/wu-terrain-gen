package com.wyverngame.terraingenerator.filter;

import java.util.Random;

import com.wyverngame.terraingenerator.Map;
import com.wyverngame.terraingenerator.SurfaceMesh;
import com.wyverngame.terraingenerator.noise.HashNoise;

public final class RiverFilter extends Filter {
	@Override
	public void apply(Map map) {
		Random rng = new Random(map.getSeed() + 1467);

		HashNoise noise = new HashNoise(rng.nextFloat(), rng.nextFloat());

		float a = rng.nextFloat();
		float b = rng.nextFloat();

		SurfaceMesh surface = map.getSurface();

		for (int x = 0; x < map.getSize(); x++) {
			for (int y = 0; y < map.getSize(); y++) {
				int th = surface.getHeight(x, y);
				double d = Math.pow(gen(x, y, noise, a, b), 4);
				//surface.addHeight(x, y, (int) (d * -200));
				double scale = 0.5 - (Math.tanh(((float) (th - 200)) / 200) / 2);
				d *= scale;
				th = (int) ((d * -300) + ((1 - d) * th));
				//System.out.println(d);
				surface.setHeight(x, y, th);

				/*double m = gen(x, y, noise, a, b);
				int h = (int) (m * th);
				h -= (int) ((1 - m) * 1600);

				double scale = 0.5 + (Math.tanh(((float) th) / 500) / 2);
				int rh = (int) ((scale * th) + ((1 - scale) * h));
				//System.out.println(rh);
				surface.setHeight(x, y, rh);*/
			}
		}
	}

	private double gen(double x, double y, HashNoise noise, float a, float b) {
		x *= 0.03;
		y *= 0.03;

		for (int i = 0; i < 5; i++) {
			double nx = (1.3623f * x) + (1.7531f * y);
			double ny = (-1.7131f * x) + (1.4623f * y);

			x = nx;
			y = ny;

			x += a * 919.19;
			y += b * 191.91;
		}

		double ff = noise.noise(x * 0.002f, y * 0.002f) * 2 - 1;
		return 1 - Math.abs(ff);
	}
}
