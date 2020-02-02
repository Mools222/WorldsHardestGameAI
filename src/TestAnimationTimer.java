import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class TestAnimationTimer extends Application {
    Circle circle = new Circle(50, 100, 50, Color.PALEGOLDENROD);
    private int counter = 0;
    private long startTimeMillis;

    @Override
    public void start(Stage primaryStage) throws Exception {

        Pane pane = new Pane(circle);

        Scene scene = new Scene(pane, 1000, 200);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Does JavaFx manipulation - Runs at the screen's hz (try moving the window between the 2 screens and watch it change in hz)
//        animationTimerWithJavaFxManipulation();

        // No JavaFx manipulation - Runs at around 60 hz on either screen
        animationTimerWithoutJavaFxManipulation();
    }

    public void animationTimerWithJavaFxManipulation() {
        AnimationTimer animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                circle.setCenterX(circle.getCenterX() + 5);

                if (circle.getCenterX() >= 950)
                    circle.setCenterX(50);

                if (counter == 0)
                    startTimeMillis = System.currentTimeMillis();

                ++counter;

                if (counter == 60) {
                    System.out.println(System.currentTimeMillis() - startTimeMillis);
                    counter = 0;
                }
            }
        };

        animationTimer.start();
    }

    public void animationTimerWithoutJavaFxManipulation() {
        AnimationTimer animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
//                circle.setCenterX(circle.getCenterX() + 5);

//                if (circle.getCenterX() >= 950)
//                    circle.setCenterX(50);

                if (counter == 0)
                    startTimeMillis = System.currentTimeMillis();

                ++counter;

                if (counter == 60) {
                    System.out.println(System.currentTimeMillis() - startTimeMillis);
                    counter = 0;
                }
            }
        };

        animationTimer.start();
    }
}
