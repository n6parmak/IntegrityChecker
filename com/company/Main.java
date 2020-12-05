package com.company;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executors;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        boolean isWindows = System.getProperty("os.name")
                .toLowerCase().startsWith("windows");
        ProcessBuilder builder = new ProcessBuilder();
        if (isWindows) {
            builder.command("cmd.exe", "/c", "keytool -genkey -alias mydomain -keyalg RSA -keystore E:\\ideaprojects\\untitled8\\a\\keystore.jks -keysize 2048");
        } else {
            builder.command("sh", "-c", "ls");
        }
        builder.directory(new File(System.getProperty("user.home")));
        Process process = builder.start();
        StreamGobbler streamGobbler =
                new StreamGobbler(process.getInputStream(), System.out::println);
        Executors.newSingleThreadExecutor().submit(streamGobbler);
        int exitCode = process.waitFor();
        assert exitCode == 0;
    }
}
