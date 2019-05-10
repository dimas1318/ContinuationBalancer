package balancer;

import balancer.serializer.Serializer;
import continuation.StatedContinuation;

import java.util.Arrays;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class Balancer {

    private static final int N_THREADS = 2;

    private final ExecutorService executorService;

    private Queue<StatedContinuation> taskQueue;
    private Queue<Pair> taskNameQueue;
    private Serializer serializer;

    private boolean isSerializationEnabled;

    public Balancer() {
        this(N_THREADS);
    }

    public Balancer(int nThreads) {
        this(nThreads, false);
    }

    public Balancer(boolean enableSerialization) {
        this(N_THREADS, enableSerialization);
    }

    public Balancer(int nThreads, boolean enableSerialization) {
        executorService = Executors.newFixedThreadPool(nThreads);
        isSerializationEnabled = enableSerialization;
        if (isSerializationEnabled) {
            taskNameQueue = new PriorityBlockingQueue<>(11, (p1, p2) -> p2.getImportance().compareTo(p1.getImportance()));
            serializer = new Serializer();
        } else {
            taskQueue = new PriorityBlockingQueue<>(11, new ContinuationStateComparator());
        }
    }

    public void addTasks(StatedContinuation... tasks) {
        if (isSerializationEnabled) {
            taskNameQueue.addAll(Arrays.stream(tasks)
                    .map(task -> {
                        String taskName = Integer.toHexString(System.identityHashCode(task));
                        serializer.serialize(task, taskName);
                        return new Pair(taskName, task.getImportance());
                    })
                    .collect(Collectors.toList()));
        } else {
            taskQueue.addAll(Arrays.asList(tasks));
        }
    }

    public void executeTasks() {
        if (isSerializationEnabled) {
            if (!taskNameQueue.isEmpty()) {
                String taskName = taskNameQueue.poll().getTaskName();
                executorService.execute(() -> {
                    StatedContinuation task = serializer.deserialize(taskName);
                    task.run();
                    if (!task.isDone()) {
                        serializer.serialize(task, taskName);
                        taskNameQueue.add(new Pair(taskName, task.getImportance()));
                    }
                    executeTasks();
                });
            } else {
                executorService.shutdown();
            }
        } else {
            if (!taskQueue.isEmpty()) {
                StatedContinuation task = taskQueue.poll();
                executorService.submit(() -> {
                    task.run();
                    if (!task.isDone()) {
                        taskQueue.add(task);
                    }
                    executeTasks();
                });
            } else {
                executorService.shutdown();
            }
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
