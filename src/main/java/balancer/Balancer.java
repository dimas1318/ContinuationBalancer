package balancer;

import continuation.StatedContinuation;

import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Queue;

public class Balancer {

    private final Queue<StatedContinuation> taskQueue;

    public Balancer() {
        taskQueue = new PriorityQueue<>(new ContinuationStateComparator());
    }

    public void addTask(StatedContinuation task) {
        taskQueue.add(task);
    }

    public void addTasks(StatedContinuation... tasks) {
        taskQueue.addAll(Arrays.asList(tasks));
    }

    public void executeTasks() {
        if (!taskQueue.isEmpty()) {
            StatedContinuation task = taskQueue.poll();
            Thread t = new Thread(task::run);
            t.start();
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (!task.isDone()) {
                taskQueue.add(task);
            }
            executeTasks();
        }
    }

//    c4, phase 1, HIGH
//    c5, phase 1, HIGH
//    c4, phase 2, HIGH
//    c5, phase 2, MEDIUM
//    c6, phase 1, MEDIUM
//    c6, phase 2, HIGH
//    c3, phase 1, MEDIUM
//    c2, phase 1, MEDIUM
//    c2, phase 2, MEDIUM
//    c5, phase 3, LOW
//    c1, phase 1, LOW
//    c1, phase 2, HIGH
//    c4, phase 3, LOW
//    c3, phase 2, LOW
//    c3, phase 3, HIGH
}
