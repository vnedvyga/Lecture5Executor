package ua.org.oa.nedvygav;


import java.io.*;

public class App{
    public static void main(String[] args) {
        CopyFiles.copyFiles(new File("sourceFolder"), new File("destFolder"), 4);
        CopyFiles.copyFiles(new File("sourceFolder"), new File("destFolder1"), 2);
        CopyFiles.copyFiles(new File("sourceFolder"), new File("destFolder2"), 1);
    }
}


