import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.MultipleSelectionModel;

public class NoSelectionModel<T> extends MultipleSelectionModel<T> {

	@Override
	public ObservableList<Integer> getSelectedIndices() {
		return FXCollections.emptyObservableList();
	}

	@Override
	public ObservableList<T> getSelectedItems() {
		return FXCollections.emptyObservableList();
	}

	@Override
	public void selectIndices(int index, int... indices) {
		// TODO Auto-generated method stub

	}

	@Override
	public void selectAll() {
		// TODO Auto-generated method stub

	}

	@Override
	public void selectFirst() {
		// TODO Auto-generated method stub

	}

	@Override
	public void selectLast() {
		// TODO Auto-generated method stub

	}

	@Override
	public void clearAndSelect(int index) {
		// TODO Auto-generated method stub

	}

	@Override
	public void select(int index) {
		// TODO Auto-generated method stub

	}

	@Override
	public void select(T obj) {
		// TODO Auto-generated method stub

	}

	@Override
	public void clearSelection(int index) {
		// TODO Auto-generated method stub

	}

	@Override
	public void clearSelection() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isSelected(int index) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void selectPrevious() {
		// TODO Auto-generated method stub

	}

	@Override
	public void selectNext() {
		// TODO Auto-generated method stub

	}

}
