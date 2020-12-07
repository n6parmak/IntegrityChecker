package com.company;
import com.company.StreamGobbler;

import java.io.*;
import java.security.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.concurrent.Executors;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException, GeneralSecurityException {
        boolean isWindows = System.getProperty("os.name")
                .toLowerCase().startsWith("windows");
        ProcessBuilder builder = new ProcessBuilder();
        if (isWindows) {
            builder.command("cmd.exe", "/c", "keytool -genkey -keyalg RSA -keystore E:\\ideaprojects\\untitled8\\a\\keystore.jks -keysize 2048");
        } else {
            builder.command("sh", "-c", "keytool -genkey -keyalg RSA -keystore /home/seyma/IdeaProjects/project_3/keystore.jks -keysize 2048");
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

        //////////////////////////////////////////////////////////////////////////////////////////////////////////////
        Thread.sleep(10000);

        ProcessBuilder builder2 = new ProcessBuilder();
        if (isWindows) {
            builder2.command("cmd.exe", "/c", "keytool -export -keystore E:\\ideaprojects\\untitled8\\a\\keystore.jks -rfc -file E:\\ideaprojects\\untitled8\\a\\public.crt");

        } else {
            builder2.command("sh", "-c", "keytool -export -keystore keystore.jks -rfc -file /home/seyma/IdeaProjects/project_3/public.crt");
        }
        builder.directory(new File(System.getProperty("user.home")));
        Process process2 = builder2.start();
        final PrintWriter writer2 = new PrintWriter(
                new OutputStreamWriter(process2.getOutputStream())
        );
        writer2.print("123456\n"); // These and other writer.print(...) statements
        writer2.print("123456\n");
        writer2.close();
        StreamGobbler streamGobbler2 =
                new StreamGobbler(process2.getInputStream(), System.out::println);
        Executors.newSingleThreadExecutor().submit(streamGobbler2);

        //////////////////////////////////////////////////////////////////////////////////////////////////////////////
        Thread.sleep(2000);

        ProcessBuilder builder3 = new ProcessBuilder();
        if (isWindows) {
            builder3.command("cmd.exe", "/c", "keytool -importkeystore -srckeystore E:\\ideaprojects\\untitled8\\a\\keystore.jks -srcstorepass 123456 -destkeystore E:\\ideaprojects\\untitled8\\a\\identity.p12 -deststoretype PKCS12 -deststorepass 123456 -destkeypass 123456");

        } else {

            builder3.command("sh", "-c", "keytool -importkeystore -srckeystore /home/seyma/IdeaProjects/project_3/keystore.jks -srcstorepass 123456 -destkeystore identity.p12 -deststoretype PKCS12 -deststorepass 123456 -destkeypass 123456");
        }
        builder.directory(new File(System.getProperty("user.home")));
        Process process3 = builder3.start();
        StreamGobbler streamGobbler3 =
                new StreamGobbler(process3.getInputStream(), System.out::println);
        Executors.newSingleThreadExecutor().submit(streamGobbler3);

        //////////////////////////////////////////////////////////////////////////////////////////////////////////////

        Thread.sleep(10000);

        ProcessBuilder builder4 = new ProcessBuilder();
        if (isWindows) {
            builder4.command("cmd.exe", "/c","openssl pkcs12 -in E:\\ideaprojects\\untitled8\\a\\identity.p12 -nodes -nocerts -out E:\\ideaprojects\\untitled8\\a\\private_key.pem");

        } else {
            builder4.command("sh", "-c", "openssl pkcs12 -in identity.p12 -nodes -nocerts -out private_key.pem");
        }
        builder.directory(new File(System.getProperty("user.home")));
        Process process4= builder4.start();
        final PrintWriter writer4 = new PrintWriter(
                new OutputStreamWriter(process4.getOutputStream())
        );
        writer4.print("123456\n"); // These and other writer.print(...) statements
        writer4.print("123456\n");
        writer4.close();
        StreamGobbler streamGobbler4 =
                new StreamGobbler(process4.getInputStream(), System.out::println);
        Executors.newSingleThreadExecutor().submit(streamGobbler4);
        Thread.sleep(100000);
        read_private_file();

    }

    private static void read_private_file() throws IOException {
        File f = new File("E:\\ideaprojects\\untitled8\\a\\private_key.pem");
        FileInputStream fis = new FileInputStream(f);
        DataInputStream dis = new DataInputStream(fis);
        byte[] keyBytes = new byte[(int) f.length()];
        dis.readFully(keyBytes);
        dis.close();

        String key = new String(keyBytes);
        int a = key.indexOf("-");
        key = key.substring(a+"-----BEGIN PRIVATE KEY-----\n".length());
        key = key.replace("-----END PRIVATE KEY-----","").trim();
        System.out.println(key);
        System.out.println(key);
    }

}
