package main.java;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Main {

    final static String savesDirectory = "F://Games//savegames";
    final static Map<String, GameProgress> gameProgresses = new LinkedHashMap<>();
    final static List<String> saveFiles = new ArrayList<>();

    public static void main(String... args) {

        File saves = new File(savesDirectory);

        if (saves.exists()) {

            gameProgresses.put(savesDirectory + "//save1", new GameProgress(100, 2, 33, 22.2));
            gameProgresses.put(savesDirectory + "//save2", new GameProgress(45, 2, 56, 56.4));
            gameProgresses.put(savesDirectory + "//save3", new GameProgress(66, 2, 88, 44.3));

            gameProgresses.forEach(Main::saveGame);
            gameProgresses.forEach((s, progress) -> saveFiles.add(s));

            zipFiles(savesDirectory + "//zip.zip", saveFiles);

        } else {
            System.out.println("savegames directory not exists!");
        }
    }

    public static void saveGame(String name, GameProgress progress) {

        try (FileOutputStream fileOutputStream = new FileOutputStream(name + ".dat");
             ObjectOutputStream outputStream = new ObjectOutputStream(fileOutputStream)) {

            outputStream.writeObject(progress);
            System.out.println(progress);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void zipFiles(String path, List<String> files) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(path);
             ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream);
             FileInputStream fileInputStream = new FileInputStream(path)) {

            files.forEach(s -> {
                try {
                    ZipEntry zipEntry = new ZipEntry(s + ".dat");
                    zipOutputStream.putNextEntry(zipEntry);
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = fileInputStream.read(buffer)) > 0) {
                        zipOutputStream.write(buffer, 0, len);
                    }
                    File file = new File(s + ".dat");
                    if (file.delete()) {
                        System.out.println("File deleted successfully");
                    } else {
                        System.out.println("Failed to delete the file");
                    }
                } catch (NullPointerException | IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
