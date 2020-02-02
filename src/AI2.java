import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.util.Duration;

public class AI2 extends Entity {
    private double[] directions;
    private int numberOfMoves;
    private double direction;
    private int moveCounter;
    private Timeline directionTimeline, movementTimeline;
    private double fitness;
    private boolean outOfMoves;

    public AI2(double x, double y, GamePane gamePane, Polygon levelSidesPolygon, double[] directions) {
        super(x, y, gamePane, levelSidesPolygon);
        this.directions = directions;
        this.numberOfMoves = directions.length;

        setFill(Color.color(Math.random(), Math.random(), Math.random()));

        createDirectionTimeline();
        createMovementTimeline();
    }

    private void createDirectionTimeline() {
        setNextDirection(); // Set the first direction

        directionTimeline = new Timeline(new KeyFrame(Duration.millis(directionSpeed), e -> setNextDirection()));
        directionTimeline.setCycleCount(Timeline.INDEFINITE);
        directionTimeline.play();
    }

    private void setNextDirection() {
        if (moveCounter < numberOfMoves)
            direction = directions[moveCounter++];
        else {
            outOfMoves = true;
            die(); // Die from running out of moves
        }
    }

    private void createMovementTimeline() {
        movementTimeline = new Timeline(new KeyFrame(Duration.millis(movementSpeed), e -> move()));
        movementTimeline.setCycleCount(Timeline.INDEFINITE);
        movementTimeline.play();
    }

    public void stopTimelines() {
        movementTimeline.stop();
        directionTimeline.stop();
    }

    private void move() {
        double degreesInRadians = Math.toRadians(360.0 * direction);
        double distanceX = distancePerMove * Math.sin(degreesInRadians);
        double distanceY = distancePerMove * Math.cos(degreesInRadians);
        double leftX = getX();
        double topY = getY();
        double rightX = leftX + entityWidthAndHeight;
        double bottomY = topY + entityWidthAndHeight;

        if (levelSidesPolygon.contains(leftX + distanceX, topY + distanceY) &&
                levelSidesPolygon.contains(leftX + distanceX, bottomY + distanceY) &&
                levelSidesPolygon.contains(rightX + distanceX, topY + distanceY) &&
                levelSidesPolygon.contains(rightX + distanceX, bottomY + distanceY)) {
            setX(leftX + distanceX);
            setY(topY + distanceY);
        }

        isWinner();
    }

    private void isWinner() {
        if (getX() > GamePane.goalX) {
            winner = true;
            stopTimelines();
            gamePane.stopAndRemoveAllNonWinnerAi2s();
            gamePane.winnerFound(directions);
        }
    }

    public void die() {
        dead = true;
        gamePane.getChildren().remove(this);
        stopTimelines();
        calculateFitness();

        if (!outOfMoves) // Don't update the alive AIs label when the AI dies from running out of moves
            gamePane.updateAliveLabel();

        gamePane.checkAllDead();
    }

    private void calculateFitness() {
        double rightX = getX() + entityWidthAndHeight;
        double distanceToGoal = GamePane.goalX - rightX;
        fitness = distanceToGoal;
//        fitness = 1 / (distanceToGoal * distanceToGoal); // Higher fitness score = better fitness
    }

    public double getFitness() {
        return fitness;
    }

    public double[] getDirections() {
        return directions;
    }

    @Override
    public String toString() {
        return "AI2{" +
                "fitness=" + fitness +
                '}';
    }

    public boolean isOutOfMoves() {
        return outOfMoves;
    }
}
