import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.util.Duration;

public class AI2Winner extends Entity {
    private double[] directions;
    private int numberOfMoves;
    private double direction;
    private int moveCounter;
    private Timeline movementTimeline;

    public AI2Winner(double x, double y, GamePane gamePane, Polygon levelSidesPolygon, double[] directions) {
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
            movementTimeline.stop();
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
        if (getX() > GamePane.goalX && !winner) {
            winner = true;
            long finishTime = System.currentTimeMillis();
            double timeSeconds = (finishTime - gamePane.startTime) / 1000.0;

            Rectangle rectangle = new Rectangle(entityWidthAndHeight, entityWidthAndHeight, getFill());
            rectangle.setStroke(Color.BLACK);
            rectangle.setStrokeType(StrokeType.INSIDE);
            rectangle.setStrokeWidth(5);
            HBox hBox = new HBox(rectangle, new Label(gamePane.winnerTimesCounter++ + ". " + timeSeconds + " seconds"));
            hBox.setSpacing(10);
            GameView.vBoxWinnerAiTimes.getChildren().add(hBox);
        }
    }

    public void die() {
        dead = true;
        gamePane.getChildren().remove(this);
        movementTimeline.stop();
        if (!winner)
            System.out.println("Guess it wasn't a winner after all");
    }
}
