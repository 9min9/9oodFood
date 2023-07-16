package goodfood.controller.dto.admin.forum;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class StoreCountResponse {
    private List<Long> seoul = new ArrayList<>();
    private List<Long> gyeonggi = new ArrayList<>();
    private List<Long> etc = new ArrayList<>();
    private List<Long> all = new ArrayList<>();

    public StoreCountResponse toDto(HashMap<String, List<Long>> countHash) {
        this.seoul = countHash.get("seoul");
        this.gyeonggi = countHash.get("gyeonggi");
        this.etc = countHash.get("etc");
        this.all = countHash.get("all");

        return this;
    }

}
