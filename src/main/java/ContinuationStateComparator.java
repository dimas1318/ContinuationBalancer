import java.util.Comparator;

public class ContinuationStateComparator implements Comparator<StatedContinuation> {

    @Override
    public int compare(StatedContinuation c1, StatedContinuation c2) {
        return c2.getImportance().compareTo(c1.getImportance());
    }
}
