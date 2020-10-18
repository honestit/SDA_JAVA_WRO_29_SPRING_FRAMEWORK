package pl.honestit.spring.core.components;

import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;

@Component
public class FilePrinter implements Printer {
    @Override
    public void print(String message) {
        String userHome = System.getProperty("user.home");

        try (PrintWriter writer = new PrintWriter(
                new FileWriter(Paths.get(userHome, "spring.txt").toFile()), true)) {

            writer.println(message);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
