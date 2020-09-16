package app;

import documentReader.*;
import documentReader.exceptions.UnsupportedFileException;
import org.apache.commons.io.FileUtils;
import profileDataExtractor.*;
import rules.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Application {
    private ProfileComposer composer;

    public Application() {
        composer = new ProfileComposer(getDataExtractors());
    }

    public void parseProfiles(File resumeFolder, Rule selectionRule) {
        String folderLocation = resumeFolder.getAbsolutePath();
        File fileToBeWritten = new File(folderLocation + File.separator + "_DONE" + getTimeStamp() + ".txt");
        StringBuilder allProfilesInfo = new StringBuilder();

        for (File file : resumeFolder.listFiles()) {
            try {
                String documentText = MainDocumentReader.getDocumentText(file, getDocumentReadersList());
                if (selectionRule.interpret(documentText)) {
                    allProfilesInfo.append(composer.getDataType()).append("\n\n").append(composer.getData(documentText)).append("\n\n");
                    FileUtils.copyFile(file, getDestinationForSelectedProfile(file, folderLocation));
                } else {
                    FileUtils.copyFile(file, getDestinationForRejectedProfile(file, folderLocation));
                }
            } catch (Exception e) {

            } catch (UnsupportedFileException e) {
                e.printStackTrace();
            }
        }
        writeToFile(fileToBeWritten, allProfilesInfo.toString());
        fileToBeWritten.renameTo(new File(folderLocation + File.separator + "ShortListedProfiles" + File.separator + fileToBeWritten.getName()));
    }

    private String getTimeStamp() {
        return new SimpleDateFormat("yyyy_MM_dd_HH-mm-ss").format(new Date());
    }

    private void writeToFile(File file, String data) {
        try {
            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fileWriter);
            bw.write(data);
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private File getDestinationForRejectedProfile(File file, String folderLocation) {
        return new File(folderLocation + File.separator + "RejectedProfiles" + File.separator + file.getName());
    }

    private File getDestinationForSelectedProfile(File file, String folderLocation) {
        return new File(folderLocation + File.separator + "ShortListedProfiles" + File.separator + file.getName());
    }

    private ArrayList<DataExtractor> getDataExtractors() {
        ArrayList<DataExtractor> dataExtractors = new ArrayList<>();
        dataExtractors.add(new FirstLineExtractor());
        //dataExtractors.add(new ExperienceExtractor());
        dataExtractors.add(new ContactExtractor());
        dataExtractors.add(new EmailIDExtractor());
        return dataExtractors;
    }

    private List<DocumentReader> getDocumentReadersList() {
        List<DocumentReader> readerList = new ArrayList<>();
        readerList.add(new DocxReader());
        readerList.add(new PDFReader());
        readerList.add(new DocReader());
        return readerList;
    }
}
