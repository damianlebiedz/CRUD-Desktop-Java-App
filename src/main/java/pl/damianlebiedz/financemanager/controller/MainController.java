package pl.damianlebiedz.financemanager.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
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
    private TextField total;
    @FXML
    private TextField search;
    @FXML
    private TextField errorField;

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
    private TableColumn<Data, Float> priceColumn;
    @FXML
    private TableColumn<Data, Date> dateColumn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showData();
        dateField.setValue(LocalDate.now());
    }
    @FXML
    private void addBtn() {
        try {
            if(categoryField.getText().equals("Select a category")) {
                throw new NumberFormatException();
            }
            String query =
                    "INSERT INTO DATA VALUES (default,'" + nameField.getText() + "','" + categoryField.getText() +
                            "'," + Float.parseFloat(priceField.getText()) + ",'" + dateField.getValue().toString() +
                            "')";
            executeUpdate(query);
            showData();
            errorField.clear();
        }
        catch(NumberFormatException e) {
            errorField.setText("INCORRECT DATA");
        }
    }
    @FXML
    private void deleteBtn() {
        Data data = table.getSelectionModel().getSelectedItem();
        int id = data.getId();
        String query =
                "DELETE FROM DATA WHERE ID="+id+";";
        executeUpdate(query);
        showData();
    }
    @FXML
    private void updateBtn() {
        try {
            if(categoryField.getText().equals("Select a category") || priceField.getText().startsWith("0")) {
                throw new NumberFormatException();
            }
            Data data = table.getSelectionModel().getSelectedItem();
            int id = data.getId();
            String query =
                    "UPDATE  DATA SET NAME ='" + nameField.getText() + "', CATEGORY = '" + categoryField.getText() + "'," + " PRICE = " +
                            Float.parseFloat(priceField.getText()) + ", DATE = '" + dateField.getValue().toString() + "' " +
                            "WHERE id = " + id + "";
            executeUpdate(query);
            showData();
            errorField.clear();
        }
        catch(NumberFormatException e) {
            errorField.setText("INCORRECT DATA");
        }
    }
    @FXML
    private void onMouseClickedTable() {
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
            case "cosmetics" -> categoryField.setText("cosmetics");
            case "medicines" -> categoryField.setText("medicines");
            case "bills" -> categoryField.setText("bills");
            case "other" -> categoryField.setText("other");
        }
    }
    private ObservableList<Data> getDataList() {
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
                        result.getFloat("price"),
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
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        priceColumn.setCellFactory(price -> new TableCell<>() {
            @Override
            protected void updateItem(Float aFloat, boolean b) {
                super.updateItem(aFloat, b);
                if (aFloat == null || b) {
                    setText(null);
                } else {
                    setText(String.format("%.2f", aFloat));
                }
            }
        });

        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        searchData();
        dateColumn.setSortType(TableColumn.SortType.DESCENDING);
        table.getSortOrder().add(dateColumn);
        table.sort();
    }
    private void executeUpdate(String query) {
        try {
            DBConnection DBConnection = new DBConnection();
            Connection connection = DBConnection.getConnection();

            PreparedStatement statement = connection.prepareStatement(query);
            statement.executeUpdate();

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void searchData() {
        FilteredList<Data> filteredData = new FilteredList<>(getDataList(), b -> true);
        search.textProperty().addListener((observableValue, oldValue, newValue) -> {
            filteredData.setPredicate(Data -> {
                if(newValue.isEmpty() || newValue.isBlank()) {
                    return true;
                }
                String lowerCase = newValue.toLowerCase();

                if(Data.getName().toLowerCase().contains(lowerCase)) {
                    return true;
                }
                else if(Data.getCategory().toLowerCase().contains(lowerCase)) {
                    return true;
                }
                else if(String.valueOf(Data.getPrice()).toLowerCase().contains(lowerCase)) {
                    return true;
                }
                else return String.valueOf(Data.getDate()).toLowerCase().contains(lowerCase);
            });
            setTotal();
        });
        SortedList<Data> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedData);
        setTotal();
    }
    private void setTotal() {
        float totalAmount = 0;
        totalAmount = table.getItems().stream().map(
                Data::getPrice).reduce(totalAmount, Float::sum);
        total.setText("Total: "+String.format("%.2f", totalAmount));
    }
}