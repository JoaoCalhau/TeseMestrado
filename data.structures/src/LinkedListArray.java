public class LinkedListArray {

    private DoublyLinkedList[] table;
    private int SIZE = 7;
    private enum types {
        ARCHIVE(0,"Archive"), DATA(1,"Data"), DISK(2,"Disk"), EXEC(3,"Exec"),
        IMAGES(4,"Images"), TEXT(5,"Text"), UNKNOWN(6,"Unknown");

        private int value;
        private String type;

        types(int value, String type) {
            this.value = value;
            this.type = type;
        }

        public int getValue() {
            return value;
        }

        public String getType() {
            return type;
        }
    }

    public LinkedListArray() {
        table = new DoublyLinkedList[SIZE];

        for(int i = 0; i < SIZE; i++) {
            table[i] = new DoublyLinkedList();
        }
    }

    public DoublyLinkedList getArchives() {
        return table[types.ARCHIVE.getValue()];
    }

    public DoublyLinkedList getData() {
        return table[types.DATA.getValue()];
    }

    public DoublyLinkedList getDisk() {
        return table[types.DISK.getValue()];
    }

    public DoublyLinkedList getExec() {
        return table[types.EXEC.getValue()];
    }

    public DoublyLinkedList getImages() {
        return table[types.IMAGES.getValue()];
    }

    public DoublyLinkedList getText() {
        return table[types.TEXT.getValue()];
    }

    public DoublyLinkedList getUnkown() {
        return table[types.UNKNOWN.getValue()];
    }

    public void insert(Inode item) {
        switch(item.getType()) {
            case "Archive":
            {
                table[types.ARCHIVE.getValue()].addLast(item);
                break;
            }
            case "Data":
            {
                table[types.DATA.getValue()].addLast(item);
                break;
            }
            case "Disk":
            {
                table[types.DISK.getValue()].addLast(item);
                break;
            }
            case "Executable":
            {
                table[types.EXEC.getValue()].addLast(item);
                break;
            }
            case "Images":
            {
                table[types.IMAGES.getValue()].addLast(item);
                break;
            }
            case "Text":
            {
                table[types.TEXT.getValue()].addLast(item);
                break;
            }
            case "Unknown":
            {
                table[types.UNKNOWN.getValue()].addLast(item);
                break;
            }
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("Types(");

        for(int i = 0; i < SIZE; i++) {
            String s = table[i].toString();
            sb.append(s + ",\n");
        }

        sb.delete(sb.length()-2, sb.length());

        sb.append(")");

        return sb.toString();
    }

}
