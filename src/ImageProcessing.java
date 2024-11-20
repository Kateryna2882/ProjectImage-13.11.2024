import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.Random;
import javax.imageio.ImageIO;

public class ImageProcessing {
    public static void main(String[] args) {
        int[][] imageData = imgToTwoD("C:/Users/Dell/IdeaProjects/ProjectImage-13.11.2024/apple.jpg");

        if (imageData == null) {
            System.out.println("Image could not be loaded. Exiting.");
            return;
        }

        int[][] trimmed = trimBorders(imageData, 60);
        twoDToImage(trimmed, "./trimmed_apple.jpg");

        int[][] negative = negativeColor(imageData);
        twoDToImage(negative, "./negative_apple.jpg");

        int[][] stretchedHImg = stretchHorizontally(imageData);
        twoDToImage(stretchedHImg, "./stretched_apple.jpg");

        int[][] shrankVImg = shrinkVertically(imageData);
        twoDToImage(shrankVImg, "./shrank_apple.jpg");

        int[][] invertedImg = invertImage(imageData);
        twoDToImage(invertedImg, "./inverted_apple.jpg");

        int[][] coloredImg = colorFilter(imageData, -75, 30, -30);
        twoDToImage(coloredImg, "./colored_apple.jpg");

        int[][] blankImg = new int[500][500];
        int[][] randomImg = paintRandomImage(blankImg);
        twoDToImage(randomImg, "./random_img.jpg");

        int[] rgba = { 255, 255, 0, 255 };
        int[][] rectangleImg = paintRectangle(randomImg, 200, 200, 100, 100, getColorIntValFromRGBA(rgba));
        twoDToImage(rectangleImg, "./rectangle.jpg");

        int[][] generatedRectangles = generateRectangles(randomImg, 1000);
        twoDToImage(generatedRectangles, "./generated_rect.jpg");
    }

    public static int[][] trimBorders(int[][] imageTwoD, int pixelCount) {
        if (imageTwoD.length > pixelCount * 2 && imageTwoD[0].length > pixelCount * 2) {
            int[][] trimmedImg = new int[imageTwoD.length - pixelCount * 2][imageTwoD[0].length - pixelCount * 2];
            for (int i = 0; i < trimmedImg.length; i++) {
                for (int j = 0; j < trimmedImg[i].length; j++) {
                    trimmedImg[i][j] = imageTwoD[i + pixelCount][j + pixelCount];
                }
            }
            return trimmedImg;
        } else {
            System.out.println("Cannot trim that many pixels from the given image.");
            return imageTwoD;
        }
    }

    public static int[][] negativeColor(int[][] imageTwoD) {
        int[][] manipulatedImg = new int[imageTwoD.length][imageTwoD[0].length];
        for (int i = 0; i < imageTwoD.length; i++) {
            for (int j = 0; j < imageTwoD[i].length; j++) {
                int[] rgba = getRGBAFromPixel(imageTwoD[i][j]);
                rgba[0] = 255 - rgba[0];
                rgba[1] = 255 - rgba[1];
                rgba[2] = 255 - rgba[2];
                manipulatedImg[i][j] = getColorIntValFromRGBA(rgba);
            }
        }
        return manipulatedImg;
    }

    public static int[][] stretchHorizontally(int[][] imageTwoD) {
        int[][] manipulatedImg = new int[imageTwoD.length][imageTwoD[0].length * 2];
        for (int i = 0; i < imageTwoD.length; i++) {
            for (int j = 0; j < imageTwoD[i].length; j++) {
                int it = j * 2;
                manipulatedImg[i][it] = imageTwoD[i][j];
                manipulatedImg[i][it + 1] = imageTwoD[i][j];
            }
        }
        return manipulatedImg;
    }

    public static int[][] shrinkVertically(int[][] imageTwoD) {
        int[][] manipulatedImg = new int[imageTwoD.length / 2][imageTwoD[0].length];
        for (int i = 0; i < manipulatedImg.length; i++) {
            for (int j = 0; j < imageTwoD[0].length; j++) {
                manipulatedImg[i][j] = imageTwoD[i * 2][j];
            }
        }
        return manipulatedImg;
    }

    public static int[][] invertImage(int[][] imageTwoD) {
        int[][] invertedImg = new int[imageTwoD.length][imageTwoD[0].length];
        for (int i = 0; i < imageTwoD.length; i++) {
            for (int j = 0; j < imageTwoD[i].length; j++) {
                invertedImg[i][j] = imageTwoD[(imageTwoD.length - 1) - i][(imageTwoD[i].length - 1) - j];
            }
        }
        return invertedImg;
    }

    public static int[][] colorFilter(int[][] imageTwoD, int redChangeValue, int greenChangeValue, int blueChangeValue) {
        int[][] manipulatedImg = new int[imageTwoD.length][imageTwoD[0].length];
        for (int i = 0; i < imageTwoD.length; i++) {
            for (int j = 0; j < imageTwoD[i].length; j++) {
                int[] rgba = getRGBAFromPixel(imageTwoD[i][j]);
                rgba[0] = clamp(rgba[0] + redChangeValue, 0, 255);
                rgba[1] = clamp(rgba[1] + greenChangeValue, 0, 255);
                rgba[2] = clamp(rgba[2] + blueChangeValue, 0, 255);
                manipulatedImg[i][j] = getColorIntValFromRGBA(rgba);
            }
        }
        return manipulatedImg;
    }

