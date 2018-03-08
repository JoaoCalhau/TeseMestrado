package joao.calhau;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Parser {

    public InodeStructure is;
    public PathStructure ps;
    public TypesStructure ts;
    private int biggest;

    public Parser() {
        biggest = 0;
        is = new InodeStructure();
        ps = new PathStructure();
        ts = new TypesStructure();
    }

    public int getBiggest() {
        return biggest;
    }

    public void setBiggest(int biggest) {
        this.biggest = biggest;
    }

    public void parse(String folder) {
        parse("sorted/" + folder + "/archive.txt", "Archive");
        parse("sorted/" + folder + "/audio.txt", "Audio");
        parse("sorted/" + folder + "/compress.txt", "Compress");
        parse("sorted/" + folder + "/crypto.txt", "Crypto");
        parse("sorted/" + folder + "/data.txt", "Data");
        parse("sorted/" + folder + "/disk.txt", "Disk");
        parse("sorted/" + folder + "/documents.txt", "Documents");
        parse("sorted/" + folder + "/exec.txt", "Exec");
        parse("sorted/" + folder + "/images.txt", "Images");
        parse("sorted/" + folder + "/system.txt", "System");
        parse("sorted/" + folder + "/text.txt", "Text");
        parse("sorted/" + folder + "/unknown.txt", "Unknown");
        parse("sorted/" + folder + "/video.txt", "Video");
    }

    public void parse(String pathToFile, String type) {

        String line = "";
        String line2 = "";
        String line3 = "";

        try {
            FileReader file = new FileReader(pathToFile);
            BufferedReader br = new BufferedReader(file);

            while((line = br.readLine()) != null && (line2 = br.readLine()) != null && (line3 = br.readLine()) != null) {
                String[] check = line.split("\\$");

                if (check.length == 1) {
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
                    String[] sss = ss[ss.length - 1].split("-");


                    if (sss.length < 3 || sss[2].equals("1")) {
                        id = sss[0];

                        Inode inode = new Inode(id, fileName, path, type);

                        is.put(inode);
                        ps.put(inode);
                        ts.insert(inode);

                        if(Integer.parseInt(id) > biggest)
                            biggest = Integer.parseInt(id);

                        br.readLine();
                    } else {
                        br.readLine();
                    }
                } else {
                    br.readLine();
                }
            }
            br.close();
            file.close();
        } catch(FileNotFoundException fnfe) {
            System.err.println("File Not Found");
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
