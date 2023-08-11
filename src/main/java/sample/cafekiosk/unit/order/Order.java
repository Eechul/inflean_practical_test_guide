package sample.cafekiosk.unit.order;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import sample.cafekiosk.unit.beverage.Beverage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Setter
@RequiredArgsConstructor
public class Order {

    private final LocalDateTime orderDateTime;
    private final List<Beverage> beverages;

}
