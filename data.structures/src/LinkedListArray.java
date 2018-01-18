public class LinkedListArray {

    private DoublyLinkedList[] table;
    private int SIZE = 7;
    private enum types {
        ARCHIVE(1,"Archive"), DATA(2,"Data"), DISK(3,"Disk"), EXEC(4,"Exec"),
        IMAGES(5,"Images"), TEXT(6,"Text"), UNKNOWN(7,"Unknown");

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

            }
            case "Data":
            {

            }
            case "Disk":
            {

            }
            case "Exec":
            {

            }
            case "Images":
            {

            }
            case "Text":
            {

            }
            case "Unknown":
            {

            }
        }
    }

}
