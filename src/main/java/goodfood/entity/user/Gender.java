package goodfood.entity.user;

public enum Gender {

    MALE("남자"), FEMALE("여자");

    private String korName;

    Gender(String korName) {
        this.korName = korName;
    }

    public String getKorName() {
        return korName;
    }

    public static String getKorToEng(String korName) {
        for (Gender gender : values()) {
            if(gender.korName.equals(korName)) {
                return gender.name();
            }

        }
        return null;
    }
}
