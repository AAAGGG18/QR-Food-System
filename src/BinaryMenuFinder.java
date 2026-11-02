import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class BinaryMenuFinder implements MenuFinder {

    private final List<MenuItem> sorted;

    public BinaryMenuFinder(List<MenuItem> source) {
        this.sorted = new ArrayList<>(source);
        Collections.sort(this.sorted, new Comparator<MenuItem>() {
            @Override
            public int compare(MenuItem a, MenuItem b) {
                return Integer.compare(a.getId(), b.getId());
            }
        });
    }

    /** แบ่งครึ่งช่วงค้นหาทีละรอบ -- O(log n) */
    @Override
    public MenuItem findById(int id) {
        int low = 0;
        int high = sorted.size() - 1;
        while (low <= high) {
            int mid = low + (high - low) / 2;
            int midId = sorted.get(mid).getId();
            if (midId == id) {
                return sorted.get(mid);
            } else if (midId < id) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return null;
    }

    @Override
    public int size() {
        return sorted.size();
    }
}