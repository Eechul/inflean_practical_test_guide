package sample.cafekiosk.spring.domain.product;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class ProductTypeTest {

    @DisplayName("상품타입이 재고 관련 타입인지를 체크한다.")
    @Test
    void containsStackType1() {
        // given
        ProductType givenType = ProductType.HANDMADE;

        // when
        boolean result = ProductType.containsStockType(givenType);

        // then
        assertThat(result).isFalse();

    }

    @DisplayName("상품타입이 재고 관련 타입인지를 체크한다.")
    @Test
    void containsStackType2() {
        // given
        ProductType givenType = ProductType.BAKERY;

        // when
        boolean result = ProductType.containsStockType(givenType);

        // then
        assertThat(result).isTrue();

    }

    // @ParameterizedTest 를 안썼을때는 enum의 모든 값을 given에 명시하고 테스트 해야했다.
    @DisplayName("상품타입이 재고 관련 타입인지를 체크한다.")
    @Test
    void containsStackType3() {
        // given
        ProductType givenType1 = ProductType.HANDMADE;
        ProductType givenType2 = ProductType.BOTTLE;
        ProductType givenType3 = ProductType.BAKERY;

        // when
        boolean result1 = ProductType.containsStockType(givenType1);
        boolean result2 = ProductType.containsStockType(givenType2);
        boolean result3 = ProductType.containsStockType(givenType3);

        // then
        assertThat(result1).isFalse();
        assertThat(result2).isTrue();
        assertThat(result3).isTrue();
    }

    // @ParameterizedTest 를 쓰면 테스트할 데이터를 Source에 명시해줄 수 있다.
    @DisplayName("상품타입이 재고 관련 타입인지를 체크한다.")
    @CsvSource({ "HANDMADE,false", "BOTTLE,true", "BAKERY,true" }) // 어떤 데이터로 테스트를 할지 정해준다. 다양하게 있다.
    @ParameterizedTest
    void containsStackType4(ProductType productType, boolean expected) {
        // given -> 어노테이션과 파라미터로 받아옴

        // when
        boolean result = ProductType.containsStockType(productType);

        // then
        assertThat(result).isEqualTo(expected);
    }

    private static Stream<Arguments> provideProductTypesForCheckingStockType() {
        return Stream.of(
                Arguments.of(ProductType.HANDMADE, false),
                Arguments.of(ProductType.BOTTLE, true),
                Arguments.of(ProductType.BAKERY, true)
        );
    }

    // CsvSource, MethodSource, ValueSorce 등을 쓰면 테스트할 데이터를 Source에 명시해줄 수 있다.
    @DisplayName("상품타입이 재고 관련 타입인지를 체크한다.")
    @MethodSource("provideProductTypesForCheckingStockType")
    @ParameterizedTest // DisplayName 을 커스텀 해줄 수 있다.
    void containsStackType5(ProductType productType, boolean expected) {
        // given -> 어노테이션과 파라미터로 받아옴

        // when
        boolean result = ProductType.containsStockType(productType);

        // then
        assertThat(result).isEqualTo(expected);
    }

}