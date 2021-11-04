import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;

public class App extends Application {
	public static void main(String[] args) throws Exception {
		// Client client = new Client("localhost", 8000);
		// client.start();
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Hello World!");
		GridPane root = new GridPane();
		ColumnConstraints c1 = new ColumnConstraints();
		c1.setPercentWidth(30);
		ColumnConstraints c2 = new ColumnConstraints();
		c1.setPercentWidth(70);
		RowConstraints r1 = new RowConstraints();
		r1.setPercentHeight(90);
		RowConstraints r2 = new RowConstraints();
		r2.setPercentHeight(10);
		root.getColumnConstraints().addAll(c1, c2);
		root.getRowConstraints().addAll(r1, r2);

		root.setPadding(new Insets(10));
		root.setHgap(5);
		root.setVgap(5);

		TextArea chat = new TextArea();
		chat.setDisable(true);
		TextArea input = new TextArea();
		input.setPromptText("Chat");
		Pane users = new Pane();

		chat.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
		// users.setBackground(new Background(new BackgroundFill(Color.RED,
		// CornerRadii.EMPTY, Insets.EMPTY)));
		// input.setBackground(new Background(new BackgroundFill(Color.RED,
		// CornerRadii.EMPTY, Insets.EMPTY)));

		root.add(chat, 0, 0, 1, 1);
		root.add(users, 1, 0, 1, 2);
		root.add(input, 0, 1, 1, 1);

		GridPane.setHgrow(users, Priority.ALWAYS);
		GridPane.setVgrow(input, Priority.ALWAYS);
		GridPane.setVgrow(chat, Priority.ALWAYS);

		primaryStage.setScene(new Scene(root, 500, 500, Color.BEIGE));
		primaryStage.show();

		input.requestFocus();
		chat.setWrapText(true);
		chat.setText("[User1]: Hello\n[User2]: :)");
		chat.setStyle("-fx-opacity: 1");
	}
}
