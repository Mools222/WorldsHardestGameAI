import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

public class GamePane extends Pane {
    public static double gameSpeed = 1; // A speed of >=3 seems to make it near impossible to find a winner
    private static double levelOffsetInPixels = 50; // Move the level a bit down on the pane
    public static double goalX = 750 + levelOffsetInPixels, goalY = 25 + levelOffsetInPixels;

    private Polygon levelWallsPolygon;
    public Player player;
    public AI[] aiArray;
    public AI2[] ai2Array;
    public AI2Winner[] ai2WinnerArray;
    private Obstacle[] obstaclesArray;
    public boolean createPlayer, createAi, createAi2, createAiWinner;
    private int generationNumber, numberOfAis, numberOfMoves, movesGained;
    private AtomicInteger aisWithMovesLeft, aisAlive; // Thread safe integers
    private boolean gameStopped, winnerFound;
    public long startTime;
    public int winnerTimesCounter;

    // Used for naturalSelection2()
    private double runningSum = 0, randomFitness;

    public GamePane() {
        createLevel();
    }

    public void start() {
        gameStopped = false;
        GameView.labelGameStatus.setText("Game running.");
        initializeVariables();

        if (createPlayer)
            createPlayer();

        if (createAi)
            createAi();

        if (createAi2)
            createAi2();

        if (createAiWinner) {
            GameView.labelGameStatus.setText("Game running. Some might die.");
            startTime = System.currentTimeMillis();
            createAiWinner();
        }

        createObstacles();
    }

    private void initializeVariables() {
        generationNumber = 1; // Set to 1 since the 1st generation doesn't go through any createAi2Babies method
        numberOfAis = 7000;
        numberOfMoves = 10;
        movesGained = 10;
        aisWithMovesLeft = new AtomicInteger(numberOfAis);
        aisAlive = new AtomicInteger(numberOfAis);
        winnerFound = false; // Needed for resetting
        winnerTimesCounter = 1;
    }

    private void createLevel() {
        addLevelColors();
        addLevelWalls();
    }

    private void addLevelWalls() {
        levelWallsPolygon = new Polygon();
        levelWallsPolygon.setFill(Color.TRANSPARENT);
        levelWallsPolygon.setStroke(Color.BLACK);
        levelWallsPolygon.setStrokeWidth(2);
        addLevelPoints(levelWallsPolygon.getPoints());
        getChildren().add(levelWallsPolygon);
    }

    private void addLevelPoints(ObservableList<Double> list) {
        File file = new File("Level 1.txt");

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNext()) {
                double x = scanner.nextDouble();
//                String comma = scanner.next();
                double y = scanner.nextDouble();

                list.add(x + levelOffsetInPixels);
                list.add(y + levelOffsetInPixels);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addLevelColors() {
        Rectangle rectangle1 = new Rectangle(0 + levelOffsetInPixels, 0 + levelOffsetInPixels, 150, 300);
        rectangle1.setStroke(Color.TRANSPARENT);
        rectangle1.setFill(Color.LIGHTGREEN);

        Rectangle rectangle2 = new Rectangle(200 + levelOffsetInPixels, 250 + levelOffsetInPixels, 50, 50);
        rectangle2.setStroke(Color.TRANSPARENT);
        rectangle2.setFill(Color.GREY);

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 4; j++) {
                if ((i % 2 == 0 && j % 2 == 0) || (i % 2 == 1 && j % 2 == 1)) {
                    double x = 200 + (50 * i);
                    double y = 50 + (50 * j);
                    Rectangle rectangle3 = new Rectangle(x + levelOffsetInPixels, y + levelOffsetInPixels, 50, 50);
                    rectangle3.setStroke(Color.TRANSPARENT);
                    rectangle3.setFill(Color.GREY);
                    getChildren().add(rectangle3);
                }
            }
        }

        Rectangle rectangle4 = new Rectangle(650 + levelOffsetInPixels, 0 + levelOffsetInPixels, 50, 50);
        rectangle4.setStroke(Color.TRANSPARENT);
        rectangle4.setFill(Color.GREY);

