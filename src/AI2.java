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
    private Timeline movementTimeline;
    private double fitness;
    private boolean outOfMoves;

    public AI2(double x, double y, GamePane gamePane, Polygon levelSidesPolygon, double[] directions) {
        super(x, y, gamePane, levelSidesPolygon);
        this.directions = directions;
        this.numberOfMoves = directions.length;

        setFill(Color.color(Math.random(), Math.random(), Math.random()));

        createMovementTimeline();
    }

    private void setNextDirection() {
        ++moveCounter;
        if (moveCounter < numberOfMoves) {
            direction = directions[moveCounter];
        } else {
            outOfMoves = true;
            die(); // Die from running out of moves
        }
    }

    private void createMovementTimeline() {
        movementTimeline = new Timeline(new KeyFrame(Duration.millis(movementSpeed), e -> move()));
        movementTimeline.setCycleCount(Timeline.INDEFINITE);
    }

    public void startTimeline() {
        movementTimeline.play();
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

        ++directionCounter;

        if (directionCounter == numberOfMovementCyclesPerDirection) {
            directionCounter = 0;
            setNextDirection();
        }
    }

    private void isWinner() {
        if (getX() > GamePane.goalX) {
            winner = true;
            movementTimeline.stop();
            gamePane.stopAndRemoveAllNonWinnerAi2s();
            gamePane.winnerFound(directions);
        }
    }

    public void die() {
        movementTimeline.stop();
        dead = true;
        gamePane.getChildren().remove(this);
        calculateFitness();
        gamePane.countDead(outOfMoves);
    }

    private void calculateFitness() {
        double rightX = getX() + entityWidthAndHeight;
        fitness = GamePane.goalX - rightX;
    }

    public double getFitness() {
        return fitness;
    }

    public double[] getDirections() {
        return directions;
    }

    public boolean isOutOfMoves() {
        return outOfMoves;
    }
}
