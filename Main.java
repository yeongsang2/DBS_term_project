import view.MainView;

import java.io.IOException;
import java.sql.*;

public class Main {
    public static void main(String[] args) throws SQLException, ClassNotFoundException, IOException {

        MainView mainView = MainView.getMainView();
        mainView.showMainView();
    }
}
