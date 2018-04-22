package joao.calhau;

import java.util.HashMap;

public class InodeStructure {

    public HashMap<String, Inode> table;

    public InodeStructure() {
        table = new HashMap<>();
    }

    public int size() {
        return table.size();
    }

    public boolean isEmpty() {
        return table.isEmpty();
    }

    public void put(Inode item) {
        table.put(item.getId(), item);
    }

    public boolean exists(String key) {
        return table.containsKey(key);
    }

    public Inode get(String key) {
        return table.get(key);
    }

    public boolean remove(String key) {
        return (table.remove(key) != null);
    }

    public Inode getAndRemove(String key) {
        return table.remove(key);
    }

    public String toString() {
        return table.toString();
    }
}
