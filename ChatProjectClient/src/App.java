import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class App extends Application {
	public static void main(String[] args) throws Exception {
		// Client client = new Client("localhost", 8000);
		// client.start();
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Chat");
		Parent root = FXMLLoader.load(getClass().getResource("fxml/index.fxml"));
		Scene scene = new Scene(root, 600, 400, Color.BEIGE);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}
