import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

public class Test extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Pane pane = new Pane();

        double x = 100;
        double y = 100;

        double sLength = 50;
        double degreesInRadians = Math.toRadians(90);
        double x2 = x + sLength * Math.sin(degreesInRadians);
        double y2 = y - sLength * Math.cos(degreesInRadians);

        Line line = new Line(x, y, x2, y2);
        pane.getChildren().add(line);

        double degreesInRadians1 = Math.toRadians(180);
        double x3 = x + sLength * Math.sin(degreesInRadians1);
        double y3 = y - sLength * Math.cos(degreesInRadians1);

        Line line2 = new Line(x, y, x3, y3);
        pane.getChildren().add(line2);

        Circle circle = new Circle(100, 100, 5, Color.BLACK);
        pane.getChildren().add(circle);

        // Create a scene and place it in the stage
        Scene scene = new Scene(pane, 300, 300);
        primaryStage.setTitle("Exercise"); // Set the stage title
        primaryStage.setScene(scene); // Place the scene in the stage
        primaryStage.show(); // Display the stage

    }
}
