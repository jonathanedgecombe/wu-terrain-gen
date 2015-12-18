package com.wyverngame.terraingenerator;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

import com.wyverngame.terraingenerator.filter.AverageFilter;
import com.wyverngame.terraingenerator.filter.BiomeFilter;
import com.wyverngame.terraingenerator.filter.DeltaFilter;
import com.wyverngame.terraingenerator.filter.EdgeFilter;
import com.wyverngame.terraingenerator.filter.ForestFilter;
import com.wyverngame.terraingenerator.filter.GrassDecoratorFilter;
import com.wyverngame.terraingenerator.filter.MountainFilter;
import com.wyverngame.terraingenerator.filter.NoiseFilter;
import com.wyverngame.terraingenerator.filter.OreFilter;
import com.wyverngame.terraingenerator.filter.ResourceFilter;
import com.wyverngame.terraingenerator.filter.RidgeFilter;
import com.wyverngame.terraingenerator.filter.RiverFilter;
import com.wyverngame.terraingenerator.filter.RockDecoratorFilter;
import com.wyverngame.terraingenerator.filter.RockLayerFilter;
import com.wyverngame.terraingenerator.filter.SmoothCoastFilter;
import com.wyverngame.terraingenerator.filter.TarPitFilter;

public final class Terragen {
	public static void main(String[] args) throws IOException {
		int size = 2048;
		long seed = System.currentTimeMillis();

		for (String arg : args) {
			String[] parts = arg.split("=", 2);
			if (parts.length != 2) continue;

			switch (parts[0].toLowerCase()) {
			case "-size":
				size = Integer.parseInt(parts[1]);
				break;
			case "-seed":
				seed = Long.parseLong(parts[1]);
				break;
			}
		}

		System.out.println("Generating " + size + "x" + size + " map with seed " + seed);
		Map map = new Map(size, seed);

		new DeltaFilter().apply(map);
		System.out.println("Generating mountains");
		new MountainFilter().apply(map);
		System.out.println("Generating ridged mountains");
		new RidgeFilter().apply(map);
		System.out.println("Generating noise");
		new NoiseFilter().apply(map);
		System.out.println("Generating rives and lakes");
		new RiverFilter().apply(map);
		System.out.println("Smoothing terrain");
		new AverageFilter().apply(map);
		System.out.println("Lowering coastlines");
		new EdgeFilter().apply(map);
		System.out.println("Smoothing coastlines");
		new SmoothCoastFilter().apply(map);
		System.out.println("Generating rock layer");
		new RockLayerFilter().apply(map);
		System.out.println("Generating rocks and cliffs");
		new RockDecoratorFilter().apply(map);
		System.out.println("Generating deserts");
		new BiomeFilter(1, 4639).apply(map);
		System.out.println("Generating tundra");
		new BiomeFilter(19, 3748).apply(map);
		System.out.println("Generating steppe");
		new BiomeFilter(22, 8178).apply(map);
		System.out.println("Generating clay");
		new ResourceFilter(6, 6578).apply(map);
		System.out.println("Generating peat");
		new ResourceFilter(18, 8739).apply(map);
		System.out.println("Generating moss");
		new ResourceFilter(20, 1043).apply(map);
		System.out.println("Generating grass and flowers");
		new GrassDecoratorFilter().apply(map);
		System.out.println("Generating tar pits");
		new TarPitFilter().apply(map);
		System.out.println("Generating forests");
		new ForestFilter().apply(map);
		System.out.println("Generating ores");
		new OreFilter().apply(map);

		System.out.println("Rendering map");
		BufferedImage img = map.getSurface().render();

		Path dir = Paths.get("./map_" + Long.toString(seed));
		if (!Files.isDirectory(dir)) {
			Files.createDirectory(dir);
		}

		System.out.println("Saving");
		ImageIO.write(img, "PNG", new BufferedOutputStream(Files.newOutputStream(dir.resolve("render.png")), 65536));
		map.save(dir);
		System.out.println("Done");
	}
}
