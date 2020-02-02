import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.util.Duration;

public class AI extends Entity {
    private double direction;
    private Timeline movementTimeline;

    public AI(double x, double y, GamePane gamePane, Polygon levelSidesPolygon) {
        super(x, y, gamePane, levelSidesPolygon);

        setFill(Color.color(Math.random(), Math.random(), Math.random()));

        createDirectionTimeline();
        createMovementTimeline();
    }

    private void createDirectionTimeline() {
        setRandomDirection(); // Set the first direction

        Timeline directionTimeline = new Timeline(new KeyFrame(Duration.millis(directionSpeed), e -> setRandomDirection()));
        directionTimeline.setCycleCount(Timeline.INDEFINITE);
        directionTimeline.play();
    }

    private void setRandomDirection() {
        direction = Math.random();
    }

    private void createMovementTimeline() {
        movementTimeline = new Timeline(new KeyFrame(Duration.millis(movementSpeed), e -> move()));
        movementTimeline.setCycleCount(Timeline.INDEFINITE);
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
    }

    public void die() {
        if (!dead) {
            movementTimeline.stop();

            FadeTransition fadeTransition = new FadeTransition(Duration.millis(1000), this);
            fadeTransition.setFromValue(1.0);
            fadeTransition.setToValue(0.0);
            fadeTransition.play();

            fadeTransition.setOnFinished(event -> {
                dead = false;
                setX(100);
                setY(175);
                setOpacity(1);
                movementTimeline.play();
            });
        }

        dead = true;
    }
}
