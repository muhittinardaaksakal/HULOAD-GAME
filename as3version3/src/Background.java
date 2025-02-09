import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import java.util.List;
import java.util.Random;
/**
 * The Background class sets up and controls the main game environment for the HU-Load game.
 */
public class Background {
    private Canvas canvas;
    private GraphicsContext gc;
    private Image[][] soilGrid;
    private List<Image> drillImageList;
    private List<Image> soilImageList;
    private DrillMachine drillMachine;
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    /**
     * Constructs the game environment, sets up the UI components and initializes the game.
     * @param primaryStage The primary stage for this application, onto which the game scene is set.
     */
    public Background(Stage primaryStage) {
        primaryStage.setTitle("HU-Load Game");
        StackPane root = new StackPane();
        canvas = new Canvas(800, 600);
        gc = canvas.getGraphicsContext2D();
        root.getChildren().add(canvas);

        this.soilImageList = Main.loadImagesFromFolder("assets/underground/");
        this.drillImageList = Main.loadImagesFromFolder("assets/drill/");
        initializeSoilGrid();

        // Pass 'this' reference to the DrillMachine constructor
        this.drillMachine = new DrillMachine(soilGrid,drillImageList, soilImageList, gc, this);

        canvas.setFocusTraversable(true);
        canvas.setOnKeyPressed(event -> drillMachine.handleKeyPress(event, gc));

        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.4), e -> redrawCanvas()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    /**
     * Initializes the soil grid and places images including obstacles and treasures randomly within the grid.
     */
    public void initializeSoilGrid() {
        int numRows = 12;
        int numCols = 16;
        soilGrid = new Image[numRows][numCols]; // Allocate space for the grid

        // Default to the first soil image for all grid positions
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                soilGrid[row][col] = soilImageList.get(25);
            }
        }

        // Set the boundaries
        Image obstacleImage_1 = soilImageList.get(21);

        for (int row = 2; row < numRows; row++) {
            int col = 15;
            soilGrid[row][col] = obstacleImage_1;
        }
        for (int row = 2; row < numRows; row++) {
            int col = 0;
            soilGrid[row][col] = obstacleImage_1;
        }
        for (int col = 0; col < numCols; col++){
            int row = 11;
            soilGrid[row][col] = obstacleImage_1;

        }

        for (int row = 0; row < numRows; row++){
            int col = 0;
            soilGrid[row][col] = obstacleImage_1;
        }
        Image grassImage = soilImageList.get(30);
        for (int col = 0; col < numCols; col++){
            int row = 2;
            soilGrid[row][col] = grassImage;


        }
        Image skyImage = soilImageList.get(14);
        for (int col = 0; col < numCols; col++){
            int row = 0;
            soilGrid[row][col] = skyImage;


        }
        for (int col = 0; col < numCols; col++) {
            int row = 1;
            soilGrid[row][col] = skyImage;
        }

        // Set the random items
        Random rand = new Random();
        int minRow = 3;
        int maxRow = 9;
        int minCol = 2;
        int maxCol = 13;
        int[] rows = new int[18];

        for (int i = 0; i < rows.length; i++) {
            rows[i] = rand.nextInt((maxRow - minRow) + 1) + minRow;
        }
        int[] columns = new int[18];

        for (int i = 0; i < columns.length; i++) {
            columns[i] = rand.nextInt((maxCol - minCol) + 1) + minCol;
        }
        Image lavaImage = soilImageList.get(17);
        Image diamondImage = soilImageList.get(38);
        Image emeraldImage = soilImageList.get(40);
        Image rubyImage = soilImageList.get(44);
        Image obstacleImage_2 = soilImageList.get(20);
        soilGrid[rows[0]][columns[0]] = lavaImage;
        soilGrid[rows[1]][columns[1]] = lavaImage;
        soilGrid[rows[2]][columns[2]] = lavaImage;
        soilGrid[rows[3]][columns[3]] = lavaImage;
        soilGrid[rows[4]][columns[4]] = obstacleImage_2;
        soilGrid[rows[5]][columns[5]] = obstacleImage_2;
        soilGrid[rows[6]][columns[6]] = obstacleImage_2;
        soilGrid[rows[7]][columns[7]] = obstacleImage_2;
        soilGrid[rows[8]][columns[8]] = diamondImage;
        soilGrid[rows[9]][columns[9]] = diamondImage;
        soilGrid[rows[10]][columns[10]] = emeraldImage;
        soilGrid[rows[11]][columns[11]] = emeraldImage;
        soilGrid[rows[12]][columns[12]] = rubyImage;
        soilGrid[rows[13]][columns[13]] = diamondImage;
        soilGrid[rows[14]][columns[14]] = emeraldImage;
        soilGrid[rows[15]][columns[15]] = rubyImage;
        soilGrid[rows[16]][columns[16]] = lavaImage;
        soilGrid[rows[17]][columns[17]] = obstacleImage_2;


    }
    /**
     * Draws elements like the soil and any images on the canvas.
     * @param gc the GraphicsContext of the canvas to draw on
     */
    private void drawImages(GraphicsContext gc) {
        int numRows = 12;
        int numCols = 16;

        // Draw images from grid
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                gc.drawImage(soilGrid[row][col], col * 50, row * 50);
            }
        }
    }

    public void redrawCanvas() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        if (drillMachine.getFuel() <= 0) {
            // Fill the background with green when game is over
            gc.setFill(Color.GREEN);
            gc.fillRect(0, 0, WIDTH, HEIGHT);

            // Set text color to black for the game over text and stats
            gc.setFill(Color.BLACK);
            gc.setFont(Font.font("Arial", 48)); // Larger font for "Game Over"
            gc.fillText("GAME OVER", WIDTH / 2 - 150, HEIGHT / 2 - 50);

            gc.setFont(Font.font("Arial", 25)); // Smaller font for details
            gc.fillText("Collected Money: " + drillMachine.getMoney(), WIDTH / 2 - 150, HEIGHT / 2 + 30);
        }
        else if (drillMachine.isGameOver()) {
            // Fill the background with red when game is over
            gc.setFill(Color.RED);
            gc.fillRect(0, 0, WIDTH, HEIGHT);

            // Set text color to white for the game over text
            gc.setFill(Color.WHITE);
            gc.setFont(Font.font("Arial", 48)); // Larger font for "Game Over"
            gc.fillText("GAME OVER", WIDTH / 2 - 150, HEIGHT / 2 - 50);

            gc.setFont(Font.font("Arial", 25)); // Smaller font for details
        }
        else {
            // Regular game drawing
            gc.setFill(Color.SKYBLUE);
            gc.fillRect(0, 0, WIDTH, HEIGHT);
            drillMachine.setFuel(drillMachine.getFuel() - 1);
            drillMachine.checkForAutomaticMovement();
            // Draw the ground
            gc.setFill(Color.BROWN);
            gc.fillRect(0, 100, WIDTH, HEIGHT - 100);

            // Draw the drill and other images
            drawImages(gc);

            gc.drawImage(drillMachine.getDrillImageList().get(drillMachine.getCurrentImageIndex()), drillMachine.getImageX(), drillMachine.getImageY());

            // Display the game status
            gc.setFill(Color.BLACK);
            gc.setFont(Font.font("Arial", 25));
            gc.fillText("Fuel: " + drillMachine.getFuel(), 10, 20);
            gc.fillText("Money: " + drillMachine.getMoney(), 10, 50);
            gc.fillText("Haul: " + drillMachine.getHaul(), 10, 80);
        }
    }
}
