package soya.framework.reflect;


import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ClassPath {

    private ClassPath() {
    }

    public static URL getURL(Class<?> clazz) {
        return clazz.getProtectionDomain().getCodeSource().getLocation();
    }

    public static Set<String> scanForPaths(String base) {

        Set<String> set = new LinkedHashSet<>();
        String[] paths = System.getProperty("java.class.path").split(";");
        if(paths.length == 1) {
            String url = getURL(ClassPath.class).toString();
            int index = url.indexOf('!');
            if(index > 0) {

            }


        } else {
            Arrays.stream(paths).forEach(e -> {
                File file = new File(e);
                if (file.isDirectory()) {
                    readFromDir(file, base, set);
                } else if (e.endsWith(".jar")) {
                    readFromJar(new File(e), base, set);
                }
            });
        }

        return set;
    }

    private static void readFromDir(File dir, String base, Set<String> set) {
        try {
            String uri = dir.toURI().toString();
            Files.walk(dir.toPath())
                    .forEach(path -> {
                        String p = path.toFile().toURI().toString().substring(uri.length());
                        if(base == null || p.startsWith(base)) {
                            set.add(p);
                        }

                    });
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static void readFromJar(File file, String base, Set<String> set) {
        try {
            ZipFile zip = new ZipFile(file);
            Enumeration<? extends ZipEntry> entries = zip.entries(); //get entries from the zip file...
            if (entries != null) {
                while (entries.hasMoreElements()) {
                    ZipEntry entry = entries.nextElement();
                    if (base == null || entry.getName().startsWith(base)) {
                        set.add(entry.getName());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void print() {
    }

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        scanForPaths(null).forEach(e -> {
            System.out.println(e);
        });

        System.out.println("------------ " + (System.currentTimeMillis() - start));
    }
}
