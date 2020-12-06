package com.company;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.concurrent.Executors;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        boolean isWindows = System.getProperty("os.name")
                .toLowerCase().startsWith("windows");
        ProcessBuilder builder = new ProcessBuilder();
        if (isWindows) {
            builder.command("cmd.exe", "/c", "keytool -genkey -alias mydomain -keyalg RSA -keystore E:\\ideaprojects\\untitled8\\a\\keystore.jks -keysize 2048");
        } else {
            builder.command("sh", "-c", "keytool -genkey -alias mydomain -keyalg RSA -keystore E:\\ideaprojects\\untitled8\\a\\keystore.jks -keysize 2048");
        }
        builder.directory(new File(System.getProperty("user.home")));
        Process process = builder.start();
        final PrintWriter writer = new PrintWriter(
                new OutputStreamWriter(process.getOutputStream())
        );
        writer.print("123456\n"); // These and other writer.print(...) statements
        writer.print("123456\n"); // write answers to questions of keytool
        writer.print("software developers\n");
        writer.print("San Francisco\n");
        writer.print("California\n");
        writer.print("US\n");
        writer.print("yes\n");
        writer.print("yes\n");
        writer.print("yes\n");
        writer.print("123456\n"); // These and other writer.print(...) statements
        writer.print("123456\n");
        writer.close();
        StreamGobbler streamGobbler =
                new StreamGobbler(process.getInputStream(), System.out::println);
        Executors.newSingleThreadExecutor().submit(streamGobbler);

    }
}
