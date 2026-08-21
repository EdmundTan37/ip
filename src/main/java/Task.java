package src.main.java;

public class Task {
    int index;
    boolean completed;
    String name;

    public Task(int index, boolean completed, String name) {
        this.index = index;
        this.completed = completed;
        this.name = name;
    }

    @Override
    public String toString() {
        if (completed) {
            System.out.println(index + ". " + "[X]" + name);
            return null;
        }
        System.out.println(index + ". " + "[ ]" + name);
        return null;
    }

    void changeCompleted() {
        completed = true;
    }

    void changeUncompleted() {
        completed = false;
    }
}
