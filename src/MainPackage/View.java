package MainPackage;

import Interfaces.Observer;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.shape.Rectangle;
import javafx.util.converter.NumberStringConverter;


public class View implements Observer<Controller> {

    public BorderPane getMainPane() {
        return mainPane;
    }

    Stage primaryStage;

    Controller controller;

    BorderPane mainPane;

    HBox buttonBar;
    Button newGame;
    Button loadGame;
    Button saveGame;

    BorderPane gameScreen;
    HBox scoreBar;
    Label scoreLabel;
    TextField score;
    Pane snakeGame;

    GridPane configScreen;
    Label heightLabel;
    TextField heightField;
    Label widthLabel;
    TextField widthField;
    Label speedMultiplierLabel;
    TextField speedMultiplierField;
    Button startButton;


    BooleanProperty startUnavailable;
    boolean heightCorrect;
    boolean widthCorrect;
    boolean speedCorrect;

    public View(Controller controller, Stage primaryStage) {
        this.controller = controller;
        controller.addObserver(this);
        this.primaryStage = primaryStage;

        mainPane = new BorderPane();

        //-------------------------------------------
        buttonBar = new HBox();

        newGame = new Button("New game"); //przenosi do konfiguratora
        loadGame = new Button("Load game"); //otwiera okno wyboru pliku
        saveGame = new Button("Save game"); //otwiera okno zapisu pliku

        saveGame.setDisable(true);

        buttonBar.getChildren().addAll(newGame, loadGame, saveGame);

        //-------------------------------------------
        gameScreen = new BorderPane();

        scoreBar = new HBox();
        scoreLabel = new Label("Snake length:");
        score = new TextField();
        score.setEditable(false);
        score.setFocusTraversable(false);
        score.setDisable(true);
        score.setStyle("-fx-opacity: 1;");
        snakeGame = new Pane();
        //snakeGame.setMinSize(300, 300);
        snakeGame.setPrefSize(800, 800);
        //snakeGame.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        snakeGame.setStyle(
                "-fx-background-color: lightgreen;" +
                "-fx-border-color: black;" +
                "-fx-border-width: 1");

        //snakeGame.setBorder(Border.EMPTY);
        clipChildren(snakeGame);
        PaneManager.getInstance().setDisplayedTerritory(snakeGame);

        scoreBar.getChildren().addAll(scoreLabel, score);
        scoreBar.setSpacing(5);
        scoreBar.setFocusTraversable(false);
        //gameScreen.getChildren().addAll(scoreBar, snakeGame);
        gameScreen.setTop(scoreBar);
        gameScreen.setCenter(snakeGame);
        gameScreen.setFocusTraversable(false);

        score.textProperty().bindBidirectional(ScoreManager.getInstance().scoreProperty(), new NumberStringConverter());

        //-------------------------------------------
        configScreen = new GridPane();

        heightLabel = new Label("Height:");
        heightField = new TextField();
        heightField.setText("20");
        widthLabel = new Label("Width:");
        widthField = new TextField();
        widthField.setText("20");
        speedMultiplierLabel = new Label("Speed multiplier:");
        speedMultiplierField = new TextField();
        speedMultiplierField.setText("2");
        startButton = new Button("Start game");
        startUnavailable = new SimpleBooleanProperty();
        startButton.disableProperty().bind(startUnavailable);

        configScreen.addRow(0, heightLabel, heightField);
        configScreen.addRow(1, widthLabel, widthField);
        configScreen.addRow(2, speedMultiplierLabel, speedMultiplierField);
        configScreen.addRow(3, startButton);

        //--------------------------------------------
        //vertical.getChildren().addAll(buttonBar);
        mainPane.setTop(buttonBar);


        newGame.setOnAction(actionEvent -> {
            //gameScreen.setVisible(false);
            //configScreen.setVisible(true);
            if(controller.t != null && controller.t.isAlive()) controller.life.abort();
            mainPane.setCenter(configScreen);
            primaryStage.sizeToScene();
            //saveGame.setDisable(true);
        });

