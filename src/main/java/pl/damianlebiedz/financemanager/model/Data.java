package pl.damianlebiedz.financemanager.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor

public class Data {
    private final int id;
    private final String name;
    private final String category;
    private final float price;
    private final String date;
}
