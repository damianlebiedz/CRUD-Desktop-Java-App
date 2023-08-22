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
import java.time.LocalTime;
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

    ObservableList<Data> dataList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String query = "SELECT * FROM DATA";
        executeQuery(query);
    }
    @FXML
    void addBtn(ActionEvent event) {

        String name = nameField.getText();
        String category = categoryField.getText();
        int price = Integer.parseInt(priceField.getText());
        String date = dateField.getValue().toString();

//        if(name.equals("") || category.equals("category") || price == 0 || date.equals(null)) {
//            //TODO
//        }

        String query = "INSERT INTO DATA VALUES ('"+name+"','"+category+"',"+price+",'"+date+"')";
        executeUpdate(query);

        executeQuery("SELECT * FROM DATA");
    }
    @FXML
    void deleteBtn(ActionEvent event) {
        String name = nameField.getText();
        String category = categoryField.getText();
        int price = Integer.parseInt(priceField.getText());
        String date = dateField.getValue().toString();

        String query = "DELETE FROM DATA WHERE NAME='"+name+"'; CATEGORY='"+category+"'; PRICE="+price+"; DATE='"+date+"';";
        executeUpdate(query);
        dataList.clear();
        executeQuery("SELECT * FROM DATA");
    }
    @FXML
    void updateBtn(ActionEvent event) {
        //TODO
    }
    @FXML
    void summaryBtn(ActionEvent event) {
        //TODO
    }
    private void fillTableWithData() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
//        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }
    @FXML
    private void category(ActionEvent event) {
        String category = ((MenuItem) event.getSource()).getText();
        switch(category) {
            case "food" -> categoryField.setText("food");
            case "chemistry" -> categoryField.setText("chemistry");
            case "other" -> categoryField.setText("other");
        }
    }
    private void executeUpdate(String query) {
        try {
            DBConnection DBConnection = new DBConnection();
            Connection connection = DBConnection.getConnection();

            PreparedStatement statement = connection.prepareStatement(query);
            int result = statement.executeUpdate();

            connection.close();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }
    private void executeQuery(String query) {
        try {
            DBConnection DBConnection = new DBConnection();
            Connection connection = DBConnection.getConnection();

            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet result = statement.executeQuery();

            while(result.next()) {
                dataList.add(new Data(
                        result.getString("name"),
                        result.getString("category"),
                        result.getInt("price"),
                        result.getString("date"))
                );
            }
            table.setItems(dataList);
            fillTableWithData();
            connection.close();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }
}
