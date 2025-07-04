package main.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.util.*;

public class FileUtils {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static <T> List<T> readListFromFile(String filePath, Class<T[]> clazz) {
        try (Reader reader = new FileReader(filePath)) {
            T[] array = gson.fromJson(reader, clazz);
            return array != null ? new ArrayList<>(Arrays.asList(array)) : new ArrayList<>();
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    public static <T> void writeListToFile(String filePath, List<T> list) {
        try (Writer writer = new FileWriter(filePath)) {
            gson.toJson(list, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
