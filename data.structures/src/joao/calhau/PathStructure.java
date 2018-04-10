package joao.calhau;

import java.util.HashMap;
import java.util.LinkedList;

public class PathStructure {

    private HashMap<String, LinkedList<Inode>> table;

    public PathStructure() {
        table = new HashMap<>();
    }

    public int size() {
        return table.size();
    }

    public boolean isEmpty() {
        return table.isEmpty();
    }

    public void put(Inode item) {
        if (table.containsKey(item.getPath())) {
            LinkedList<Inode> ll = table.get(item.getPath());
            ll.addLast(item);
        } else {
            LinkedList<Inode> ll = new LinkedList<>();
            ll.addLast(item);
            table.put(item.getPath(), ll);
        }
    }

    public boolean exists(String key) {
        return table.containsKey(key);
    }

    public LinkedList<Inode> get(String key) {
        return table.get(key);
    }

    public boolean remove(String key) {
        return (table.remove(key) != null);
    }

    public LinkedList<Inode> getAndRemove(String key) {
        return table.remove(key);
    }

    public String toString() {
        return table.toString();
    }

}
