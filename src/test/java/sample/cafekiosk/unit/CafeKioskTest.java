package sample.cafekiosk.unit;

import org.junit.jupiter.api.Test;
import sample.cafekiosk.unit.beverage.Americano;

import static org.junit.jupiter.api.Assertions.*;

class CafeKioskTest {

    // 테스트가 성공하지만, 아래를 자동화된 테스트라고 할 수 있을까? 이 테스트는 수동적인 테스트라 볼 수 있다.
    // 왜냐하면 테스트를 실행할때 나오는 결과를 사람의 눈으로 다시 한번 봐야 하기 때문이다.
    // 이렇게 되면 어떤 의도로 이 테스트를 수행 했는지, 이 테스트를 어떤 관점에서 봐야하는지에 대해 놓칠 우려가 있다.
    @Test
    void add() {
        CafeKiosk cafeKiosk = new CafeKiosk();
        cafeKiosk.add(new Americano());

        System.out.println(">>> 음료 수 "+cafeKiosk.getBeverages().size());
        System.out.println(">>> 음료 이름 "+cafeKiosk.getBeverages().get(0).getName());
    }
}