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
}
