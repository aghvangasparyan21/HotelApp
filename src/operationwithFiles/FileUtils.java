package operationwithFiles;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {
    public static void writeToFile(String filePath, String content) {
        try {
            FileWriter writer = new FileWriter(filePath);
            writer.write(content);
            writer.close();
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    public static void serializeToFile(String filePath, Object... objects) {
        try {
            FileOutputStream fileOut = new FileOutputStream(filePath);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            for (Object obj : objects) {
                objectOut.writeObject(obj);
            }
            objectOut.close();
            fileOut.close();
        } catch (IOException e) {
            System.out.println("Error serializing objects: " + e.getMessage());
        }
    }

    public static Object[] deserializeFromFile(String filePath) {
        try {
            FileInputStream fileIn = new FileInputStream(filePath);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);
            List<Object> objects = new ArrayList<>();
            while (true) {
                try {
                    Object obj = objectIn.readObject();
                    objects.add(obj);
                } catch (EOFException e) {
                    break;
                }
            }
            objectIn.close();
            fileIn.close();
            return objects.toArray();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error deserializing objects: " + e.getMessage());
            return null;
        }
    }
}