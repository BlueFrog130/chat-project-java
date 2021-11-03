import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class App extends Application {
	public static void main(String[] args) throws Exception {
		Client client = new Client("localhost", 8000);
		client.start();
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Hello World!");
		StackPane root = new StackPane();
		primaryStage.setScene(new Scene(root, 300, 300));
		primaryStage.show();
	}
}
