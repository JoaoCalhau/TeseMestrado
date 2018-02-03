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
    private String fileName;
    private String path;
    private String type;

    public Inode() {
        id = null;
        fileName = null;
        path = null;
        type = null;
    }

    public Inode(String id, String fileName, String path, String type) {
        this.id = id;
        this.fileName = fileName;
        this.path = path;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
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
        return "Inode(" + id + ", " + fileName + ", " + path + ", " + type + ")";
    }
}
