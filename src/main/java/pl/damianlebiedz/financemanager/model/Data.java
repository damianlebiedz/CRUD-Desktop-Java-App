package pl.damianlebiedz.financemanager.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.sql.Date;

@Getter
@AllArgsConstructor

public class Data {
    private final int id;
    private final String name;
    private final String category;
    private final BigDecimal price;
    private final Date date;
}