        Rectangle rectangle5 = new Rectangle(750 + levelOffsetInPixels, 0 + levelOffsetInPixels, 150, 300);
        rectangle5.setStroke(Color.TRANSPARENT);
        rectangle5.setFill(Color.LIGHTGREEN);

        getChildren().addAll(rectangle1, rectangle2, rectangle4, rectangle5);
    }

    private void createPlayer() {
        player = new Player(50 + levelOffsetInPixels, 125 + levelOffsetInPixels, this, levelWallsPolygon);
        getChildren().add(player);
    }

    private void createAi() {
        aiArray = new AI[numberOfAis];

        for (int i = 0; i < numberOfAis; i++) {
            AI ai = new AI(50 + levelOffsetInPixels, 125 + levelOffsetInPixels, this, levelWallsPolygon);
            aiArray[i] = ai;
            getChildren().add(ai);
        }
    }

    private void createAi2() {
        ai2Array = new AI2[numberOfAis];

        for (int i = 0; i < numberOfAis; i++) {
            double[] directions = createDirections(new double[numberOfMoves], 0);
            AI2 ai2 = new AI2(50 + levelOffsetInPixels, 125 + levelOffsetInPixels, this, levelWallsPolygon, directions);
            ai2Array[i] = ai2;
        }

        updateAi2Labels();

        addAi2();
        startAi2();
    }

    private double[] createDirections(double[] directions, int startingIndex) {
        for (int i = startingIndex; i < directions.length; i++)
            directions[i] = Math.random();

        return directions;
    }

    private void addAi2() {
        for (AI2 ai2 : ai2Array)
            getChildren().add(ai2);
    }

    // Trying to separate this out from creating the AIs and adding them to the pane, so they don't start moving at too different times
    private void startAi2() {
        for (AI2 ai2 : ai2Array)
            ai2.startTimeline();
    }

    private void createObstacles() {
        obstaclesArray = new Obstacle[4];
        double radius = 12.5;
        obstaclesArray[0] = new Obstacle(225 + levelOffsetInPixels, 75 + levelOffsetInPixels, radius, 1, player, aiArray, ai2Array, ai2WinnerArray);
        obstaclesArray[1] = new Obstacle(675 + levelOffsetInPixels, 125 + levelOffsetInPixels, radius, -1, player, aiArray, ai2Array, ai2WinnerArray);
        obstaclesArray[2] = new Obstacle(225 + levelOffsetInPixels, 175 + levelOffsetInPixels, radius, 1, player, aiArray, ai2Array, ai2WinnerArray);
        obstaclesArray[3] = new Obstacle(675 + levelOffsetInPixels, 225 + levelOffsetInPixels, radius, -1, player, aiArray, ai2Array, ai2WinnerArray);
        getChildren().addAll(obstaclesArray[0], obstaclesArray[1], obstaclesArray[2], obstaclesArray[3]);
    }

    private void removeObstacles() {
        for (Obstacle obstacle : obstaclesArray) {
            obstacle.stopMovementTimeline();
            getChildren().remove(obstacle);
        }
        obstaclesArray = null;
    }


    public void countDead(boolean outOfMoves) {
        if (!outOfMoves) // Don't update the "alive AIs" label when the AI dies from running out of moves
            GameView.labelAi2sAliveNumber.setText(String.valueOf(aisWithMovesLeft.decrementAndGet()));

        if (aisAlive.decrementAndGet() == 0 && !gameStopped && !winnerFound) {
//            createAi2Babies();
            createAi2BabiesImproved();
        }
    }

    public void reset() {
        gameStopped = true;
        GameView.labelGameStatus.setText("Game reset.");

        removeObstacles();
        removeAllEntities();
        resetLabels();
        enableViews();
    }

    private void removeAllEntities() {
        if (createPlayer) {
            getChildren().remove(player);
            player = null;
        }

        if (createAi) {
            for (AI ai : aiArray)
                getChildren().remove(ai);
            aiArray = null;
        }

        if (createAi2) {
            for (AI2 ai2 : ai2Array)
                ai2.die();
            ai2Array = null;
        }

        if (createAiWinner) {
            for (AI2Winner ai2Winner : ai2WinnerArray)
                ai2Winner.die();
            ai2WinnerArray = null;
        }
    }

    private void enableViews() {
        GameView.buttonStart.setDisable(false);
        GameView.checkBoxPlayer.setDisable(false);
        GameView.checkBoxSimpleAi.setDisable(false);
        GameView.checkBoxLearningAi.setDisable(false);
        GameView.checkBoxWinnerAis.setDisable(false);
    }

    private void createAi2Babies() {
        // Stop if no AI ran out of moves (meaning every AI got hit by an obstacle)
        if (aisWithMovesLeft.get() == 0) {
            System.out.println("All AI2s killed by obstacles");
            GameView.labelGameStatus.setText("All AIs died to obstacles. Please reset.");
            return;
        }

        // Calculate next generation's amount of moves
        int newNumberOfMoves = numberOfMoves + movesGained;

        // Create array for next generation
        AI2[] ai2BabiesArray = new AI2[numberOfAis];

        // Used for method 1 (see below)
//        double sumOfFitness = calculateSumOfFitness();
//        randomFitness = sumOfFitness * Math.random();
        // Used for method 2 (see below)
//        double highestFitness = calculateHighestFitness();
        // Used for method 3 (see below)
//        Arrays.parallelSort(ai2Array, Comparator.comparing(AI2::getFitness).reversed()); // Sort the list in descending order by fitness
        Arrays.parallelSort(ai2Array, Comparator.comparing(AI2::getFitness)); // Sort the list in ascending order by fitness (lowest distance to goal first)
        double topFitnessPercentageSelectedToBeParents = 0.1;

        for (int i = 0; i < numberOfAis; i++) {
            // Natural selection - Select most fit parent directions (i.e the ones that made it farthest)
            // Method 1
//            double[] parentDirections = naturalSelection1(highestFitness);
            // Method 2
//            double[] parentDirections = naturalSelection2(sumOfFitness);
            // Method 3
            double[] parentDirections = naturalSelection3((int) (i % (numberOfAis * topFitnessPercentageSelectedToBeParents)));

            // Mutate parent directions
            double[] newDirections = new double[newNumberOfMoves];
            System.arraycopy(parentDirections, 0, newDirections, 0, parentDirections.length);
            double[] mutatedDirections = createDirections(newDirections, numberOfMoves);

            // Create mutated baby
            ai2BabiesArray[i] = new AI2(50 + levelOffsetInPixels, 125 + levelOffsetInPixels, this, levelWallsPolygon, mutatedDirections);
        }

        // Initialize ai2Array with mutated babies
        ai2Array = ai2BabiesArray;

        // Increment generation
        ++generationNumber;

        // Increment numberOfMoves
        numberOfMoves = newNumberOfMoves;

        // Update labels
        updateAi2Labels();

        // Reset atomic ints
        aisWithMovesLeft = new AtomicInteger(numberOfAis);
        aisAlive = new AtomicInteger(numberOfAis);

        removeObstacles();
        addAi2();
        startAi2();
        createObstacles();
    }

    // This vastly improves the final time it takes the AI to make it to the goal (from ~20 seconds to ~13 seconds). However, it's not as fun to watch as the createAi2Babies() method, since the createAi2BabiesImproved() method takes multiple generations before the number of moves go up
    private void createAi2BabiesImproved() {
        // Stop if no AI ran out of moves (meaning every AI got hit by an obstacle)
        if (aisWithMovesLeft.get() == 0) {
            System.out.println("All AI2s killed by obstacles");
            GameView.labelGameStatus.setText("All AIs died to obstacles. Please reset.");
            return;
        }

        // Calculate next generation's amount of moves
        int numberOfMovesGainedThisGeneration = generationNumber > 0 && generationNumber % 5 == 0 ? movesGained : 0;
        int newNumberOfMoves = numberOfMoves + numberOfMovesGainedThisGeneration;

        // Create array for next generation
        AI2[] ai2BabiesArray = new AI2[numberOfAis];

        // Sort the list in ascending order by fitness (lowest distance to goal first)
        Arrays.parallelSort(ai2Array, Comparator.comparing(AI2::getFitness));

        double topFitnessPercentageSelectedToBeParents = 0.1;

        // If no new moves are gained, save the best directions from the generation and re-mutate the moves gained most recently for the rest
        if (numberOfMovesGainedThisGeneration == 0) {
            double topFitnessPercentageToBeSaved = 0.1;
            int numberOfAisSaved = (int) (numberOfAis * topFitnessPercentageToBeSaved);

            // Save the best directions without altering them
            for (int i = 0; i < numberOfAisSaved; i++) {
                double[] parentDirections = naturalSelection3((int) (i % (numberOfAis * topFitnessPercentageSelectedToBeParents)));
                ai2BabiesArray[i] = new AI2(50 + levelOffsetInPixels, 125 + levelOffsetInPixels, this, levelWallsPolygon, parentDirections);
            }

            // Re-mutate the rest of the directions
            for (int i = numberOfAisSaved; i < numberOfAis; i++) {
                double[] parentDirections = naturalSelection3((int) (i % (numberOfAis * topFitnessPercentageSelectedToBeParents)));
                double[] newDirections = new double[numberOfMoves];
                System.arraycopy(parentDirections, 0, newDirections, 0, parentDirections.length);
                double[] mutatedDirections = createDirections(newDirections, numberOfMoves - movesGained);
                ai2BabiesArray[i] = new AI2(50 + levelOffsetInPixels, 125 + levelOffsetInPixels, this, levelWallsPolygon, mutatedDirections);
            }
        } else { // If new moves are gained
            for (int i = 0; i < numberOfAis; i++) {
                // Natural selection - Select most fit parent directions (i.e the ones that made it farthest)
                double[] parentDirections = naturalSelection3((int) (i % (numberOfAis * topFitnessPercentageSelectedToBeParents)));

                // Mutate parent directions
                double[] newDirections = new double[newNumberOfMoves];
                System.arraycopy(parentDirections, 0, newDirections, 0, parentDirections.length);
                double[] mutatedDirections = createDirections(newDirections, numberOfMoves);

                // Create mutated baby
                ai2BabiesArray[i] = new AI2(50 + levelOffsetInPixels, 125 + levelOffsetInPixels, this, levelWallsPolygon, mutatedDirections);
            }
        }

        // Initialize ai2Array with mutated babies
        ai2Array = ai2BabiesArray;

        // Increment generation
        ++generationNumber;

        // Increment numberOfMoves
        numberOfMoves = newNumberOfMoves;

        // Update labels
        updateAi2Labels();

        // Reset atomic ints
        aisWithMovesLeft = new AtomicInteger(numberOfAis);
        aisAlive = new AtomicInteger(numberOfAis);

        removeObstacles();
        addAi2();
        startAi2();
        createObstacles();
    }

    private void updateAi2Labels() {
        GameView.labelGenerationNumber.setText(String.valueOf(generationNumber));
        GameView.labelNumberOfMovesNumber.setText(String.valueOf(numberOfMoves));
        GameView.labelAi2sAlivePreviousGenNumber.setText(String.valueOf(aisWithMovesLeft.get()));
        GameView.labelAi2sAliveNumber.setText(String.valueOf(numberOfAis));
    }

    private void resetLabels() {
        GameView.labelGenerationNumber.setText("N/A");
        GameView.labelNumberOfMovesNumber.setText("N/A");
        GameView.labelAi2sAlivePreviousGenNumber.setText("N/A");
        GameView.labelAi2sAliveNumber.setText("N/A");
        GameView.vBoxWinnerAiTimes.getChildren().clear();
        GameView.vBoxWinnerAiTimes.getChildren().add(new Label("Winning AI times:"));
    }

    private double calculateSumOfFitness() {
        double sumOfFitness = 0;
        for (AI2 ai2 : ai2Array)
            sumOfFitness += ai2.getFitness();
        return sumOfFitness;
    }

    private double calculateHighestFitness() {
        double highestFitness = 0;
        for (AI2 ai2 : ai2Array)
            if (ai2.getFitness() > highestFitness)
                highestFitness = ai2.getFitness();
        return highestFitness;
    }

    private double[] naturalSelection1(double highestFitness) {
        double topFitness = highestFitness * 0.95;

        while (true) {
            int randomIndex = (int) (numberOfAis * Math.random());
            AI2 randomAi2 = ai2Array[randomIndex];
            if (randomAi2.getFitness() >= topFitness && randomAi2.isOutOfMoves()) {
                return randomAi2.getDirections();
            }
        }
    }

    private double[] naturalSelection2(double sumOfFitness) {
        for (AI2 ai2 : ai2Array) {
            runningSum += ai2.getFitness();
            if (runningSum > randomFitness)
                return ai2.getDirections();
        }

        return null;
    }

    /**
     * The ai2Array is sorted by fitness (in this case fitness means least distance to goal x coordinate)
     * A percentage is chosen (e.g. 0.1 / 10 %) (see the variable getThisTopFitnessPercentage)
     * The createAi2Babies() creates a loop to create the ai2BabiesArray
     * For each new baby, the naturalSelection3() method is called with an index (i), which is mod'ed by numberOfAis * getThisTopFitnessPercentage
     * Example:
     * i = 230
     * numberOfAis = 1000
     * getThisTopFitnessPercentage = 0.1
     * numberOfAis * getThisTopFitnessPercentage = 100 - (Main case: Only parents in top 100 (index 0 to 99) are picked. Exception: If a parent in top 100 did not run out of moves (i.e. died to an obstacle), a parent at a higher index may be picked)
     * 230 % 100 = 30
     * If index 30 of ai2Array is not out of moves, this will the the baby's parent
     * If index 30 of ai2Array is out of moves, the method will recursively call itself and try the next index (31) - it will continue to do so until it reaches index 1000, which will then be modulus'ed by the recursive call to 0 and it starts from the beginning of the list. This only works as long as at least one AI didn't die from an obstacle, which is why the game stops if this is not the case
     */
    private double[] naturalSelection3(int i) {
        if (ai2Array[i].isOutOfMoves()) // Only get the directions where the AI ran out of moves. We don't want the ones where the AI got hit by an obstacle
            return ai2Array[i].getDirections();
        else {
            return naturalSelection3((i + 1) % numberOfAis);
        }
    }

    public void stopAndRemoveAllNonWinnerAi2s() {
        for (AI2 ai2 : ai2Array)
            if (!ai2.winner && !ai2.dead)
                ai2.die();
    }

    public void winnerFound(double[] directions) {
        winnerFound = true;

        ArrayList<double[]> winnerList = openWinnerList();
        winnerList.add(directions);
        GameView.labelNumberOfWinners.setText(String.valueOf(winnerList.size()));
        saveWinnerList(winnerList);
    }

    public ArrayList<double[]> openWinnerList() {
        File file = new File("AI2 winners list.dat");

        if (file.exists()) {
            try (ObjectInputStream input = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)))) {
                return (ArrayList<double[]>) input.readObject();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        System.out.println("Creating new file for winners.");
        return new ArrayList<>();
    }

    private void saveWinnerList(ArrayList<double[]> winnerList) {
        File file = new File("AI2 winners list.dat");

        try (ObjectOutputStream output = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file)))) {
            output.writeObject(winnerList);
            System.out.println("Added new winner to list.");
            GameView.labelGameStatus.setText("Game won. Added winner to file.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createAiWinner() {
        ArrayList<double[]> winnerList = openWinnerList();
        int numberOfAis = winnerList.size();
        ai2WinnerArray = new AI2Winner[numberOfAis];

        for (int i = 0; i < numberOfAis; i++) {
            double[] directions = winnerList.get(i);
            AI2Winner ai2Winner = new AI2Winner(50 + levelOffsetInPixels, 125 + levelOffsetInPixels, this, levelWallsPolygon, directions);
            ai2WinnerArray[i] = ai2Winner;
            getChildren().add(ai2Winner);
        }

        for (AI2Winner ai2Winner : ai2WinnerArray) {
            ai2Winner.startTimeline();
        }
    }
}
