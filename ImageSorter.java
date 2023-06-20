import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;

public class ImageSorter extends JFrame implements KeyListener {

    private final String pathToImage = "***";
    public boolean isHorziontalDone;
    public boolean isVerticleDone;
    private BufferedImage bufferedImage;
    private int delay;
    private Timer globalTimer;

    public ImageSorter(String title) throws HeadlessException {
        super(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        pack();
        setVisible(true);
        addKeyListener(this);
        delay = 0;
        isHorziontalDone = false;
        isVerticleDone = false;


    }

    public static void main(String[] args) throws IOException {
        ImageSorter test = new ImageSorter("Lab6");
        test.loadImage(test.pathToImage);
        test.displayImage();
        test.startAnimatedBubbleSort();
    }

    public void loadImage(String fileName) throws IOException {
        bufferedImage = ImageIO.read(new File(fileName));
    }

    public void horizontalStep() {

        isHorziontalDone = true;
        for (int y = 0; y < bufferedImage.getHeight(); y++) {
            for (int x = 0; x < bufferedImage.getWidth() - 1; x++) {
                Raster raster = bufferedImage.getRaster();
                int[] pixel1 = raster.getPixel(x, y, new int[3]);
                int[] pixel2 = raster.getPixel(x + 1, y, new int[3]);

                int pixel1RGB = bufferedImage.getRGB(x, y);
                int pixel2RGB = bufferedImage.getRGB(x + 1, y);


                if (compareBrightness(pixel1, pixel2) == 0) {
                    bufferedImage.setRGB(x, y, pixel2RGB);
                    bufferedImage.setRGB(x + 1, y, pixel1RGB);
                    isHorziontalDone = false;
                }

            }
        }


    }

    public void verticleStep() {
        isVerticleDone = true;
        for (int x = 0; x < bufferedImage.getWidth(); x++) {
            for (int y = 0; y < bufferedImage.getHeight() - 1; y++) {
                Raster raster = bufferedImage.getRaster();
                int[] pixel1 = raster.getPixel(x, y, new int[3]);
                int[] pixel2 = raster.getPixel(x, y + 1, new int[3]);

                int pixel1RGB = bufferedImage.getRGB(x, y);
                int pixel2RGB = bufferedImage.getRGB(x, y + 1);


                if (compareBrightness(pixel1, pixel2) == 0) {
                    bufferedImage.setRGB(x, y, pixel2RGB);
                    bufferedImage.setRGB(x, y + 1, pixel1RGB);
                    isVerticleDone = false;
                }

            }
        }
    }

    public void diagonalStep() {

        horizontalStep();
        verticleStep();
        displayImage();

    }

    public void startAnimatedBubbleSort() {
        ActionListener animation = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                diagonalStep();
                if (isHorziontalDone && isVerticleDone) {
                    System.out.println("stop");
                    globalTimer.stop();
                }
            }
        };

        globalTimer = new Timer(delay, animation);
        globalTimer.start();

    }


    public void displayImage() {
        getContentPane().removeAll();
        getContentPane().add(new JLabel(new ImageIcon(bufferedImage)));
        pack();
        setLocationRelativeTo(null);

    }

    public int compareBrightness(int[] point1, int[] point2) {

        int redValueOfPoint1 = point1[0];
        int greenValueOfPoint1 = point1[1];
        int blueValueOfPoint1 = point1[2];

        int redValueOfPoint2 = point2[0];
        int greenValueOfPoint2 = point2[1];
        int blueValueOfPoint2 = point2[2];


        double brightnessOfPoint1 = 0.2126 * redValueOfPoint1 + 0.7152 * greenValueOfPoint1 + 0.0722 * blueValueOfPoint1;
        double brightnessOfPoint2 = 0.2126 * redValueOfPoint2 + 0.7152 * greenValueOfPoint2 + 0.0722 * blueValueOfPoint2;


        if (brightnessOfPoint1 > brightnessOfPoint2) {
            return 1;
        } else if (brightnessOfPoint1 < brightnessOfPoint2) {
            return 0;
        } else {
            return -1;
        }


    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyChar()) {
            case 'r' -> {
                try {
                    reloadTheImage();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
            case 's' -> {
                delay += 100;
                globalTimer.setDelay(delay);
            }
            case 'w' -> {
                if ((delay - 100 >= 0)) {
                    delay -= 100;
                    globalTimer.setDelay(delay);
                }
            }
        }

    }

    private void reloadTheImage() throws IOException {
        globalTimer.stop();
        loadImage(pathToImage);
        displayImage();
        startAnimatedBubbleSort();

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
