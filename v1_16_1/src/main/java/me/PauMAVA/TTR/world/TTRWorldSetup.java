package me.PauMAVA.TTR.world;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class TTRWorldSetup {


    public void deleteWorld(String cible) {
        Path dir = Paths.get(cible);
        try {
            Files.walkFileTree(dir, new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    System.out.println("Deleting file: " + file);
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    System.out.println("Deleting dir: " + dir);
                    if (exc == null) {
                        Files.delete(dir);
                        return FileVisitResult.CONTINUE;
                    } else {
                        throw exc;
                    }
                }

            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void copyWorld(String source, String cible) {
        Path sourceDir = Paths.get(source);
        Path destinationDir = Paths.get(cible);

        // Traverse the file tree and copy each file/directory.
        try {
            Files.walk(sourceDir).forEach(sourcePath -> {
                try {
                    Path targetPath = destinationDir.resolve(sourceDir.relativize(sourcePath));
                    System.out.printf("Copying %s to %s%n", sourcePath, targetPath);
                    Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException ex) {
                    System.out.format("I/O error: %s%n", ex);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
