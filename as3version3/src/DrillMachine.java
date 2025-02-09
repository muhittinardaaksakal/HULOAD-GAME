import javafx.scene.input.KeyEvent;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import java.util.List;

/**
 * Represents the drilling machine in the game. This class manages the drill's position,
 * movement, fuel, mineral collection, and interaction with the game environment.
 */
public class DrillMachine {
    private double imageX = 400;
    private double imageY = 50;
    private long lastMoveTime = System.currentTimeMillis();
    private int currentImageIndex = 0;
    private int fuel = 10000; // Initial fuel
    private int haul = 0; // Minerals collected
    private int money = 0; // Money earned from minerals
    private boolean isGameOver = false; // Game status flag
    private Background background;
    private Image[][] soilGrid;
    private List<Image> soilImageList;
    private GraphicsContext gc;
    private List<Image> drillImageList;

    /**
     * Constructor to initialize the DrillMachine with required game components.
     *
     * @param soilGrid       the grid representation of the soil and other elements
     * @param drillImageList list of images for different drill states
     * @param soilImageList  list of soil and mineral images
     * @param gc             the graphics context for drawing on the canvas
     * @param background     the game's background
     */
    public DrillMachine(Image[][] soilGrid, List<Image> drillImageList, List<Image> soilImageList, GraphicsContext gc, Background background) {
        this.background = background;
        this.soilGrid = soilGrid;
        this.drillImageList = drillImageList;
        this.soilImageList = soilImageList;
        this.gc = gc;
    }

    /**
     * Checks for automatic downward movement based on time elapsed.
     */
    public void checkForAutomaticMovement() {
        if (System.currentTimeMillis() - lastMoveTime >= 1000) {
            int col = (int) (imageX / 50);
            int row = (int) (imageY / 50) + 1;
            if (row < 12 && soilGrid[row][col].equals(soilImageList.get(14))) {
                imageY += 50;
                lastMoveTime = System.currentTimeMillis();
            }
        }
    }

    /**
     * Checks for obstacles in the path of the drill machine based on the direction of movement.
     *
     * @param event the key event triggering the movement
     * @return true if there are no obstacles; false otherwise
     */
    public boolean checkForObstacles(KeyEvent event) {
        int numRows = 12;  // Total number of rows
        int numCols = 16;  // Total number of columns
        int col = (int) (imageX / 50);
        int row = (int) ((imageY ) / 50);

        switch (event.getCode()) {
            case UP:
                if (row > 0 && (soilGrid[row - 1][col] == soilImageList.get(20) || soilGrid[row - 1][col] == soilImageList.get(21))) {
                    return false;
                }
                break;
            case DOWN:
                if (row < numRows - 1 && (soilGrid[row + 1][col] == soilImageList.get(20) || soilGrid[row + 1][col] == soilImageList.get(21))) {
                    return false;
                }
                break;
            case RIGHT:
                if (col < numCols - 1 && (soilGrid[row][col + 1] == soilImageList.get(20) || soilGrid[row][col + 1] == soilImageList.get(21))) {
                    return false;
                }
                break;
            case LEFT:
                if (col > 0 && (soilGrid[row][col - 1] == soilImageList.get(20) || soilGrid[row][col - 1] == soilImageList.get(21))) {
                    return false;
                }
                break;
        }
        return true;
    }

    /**
     * Checks if upward movement is possible.
     * @return true if the move is possible; false otherwise
     */
    private boolean upMoveCheck() {
        int col = (int) (imageX / 50);
        int row = (int) ((imageY ) / 50);

        if (row > 0 && (soilGrid[row - 1][col] == soilImageList.get(14)) ) {
            return true;
        }

        return false;
    }

    /**
     * Manages the digging process, updating the collected minerals and managing collisions.
     */
    public void digSoil() {
        int col = (int) (imageX / 50);
        int row = (int) ((imageY ) / 50);

        if (row >= 0 && row < 12 && col >= 0 && col < 16) {
            Image currentSoil = soilGrid[row][col];
            // Check for collision with lava or obstacle
            if (currentSoil.equals(soilImageList.get(17))) {
                isGameOver = true; // Set game over if lava or obstacle is encountered
            } else if (currentSoil.equals(soilImageList.get(38))) {
                haul += 100;
                money += 10000000;
            } else if (currentSoil.equals(soilImageList.get(40))) {
                haul += 60;
                money += 300000;
            } else if (currentSoil.equals(soilImageList.get(44))) {
                haul += 80;
                money += 1600000;
            }
            soilGrid[row][col] = soilImageList.get(14);  // Change to a "dug" image
        }
    }

    /**
     * Handles key press events to control drill movements and interactions.
     * @param event the KeyEvent triggering the action
     * @param gc the GraphicsContext for drawing updates
     */
    public void handleKeyPress(KeyEvent event, GraphicsContext gc) {
        switch (event.getCode()) {
            case UP:
                currentImageIndex = 50;
                fuel -=100;
                if (checkForObstacles(event)) {
                    if (upMoveCheck()) {
                        imageY -= 50;
                    }
                }
                break;
            case DOWN:
                currentImageIndex = 44;
                fuel -=100;
                if (checkForObstacles(event)) {imageY += 50;}
                digSoil();
                break;
            case LEFT:
                currentImageIndex = 3;
                fuel -=100;
                if (checkForObstacles(event)) {imageX -= 50;}
                digSoil();
                break;
            case RIGHT:
                currentImageIndex = 56;
                fuel -=100;
                if (checkForObstacles(event)) {imageX += 50;}
                digSoil();
                break;
        }

        // Redraw the background canvas
        background.redrawCanvas();
    }

    // Additional getter and setter methods...


    // For imageX
    public double getImageX() {
        return imageX;
    }



    // For imageY
    public double getImageY() {
        return imageY;
    }



    // For lastMoveTime




    // For currentImageIndex
    public int getCurrentImageIndex() {
        return currentImageIndex;
    }



    // For fuel
    public int getFuel() {
        return fuel;
    }

    public void setFuel(int fuel) {
        this.fuel = fuel;
    }

    // For haul
    public int getHaul() {
        return haul;
    }

    public void setHaul(int haul) {
        this.haul = haul;
    }

    // For money
    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    // For isGameOver
    public boolean isGameOver() {
        return isGameOver;
    }

    // For drillImageList
    public List<Image> getDrillImageList() {
        return drillImageList;
    }

}
