package sample.cafekiosk.spring.api.service.order;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import sample.cafekiosk.spring.api.controller.order.request.OrderCreateRequest;
import sample.cafekiosk.spring.domain.order.OrderRepository;
import sample.cafekiosk.spring.domain.order.OrderResponse;
import sample.cafekiosk.spring.domain.orderproduct.OrderProductRepository;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductType;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.*;
import static sample.cafekiosk.spring.domain.product.ProductType.HANDMADE;

@ActiveProfiles("test")
@SpringBootTest
class OrderServiceTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderProductRepository orderProductRepository;

    @Autowired
    private OrderService orderService;

    @AfterEach // 테스트가 끝날때 마다 실행해준다.
    void tearDown() {
        orderProductRepository.deleteAllInBatch();
        productRepository.deleteAllInBatch(); // delteAll과 차이점은 뭘까?: deleteAll은 엔티티를 하나씩 삭제하고, deleteAllInBatch는 한번에 삭제한다.
        orderRepository.deleteAllInBatch();

    }

    @DisplayName("상품번호 리스트를 받아서 주문을 생성한다.")
    @Test
    void createOrder() {
        // given
        LocalDateTime registeredDateTime = LocalDateTime.now();

        Product product1 = createProduct("001", HANDMADE, "아메리카노", 4000);
        Product product2 = createProduct("002", HANDMADE, "카페라떼", 4500);
        Product product3 = createProduct("003", HANDMADE, "팥빙수", 7000);
        productRepository.saveAll(List.of(product1, product2, product3));

        OrderCreateRequest request = OrderCreateRequest.builder()
                .productNumbers(List.of("001", "002"))
                .build();

        // when [행위만 수행]
        OrderResponse orderResponse = orderService.createOrder(request, registeredDateTime);

        // then
        assertThat(orderResponse.getId()).isNotNull();
        assertThat(orderResponse.getProducts()).hasSize(2)
                .extracting("productNumber", "name", "price")
                .containsExactlyInAnyOrder(
                        tuple("001", "아메리카노", 4000),
                        tuple("002", "카페라떼", 4500)
                );
        assertThat(orderResponse)
                .extracting("registeredDateTime", "totalPrice")
                .contains(registeredDateTime, 8500);
    }

    @DisplayName("중복되는 상품번호 리스트로 주문을 생성 할 수 있다.")
    @Test
    void creatOrderWithDuplicateProductNumbers() {
        // given
        LocalDateTime registeredDateTime = LocalDateTime.now();

        Product product1 = createProduct("001", HANDMADE, "아메리카노", 4000);
        Product product2 = createProduct("002", HANDMADE, "카페라떼", 4500);
        Product product3 = createProduct("003", HANDMADE, "팥빙수", 7000);
        productRepository.saveAll(List.of(product1, product2, product3));

        OrderCreateRequest request = OrderCreateRequest.builder()
                .productNumbers(List.of("001", "001"))
                .build();

        // when [행위만 수행]
        OrderResponse orderResponse = orderService.createOrder(request, registeredDateTime);

        // then
        assertThat(orderResponse.getId()).isNotNull();
        assertThat(orderResponse.getProducts()).hasSize(2)
                .extracting("productNumber", "name", "price")
                .containsExactlyInAnyOrder(
                        tuple("001", "아메리카노", 4000),
                        tuple("001", "아메리카노", 4000)
                );
        assertThat(orderResponse)
                .extracting("registeredDateTime", "totalPrice")
                .contains(registeredDateTime, 8000);
    }

    private Product createProduct( String productNumber, ProductType type, String productName, int price) {
        return Product.builder()
                .productNumber(productNumber)
                .type(type)
                .sellingStatus(SELLING)
                .name(productName)
                .price(price)
                .build();
    }
}