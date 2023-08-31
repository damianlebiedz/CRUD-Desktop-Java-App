module pl.damianlebiedz.financemanager {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires static lombok;

    opens pl.damianlebiedz.financemanager to javafx.fxml;
    exports pl.damianlebiedz.financemanager;
    exports pl.damianlebiedz.financemanager.controller;
    opens pl.damianlebiedz.financemanager.controller to javafx.fxml;
    exports pl.damianlebiedz.financemanager.model;
    opens pl.damianlebiedz.financemanager.model to javafx.fxml;
    exports pl.damianlebiedz.financemanager.db;
    opens pl.damianlebiedz.financemanager.db to javafx.fxml;
}