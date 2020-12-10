package com.company;

import com.company.Commands;
import com.company.CreateReg;

import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Main {
    public static void main(String[] args) throws Exception {
        Commands commands = new Commands();
        String PriKeyPath = "";
        String PubKeyCertificatePath = "";
        byte [] regfilesignature=null;
        File RegFile=null;
        File LogFile=null;
        String WholeRegFile = null;
        FileWriter RegFileWriter=null;
        FileWriter LogFileWriter=null;

        if(args[1].equals("createCert")){
            PriKeyPath = args[3];
            PubKeyCertificatePath = args[5];
            commands.GenerateKey(PriKeyPath);
            commands.GenerateCert(PubKeyCertificatePath,PriKeyPath);
            commands.ImportKey(PriKeyPath);
            commands.CreateKeyPem(PriKeyPath);

            String privateKey = commands.ReadPrivateFile(PriKeyPath);
            privateKey = privateKey + "This is the private key file";
            String password = commands.GetPass();
            String hashedPass = commands.GetMD5(password);

            String encrypted = commands.EncryptAES(privateKey, hashedPass);
            commands.CreatePriKeyFile(PriKeyPath, encrypted);
            commands.CreatePubKeyCertificateFile(PubKeyCertificatePath);
            //System.out.println("This is the decrypted text " + com.company.Commands.DecryptAES(encrypted, hashedPass));
	    System.exit(0);
        }

        else if(args[1].equals("createReg")){
            String RegFilePath = args[3];
            String SourcePath = args[5];
            String LogFilePath = args[7];
            String HashMode = args[9];
            String PrivateKeyPath = args[11];

            RegFile = CreateReg.CreateFile(RegFilePath);
            LogFile = CreateReg.CreateFile(LogFilePath);
            RegFileWriter = new FileWriter(RegFile);
            LogFileWriter = new FileWriter(LogFile,true);

            String password = commands.GetPass();
            String hashedPass = commands.GetMD5(password);
            String privateKeyContent = CreateReg.ReadFile(PrivateKeyPath);
            String decryptedPrivateKey= CreateReg.DecryptAES(privateKeyContent, hashedPass, LogFileWriter);
            CreateReg.CheckPassword(decryptedPrivateKey, LogFileWriter);

            File folder = new File(SourcePath);
            File[] listOfFiles = folder.listFiles();
            CreateReg.CreateLogFile(listOfFiles, LogFileWriter, RegFilePath, SourcePath);
            WholeRegFile = CreateReg.CreateRegFile(listOfFiles, RegFileWriter, SourcePath, RegFilePath, HashMode, privateKeyContent);
            regfilesignature=CreateReg.Sign(WholeRegFile,args[9],args[11]);
            RegFileWriter.write(DatatypeConverter.printHexBinary(regfilesignature));
            RegFileWriter.close();
            System.exit(0);
        }
        else if(args[1].equals("check")){
            WholeRegFile = new String(Files.readAllBytes(Paths.get(args[3])), StandardCharsets.UTF_8);
            BufferedReader input = new BufferedReader(new FileReader(args[3]));
            String last="", line="";

            while ((line = input.readLine()) != null) {
                last = line;
            }

            boolean isvalid=CreateReg.verifySignature(WholeRegFile.substring(0,WholeRegFile.indexOf(last)),hexStringToByteArray(last.trim()),args[11],args[9]);
            if(isvalid){

                File folder = new File(args[5]);
                File[] listOfFiles = folder.listFiles();
                CreateReg.checkIntegrityOfDirectory(listOfFiles,args[5],args[9],new String(Files.readAllBytes(Paths.get(args[3])), StandardCharsets.UTF_8),new FileWriter(args[7],true));

            }
            else{
                String s = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss").format(LocalDateTime.now());
                LogFileWriter = new FileWriter(args[7],true);
                LogFileWriter.write(s+" Registry file verification failed!");
		LogFileWriter.close();
                System.exit(0);
            }
        }

    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }
}
