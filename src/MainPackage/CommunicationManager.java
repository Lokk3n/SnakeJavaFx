package MainPackage;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class CommunicationManager {

    private static Stage mainStage;

    public static void initialize(Stage owner){
        mainStage = owner;
    }

    public static void displayGameOver(){
        BorderPane container = new BorderPane();
        Text label = new Text("Game Over - " + ScoreManager.instance.getScore() + " points");
        label.setFont(Font.font ("Verdana", FontWeight.BOLD, 30));
        label.setTextAlignment(TextAlignment.CENTER);
        label.setStyle(
                "-fx-background-color: lightgreen;" +
                        "-fx-border-color: black;" +
                        "-fx-border-width: 1"
        );
        container.setPrefSize(400, 100);
        container.setStyle(
                "-fx-background-color: lightgreen;" +
                        "-fx-border-color: black;" +
                        "-fx-border-width: 1"
        );
        container.setCenter(label);
        Scene scene = new Scene(container);


        Stage dialog = new Stage();
        dialog.setScene(scene);

        dialog.initOwner(mainStage);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.showAndWait();
    }

    public static void displayError(String message){
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.showAndWait();

//        if (alert.getResult() == ButtonType.OK) {
//            //do stuff
//        }
    }
}
