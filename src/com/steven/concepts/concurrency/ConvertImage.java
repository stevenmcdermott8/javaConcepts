package com.steven.concepts.concurrency;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * stevmc created on 2/23/20
 */
public class ConvertImage {

	private static final String SOURCE_FILE = "./resources/many-flowers.jpg";
	private static final String DESTINATION_FILE_SINGLE_THREAD = "./out/many-flowers.jpg";
	private static final String DESTINATION_FILE_MULTI_THREAD = "./out/many-flowers2.jpg";

	public static void main(String[] args) throws IOException {
		BufferedImage originalImage = ImageIO.read(new File(SOURCE_FILE));
		BufferedImage outputImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(),
				BufferedImage.TYPE_INT_RGB);

		long start = System.currentTimeMillis();
		recolorSingleThreaded(originalImage, outputImage);
		long end = System.currentTimeMillis();
		System.out.println("Total time to process image colors SINGLE threaded is " + (end - start));

		File outputFile = new File(DESTINATION_FILE_SINGLE_THREAD);
		ImageIO.write(outputImage, "jpg", outputFile);

		BufferedImage outputImage2 = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(),
				BufferedImage.TYPE_INT_RGB);
		int processors = Runtime.getRuntime().availableProcessors();
		System.out.println("Current computer has " + processors
				+ " available processors, running this many threads to process image");
		start = System.currentTimeMillis();
		recolorMultiThreaded(originalImage, outputImage2, processors);
		end = System.currentTimeMillis();
		System.out.println("Total time to process image colors MULTI threaded is " + (end - start));

		File outputFile2 = new File(DESTINATION_FILE_MULTI_THREAD);
		ImageIO.write(outputImage, "jpg", outputFile2);

	}

	private static void recolorMultiThreaded(BufferedImage originalImage, BufferedImage outputImage,
			int numberOfThreads) {
		List<Thread> threads = new ArrayList<>();
		int width = originalImage.getWidth();
		int height = originalImage.getHeight() / numberOfThreads;

		for (int i = 0; i < numberOfThreads; i++) {
			final int threadMultiplier = i;
			Thread thread = new Thread(new Runnable() {
				@Override
				public void run() {
					int leftCorner = 0;
					int topCorner = height * threadMultiplier;

					recolorImage(originalImage, outputImage, leftCorner, topCorner, width, height);
				}
			});

			threads.add(thread);
		}

		for (Thread thread : threads) {
			thread.start();
		}

		for (Thread thread : threads) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private static void recolorSingleThreaded(BufferedImage originalImage, BufferedImage outputImage) {
		recolorImage(originalImage, outputImage, 0, 0, originalImage.getWidth(), originalImage.getHeight());
	}

	private static void recolorImage(BufferedImage originalImage, BufferedImage outputImage, int leftCorner,
			int topCorner, int width, int height) {

		for (int x = leftCorner; x < leftCorner + width && x < originalImage.getWidth(); x++) {
			for (int y = topCorner; y < topCorner + height && y < originalImage.getHeight(); y++) {
				recolorPixel(originalImage, outputImage, x, y);
			}
		}
	}

	private static void recolorPixel(BufferedImage originalImage, BufferedImage outputImage, int x, int y) {
		int rgb = originalImage.getRGB(x, y);

		int red = getRed(rgb);
		int green = getGreen(rgb);
		int blue = getBlue(rgb);

		int newRed;
		int newGreen;
		int newBlue;

		// color choices are to get a slightly more redish blue purple, if not shade of gray,
		// keep existing colors in else
		if (isShadeOfGray(red, green, blue)) {
			newRed = Math.min(255, red + 10);
			newGreen = Math.max(0, green - 80);
			newBlue = Math.max(0, blue - 20);
		} else {
			newRed = red;
			newGreen = green;
			newBlue = blue;
		}

		int newRGB = getRGBFromColors(newRed, newGreen, newBlue);
		setRGB(outputImage, x, y, newRGB);
	}

	private static void setRGB(BufferedImage image, int x, int y, int rgb) {
		image.getRaster().setDataElements(x, y, image.getColorModel().getDataElements(rgb, null));
	}

	private static boolean isShadeOfGray(int red, int green, int blue) {
		// check to see if red, green, and blue are very close to each other in value,
		// if so the pixel is gray(ish), using 30 as an arbitrary value for a range to determine
		// if colors are close to each other.
		return Math.abs(red - green) < 30 && Math.abs(red - blue) < 30 && Math.abs(blue - green) < 30;
	}

	private static int getRGBFromColors(int red, int green, int blue) {
		int rgb = 0;
		rgb |= blue;
		rgb |= green << 8;
		rgb |= red << 16;
		rgb |= 0xFF000000;

		return rgb;
	}

	private static int getRed(int rgb) {
		return (rgb & 0x00FF0000) >> 16;
	}

	private static int getGreen(int rgb) {
		return (rgb & 0x0000FF00) >> 8;
	}

	private static int getBlue(int rgb) {
		return rgb & 0x000000FF;
	}

}
