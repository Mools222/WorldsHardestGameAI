import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GameView extends Application {
    public static Label labelGameStatus, labelNumberOfWinners, labelGenerationNumber, labelNumberOfMovesNumber, labelAi2sAliveNumber, labelAi2sAlivePreviousGenNumber;
    public static Button buttonStart, buttonReset;
    public static CheckBox checkBoxPlayer, checkBoxSimpleAi, checkBoxLearningAi, checkBoxWinnerAis;
    public static VBox vBoxWinnerAiTimes;

    @Override
    public void start(Stage primaryStage) throws Exception {
        GamePane gamePane = new GamePane();
        gamePane.getStyleClass().add("game-pane");

        vBoxWinnerAiTimes = new VBox(10, new Label("Winning AI times:"));
        vBoxWinnerAiTimes.setId("vBoxWinnerAiTimes");

        HBox hBoxGameAndTimes = new HBox(gamePane, vBoxWinnerAiTimes);

        buttonStart = new Button("Start");
        buttonReset = new Button("Reset");

        checkBoxPlayer = new CheckBox("Spawn player");
        checkBoxPlayer.setFocusTraversable(false);
        checkBoxSimpleAi = new CheckBox("Spawn simple AI");
        checkBoxSimpleAi.setFocusTraversable(false);
        checkBoxLearningAi = new CheckBox("Spawn learning AI");
        checkBoxLearningAi.setFocusTraversable(false);
        checkBoxWinnerAis = new CheckBox("Spawn winner AI(s)");
        checkBoxWinnerAis.setFocusTraversable(false);

        HBox hBoxControls = new HBox(10, buttonStart, buttonReset, checkBoxPlayer, checkBoxSimpleAi, checkBoxLearningAi, checkBoxWinnerAis);
        hBoxControls.setAlignment(Pos.CENTER);

        labelGameStatus = new Label("Awaiting instructions.");
        labelNumberOfWinners = new Label();
        labelGenerationNumber = new Label("N/A");
        labelNumberOfMovesNumber = new Label("N/A");
        labelAi2sAliveNumber = new Label("N/A");
        labelAi2sAlivePreviousGenNumber = new Label("N/A");

        GridPane gridPane = new GridPane();
        gridPane.addRow(0, new Label("Game info:"));
        gridPane.addRow(1, new Label("Game status:"), labelGameStatus);
        gridPane.addRow(2, new Label("Winners on file:"), labelNumberOfWinners);

        gridPane.addRow(3, new Label(""));

        gridPane.addRow(4, new Label("Learning AI info:"));
        gridPane.addRow(5, new Label("Generation: "), labelGenerationNumber);
        gridPane.addRow(6, new Label("Number of moves: "), labelNumberOfMovesNumber);
        gridPane.addRow(7, new Label("Alive AIs: "), labelAi2sAliveNumber);
        gridPane.addRow(8, new Label("Alive AIs previous gen: "), labelAi2sAlivePreviousGenNumber);

        // Suggest spawning the learning AI by default
        checkBoxLearningAi.setSelected(true);
        gamePane.createAi2 = true;

        // Count number of winners
        labelNumberOfWinners.setText(String.valueOf(gamePane.openWinnerList().size()));

        VBox vBox = new VBox(20, hBoxGameAndTimes, hBoxControls, gridPane);
        vBox.setId("vbox");
        vBox.setPrefWidth(1300);

        // Create a scene and place it in the stage
        Scene scene = new Scene(vBox);
        scene.getStylesheets().add("style.css");
        primaryStage.setTitle("World's Hardest Game"); // Set the stage title
        primaryStage.setScene(scene); // Place the scene in the stage
        primaryStage.show(); // Display the stage

        buttonStart.requestFocus();

        buttonStart.setOnAction(event -> {
            buttonStart.setDisable(true);
            checkBoxPlayer.setDisable(true);
            checkBoxSimpleAi.setDisable(true);
            checkBoxLearningAi.setDisable(true);
            checkBoxWinnerAis.setDisable(true);

            gamePane.start();
            buttonReset.requestFocus();
        });

        buttonReset.setOnAction(event -> {
            gamePane.reset();
            buttonStart.requestFocus();
        });

        checkBoxPlayer.setOnAction(event -> {
            gamePane.createPlayer = !gamePane.createPlayer;

            gamePane.createAi2 = false;
            checkBoxLearningAi.setSelected(false);

            gamePane.createAiWinner = false;
            checkBoxWinnerAis.setSelected(false);
        });

        checkBoxSimpleAi.setOnAction(event -> {
            gamePane.createAi = !gamePane.createAi;

            gamePane.createAi2 = false;
            checkBoxLearningAi.setSelected(false);

            gamePane.createAiWinner = false;
            checkBoxWinnerAis.setSelected(false);
        });

        checkBoxLearningAi.setOnAction(event -> {
            gamePane.createAi2 = !gamePane.createAi2;

            gamePane.createPlayer = false;
            checkBoxPlayer.setSelected(false);

            gamePane.createAi = false;
            checkBoxSimpleAi.setSelected(false);

            gamePane.createAiWinner = false;
            checkBoxWinnerAis.setSelected(false);
        });

        checkBoxWinnerAis.setOnAction(event -> {
            gamePane.createAiWinner = !gamePane.createAiWinner;

            gamePane.createPlayer = false;
            checkBoxPlayer.setSelected(false);

            gamePane.createAi = false;
            checkBoxSimpleAi.setSelected(false);

            gamePane.createAi2 = false;
            checkBoxLearningAi.setSelected(false);
        });
    }
}
