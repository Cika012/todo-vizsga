package hu.modm.todo.entity.enums;

public enum Status {
    DONE(3),
    WORKING_ON_IT(2),
    NOT_STARTED(1);

    private int value;

    private Status(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