    public static int[][] paintRandomImage(int[][] canvas) {
        Random rand = new Random();
        for (int i = 0; i < canvas.length; i++) {
            for (int j = 0; j < canvas[i].length; j++) {
                int[] rgba = { rand.nextInt(256), rand.nextInt(256), rand.nextInt(256), 255 };
                canvas[i][j] = getColorIntValFromRGBA(rgba);
            }
        }
        return canvas;
    }

    public static int[][] paintRectangle(int[][] canvas, int width, int height, int rowPosition, int colPosition, int color) {
        for (int i = rowPosition; i < rowPosition + height && i < canvas.length; i++) {
            for (int j = colPosition; j < colPosition + width && j < canvas[0].length; j++) {
                canvas[i][j] = color;
            }
        }
        return canvas;
    }

    public static int[][] generateRectangles(int[][] canvas, int numRectangles) {
        Random rand = new Random();
        for (int i = 0; i < numRectangles; i++) {
            int randomWidth = rand.nextInt(canvas[0].length / 2) + 1;
            int randomHeight = rand.nextInt(canvas.length / 2) + 1;
            int randomRowPos = rand.nextInt(canvas.length - randomHeight);
            int randomColPos = rand.nextInt(canvas[0].length - randomWidth);
            int[] rgba = { rand.nextInt(256), rand.nextInt(256), rand.nextInt(256), 255 };
            int randomColor = getColorIntValFromRGBA(rgba);
            paintRectangle(canvas, randomWidth, randomHeight, randomRowPos, randomColPos, randomColor);
        }
        return canvas;
    }

    public static int[][] imgToTwoD(String inputFileOrLink) {
        try {
            BufferedImage image = null;
            if (inputFileOrLink.toLowerCase().startsWith("http")) {
                URL imageUrl = new URL(inputFileOrLink);
                image = ImageIO.read(imageUrl);
                if (image == null) {
                    System.out.println("Failed to get image from provided URL.");
                }
            } else {
                image = ImageIO.read(new File(inputFileOrLink));
            }

            if (image == null) {
                return null;
            }

            int imgRows = image.getHeight();
            int imgCols = image.getWidth();
            int[][] pixelData = new int[imgRows][imgCols];

            for (int i = 0; i < imgRows; i++) {
                for (int j = 0; j < imgCols; j++) {
                    pixelData[i][j] = image.getRGB(j, i);
                }
            }

            return pixelData;
        } catch (Exception e) {
            System.out.println("Failed to load image: " + e.getLocalizedMessage());
            return null;
        }
    }

    public static void twoDToImage(int[][] imgData, String fileName) {
        try {
            int imgRows = imgData.length;
            int imgCols = imgData[0].length;
            BufferedImage result = new BufferedImage(imgCols, imgRows, BufferedImage.TYPE_INT_ARGB);

            for (int i = 0; i < imgRows; i++) {
                for (int j = 0; j < imgCols; j++) {
                    result.setRGB(j, i, imgData[i][j]);
                }
            }

            File output = new File(fileName);
            ImageIO.write(result, "png", output);  // Збереження в PNG для підтримки прозорості
        } catch (Exception e) {
            System.out.println("Failed to save image: " + e.getLocalizedMessage());
        }
    }

    public static int[] getRGBAFromPixel(int pixelColorValue) {
        Color pixelColor = new Color(pixelColorValue, true);
        return new int[] { pixelColor.getRed(), pixelColor.getGreen(), pixelColor.getBlue(), pixelColor.getAlpha() };
    }

    public static int getColorIntValFromRGBA(int[] colorData) {
        if (colorData.length == 4) {
            Color color = new Color(colorData[0], colorData[1], colorData[2], colorData[3]);
            return color.getRGB();
        } else {
            System.out.println("Incorrect number of elements in RGBA array.");
            return -1;
        }
    }

    public static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    public static void viewImageData(int[][] imageTwoD) {
        if (imageTwoD.length > 3 && imageTwoD[0].length > 3) {
            int[][] rawPixels = new int[3][3];
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    rawPixels[i][j] = imageTwoD[i][j];
                }
            }
            System.out.println("Raw pixel data from the top left corner.");
            System.out.print(Arrays.deepToString(rawPixels).replace("],", "],\n") + "\n");

            int[][][] rgbPixels = new int[3][3][4];
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    rgbPixels[i][j] = getRGBAFromPixel(imageTwoD[i][j]);
                }
            }
            System.out.println();
            System.out.println("Extracted RGBA pixel data from the top left corner.");

            for (int[][] row : rgbPixels) {
                System.out.print(Arrays.deepToString(row) + System.lineSeparator());
            }
        } else {
            System.out.println("The image is not large enough to extract 9 pixels from the top left corner");
        }
    }
}
