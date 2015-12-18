package com.wyverngame.terraingenerator;

import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.file.Path;

public final class Map {
	private final SurfaceMesh surface;
	private final RockMesh rock;
	private final CaveMesh cave;
	private final ResourceMesh resource;

	private final int size;
	private final long seed;

	public Map(int size, long seed) {
		this.size = size;
		this.seed = seed;
		this.surface = new SurfaceMesh(size);
		this.rock = new RockMesh(size);
		this.cave = new CaveMesh(size);
		this.resource = new ResourceMesh(size);
	}

	public SurfaceMesh getSurface() {
		return surface;
	}

	public RockMesh getRock() {
		return rock;
	}

	public CaveMesh getCave() {
		return cave;
	}

	public ResourceMesh getResource() {
		return resource;
	}

	public int getSize() {
		return size;
	}

	public long getSeed() {
		return seed;
	}

	public void save(Path path) throws IOException {
		surface.save(path);
		rock.save(path);
		cave.save(path);
		resource.save(path);
	}

	private static final long MAGIC_NUMBER = 5136955264682433437L;
	private static final int VERSION = 0;

	public static void writeHeader(DataOutputStream out, int size) throws IOException {
		out.writeLong(MAGIC_NUMBER);
		out.writeByte(VERSION);

		int logSize = 0;

		for (int i = 0; i < 32; i++) {
			if (((size >> i) & 1) == 1) {
				logSize = i;
			}
		}
		out.writeByte(logSize);
		out.write(new byte[1024 - 8 - 1 - 1]);
	}
}
