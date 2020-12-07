package com.company;

import jdk.nashorn.internal.ir.LiteralNode;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.util.Base64;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, InvalidAlgorithmParameterException, NoSuchPaddingException {
	// write your code here



//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        String privateKey = read_private_file();
        privateKey = privateKey + "This is the private key file";
        System.out.println(privateKey);
        String password = getPass();
        System.out.println("This is the password " + password);
        String hashedPass = getMd5(password);
        System.out.println("This is the hashed password " +hashedPass);
        String encrypted = encrypt(privateKey, hashedPass);
        System.out.println("This is the encrypted text " +encrypted);
        System.out.println("This is the decrypted text " + decrypt(encrypted, hashedPass));

        File folder = new File("E:\\ideaprojects\\untitled9\\deneme-directory");
        File[] listOfFiles = folder.listFiles();
        String hashtype ="sha-256";
        assert listOfFiles != null;
        for (File file : listOfFiles) {
            if (file.isFile()) {
                if (hashtype.equals("md5")){
                    getMd5byte(file.getPath());
                }
                else{
                    getsha256byte(file.getPath());

                }
            }
        }
    }

    private static void getsha256byte(String filepath) {
        try {
            byte[] b = Files.readAllBytes(Paths.get(filepath));
            byte[] hash = MessageDigest.getInstance("SHA-256").digest(b);
            String actual = DatatypeConverter.printHexBinary(hash);
            System.out.println(filepath+" adlı dosyanın sha-256 hashı "+actual);
        }

        catch (NoSuchAlgorithmException e) {

            System.out.println("Exception thrown : " + e);
        }
        catch (NullPointerException e) {

            System.out.println("Exception thrown : " + e);
        }
        catch (FileNotFoundException e) {

            System.out.println("Exception thrown : " + e);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getMd5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");

            byte[] messageDigest = md.digest(input.getBytes());

            BigInteger no = new BigInteger(1, messageDigest);

            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        }

        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
    public static void getMd5byte(String filepath) {
        try {
            byte[] b = Files.readAllBytes(Paths.get(filepath));
            byte[] hash = MessageDigest.getInstance("MD5").digest(b);
            String actual = DatatypeConverter.printHexBinary(hash);
            System.out.println(filepath+" adlı dosyanın md5 hashı "+actual);
        }

        catch (NoSuchAlgorithmException e) {

            System.out.println("Exception thrown : " + e);
        }
        catch (NullPointerException e) {

            System.out.println("Exception thrown : " + e);
        }
        catch (FileNotFoundException e) {

            System.out.println("Exception thrown : " + e);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getPass() {
        Scanner myObj = new Scanner(System.in);  // Create a Scanner object
        System.out.println("Enter username");

        String password = myObj.nextLine();  // Read user input
        System.out.println("Username is: " + password);  // Output user input
        return password;
    }

    private static String read_private_file() throws IOException {
        File f = new File("E:\\ideaprojects\\untitled9\\src\\com\\company\\private_key.pem");
        FileInputStream fis = new FileInputStream(f);
        DataInputStream dis = new DataInputStream(fis);
        byte[] keyBytes = new byte[(int) f.length()];
        dis.readFully(keyBytes);
        dis.close();

        String key = new String(keyBytes);
        int a = key.indexOf("-");
        key = key.substring(a+"-----BEGIN PRIVATE KEY-----\n".length());
        key = key.replace("-----END PRIVATE KEY-----","").trim();
        return key;
    }

    public static String encrypt(String input, String key) {
        byte[] crypted = null;
        try {

            SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "AES");

            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, skey);
            crypted = cipher.doFinal(input.getBytes());
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        java.util.Base64.Encoder encoder = java.util.Base64.getEncoder();

        return new String(encoder.encodeToString(crypted));
    }

    public static String decrypt(String input, String key) {
        byte[] output = null;
        try {
            java.util.Base64.Decoder decoder = java.util.Base64.getDecoder();
            SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skey);
            output = cipher.doFinal(decoder.decode(input));
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return new String(output);
    }

    }

