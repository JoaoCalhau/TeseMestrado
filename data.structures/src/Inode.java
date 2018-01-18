/*
 *
 * This class represents a possible simple
 * representation of an Inode
 *
 */
public class Inode {

    private String id;
    private String path;
    private String type;

    public Inode() {
        this.id = null;
        this.path = null;
        this.type = null;
    }

    public Inode(String id) {
        this.id = id;
        this.path = null;
        this.type = null;
    }

    public Inode(String id, String path) {
        this.id = id;
        this.path = path;
        this.type = null;
    }

    public Inode(String id, String path, String type) {
        this.id = id;
        this.path = path;
        this.type = type;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String toString() {
        return "Inode(" + id + ", " + path + ", " + type + ")";
    }
}
