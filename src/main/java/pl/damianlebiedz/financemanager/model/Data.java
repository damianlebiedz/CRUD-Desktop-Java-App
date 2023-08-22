package pl.damianlebiedz.financemanager.model;

public class Data {
    private int id;
    private String name;
    private String category;
    private int price;
    private String date;

    public Data(int id, String name, String category, int price, String date) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.date = date;
    }
    public int getId() { return id; }

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
