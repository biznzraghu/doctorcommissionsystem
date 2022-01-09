package org.nh.artha.web.rest;

import org.apache.commons.io.IOUtils;
import org.nh.artha.config.ApplicationProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URISyntaxException;

/**
 *Class for download file.
 */

@RestController
public class FileDownloadResource {

    private static final Logger log= LoggerFactory.getLogger(FileDownloadResource.class);
    @Autowired
    private ApplicationProperties applicationProperties;

    /**
     * This method will download given filename from given path.
     * @param pathReference - File path location.
     * @param fileName - file to be downloaded
     * @throws URISyntaxException
     * @throws IOException
     */
    @GetMapping("/download-file")
    public void downloadFile(@RequestParam("pathReference") String pathReference,
                             @RequestParam("fileName") String fileName,
                             HttpServletResponse response) throws URISyntaxException, IOException {

        log.debug("REST request to download file with PathReference:",pathReference);
        File file = new File(applicationProperties.getAthmaBucket().getStoragePath(pathReference) + fileName);
        writeToFile(response, file, fileName);
    }

    @GetMapping("/_document/download-file")
    public void downloadMedicalRecordFile(@RequestParam("pathReference") String pathReference,
                             @RequestParam("fileName") String fileName,
                             HttpServletResponse response) throws URISyntaxException, IOException {
        log.debug("REST request to download file with PathReference:",pathReference);
        File file = new File(pathReference.concat(fileName));
        writeToFile(response, file, fileName);
    }

    private void writeToFile(HttpServletResponse response, File file, String fileName) throws IOException {
        try (InputStream fileInputStream = new FileInputStream(file);
             OutputStream output = response.getOutputStream();) {
            response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");
            String headerValue = String.format("attachment; filename=\"%s\"",fileName);
            response.setHeader("Content-Disposition", headerValue);
            IOUtils.copyLarge(fileInputStream, output);
        }
    }

}
