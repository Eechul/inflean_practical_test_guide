package sample.cafekiosk.unit;

import org.junit.jupiter.api.Test;
import sample.cafekiosk.unit.beverage.Americano;
import sample.cafekiosk.unit.beverage.Latte;
import sample.cafekiosk.unit.order.Order;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CafeKioskTest {

    // 테스트가 성공하지만, 아래를 자동화된 테스트라고 할 수 있을까? 이 테스트는 수동적인 테스트라 볼 수 있다.
    // 왜냐하면 테스트를 실행할때 나오는 결과를 사람의 눈으로 다시 한번 봐야 하기 때문이다.
    // 이렇게 되면 어떤 의도로 이 테스트를 수행 했는지, 이 테스트를 어떤 관점에서 봐야하는지에 대해 놓칠 우려가 있다.
    @Test
    void add_manual_test() {
        CafeKiosk cafeKiosk = new CafeKiosk();
        cafeKiosk.add(new Americano());

        System.out.println(">>> 음료 수 "+cafeKiosk.getBeverages().size());
        System.out.println(">>> 음료 이름 "+cafeKiosk.getBeverages().get(0).getName());
    }

    // 이 테스트는 자동화된 테스트라고 할 수 있다. 사람이 확인한건 없다.
    // 테스트의 목적이 수동적인 테스트와 다르게 명시적이다.
    // 키오스크에 아메리카노 음료가 잘 추가됬는지 명시적으로 알 수 있다.
    @Test
    void add() {
        CafeKiosk cafeKiosk = new CafeKiosk();
        cafeKiosk.add(new Americano());

//        assertThat(cafeKiosk.getBeverages().size()).isEqualTo(1); 와 같다.
        assertThat(cafeKiosk.getBeverages()).hasSize(1);
        assertThat(cafeKiosk.getBeverages().get(0).getName()).isEqualTo("아메리카노");
    }

    // 해피 케이스
    @Test
    void addSeveralBeverage() {
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();

        cafeKiosk.add(americano, 2);
        assertThat(cafeKiosk.getBeverages()).hasSize(2);
        assertThat(cafeKiosk.getBeverages().get(0).getName()).isEqualTo("아메리카노");
        assertThat(cafeKiosk.getBeverages().get(1).getName()).isEqualTo("아메리카노");
    }

    // 예외 케이스1
    // 이렇듯 테스트 케이스 세분화(해피, 예외)는 경계값(범위, 기타 조건 등)으로 판단하여 케이스별로 작성하는것이 좋다.
    @Test
    void addZeroBeverage() {
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();

        assertThatThrownBy(() -> cafeKiosk.add(americano, 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("음료는 1잔 이상 주문하실 수 있습니다.");
    }

    // 예외 케이스2
    @Test
    void addMinusBeverage() {
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();

        assertThatThrownBy(() -> cafeKiosk.add(americano, -1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("음료는 1잔 이상 주문하실 수 있습니다.");
    }

    // 이 테스트도 자동화된 테스트라고 할 수 있다.
    // 키오스크에 아메리카노 음료가 잘 추가 되고, 삭제까지 잘 되는지 명시적으로 알 수 있다.
    @Test
    void remove() {
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();

        cafeKiosk.add(americano);
        assertThat(cafeKiosk.getBeverages()).hasSize(1);
        assertThat(cafeKiosk.getBeverages().get(0).getName()).isEqualTo("아메리카노");

        cafeKiosk.remove(americano);
        assertThat(cafeKiosk.getBeverages()).isEmpty();
    }

    // 키오스크 클래스의 'clear' 메서드가 잘 작동하는지 자동화된 테스트로 알 수 있다.
    @Test
    void clear() {
        CafeKiosk cafeKiosk = new CafeKiosk();
        cafeKiosk.add(new Americano());
        cafeKiosk.add(new Latte());

        assertThat(cafeKiosk.getBeverages()).hasSize(2);

        cafeKiosk.clear();
        assertThat(cafeKiosk.getBeverages()).isEmpty();
    }

    // TDD 강의 예제
    // RED - GREEN - REFACTORING 의 순서대로 TDD를 진행한다.
    // 기능 구현 후, 테스트를 작성하는게 아니라, 테스트를 작성한 후 기능 구현을 하는 방식이다.(클라이언트 관점의 변환)
    // 이러한 관점 변환은 해피 케이스, 예외 케이스를 좋치지 않게 해준다.
    // 피드백도 빠르게 받을 수 있다.
    @Test
    void calculate() {
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();
        Latte latte = new Latte();

        cafeKiosk.add(americano);
        cafeKiosk.add(latte);

        int totalPrice = cafeKiosk.calculate();
        assertThat(totalPrice).isEqualTo(8500);
    }

    // 현재시간을 가지고 예외를 던지는 로직이 존재하여 테스트에 어려움이 있는 상황(현재시간에 따라 테스트가 달라짐)
//    @Test
//    void createOrder() {
//        CafeKiosk cafeKiosk = new CafeKiosk();
//        cafeKiosk.add(new Americano());
//
//        Order order = cafeKiosk.createOrder();
//        assertThat(order.getBeverages()).hasSize(1);
//        assertThat(order.getBeverages().get(0).getName()).isEqualTo("아메리카노");
//    }

    // 현재시간은 계속해서 변한다. 따라서 이 값은 앞에서 봤던 경계값을 고려해 '직접' 할당해준다면
    // 계속해서 변하지 않고, 테스트를 이어나갈 수 있다.
    @Test
    void createOrderWithCurrentTime() {
        CafeKiosk cafeKiosk = new CafeKiosk();
        cafeKiosk.add(new Americano());

        // 외부 세계(여기서는 상위레벨 메서드)에서 파라미터로 '직접' 전달
        Order order = cafeKiosk.createOrder(LocalDateTime.of(2023, 8, 11, 10, 0));
        assertThat(order.getBeverages()).hasSize(1);
        assertThat(order.getBeverages().get(0).getName()).isEqualTo("아메리카노");
    }

    @Test
    void createOrderWithOutsideOpenTime() {
        CafeKiosk cafeKiosk = new CafeKiosk();
        cafeKiosk.add(new Americano());

        assertThatThrownBy(() -> cafeKiosk.createOrder(LocalDateTime.of(2023, 8, 11, 23, 0)))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("주문 시간이 아닙니다. 관리자에게 문의해주세요.");
    }
}