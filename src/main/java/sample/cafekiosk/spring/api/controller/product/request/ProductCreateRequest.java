package sample.cafekiosk.spring.api.controller.product.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sample.cafekiosk.spring.api.service.product.request.ProductCreateServiceRequest;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;
import sample.cafekiosk.spring.domain.product.ProductType;

@Getter
@NoArgsConstructor
public class ProductCreateRequest {

    @NotNull(message = "상품 타입은 필수입니다.")
    private ProductType type;

    @NotNull(message = "상품 판매상태는 필수입니다.")
    private ProductSellingStatus sellingStatus;

    // 셋 차이 알아두자!
    @NotBlank(message = "상품 이름은 필수입니다.") // 공백이여도 안돼고, NULL이여도 안되고, 문자열이 있어야 한다는 검증
    // @NotNull NULL이면 안되지만, 공백은 허용하는 검증
    // @NotEmpty 공백은 안되지만, 빈칸은 허용하는 검증
    private String name;
    // name에 대한 길이 검증은 @Max로 가능하다. 하지만 강의에서도 말했지만 '여기서 검증을 하는게 맞는건가?' 하는 의문이 드는게 사실이다.
    // 강사님 말씀에 따르면, 모든 검증을 controller validation에서 할 필요는 없다고 말한다. 서비스단에서 하던, 엔티티 생성자에서 할 수 있는 것이다.
    // controller validation 단에서는 기초적인 검증만 이루어지게 하고, 나머지는 서비스단에서 하도록 해봐야겠다.

    @Positive(message = "상품 가격은 양수여야 합니다.")
    private int price;

    @Builder
    public ProductCreateRequest(ProductType type, ProductSellingStatus sellingStatus, String name, int price) {
        this.type = type;
        this.sellingStatus = sellingStatus;
        this.name = name;
        this.price = price;
    }

    public ProductCreateServiceRequest toServiceRequest() {
        return ProductCreateServiceRequest.builder()
                .type(type)
                .sellingStatus(sellingStatus)
                .name(name)
                .price(price)
                .build();
    }
}
