package main.java;

import java.io.File;
import java.util.Scanner;

import main.java.Core.main_dict.DBManager;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

public class Test {

    /**
     * db modifier.
     */
    public static void dbmod() {
        try {
            String path = DBManager.pathGetter("/src/main/resources/EV.txt");
            System.err.println(path);
            File DBFile = new File(path);
            System.err.println(DBFile.exists() ? "file found" : "file not found");
            Scanner scanner = new Scanner(DBFile, "UTF-8");
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream("output.txt"), StandardCharsets.UTF_8));
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                writer.write(line);
                writer.newLine();
            }
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // dbmod();
        DBManager.scan();
        // DBManager.export();
    }
}
