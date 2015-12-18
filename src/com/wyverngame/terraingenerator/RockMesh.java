package com.wyverngame.terraingenerator;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class RockMesh {
	private final short[][] height;
	private final int size;

	public RockMesh(int size) {
		this.size = size;
		this.height = new short[size][size];
	}

	public void setHeight(int x, int y, int height) {
		this.height[x][y] = (short) height;
	}

	public short getHeight(int x, int y) {
		return this.height[x][y];
	}

	public int getSize() {
		return size;
	}

	public void save(Path path) throws IOException {
		try (DataOutputStream out = new DataOutputStream(new BufferedOutputStream(Files.newOutputStream(path.resolve("rock_layer.map"))))) {
			Map.writeHeader(out, size);
			for (int y = 0; y < size; y++) {
				for (int x = 0; x < size; x++) {
					out.writeInt(height[x][y]);
				}
			}
		}
	}
}
