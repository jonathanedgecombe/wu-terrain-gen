package com.wyverngame.terraingenerator;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public final class ResourceMesh {
	private final short[][] ore;
	private final int size;

	public ResourceMesh(int size) {
		this.size = size;
		this.ore = new short[size][size];

		for (int x = 0; x < size; x++) {
			Arrays.fill(ore[x], (short) 50);
		}
	}

	public void setOre(int x, int y, int ore) {
		this.ore[x][y] = (short) ore;
	}

	public int getSize() {
		return size;
	}

	public void save(Path path) throws IOException {
		try (DataOutputStream out = new DataOutputStream(new BufferedOutputStream(Files.newOutputStream(path.resolve("resources.map"))))) {
			Map.writeHeader(out, size);
			for (int y = 0; y < size; y++) {
				for (int x = 0; x < size; x++) {
					out.writeShort(ore[x][y]);
					out.writeShort(0);
				}
			}
		}
	}
}
