package lftc.edu.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FileUtil {

    public static Map<String, Integer> readFileInMap(String fileName) throws IOException {
        String line;
        Map<String, Integer> map = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(getFile(fileName)))) {
            while ((line = br.readLine()) != null) {
                String[] split = line.split(" ");
                map.put(split[0], Integer.valueOf(split[1]));
            }
        }
        return map;
    }

    public static File getFile(String fileName) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        return new File(classLoader.getResource(fileName).getFile());
    }
}
