package gameframework.libs;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.jar.*;

import javax.imageio.ImageIO;

public class JarDirLoader {
    public static void LoadAllTXT(String jarFilePath, Map<String, BufferedReader> allTxts) throws IOException {

        JarFile jf = new JarFile(jarFilePath);
        Enumeration<JarEntry> iter = jf.entries();
        while (iter.hasMoreElements()) {
            JarEntry entry = iter.nextElement();
            if (entry.isDirectory()) {
                processDirectoryTXT(jf, entry, allTxts);
            } else {
                processEntryTXT(entry, allTxts);
            }
        }
    }

    public static void LoadAllPNG(String jarFilePath, Map<String, BufferedImage> allTextures) throws IOException {

        JarFile jf = new JarFile(jarFilePath);
        Enumeration<JarEntry> iter = jf.entries();
        while (iter.hasMoreElements()) {
            JarEntry entry = iter.nextElement();
            if (entry.isDirectory()) {
                processDirectory(jf, entry, allTextures);
            } else {
                processEntry(entry, allTextures);
            }
        }
    }

    private static void processEntry(JarEntry je, Map<String, BufferedImage> allTextures) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (classLoader == null) {
            classLoader = Class.class.getClassLoader();
        }
        try {

            if (je.getName().substring(je.getName().length() - 4, je.getName().length()).equals(".gif")
                    || je.getName().substring(je.getName().length() - 4, je.getName().length()).equals(".png")) {

                String[] splited = je.getName().split("/");
                String filename = splited[splited.length - 2] + "/" + splited[splited.length - 1];
                URL modelUrl = classLoader.getResource(je.getName());

                allTextures.put(filename, ImageIO.read(modelUrl));

            }
        } catch (Exception e) {
        }

    }

    private static void processEntryTXT(JarEntry je, Map<String, BufferedReader> allTxts) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (classLoader == null) {
            classLoader = Class.class.getClassLoader();
        }
        try {

            if (je.getName().substring(je.getName().length() - 4, je.getName().length()).equals(".txt")) {
                String[] splited = je.getName().split("/");
                String filename = splited[splited.length - 2] + "/" + splited[splited.length - 1];
                URL modelUrl = classLoader.getResource(je.getName());

                BufferedReader in = new BufferedReader(new InputStreamReader(modelUrl.openStream()));

                allTxts.put(filename, in);
            }
        } catch (Exception e) {
        }

    }

    private static void processDirectoryTXT(JarFile jf, JarEntry entry, Map<String, BufferedReader> allTxts) throws IOException {
        JarInputStream jis = new JarInputStream(jf.getInputStream(entry));
        JarEntry je;
        try {
            while ((je = jis.getNextJarEntry()) != null) {
                if (je.isDirectory()) {
                    processDirectoryTXT(jf, je, allTxts);
                } else {
                    processEntryTXT(je, allTxts);
                }
            }
        } finally {
            jis.close();
        }
    }

    private static void processDirectory(JarFile jf, JarEntry entry, Map<String, BufferedImage> allTextures) throws IOException {
        JarInputStream jis = new JarInputStream(jf.getInputStream(entry));
        JarEntry je;
        try {
            while ((je = jis.getNextJarEntry()) != null) {
                if (je.isDirectory()) {
                    processDirectory(jf, je, allTextures);
                } else {
                    processEntry(je, allTextures);
                }
            }
        } finally {
            jis.close();
        }
    }
}