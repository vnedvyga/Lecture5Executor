package ua.org.oa.nedvygav;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CopyFiles implements Runnable{
    private static ExecutorService executor;
    private File fileInstToCopy;
    private static File destinationFolder;
    private CopyFiles(File fileInstance){fileInstToCopy=fileInstance;}
    private static CountDownLatch customMonitor;
    private static final Logger logger = Logger.getLogger("CopyFiles logger");
    public static void copyFiles(File sourceFolder, File destinationFolder, int threads){
        long timeOfCopy = System.currentTimeMillis();
        executor = Executors.newFixedThreadPool(threads);
        CopyFiles.destinationFolder=destinationFolder;
        for (File fileInstance : sourceFolder.listFiles()){
            executor.execute(new CopyFiles(fileInstance));
        }
        customMonitor = new CountDownLatch(sourceFolder.list().length);
        try {
            customMonitor.await();
            executor.shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String result = new String("Copying of all files from "+sourceFolder.getAbsolutePath()+" to "+
        destinationFolder.getAbsolutePath()+" with "+threads+" threads takes "+(System.currentTimeMillis()-timeOfCopy)+" ms.");
        logger.log(Level.INFO, result);
    }
    @Override
    public void run() {
        try {
            String s = "File "+fileInstToCopy.getAbsolutePath()+" was copied with thread: "+Thread.currentThread().getName();
            Files.copy(fileInstToCopy.toPath(), new File(destinationFolder,fileInstToCopy.getName()).toPath(), StandardCopyOption.REPLACE_EXISTING);
            logger.log(Level.INFO, s);
            customMonitor.countDown();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
