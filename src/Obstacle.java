import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;
import javafx.util.Duration;

public class Obstacle extends Circle {
    private double startingX;
    private int direction;
    private double distancePerMove = 3;
    private double obstacleSpeed = 10 / GamePane.gameSpeed;
    private Player player;
    private AI[] aiArray;
    private AI2[] ai2Array;
    public AI2Winner[] ai2WinnerArray;
    private Timeline movementTimeline;

    public Obstacle(double centerX, double centerY, double radius, int direction, Player player, AI[] aiArray, AI2[] ai2Array, AI2Winner[] ai2WinnerArray) {
        super(centerX, centerY, radius);

        if (direction == 1)
            startingX = centerX;
        else
            startingX = centerX - 450;

        this.direction = direction;
        this.player = player;
        this.aiArray = aiArray;
        this.ai2Array = ai2Array;
        this.ai2WinnerArray = ai2WinnerArray;

        setFill(Color.BLUE);
        setStroke(Color.BLACK);
        setStrokeType(StrokeType.INSIDE);
        setStrokeWidth(4);

        createMovementTimeline();
    }

    private void createMovementTimeline() {
        movementTimeline = new Timeline(new KeyFrame(Duration.millis(obstacleSpeed), e -> moveHorizontally()));
        movementTimeline.setCycleCount(Timeline.INDEFINITE);
        movementTimeline.play();
    }

    public void stopMovementTimeline() {
        movementTimeline.stop();
    }

    private void moveHorizontally() {
        setCenterX(getCenterX() + (distancePerMove * direction));

        double x = getCenterX();
        if (x == startingX || x == startingX + 450)
            direction *= -1;

        if (player != null)
            checkPlayerDead();

        if (aiArray != null)
            checkAiDead();

        if (ai2Array != null)
            checkAi2Dead();

        if (ai2WinnerArray != null)
            checkAi2WinnerDead();
    }

    private void checkPlayerDead() {
        if (intersects(player.getLayoutBounds())) // No need for a "not dead check", since the player is moved to the starting position
            player.die();
    }

    private void checkAiDead() {
        for (AI ai : aiArray) {
            if (intersects(ai.getLayoutBounds())) // No need for a "not dead check", since the AI is moved to the starting position
                ai.die();
        }
    }

    private void checkAi2Dead() {
        for (AI2 ai2 : ai2Array) {
            if (intersects(ai2.getLayoutBounds()) && !ai2.dead) { // Must do a "not dead check", since the layout bounds of the AI don't disappear just because the AI is removed from the GamePane
                ai2.die();
            }

//            double circleX = getCenterX();
//            double circleY = getCenterY();
//            double rectangleX = ai2.getX();
//            double rectangleY = ai2.getY();
//
//            double deltaX = circleX - Math.max(rectangleX, Math.min(circleX, ai2.getWidth()));
//            double deltaY = circleY - Math.max(rectangleY, Math.min(circleY, ai2.getHeight()));
//            if ((deltaX * deltaX + deltaY * deltaY) < (getRadius() * getRadius())) {
//                ai2.die();
//            }
        }
    }

    private void checkAi2WinnerDead() {
        for (AI2Winner ai2Winner : ai2WinnerArray) {
            if (intersects(ai2Winner.getLayoutBounds()) && !ai2Winner.dead) // Must do a "not dead check", since the layout bounds of the AI don't disappear just because the AI is removed from the GamePane
                ai2Winner.die();
        }
    }
}
