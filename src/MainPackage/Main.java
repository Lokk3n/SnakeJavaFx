package MainPackage;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        primaryStage.setTitle("Snake");
        new PaneManager();
        new ScoreManager();
        FileManager.initialize(primaryStage);
        Controller controller = new Controller();
        View view = new View(controller, primaryStage);
        Scene scene = new Scene(view.getMainPane());
        primaryStage.setScene(scene);
        primaryStage.show();
//        primaryStage.focusedProperty().addListener((observable, oldValue, newValue) -> {
//            if (!newValue) {
//                System.out.println("lost focus");
//                primaryStage.requestFocus();
//            }
//        });



        primaryStage.setOnCloseRequest(windowEvent -> {
            if(controller.t != null && controller.t.isAlive()) controller.life.abort();
        });
    }


    public static void main(String[] args) {
        launch(args);
    }
}
