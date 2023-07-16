package goodfood.service.dto.store;

import goodfood.entity.store.*;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class StoreServiceDto {
    private String title;
    private String address;
    private String roadAddress;
    private Category category;
    private PriceRange priceRange;
    private Waiting waiting;
    private int status;


    public Store toEntity() {
        return Store.builder()
                .title(title)
                .address(address)
                .roadAddress(roadAddress)
                .category(category)
                .priceRange(priceRange)
                .waiting(waiting)
                .status(status)
                .build();
    }

}
