package pl.damianlebiedz.financemanager.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import pl.damianlebiedz.financemanager.db.DBConnection;
import pl.damianlebiedz.financemanager.model.Data;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private TextField balance;
    @FXML
    private TextField currentBudget;
    @FXML
    private TextField balancePercent;
    @FXML
    private TextField budget;
    @FXML
    private DatePicker dateFromBtn;
    @FXML
    private DatePicker dateToBtn;
    @FXML
    private TextField search;

    @FXML
    private TextField nameField;
    @FXML
    private MenuButton categoryField;
    @FXML
    private TextField priceField;
    @FXML
    private DatePicker dateField;

    @FXML
    private TableView<Data> table;
    @FXML
    private TableColumn<Data, String> nameColumn;
    @FXML
    private TableColumn<Data, String> categoryColumn;
    @FXML
    private TableColumn<Data, Integer> priceColumn;
    @FXML
    private TableColumn<Data, Date> dateColumn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showData();
        dateField.setValue(LocalDate.now());
    }

    public ObservableList<Data> getDataList() {
        ObservableList<Data> dataList = FXCollections.observableArrayList();

        DBConnection DBConnection = new DBConnection();
        Connection connection = DBConnection.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM DATA");
            ResultSet result = statement.executeQuery();

            while (result.next()) {
                dataList.add(new Data(
                        result.getInt("id"),
                        result.getString("name"),
                        result.getString("category"),
                        result.getInt("price"),
                        result.getString("date"))
                );
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dataList;
    }
    private void showData() {
        ObservableList<Data> dataList = getDataList();

        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
//        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        table.setItems(dataList);
    }
    private void executeUpdate(String query) {
        try {
            DBConnection DBConnection = new DBConnection();
            Connection connection = DBConnection.getConnection();

            PreparedStatement statement = connection.prepareStatement(query);
            int result = statement.executeUpdate();

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void addBtn(ActionEvent event) {
        String query =
                "INSERT INTO DATA VALUES (default,'" + nameField.getText() + "','" + categoryField.getText() + "'," + Integer.parseInt(priceField.getText()) + ",'" + dateField.getValue().toString() + "')";
        executeUpdate(query);
        showData();
    }
    @FXML
    void deleteBtn(ActionEvent event) {
        Data data = table.getSelectionModel().getSelectedItem();
        int id = data.getId();
        String query =
                "DELETE FROM DATA WHERE ID="+id+";";
        executeUpdate(query);
        showData();
    }
    @FXML
    void updateBtn(ActionEvent event) {
        Data data = table.getSelectionModel().getSelectedItem();
        int id = data.getId();
        String query =
                "UPDATE  DATA SET NAME ='" + nameField.getText() + "', CATEGORY = '" + categoryField.getText() + "'," + " PRICE = " +
                Integer.parseInt(priceField.getText()) + ", DATE = '" + dateField.getValue().toString() + "' WHERE id = " + id + "";
        executeUpdate(query);
        showData();
    }
    @FXML
    void summaryBtn(ActionEvent event) {
        //TODO
    }
    @FXML
    void onMouseClickedTable(javafx.scene.input.MouseEvent mouseEvent) {
        try {
            Data data = table.getSelectionModel().getSelectedItem();
            nameField.setText(data.getName());
            categoryField.setText(data.getCategory());
            priceField.setText(String.valueOf(data.getPrice()));
            dateField.setValue(LocalDate.parse(data.getDate()));
        }
        catch (NullPointerException ignored) {
        }
    }
    @FXML
    private void category(ActionEvent event) {
        String category = ((MenuItem) event.getSource()).getText();
        switch (category) {
            case "food" -> categoryField.setText("food");
            case "chemistry" -> categoryField.setText("chemistry");
            case "other" -> categoryField.setText("other");
        }
    }
}
