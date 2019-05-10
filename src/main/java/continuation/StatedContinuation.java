package continuation;

public class StatedContinuation extends Continuation {

    private Importance importance;

    public StatedContinuation(ContinuationScope scope, Runnable target) {
        super(scope, target);
        importance = Importance.MEDIUM;
    }

    public StatedContinuation(ContinuationScope scope, int stackSize, Runnable target) {
        super(scope, stackSize, target);
        importance = Importance.MEDIUM;
    }

    public StatedContinuation(ContinuationScope scope, Runnable target, Importance importance) {
        super(scope, target);
        this.importance = importance;
    }

    public StatedContinuation(ContinuationScope scope, int stackSize, Runnable target, Importance importance) {
        super(scope, stackSize, target);
        this.importance = importance;
    }

    public Importance getImportance() {
        return importance;
    }

    public void setImportance(Importance importance) {
        this.importance = importance;
    }

    public static void yield(ContinuationScope scope, Importance importance) {
        Continuation continuation = getCurrentContinuation(scope);
        if (continuation instanceof StatedContinuation) {
            ((StatedContinuation) continuation).setImportance(importance);
        }
        yield(scope);
    }

}
