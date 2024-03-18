package com.backstage.xduedu.service;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.zip.*;

public class ZipFolder {
    public static void main(String[] args) {
        String sourceFolder = "/path/to/sourceFolder"; // 源文件夹路径，您想压缩的文件夹
        String zipFile = "/path/to/destinationZip.zip"; // 目的ZIP文件路径

        zipFolder(Paths.get(sourceFolder), zipFile);
    }

    public static void zipFolder(Path sourceFolderPath, String zipPath) {
        try (ZipOutputStream zos = new ZipOutputStream(Files.newOutputStream(Paths.get(zipPath)))) {
            Files.walkFileTree(sourceFolderPath, new SimpleFileVisitor<Path>() {
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    zos.putNextEntry(new ZipEntry(sourceFolderPath.relativize(file).toString()));
                    Files.copy(file, zos);
                    zos.closeEntry();
                    return FileVisitResult.CONTINUE;
                }

                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    zos.putNextEntry(new ZipEntry(sourceFolderPath.relativize(dir).toString() + "/"));
                    zos.closeEntry();
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException ex) {
            System.err.println("I/O Error: " + ex);
        }
    }
}