        startButton.setOnAction(actionEvent -> {

            Configuration.mapHeight = Integer.parseInt(heightField.getText());
            Configuration.mapWidth = Integer.parseInt(widthField.getText());
            Configuration.speedMultiplier = Integer.parseInt(speedMultiplierField.getText());
            PaneManager.getInstance().clearPane();
            controller.startNewGame();
            controller.getTerritory().getSnakeTail().addObserver(ScoreManager.getInstance());

            mainPane.setCenter(gameScreen);
            fitGameScreen();
            primaryStage.sizeToScene();
            //saveGame.setDisable(false);

        });

        mainPane.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.A) {
                System.out.println("LEFT pressed");
                controller.goLeft();
            }
            else if (event.getCode() == KeyCode.D) {
                System.out.println("RIGHT pressed");
                controller.goRight();
            }
            else if (event.getCode() == KeyCode.W) {
                System.out.println("UP pressed");
                controller.goUp();
            }
            else if (event.getCode() == KeyCode.S) {
                System.out.println("DOWN pressed");
                controller.goDown();
            }
            else if (event.getCode() == KeyCode.P){
                System.out.println("P pressed");
                controller.life.start(!(controller.life.isStart()));
            }
            else if (event.getCode() == KeyCode.X){
                System.out.println("X pressed");
                controller.life.abort();
            }
        });

        //mainPane.setGridLinesVisible(true);
        mainPane.setStyle(
                "-fx-background-color: lightgray;"
        );

        saveGame.setOnAction(actionEvent -> {
            FileManager.writeFile();
        });

        loadGame.setOnAction(actionEvent -> {
            if(FileManager.readFile()) {
                mainPane.setCenter(gameScreen);
                fitGameScreen();
                primaryStage.sizeToScene();
                //saveGame.setDisable(false);
            }
        });

        ChangeListener<String> changeListener = new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                int value1 = 0;
                int value2= 0;
                int value3 = 0;
                boolean correct = true;
                try {
                    value1 = Integer.parseInt(heightField.getText());
                    value2 = Integer.parseInt(widthField.getText());
                    value3 = Integer.parseInt(speedMultiplierField.getText());
                } catch (NumberFormatException e) {
                    correct = false;
                }
                if (value1 < 15) correct = false;
                if (value2 < 15) correct = false;
                if (value3 < 1) correct = false;
                startUnavailable.set(!correct);
            }
        };

        heightField.textProperty().addListener((observableValue, s, t1) -> {
            int value = 0;
            boolean correct = true;
            try {
                value = Integer.parseInt(t1);
            } catch (NumberFormatException e) {
                correct = false;
            }
            if (value < 15) correct = false;
            if (!correct) {
                heightField.setStyle("-fx-text-inner-color: red;");
            } else {
                heightField.setStyle("-fx-text-inner-color: black;");
            }
        });

        widthField.textProperty().addListener((observableValue, s, t1) -> {
            int value = 0;
            boolean correct = true;
            try {
                value = Integer.parseInt(t1);
            } catch (NumberFormatException e) {
                correct = false;
            }
            if (value < 15) correct = false;
            if (!correct) {
                widthField.setStyle("-fx-text-inner-color: red;");
            } else {
                widthField.setStyle("-fx-text-inner-color: black;");
            }
        });

        speedMultiplierField.textProperty().addListener((observableValue, s, t1) -> {
            int value = 0;
            boolean correct = true;
            try {
                value = Integer.parseInt(t1);
            } catch (NumberFormatException e) {
                correct = false;
            }
            if (value < 1) correct = false;
            if (!correct) {
                speedMultiplierField.setStyle("-fx-text-inner-color: red;");
            } else {
                speedMultiplierField.setStyle("-fx-text-inner-color: black;");
            }
        });

        heightField.textProperty().addListener(changeListener);
        widthField.textProperty().addListener(changeListener);
        speedMultiplierField.textProperty().addListener(changeListener);
    }

    public void clipChildren(Region region) {
        final Rectangle clipPane = new Rectangle();
        region.setClip(clipPane);

        region.layoutBoundsProperty().addListener((ov, oldValue, newValue) -> {
            clipPane.setWidth(newValue.getWidth());
            clipPane.setHeight(newValue.getHeight());
        });
    }

    public void fitGameScreen() {
        int height = controller.territory.getHeight();
        int width = controller.territory.getWidth();
        snakeGame.setPrefHeight(50 * height);
        snakeGame.setPrefWidth(50 * width);
    }

    @Override
    public void update(Controller data) {
        saveGame.setDisable(!data.isRunning());
    }
}
