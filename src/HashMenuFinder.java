import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HashMenuFinder implements MenuFinder {

    private final Map<Integer, MenuItem> table;

    public HashMenuFinder(List<MenuItem> source) {
        this.table = new HashMap<>();
        for (MenuItem item : source) {
            table.put(item.getId(), item);
        }
    }

    /** คำนวณตำแหน่งจาก id ตรงๆ -- O(1) โดยเฉลี่ย */
    @Override
    public MenuItem findById(int id) {
        return table.get(id);
    }

    @Override
    public int size() {
        return table.size();
    }
}