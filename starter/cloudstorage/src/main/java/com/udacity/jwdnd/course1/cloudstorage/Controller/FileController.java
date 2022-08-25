package com.udacity.jwdnd.course1.cloudstorage.Controller;

import com.udacity.jwdnd.course1.cloudstorage.Model.File;
import com.udacity.jwdnd.course1.cloudstorage.Model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.io.InputStream;

import static com.udacity.jwdnd.course1.cloudstorage.constant.ConstantMsgs.*;
@Controller
public class FileController {
    private UserService userService;
    private FileService fileService;

    public FileController(UserService userService, FileService fileService) {
        this.userService = userService;
        this.fileService = fileService;
    }

    private Logger logger = LoggerFactory.getLogger(FileController.class);

    /*
    handle file upload
     */
    @PostMapping("/home/fileUpload")
    public String uploadFile(@RequestParam("fileUpload") MultipartFile multipartFile, RedirectAttributes redirectAttributes, Authentication authentication) {

        logger.error("fileuploadController, upload file");
        String file_err = null;
        final String file_ok = FILE_UPLOAD_SUCCESS;
        byte[] fb = null;
        String fileName = null;

        try {
            Integer userId = userService.getUser(authentication.getName()).getUserId();
            fileName = multipartFile.getOriginalFilename();
            String contentType = multipartFile.getContentType();
            Long fileSize = multipartFile.getSize();

            logger.error("filename=" + fileName);
            if (fileName.length() == 0) {
                file_err=FILE_NOT_SELECTED_ERR;
                Exception a = new Exception("No file was selected");
                throw(a);
            }

            if (multipartFile.getSize() > 5242880) {
                throw new MaxUploadSizeExceededException(multipartFile.getSize());
            }

            if (fileService.isFileDuplicate(userId, fileName)) {
                file_err=FILE_DUPLICATE_ERR;
                Exception a = new Exception("Duplicated file name");
                throw a;
            }

            InputStream fis = multipartFile.getInputStream();
            fb = new byte[fis.available()];
            fis.read(fb);
            fis.close();

            File file = new File(null, fileName, contentType, fileSize.toString(), userId, fb);
            fileService.insertFile(file);



        } catch (MaxUploadSizeExceededException a) {
            logger.error("file size limit exceed exception");
            logger.error(a.toString());
            a.printStackTrace();
            file_err=FILE_SIZE_LIMIT_EXCEED;
        }

        catch (Exception a) {
            if(file_err==null) file_err=FILE_UNKNOWN_ERR;
            logger.error(a.toString());
            a.printStackTrace();
        }

        if(file_err==null) {
            redirectAttributes.addAttribute("opok", true);
            redirectAttributes.addAttribute("opmsg", file_ok+":"+fileName);
        }
        else {
            redirectAttributes.addAttribute("opnotok", true);
            redirectAttributes.addAttribute("opmsg", file_err+":"+fileName);
        }

        return ("redirect:/home");
    }

    @GetMapping("home/files/download/{filename}")
    public ResponseEntity downloadFile(@PathVariable("filename") String fileName, Authentication authentication, RedirectAttributes redirectAttributes) {
        Integer userId = userService.getUser(authentication.getName()).getUserId();
        File dfile = null;
        try {
            dfile = fileService.getFileByNameForUser(userId, fileName);
        } catch (Exception a) {
            logger.error(a.toString());
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(dfile.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+fileName+"\"")
                .body(dfile.getFileData());
    }

    @GetMapping("home/file/delete/{filename}")
    public String deleteFile(@PathVariable("filename") String fileName, Authentication authentication, RedirectAttributes redirectAttributes) {
        String file_err = null;
        String file_ok = FILE_DELETE_SUCCESS;
        logger.error("File delete controller");
        try {
            int rowDeleted = fileService.deleteFile(fileName);
            if (rowDeleted < 0) file_err=FILE_DELETE_FAILURE;
        }catch (Exception a) {
            if (file_err==null) file_err=FILE_UNKNOWN_ERR;
            logger.error(a.toString());
        }

        if(file_err==null) {
            redirectAttributes.addAttribute("opok", true);
            redirectAttributes.addAttribute("opmsg", file_ok+":"+fileName);
        } else {
            redirectAttributes.addAttribute("opnotok", true);
            redirectAttributes.addAttribute("opmsg", file_err+":"+fileName);
        }
        return "redirect:/home";
    }

}















