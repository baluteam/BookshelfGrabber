/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bookshelfgrabber;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;

/**
 *
 */
public class BookshelfGrabber {

    private static final int SEC_IN_MILLIS = 1000;
    private static final int DELAY_IN_SEC = 1;
    private static final int START_DELAY_IN_SEC = 10;
    private static final int MAX_RAND_WAIT_IN_SEC = 4;
    private static final int EBOOK_APP_UPPER_X = 0;
    private static final int EBOOK_APP_UPPER_Y = 90; //below the file and the navigation
    private static final int TASKBAR_HEIGHT_BELOW = 45;
    private static final int NUMBER_OF_PAGES_TO_SCROLL_DOWN = 17 + 2294 + 1;
    private static final int BOOK_UPPER_LEFT_X = 600; //from screen edge
    private static final int BOOK_UPPER_LEFT_Y = 0; //from EBOOK_APP_UPPER_Y
    private static final int BOOK_WIDTH = 717;
    private static final String PATH_TO_SAVE_IMAGES = "D:\\temp2\\";
    private static final String IMAGE_TYPE = "PNG";
    private static final String IMAGE_EXTENSION = ".png";
    private static int currentPage = 1;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new GUICounter(START_DELAY_IN_SEC, (n) -> {
            initScreenGrabbing();
        });
    }

    private static void initScreenGrabbing() {
        try {
            Thread.sleep(DELAY_IN_SEC * SEC_IN_MILLIS);
            startScreenGrabbing();
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
            e.printStackTrace(System.err);
        }
    }
    
    private static void startScreenGrabbing() {
        try {
            Robot robot = new Robot();
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); //1920x1080
            final int EBOOK_APP_WIDTH = screenSize.width;
            final int EBOOK_APP_HEIGHT = screenSize.height - EBOOK_APP_UPPER_Y - TASKBAR_HEIGHT_BELOW;
            final int BOOK_HEIGHT =  EBOOK_APP_HEIGHT - 15;
            Rectangle screenRect = new Rectangle(EBOOK_APP_UPPER_X, EBOOK_APP_UPPER_Y, EBOOK_APP_WIDTH, EBOOK_APP_HEIGHT);
            BufferedImage previousScreen;
            BufferedImage currentScreen;
            BufferedImage bookScreen;
            while(currentPage <= NUMBER_OF_PAGES_TO_SCROLL_DOWN) {
                previousScreen = robot.createScreenCapture(screenRect);
                int firstDelayInSec = new Random().nextInt(MAX_RAND_WAIT_IN_SEC) + 2;
                int secondDelayInSec = new Random().nextInt(MAX_RAND_WAIT_IN_SEC) + 2;
                Thread.sleep((firstDelayInSec + secondDelayInSec) * SEC_IN_MILLIS);
                currentScreen = robot.createScreenCapture(screenRect);
                if(!compareImages(previousScreen, currentScreen)) {
                    String errorImageFileName1 = "_error_1" + IMAGE_EXTENSION;
                    String errorImageFileName2 = "_error_2" + IMAGE_EXTENSION;
                    System.err.println("Check " + PATH_TO_SAVE_IMAGES + "folder. '" + errorImageFileName1 + "' and '" + errorImageFileName2 + "' are not the same.");
                    ImageIO.write(previousScreen, IMAGE_TYPE, new File(PATH_TO_SAVE_IMAGES + errorImageFileName1));
                    ImageIO.write(currentScreen, IMAGE_TYPE, new File(PATH_TO_SAVE_IMAGES + errorImageFileName2));
                    System.exit(-1);
                }
                bookScreen = currentScreen.getSubimage(BOOK_UPPER_LEFT_X, BOOK_UPPER_LEFT_Y, BOOK_WIDTH, BOOK_HEIGHT);
                String imageFileName = currentPage + IMAGE_EXTENSION;
                ImageIO.write(bookScreen, IMAGE_TYPE, new File(PATH_TO_SAVE_IMAGES + imageFileName));
                currentPage++;
                robot.mouseWheel(1);
                Thread.sleep(1 * SEC_IN_MILLIS);
            }
            System.out.println((currentPage-1) + " images have been created successfully into " + PATH_TO_SAVE_IMAGES);
        } catch (AWTException | IOException | InterruptedException e) {
            System.err.println(e.getMessage());
            e.printStackTrace(System.err);
        }
    }

    /**
     * Compares two images pixel by pixel.
     *
     * @param imgA the first image.
     * @param imgB the second image.
     * @return whether the images are both the same or not.
     */
    public static boolean compareImages(BufferedImage imgA, BufferedImage imgB) {
        // The images must be the same size.
        if (imgA.getWidth() != imgB.getWidth() || imgA.getHeight() != imgB.getHeight()) {
            return false;
        }
        int width = imgA.getWidth();
        int height = imgA.getHeight();
        // Loop over every pixel.
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // Compare the pixels for equality.
                if (imgA.getRGB(x, y) != imgB.getRGB(x, y)) {
                    return false;
                }
            }
        }
        return true;
    }
}
