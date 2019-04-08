package com.sam58.example.sprng_boot_download.Controllers;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Slf4j
@RestController
public class DownloadController {


    @Value("classpath:data/RAOS_Gate_User_ver_2.pdf")
    private Resource userManualResource_ru;

    @Autowired
    private ServletContext servletContext;


    @GetMapping("/download/Raos_gate_manual_rus.pdf")
    public void getReportTrmRu(HttpServletResponse response){

        try (InputStream is = userManualResource_ru.getInputStream()){
            IOUtils.copy(is, response.getOutputStream());
            response.flushBuffer();
        } catch (IOException e) {
            log.error("Can not find resorce for um.docx",e);
        }
    }

    @GetMapping("/download/Test1.zip")
    public void getReportTestAmp(HttpServletResponse response){

        try(InputStream is = userManualResource_ru.getInputStream();
            ZipOutputStream zipOut = new ZipOutputStream(response.getOutputStream());){
            addNewEntryToZip(is, zipOut,"test&.&&&.&&.pdf");
        }catch(Exception e){
            log.error("MAMA HJLYFZ");
        }

    }

    @GetMapping("/download/Test2")
    public void getReportTest2Amp(HttpServletResponse response){


        String fileName= "Ampersandy&&&.zip";


        //получил миме
        String mineType = this.servletContext.getMimeType(fileName);
        MediaType mediaType =MediaType.parseMediaType(mineType);

        // Content-Type
        response.setContentType(mediaType.getType());

        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,"attachment;filename="+fileName);


        try(InputStream is = userManualResource_ru.getInputStream()){
            File tempFile = File.createTempFile("22яч","ыыв");

            try(ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(tempFile))){
                addNewEntryToZip(is, zipOut,"test&.&&&.&&33333.&&&&&&&&pdf");

            }catch(Exception e){
                log.error("MAMA 444444444");
            }

            // Content-Length
            response.setContentLength((int) tempFile.length());
            try(BufferedInputStream inStream = new BufferedInputStream(new FileInputStream(tempFile));
                BufferedOutputStream outStream = new BufferedOutputStream(response.getOutputStream());){
                byte[] buffer = new byte[1024];
                int bytesRead = 0;
                while ((bytesRead = inStream.read(buffer)) != -1) {
                    outStream.write(buffer, 0, bytesRead);
                }

                outStream.flush();
            }
            tempFile.deleteOnExit();
        }catch(Exception e){
            log.error("MAMA HJLYFZ");
        }
    }

    /**
     * Добавляет в архив zipout пункт is под именеим entryName
     * @param is
     * @param zipOut
     * @param entryName
     * @throws IOException
     */
    private void addNewEntryToZip(InputStream is, ZipOutputStream zipOut,String entryName) throws IOException {
        ZipEntry zipEntry = new ZipEntry(entryName);
        zipOut.putNextEntry(zipEntry);
        byte[] bytes = new byte[1024];
        int length;
        while((length = is.read(bytes)) >= 0) {
            zipOut.write(bytes, 0, length);
        }
    }

}
