package com.wyverngame.terraingenerator;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public final class CaveMesh {
	private final short[][] height;
	private final byte[][] type;
	private final byte[][] ceilingHeight;

	private final int size;

	public CaveMesh(int size) {
		this.size = size;

		this.height = new short[size][size];
		this.type = new byte[size][size];
		this.ceilingHeight = new byte[size][size];

		for (int x = 0; x < size; x++) {
			Arrays.fill(type[x], (byte) 202);
			Arrays.fill(height[x], (short) -100);
		}
	}

	public void setHeight(int x, int y, int height) {
		this.height[x][y] = (short) height;
	}

	public void setType(int x, int y, int type) {
		this.type[x][y] = (byte) type;
	}

	public byte getType(int x, int y) {
		return this.type[x][y];
	}

	public void setCeilingHeight(int x, int y, int ceilingHeight) {
		this.ceilingHeight[x][y] = (byte) ceilingHeight;
	}

	public void save(Path path) throws IOException {
		try (DataOutputStream out = new DataOutputStream(new BufferedOutputStream(Files.newOutputStream(path.resolve("map_cave.map"))))) {
			Map.writeHeader(out, size);
			for (int y = 0; y < size; y++) {
				for (int x = 0; x < size; x++) {
					out.writeByte(type[x][y]);
					out.writeByte(ceilingHeight[x][y]);
					out.writeShort(height[x][y]);
				}
			}
		}
	}
}
