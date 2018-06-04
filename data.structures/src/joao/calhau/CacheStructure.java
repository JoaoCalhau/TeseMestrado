package joao.calhau;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class CacheStructure {

    private Map<String, LinkedList<Inode>> cache;
    private String folder;

    public CacheStructure(String folder) {
        this.folder = folder;
        readFromFile();
    }

    public void addToCache(String key, LinkedList<Inode> ll) {
        cache.put(key, ll);
    }

    public boolean existsInCache(String key) {
        return cache.containsKey(key);
    }

    public LinkedList<Inode> getListFromCache(String key) {
        return cache.get(key);
    }

    private void readFromFile() {
        try {

            File cacheFile = new File("./cache/" + this.folder + ".cache");

            if(cacheFile.exists()) {
                FileInputStream fin = new FileInputStream(cacheFile);
                ObjectInputStream ois = new ObjectInputStream(fin);
                cache = (HashMap) ois.readObject();

                ois.close();
                fin.close();
            } else {
                cache = new HashMap<>();
            }


        } catch (FileNotFoundException fnfe) {
            System.err.println("File not found...\nCheck your file paths");
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        }
    }

    public void saveToFile() {
        try {
            File cacheFile = new File("./cache/" + this.folder + ".cache");

            if(!cacheFile.exists())
                cacheFile.createNewFile();

            FileOutputStream fos = new FileOutputStream(cacheFile, false);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(cache);

            oos.close();
            fos.close();

        } catch (FileNotFoundException fnfe) {
            System.err.println("File not found...\nCheck your file paths");
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }


}
