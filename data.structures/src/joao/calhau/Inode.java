package joao.calhau;

/*
 *
 * This class represents a possible simple
 * representation of an Inode
 *
 */
public class Inode {

    private String id;
    //Add a file name for easier identification (can include file format at the end)
    //private String fileName;
    private String path;
    private String type;

    public Inode() {
        id = null;
        path = null;
        type = null;
    }

    public Inode(String id) {
        this.id = id;
        path = null;
        type = null;
    }

    public Inode(String id, String path) {
        this.id = id;
        this.path = path;
        type = null;
    }

    public Inode(String id, String path, String type) {
        this.id = id;
        this.path = path;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean equals(String id) {
        return this.id.equals(id);
    }

    public String toString() {
        return "Inode(" + id + ", " + path + ", " + type + ")";
    }
}
