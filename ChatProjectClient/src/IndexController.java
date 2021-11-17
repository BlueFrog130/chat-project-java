import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import javafx.util.Pair;

public class IndexController implements Initializable {
	private Client client;

	@FXML
	private TextArea input;

	@FXML
	private ListView<String> users;

	@FXML
	private ListView<Response> chat;

	@FXML
	private Label name;

	public IndexController() {
		Dialog<Pair<String, Integer>> dialog = new Dialog<>();
		dialog.setTitle("Connect");
		ButtonType connectButtonType = new ButtonType("Connect", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(connectButtonType);

		VBox vbox = new VBox();
		vbox.setSpacing(10);

		TextField host = new TextField();
		host.setPromptText("Hostname");

		TextField port = new TextField();
		port.setPromptText("Port");
		port.textProperty().addListener((observable, o, n) -> {
			if (!n.matches("\\d*")) {
				port.setText(n.replaceAll("[^\\d]", ""));
			}
		});

		dialog.getDialogPane().lookupButton(connectButtonType).disableProperty()
				.bind(Bindings.isEmpty(host.textProperty()).or(Bindings.isEmpty(port.textProperty())));

		vbox.getChildren().addAll(host, port);

		dialog.getDialogPane().setContent(vbox);
		Platform.runLater(() -> host.requestFocus());

		dialog.setResultConverter(dialogButton -> {
			String text = port.getText();
			text = text.isEmpty() ? "-1" : text;
			return new Pair<String, Integer>(host.getText(), Integer.parseInt(text));
		});

		Optional<Pair<String, Integer>> result = dialog.showAndWait();

		Pair<String, Integer> hostPort = result.get();

		if (hostPort.getValue() == -1) {
			Platform.exit();
			System.exit(0);
		} else {
			client = new Client(hostPort.getKey(), hostPort.getValue());

			try {
				client.start();
			} catch (IOException ex) {
				Alert alert = new Alert(AlertType.ERROR, ex.getMessage(), ButtonType.CLOSE);
				alert.showAndWait();
				Platform.exit();
			}
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		input.setOnKeyPressed(e -> {
			if (e.getCode() == KeyCode.ENTER) {
				input.setText(input.getText().replaceAll("\n", ""));
				e.consume();
			}
		});
		input.setOnKeyReleased(e -> {
			if (e.getCode() == KeyCode.ENTER) {
				if (!input.getText().isBlank())
					client.send(input.getText());
				input.clear();
			}
		});

		name.textProperty().bind(client.userNameProperty());

		users.setItems(client.getUsersList());
		users.setFocusTraversable(false);
		users.setSelectionModel(new NoSelectionModel<String>());

		chat.setItems(client.getMessages());
		chat.setFocusTraversable(false);
		chat.setSelectionModel(new NoSelectionModel<Response>());
		chat.setCellFactory(new Callback<ListView<Response>, ListCell<Response>>() {
			@Override
			public ListCell<Response> call(ListView<Response> listView) {
				return new ResponseListCell();
			}
		});
	}
}
