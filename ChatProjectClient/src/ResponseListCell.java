import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;

public class ResponseListCell extends ListCell<Response> {
	@FXML
	private Label message;

	@FXML
	private Label user;

	@Override
	protected void updateItem(Response response, boolean empty) {
		super.updateItem(response, empty);
	}
}
