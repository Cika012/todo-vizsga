package hu.modm.todo.entity.enums;

public enum Importance {
    URGENT(3),
    IMPORTANT(2),
    NON_URGENT(1);

    private int value;

    private Importance(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
