package goodfood.entity.user;

public enum UserRole {
    ADMIN("ROLE_ADMIN"), USER("ROLE_USER"), MANAGER("ROLE_MANAGER");

    String role;

    UserRole(String role) {
        this.role = role;
    }

    public String value() {
        return role;
    }
}
