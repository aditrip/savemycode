package com.adi.files;

import java.io.File;
import java.io.IOException;


public class RenameFile {

    public static void main(String[] args) throws IOException {
        File f = renameFile(0,"newsuffix",null);
        System.out.println("File renamed:" + f);

    }
    
    public static File renameFile(final long fileNum,
                           final String newSuffix,
                           final String subDir)
        throws IOException {

        final File oldDir = new File("/home/adi");
        final String oldName = "rename_test.oldsuffix";
        final File oldFile = new File(oldDir,oldName);

        final File newDir =
            (subDir != null) ? (new File(oldDir, subDir)) : oldDir;

        final String newName = "rename_test.newsuffix";

        String generation = "";
        int repeatNum = 0;

        while (true) {
            final File newFile = new File(newDir, newName + generation);

            if (newFile.exists()) {
                repeatNum++;
                generation = "." + repeatNum;
                continue;
            }



                final boolean success = oldFile.renameTo(newFile);
                return success ? newFile : null;
            
        }
    }

}
