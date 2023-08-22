package pl.damianlebiedz.financemanager.model;

public class Data {
    private String name;
    private String category;
    private int price;
    private String date;

    public Data(String name, String category, int price, String date) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public int getPrice() {
        return price;
    }

    public String getDate() {
        return date;
    }
}
