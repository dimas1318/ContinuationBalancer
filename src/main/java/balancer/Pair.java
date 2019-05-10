package balancer;

import continuation.Importance;

public class Pair {

    private String taskName;
    private Importance importance;

    public Pair(String taskName, Importance importance) {
        this.taskName = taskName;
        this.importance = importance;
    }

    public String getTaskName() {
        return taskName;
    }

    public Importance getImportance() {
        return importance;
    }
}
