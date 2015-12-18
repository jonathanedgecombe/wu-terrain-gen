package com.wyverngame.terraingenerator;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public final class SurfaceMesh {
	public final static int SCALE_FACTOR = 40;

	private final short[][] height;
	private final byte[][] type;
	private final byte[][] meta;

	private final int size;

	public SurfaceMesh(int size) {
		this.size = size;

		this.height = new short[size][size];
		this.type = new byte[size][size];
		this.meta = new byte[size][size];

		for (int x = 0; x < size; x++) {
			Arrays.fill(type[x], (byte) 5);
		}
	}

	public void setHeight(int x, int y, int height) {
		this.height[x][y] = (short) height;
	}

	public void addHeight(int x, int y, int height) {
		this.height[x][y] = (short) (this.height[x][y] + height);
	}

	public void setHigher(int x, int y, int height) {
		if (height > this.height[x][y]) this.height[x][y] = (short) height;
	}

	public short getHeight(int x, int y) {
		return this.height[x][y];
	}

	public void setType(int x, int y, int type) {
		this.type[x][y] = (byte) type;
	}

	public byte getType(int x, int y) {
		return this.type[x][y];
	}

	public void setMeta(int x, int y, int meta) {
		this.meta[x][y] = (byte) meta;
	}

	public BufferedImage render() {
		BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
		int[] pixels = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();

		for (int x = 0; x < size - 1; x++) {
			for (int y = 0; y < size - 1; y++) {
				float h1 = height[x][y];
				float h2 = height[x][y + 1];
				float h3 = height[x + 1][y + 1];

				int colour = shade(Tile.COLOURS[type[x][y] & 0xFF], h3 - h1, h1 <= 0);

				int start = (int) (y - (h1 / SCALE_FACTOR));
				int end = (int) (start + (Math.abs(h2 - h1) / SCALE_FACTOR) + 2);
				if (end >= size) end = size - 1;
				if (start < 0) start = 0;

				for (int i = start; i <= end; i++) {
					pixels[i * size + x] = colour;
				}
			}
		}

		return img;
	}

	public int shade(int colour, float delta, boolean water) {
		int r = (colour >> 16) & 0xFF;
		int g = (colour >> 8) & 0xFF;
		int b = colour & 0xFF;

		float mult = (float) (1 + (Math.tanh(delta / 128) * 0.66));

		r *= mult;
		g *= mult;
		b *= mult;

		if (water) {
			r = (r / 5) + 41;
			g = (r / 5) + 51;
			b = (r / 5) + 102;
		}

		if (r < 0) r = 0;
		if (g < 0) g = 0;
		if (b < 0) b = 0;
		if (r > 255) r = 255;
		if (g > 255) g = 255;
		if (b > 255) b = 255;

		return 0xFF000000 | (r << 16) | (g << 8) | b;
	}

	public void save(Path path) throws IOException {
		try (DataOutputStream out = new DataOutputStream(new BufferedOutputStream(Files.newOutputStream(path.resolve("top_layer.map"))))) {
			Map.writeHeader(out, size);
			for (int y = 0; y < size; y++) {
				for (int x = 0; x < size; x++) {
					out.writeByte(type[x][y]);
					out.writeByte(meta[x][y]);
					out.writeShort(height[x][y]);
				}
			}
		}
	}
}
