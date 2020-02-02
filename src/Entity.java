import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

import java.io.Serializable;

public class Entity extends Rectangle implements Serializable {
    protected double distancePerMove = 3;
    protected double movementSpeed = 25 / GamePane.gameSpeed; // How often the entity moves
    protected double directionSpeed = 100 / GamePane.gameSpeed; // For long the entity moves in one direction
    protected double entityWidthAndHeight = 25;
    protected GamePane gamePane;
    protected Polygon levelSidesPolygon;
    protected boolean dead, winner;

    public Entity(double x, double y, GamePane gamePane, Polygon levelSidesPolygon) {
        setX(x);
        setY(y);
        setWidth(entityWidthAndHeight);
        setHeight(entityWidthAndHeight);

        this.gamePane = gamePane;
        this.levelSidesPolygon = levelSidesPolygon;

        setStroke(Color.BLACK);
        setStrokeType(StrokeType.INSIDE);
        setStrokeWidth(5);
    }
}
