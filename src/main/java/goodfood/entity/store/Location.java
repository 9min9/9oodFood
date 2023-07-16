package goodfood.entity.store;

public enum Location {
    SEOUL("서울"), GYEONGGI("경기"), ETC("기타");

    private String korName;

    Location(String korName) {
        this.korName = korName;
    }

    public String getKorName() {
        return korName;
    }

    public static String getKorToEng(String korName) {
        for (Location location : values()) {
            if (location.korName.equals(korName)) {
                return location.name();
            }
        }
        return null;
    }


}
