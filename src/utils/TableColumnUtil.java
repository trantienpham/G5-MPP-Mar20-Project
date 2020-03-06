package utils;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class TableColumnUtil {
	public static <T> TableColumn<T, ?> getTableColumnByName(TableView<T> tableView, String name) {
        for (TableColumn<T, ?> col : tableView.getColumns()) {
            if (col.getText().equals(name)) {
                return col;
            }
        }
        return null;
    }
}
