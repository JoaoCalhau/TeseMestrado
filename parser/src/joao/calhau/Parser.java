package joao.calhau;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Parser {

    public InodeStructure is;
    public PathStructure ps;
    public TypesStructure ts;

    public Parser() {
        is = new InodeStructure();
        ps = new PathStructure();
        ts = new TypesStructure();
    }

    public void parse() {
        parse("sorted/archive.txt", "Archive");
        parse("sorted/data.txt", "Data");
        parse("sorted/disk.txt", "Disk");
        parse("sorted/exec.txt", "Exec");
        parse("sorted/images.txt", "Images");
        parse("sorted/text.txt", "Text");
        parse("sorted/unknown.txt", "Unknown");
    }

    public void parse(String pathToFile, String type) {

        String line = "";
        String line2 = "";
        String line3 = "";

        try {
            FileReader file = new FileReader(pathToFile);
            BufferedReader br = new BufferedReader(file);

            while((line = br.readLine()) != null && (line2 = br.readLine()) != null && (line3 = br.readLine()) != null) {
                if (!line.startsWith("$")) {
                    System.out.println(line);
                    System.out.println(line2);
                    System.out.println(line3);
                    String[] ss = line.split("/");
                    String path = "";
                    String id = "";
                    String fileName = "";

                    if (ss.length == 1) {
                        path = "/";
                        fileName = ss[0];
                    } else {
                        for (int i = 0; i < ss.length - 1; i++) {
                            path += ss[i] + "/";
                        }

                        path = path.substring(0, path.length() - 1);

                        fileName = ss[ss.length - 1];
                    }


                    ss = line3.split(" ");
                    System.out.println(ss[ss.length-1]);
                    String[] sss = ss[ss.length - 1].split("-");

                    if (sss[2].equals("1")) {
                        id = sss[0];

                        Inode inode = new Inode(id, fileName, path, type);

                        is.put(inode);
                        ps.put(inode);
                        ts.insert(inode);

                        line = br.readLine();
                    }
                }
            }

            br.close();
            file.close();
        } catch(FileNotFoundException fnfe) {
            System.err.println("File Not Found");
            fnfe.printStackTrace();
        } catch(IOException ioe) {
            System.err.println("Failure On Read");
            ioe.printStackTrace();
        } catch(IndexOutOfBoundsException iobe) {
            System.err.println("Array Index Out of Bounds");
            iobe.printStackTrace();
        } finally {
        }
    }
}
