import java.util.ArrayList;
import java.util.List;

public class LinearMenuFinder implements MenuFinder {

    private final List<MenuItem> items;

    public LinearMenuFinder(List<MenuItem> source) {
        this.items = new ArrayList<>(source);
    }

    /** ไล่หาทีละตัว -- O(n) */
    @Override
    public MenuItem findById(int id) {
        for (MenuItem item : items) {
            if (item.getId() == id) {
                return item;
            }
        }
        return null;
    }

    @Override
    public int size() {
        return items.size();
    }
}
