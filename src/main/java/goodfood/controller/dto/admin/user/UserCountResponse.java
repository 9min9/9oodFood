package goodfood.controller.dto.admin.user;

import goodfood.entity.user.Gender;
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
public class UserCountResponse {
    private List<Long> male = new ArrayList<>();
    private List<Long> female = new ArrayList<>();
    private List<Long> all = new ArrayList<>();

    public UserCountResponse toDto(HashMap<String, List<Long>> countHash) {
        this.male = countHash.get(Gender.MALE.name());
        this.female = countHash.get(Gender.FEMALE.name());
        this.all = countHash.get("ALL");

        return this;
    }
}
