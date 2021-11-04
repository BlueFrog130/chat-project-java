import java.io.IOException;
import java.util.Optional;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

public class IndexController {
	private Client client;

	@FXML
	private TextArea input;

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
			return new Pair<String, Integer>(host.getText(), Integer.parseInt(port.getText()));
		});

		Optional<Pair<String, Integer>> result = dialog.showAndWait();

		Pair<String, Integer> hostPort = result.get();

		client = new Client(hostPort.getKey(), hostPort.getValue());

		try {
			client.start();
		} catch (IOException ex) {
			Alert alert = new Alert(AlertType.ERROR, ex.getMessage(), ButtonType.CLOSE);
			alert.showAndWait();
			Platform.exit();
		}
	}

	public void initialize() {
		input.setOnKeyPressed(e -> {
			if (e.getCode() == KeyCode.ENTER) {
				input.clear();
				e.consume();
			}
		});
	}
}
