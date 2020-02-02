import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class TestAnimationTimer2 extends Application {
    private Circle circle = new Circle(50, 100, 50, Color.PALEGOLDENROD);
    private long lastUpdateInNanoseconds = 0;
    private int counter = 0;
    private long startTimeMillis;
    private long startTimeNanos;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Pane pane = new Pane(circle);
        Scene scene = new Scene(pane, 1000, 200);
        primaryStage.setScene(scene);
        primaryStage.show();

        AnimationTimer animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (now - lastUpdateInNanoseconds >= 10000000) { // I have no clue why 10000000 ns (0.01 s) works, but it seems to guarantee 60 FPS - I thought it was supposed to be 16666666.667 ns (0.016666666667 s = 1 / 60 s = 60 fps)
                    moveCircle();
                    lastUpdateInNanoseconds = now;

                    if (counter == 0) {
                        startTimeMillis = System.currentTimeMillis();
                        startTimeNanos = now;
                    }

                    ++counter;

                    if (counter == 60) {
                        System.out.println((System.currentTimeMillis() - startTimeMillis) / 1000.0);
                        System.out.println((now - startTimeNanos) / 1000000000.0);
                        counter = 0;
                    }
                }
            }
        };

        animationTimer.start();

//        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(1000), event -> System.out.println(System.currentTimeMillis())));
//        timeline.setCycleCount(Timeline.INDEFINITE);
//        timeline.play();
    }

    private void moveCircle() {
        circle.setCenterX(circle.getCenterX() + 5);

        if (circle.getCenterX() >= 950)
            circle.setCenterX(50);
    }
}
