package pl.damianlebiedz.financemanager.model;

import lombok.Getter;

@Getter

public class Data {
    private final int id;
    private final String name;
    private final String category;
    private final float price;
    private final String date;

    public Data(int id, String name, String category, float price, String date) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.date = date;
    }
}
