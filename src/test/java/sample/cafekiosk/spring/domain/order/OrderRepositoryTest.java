package sample.cafekiosk.spring.domain.order;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import sample.cafekiosk.spring.IntegrationTestSupport;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class OrderRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderRepository orderRepository;

//    @Disabled 테스트를 ignore 하는방법!
    @DisplayName("시작날짜와 끝날짜, '주문생성' 상태로 주문을 조회한다.")
    @Test
    void findOrdersBy1() {
        // given
        List<Product> products = productRepository.findAllByProductNumberIn(List.of("001", "002"));
        LocalDateTime now = LocalDateTime.of(2023, 8, 20, 9, 0, 0, 0);

        Order order1 = Order.create(products, LocalDateTime.of(2023, 8, 19, 0, 0));
        Order order2 = Order.create(products, now);
        Order order3 = Order.create(products, LocalDateTime.of(2023, 8, 21, 11, 0));
        orderRepository.saveAll(List.of(order1, order2, order3));

        // when
        LocalDate today = LocalDate.of(2023, 8, 20);
        List<Order> orders = orderRepository.findOrdersBy(today.atStartOfDay(), today.plusDays(1).atStartOfDay(), OrderStatus.INIT);

        // then
        assertThat(orders).hasSize(1);
        assertThat(orders)
                .extracting("orderStatus", "registeredDateTime")
                .containsExactlyInAnyOrder(
                        Assertions.tuple(OrderStatus.INIT, now)
                );
    }

    @DisplayName("시작날짜와 끝날짜, '결제완료' 상태로 주문을 조회한다.")
    @Test
    void findOrdersBy2() {
        // given
        List<Product> products = productRepository.findAllByProductNumberIn(List.of("001", "002"));
        LocalDateTime now = LocalDateTime.now();

        Order order1 = Order.builder()
                .orderStatus(OrderStatus.PAYMENT_COMPLETED)
                .products(products)
                .registeredDateTime(LocalDateTime.of(2023, 8, 19, 0, 0))
                .build();
        Order order2 = Order.builder()
                .orderStatus(OrderStatus.PAYMENT_COMPLETED)
                .products(products)
                .registeredDateTime(now)
                .build();
        Order order3 = Order.builder()
                .orderStatus(OrderStatus.PAYMENT_COMPLETED)
                .products(products)
                .registeredDateTime(LocalDateTime.of(2023, 8, 21, 11, 0))
                .build();
        orderRepository.saveAll(List.of(order1, order2, order3));

        // when
        LocalDate today = LocalDate.now();
        List<Order> orders = orderRepository.findOrdersBy(today.atStartOfDay(), today.plusDays(1).atStartOfDay(), OrderStatus.PAYMENT_COMPLETED);

        // then
        assertThat(orders).hasSize(1);
        assertThat(orders)
                .extracting("orderStatus", "registeredDateTime")
                .containsExactlyInAnyOrder(
                        Assertions.tuple(OrderStatus.PAYMENT_COMPLETED, now)
                );
    }
}