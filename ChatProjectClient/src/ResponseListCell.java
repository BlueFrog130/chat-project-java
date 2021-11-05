import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.GridPane;
import javafx.scene.text.TextAlignment;

public class ResponseListCell extends ListCell<Response> {
	@FXML
	private Label message;

	@FXML
	private Label user;

	@FXML
	private GridPane container;

	FXMLLoader loader;

	@Override
	protected void updateItem(Response response, boolean empty) {
		super.updateItem(response, empty);
		if (empty || response == null) {
			setText(null);
			setGraphic(null);
		} else {
			if (loader == null) {
				loader = new FXMLLoader(getClass().getResource("fxml/responseListCell.fxml"));
				loader.setController(this);
				try {
					loader.load();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
				container.setPrefWidth(1);
			}
			GridPane.setFillWidth(message, true);
			GridPane.setFillHeight(user, true);

			message.setMaxWidth(Double.MAX_VALUE);
			user.setMaxWidth(Double.MAX_VALUE);

			user.setText(response.getUser());
			message.setText(response.getData());

			message.setWrapText(true);
			if (response.getUser().equals("server")) {
				container.getChildren().remove(user);
				message.setAlignment(Pos.CENTER);
				message.setOpacity(0.8);
			} else if (response.isSelf()) {
				container.getChildren().remove(user);
				message.setAlignment(Pos.CENTER_RIGHT);
				message.setTextAlignment(TextAlignment.RIGHT);
			}
			setText(null);
			setGraphic(container);
		}
	}
}
