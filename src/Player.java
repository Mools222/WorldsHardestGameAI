import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.util.Duration;

public class Player extends Entity {
    private boolean up, down, left, right;
    private Timeline movementTimeline;

    public Player(double x, double y, GamePane gamePane, Polygon levelSidesPolygon) {
        super(x, y, gamePane, levelSidesPolygon);

        setFill(Color.RED);

        createControls();
        createMovementTimeline();
    }

    private void createControls() {
        gamePane.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.UP || event.getCode() == KeyCode.W)
                up = true;
            if (event.getCode() == KeyCode.DOWN || event.getCode() == KeyCode.S)
                down = true;
            if (event.getCode() == KeyCode.RIGHT || event.getCode() == KeyCode.D)
                right = true;
            if (event.getCode() == KeyCode.LEFT || event.getCode() == KeyCode.A)
                left = true;
        });

        gamePane.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.UP || event.getCode() == KeyCode.W)
                up = false;
            if (event.getCode() == KeyCode.DOWN || event.getCode() == KeyCode.S)
                down = false;
            if (event.getCode() == KeyCode.RIGHT || event.getCode() == KeyCode.D)
                right = false;
            if (event.getCode() == KeyCode.LEFT || event.getCode() == KeyCode.A)
                left = false;
        });
    }

    private void createMovementTimeline() {
        movementTimeline = new Timeline(new KeyFrame(Duration.millis(movementSpeed), e -> move()));
        movementTimeline.setCycleCount(Timeline.INDEFINITE);
        movementTimeline.play();
    }

    private void move() {
        double leftX = getX();
        double topY = getY();
        double rightX = leftX + entityWidthAndHeight;
        double bottomY = topY + entityWidthAndHeight;

        double distanceX = right ? distancePerMove : -distancePerMove;
        double distanceY = down ? distancePerMove : -distancePerMove;

        if (up && levelSidesPolygon.contains(leftX, topY + distanceY) && levelSidesPolygon.contains(rightX, topY + distanceY))
            setY(topY - distancePerMove);
        if (down && levelSidesPolygon.contains(leftX, bottomY + distanceY) && levelSidesPolygon.contains(rightX, bottomY + distanceY))
            setY(topY + distancePerMove);
        if (right && levelSidesPolygon.contains(rightX + distanceX, topY) && levelSidesPolygon.contains(rightX + distanceX, bottomY))
            setX(leftX + distancePerMove);
        if (left && levelSidesPolygon.contains(leftX + distanceX, topY) && levelSidesPolygon.contains(leftX + distanceX, bottomY))
            setX(leftX - distancePerMove);

        isWinner();
    }

    private void isWinner() {
        if (!winner && getX() > GamePane.goalX) {
            winner = true;
            System.out.println("You win!");
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
