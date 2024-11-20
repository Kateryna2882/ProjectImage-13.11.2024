import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.Random;
import javax.imageio.ImageIO;

public class Solution {
    public static void main(String[] args) {
        // Завантаження зображення apple.jpg
        int[][] imageData = imgToTwoD("./apple.jpg");

        // Обрізання рамок
        int[][] trimmed = trimBorders(imageData, 60);
        twoDToImage(trimmed, "./trimmed_apple.jpg");

        // Негатив кольорів
        int[][] negative = negativeColor(imageData);
        twoDToImage(negative, "./negative_apple.jpg");

        // Розтягування по горизонталі
        int[][] stretchedHImg = stretchHorizontally(imageData);
        twoDToImage(stretchedHImg, "./stretched_apple.jpg");

        // Зменшення по вертикалі
        int[][] shrankVImg = shrinkVertically(imageData);
        twoDToImage(shrankVImg, "./shrank_apple.jpg");

        // Інвертоване зображення
        int[][] invertedImg = invertImage(imageData);
        twoDToImage(invertedImg, "./inverted_apple.jpg");

        // Колірний фільтр
        int[][] coloredImg = colorFilter(imageData, -75, 30, -30);
        twoDToImage(coloredImg, "./colored_apple.jpg");

        // Створення випадкового зображення
        int[][] blankImg = new int[500][500];
        int[][] randomImg = paintRandomImage(blankImg);
        twoDToImage(randomImg, "./random_img.jpg");

        // Малювання прямокутника
        int[] rgba = { 255, 255, 0, 255 };
        int[][] rectangleImg = paintRectangle(randomImg, 200, 200, 100, 100, getColorIntValFromRGBA(rgba));
        twoDToImage(rectangleImg, "./rectangle.jpg");

        // Генерація випадкових прямокутників
        int[][] generatedRectangles = generateRectangles(randomImg, 1000);
        twoDToImage(generatedRectangles, "./generated_rect.jpg");
    }

    // Методи обробки зображень
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
                manipulatedImg[i][j * 2] = imageTwoD[i][j];
                manipulatedImg[i][j * 2 + 1] = imageTwoD[i][j];
            }
        }
        return manipulatedImg;
    }

    public static int[][] shrinkVertically(int[][] imageTwoD) {
        int[][] manipulatedImg = new int[imageTwoD.length / 2][imageTwoD[0].length];
        for (int i = 0; i < manipulatedImg.length; i++) {
            for (int j = 0; j < imageTwoD[i].length; j++) {
                manipulatedImg[i][j] = imageTwoD[i * 2][j];
            }
        }
        return manipulatedImg;
    }

    public static int[][] invertImage(int[][] imageTwoD) {
        int[][] invertedImg = new int[imageTwoD.length][imageTwoD[0].length];
        for (int i = 0; i < imageTwoD.length; i++) {
            for (int j = 0; j < imageTwoD[i].length; j++) {
                invertedImg[i][j] = imageTwoD[imageTwoD.length - 1 - i][imageTwoD[i].length - 1 - j];
            }
        }
        return invertedImg;
    }

    public static int[][] colorFilter(int[][] imageTwoD, int redChangeValue, int greenChangeValue, int blueChangeValue) {
        int[][] manipulatedImg = new int[imageTwoD.length][imageTwoD[0].length];
        for (int i = 0; i < imageTwoD.length; i++) {
            for (int j = 0; j < imageTwoD[i].length; j++) {
                int[] rgba = getRGBAFromPixel(imageTwoD[i][j]);
                rgba[0] = clamp(rgba[0] + redChangeValue);
                rgba[1] = clamp(rgba[1] + greenChangeValue);
                rgba[2] = clamp(rgba[2] + blueChangeValue);
                manipulatedImg[i][j] = getColorIntValFromRGBA(rgba);
            }
        }
        return manipulatedImg;
    }

    // Метод обмеження значення (0 - 255)
    private static int clamp(int value) {
        return Math.max(0, Math.min(255, value));
    }

    // Методи для малювання
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

    public static int[][] paintRectangle(int[][] canvas, int rowPosition, int colPosition, int width, int height, int color) {
        for (int i = rowPosition; i < rowPosition + height && i < canvas.length; i++) {
            for (int j = colPosition; j < colPosition + width && j < canvas[i].length; j++) {
                canvas[i][j] = color;
            }
        }
        return canvas;
    }

    public static int[][] generateRectangles(int[][] canvas, int numRectangles) {
        Random rand = new Random();
        for (int i = 0; i < numRectangles; i++) {
            int randomWidth = rand.nextInt(canvas[0].length);
            int randomHeight = rand.nextInt(canvas.length);
            int randomRowPos = rand.nextInt(Math.max(1, canvas.length - randomHeight));
            int randomColPos = rand.nextInt(Math.max(1, canvas[0].length - randomWidth));
            int[] rgba = { rand.nextInt(256), rand.nextInt(256), rand.nextInt(256), 255 };
            int randomColor = getColorIntValFromRGBA(rgba);
            paintRectangle(canvas, randomRowPos, randomColPos, randomWidth, randomHeight, randomColor);
        }
        return canvas;
    }

    // Утиліти
    public static int[][] imgToTwoD(String inputFileOrLink) {
        try {
            BufferedImage image = null;
            if (inputFileOrLink.toLowerCase().startsWith("http")) {
                URL imageUrl = new URL(inputFileOrLink);
                image = ImageIO.read(imageUrl);
            } else {
                image = ImageIO.read(new File(inputFileOrLink));
            }

            if (image == null) {
                throw new IllegalArgumentException("Не вдалося завантажити зображення.");
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
            System.out.println("Помилка при завантаженні зображення: " + e.getMessage());
            return null;
        }
    }

    public static void twoDToImage(int[][] imgData, String fileName) {
        try {
            int imageHeight = imgData.length;
            int imageWidth = imgData[0].length;
            BufferedImage result = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);

            for (int i = 0; i < imageHeight; i++) {
                for (int j = 0; j < imageWidth; j++) {
                    result.setRGB(j, i, imgData[i][j]);
                }
            }

            File output = new File(fileName);
            ImageIO.write(result, "jpg", output);
        } catch (Exception e) {
            System.out.println("Помилка при збереженні зображення: " + e.getMessage());
        }
    }

    public static int[] getRGBAFromPixel(int pixelColorValue) {
        Color color = new Color(pixelColorValue, true);
        return new int[]{ color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha() };
    }

    public static int getColorIntValFromRGBA(int[] colorData) {
        return new Color(colorData[0], colorData[1], colorData[2], colorData[3]).getRGB();
    }
}
