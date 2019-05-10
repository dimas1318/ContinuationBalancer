public class Main {

    public static void main(String[] args) {

        Balancer balancer = new Balancer();

        ContinuationScope scope = new ContinuationScope("1");

        final StatedContinuation c1 = new StatedContinuation(scope, () -> {
            System.out.println("c1, phase 1, LOW");
            StatedContinuation.yield(scope, StatedContinuation.Importance.HIGH);
            System.out.println("c1, phase 2, HIGH");
        }, StatedContinuation.Importance.LOW);

        final StatedContinuation c2 = new StatedContinuation(scope, () -> {
            System.out.println("c2, phase 1, MEDIUM");
            StatedContinuation.yield(scope);
            System.out.println("c2, phase 2, MEDIUM");
        });

        final StatedContinuation c3 = new StatedContinuation(scope, () -> {
            System.out.println("c3, phase 1, MEDIUM");
            StatedContinuation.yield(scope, StatedContinuation.Importance.LOW);
            System.out.println("c3, phase 2, LOW");
            StatedContinuation.yield(scope, StatedContinuation.Importance.HIGH);
            System.out.println("c3, phase 3, HIGH");
        });

        final StatedContinuation c4 = new StatedContinuation(scope, () -> {
            System.out.println("c4, phase 1, HIGH");
            StatedContinuation.yield(scope);
            System.out.println("c4, phase 2, HIGH");
            StatedContinuation.yield(scope, StatedContinuation.Importance.LOW);
            System.out.println("c4, phase 3, LOW");
        }, StatedContinuation.Importance.HIGH);

        final StatedContinuation c5 = new StatedContinuation(scope, () -> {
            System.out.println("c5, phase 1, HIGH");
            StatedContinuation.yield(scope, StatedContinuation.Importance.MEDIUM);
            System.out.println("c5, phase 2, MEDIUM");
            StatedContinuation.yield(scope, StatedContinuation.Importance.LOW);
            System.out.println("c5, phase 3, LOW");
        }, StatedContinuation.Importance.HIGH);

        final StatedContinuation c6 = new StatedContinuation(scope, () -> {
            System.out.println("c6, phase 1, MEDIUM");
            StatedContinuation.yield(scope, StatedContinuation.Importance.HIGH);
            System.out.println("c6, phase 2, HIGH");
        });

        balancer.addTasks(c1, c2, c3, c4, c5, c6);
        balancer.executeTasks();
    }
}
