package com.endava.automation.atf.screenshot;

import com.endava.automation.atf.manager.FileReaderManager;
import lombok.extern.log4j.Log4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Log4j
public class CreateFolder {

    public CreateFolder() {
    }

    // creating the folder for holding pack of screen shots of one test run
    public static void createFolder(String folder) throws IOException {
        Files.createDirectories(Paths.get(FileReaderManager.getInstance()
                .getConfigFileReader().getScreenShotSaveDirectoryPath() + folder));
        log.info("Folder: " + folder + " was created.");
    }
}
