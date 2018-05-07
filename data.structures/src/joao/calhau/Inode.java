package joao.calhau;

import java.io.Serializable;

/*
 *
 * This class represents a possible simple
 * representation of an Inode
 *
 */
public class Inode implements Serializable {

    private String id;
    private String fileName;
    private String path;
    private String type;
    private String dateTime;

    public Inode() {
        id = null;
        fileName = null;
        path = null;
        type = null;
        dateTime = null;
    }

    public Inode(String id) {
        this.id = id;
        fileName = null;
        path = null;
        type = null;
        dateTime = null;
    }

    public Inode(String id, String fileName, String path, String type) {
        this.id = id;
        this.fileName = fileName;
        this.path = path;
        this.type = type;
        this.dateTime = null;
    }

    public Inode(String id, String fileName, String path, String type, String dateTime) {
        this.id = id;
        this.fileName = fileName;
        this.path = path;
        this.type = type;
        this.dateTime = dateTime;
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

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public boolean equals(Object obj) {
        if (obj instanceof Inode) {
            Inode toCompare = (Inode) obj;
            return this.id.equals(toCompare.getId());
        } else {
            return false;
        }
    }

    public String toString() {
        return "Inode(" + id + ", " + fileName + ", " + path + ", " + type + ", " + dateTime + ")";
    }
}
