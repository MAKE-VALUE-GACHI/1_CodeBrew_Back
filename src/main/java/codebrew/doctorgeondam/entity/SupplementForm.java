package codebrew.doctorgeondam.entity;

public enum SupplementForm {
    CAPSULE("캡슐"),
    TABLET("정제"),
    POWDER("분말"),
    JELLY("젤리"),
    LIQUID("액상"),
    SOFT_CAPSULE("소프트캡슐");

    private final String description;

    SupplementForm(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
