package sample.cafekiosk.spring.api.controller.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import sample.cafekiosk.spring.ControllerTestSupport;
import sample.cafekiosk.spring.api.controller.order.request.OrderCreateRequest;
import sample.cafekiosk.spring.api.service.order.OrderService;
import sample.cafekiosk.spring.domain.order.Order;
import sample.cafekiosk.spring.api.service.order.response.OrderResponse;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;
import sample.cafekiosk.spring.domain.product.ProductType;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class OrderControllerTest extends ControllerTestSupport {

    @DisplayName("주문을 생성한다.")
    @Test
    void createOrder() throws Exception {
        // given
        OrderCreateRequest request = OrderCreateRequest.builder()
                .productNumbers(List.of("001"))
                .build();

        LocalDateTime now = LocalDateTime.now();
        Product product001 = createProduct("001", ProductType.HANDMADE, ProductSellingStatus.SELLING, "아메리카노", 40000);

        Order order = Order.create(List.of(product001), now);
        OrderResponse orderResponse = OrderResponse.of(order);

//        when(orderService.createOrder(request, now)).thenReturn(OrderResponse.of());

        // when // then
        mockMvc.perform(post("/api/v1/orders/new")
                .content(objectMapper.writeValueAsString(request))
                .contentType(APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"));

    }

    @DisplayName("주문을 생성할때, 상품번호는 1개 이상이여야 한다.")
    @Test
    void createOrderWithEmptyProductNumber() throws Exception {
        // given
        OrderCreateRequest request = OrderCreateRequest.builder()
                .productNumbers(List.of()) // 혹은 null
                .build();

        // when // then
        mockMvc.perform(post("/api/v1/orders/new")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("상품번호는 리스트는 필수입니다."))
                .andExpect(jsonPath("$.data").isEmpty());

    }

    private Product createProduct(String productNumber, ProductType type, ProductSellingStatus sellingStatus, String productName, int price) {
        return Product.builder()
                .productNumber(productNumber)
                .type(type)
                .sellingStatus(sellingStatus)
                .name(productName)
                .price(price)
                .build();
    }
}