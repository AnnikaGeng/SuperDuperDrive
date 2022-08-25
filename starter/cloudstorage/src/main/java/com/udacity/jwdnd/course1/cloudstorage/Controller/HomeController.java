package com.udacity.jwdnd.course1.cloudstorage.Controller;

import com.udacity.jwdnd.course1.cloudstorage.Model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.Model.File;
import com.udacity.jwdnd.course1.cloudstorage.Model.Note;
import com.udacity.jwdnd.course1.cloudstorage.services.*;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.slf4j.Logger;

import java.util.List;

@Controller
@RequestMapping("/home")
public class HomeController {

    private UserService userService;
    private FileService fileService;

    private NoteService noteService;

    private CredentialService credentialService;

    private EncryptionService encryptionService;

    private Logger logger = LoggerFactory.getLogger(HomeController.class);

    public HomeController(UserService userService, FileService fileService, NoteService noteService, CredentialService credentialService, EncryptionService encryptionService) {
        this.userService = userService;
        this.fileService = fileService;
        this.noteService = noteService;
        this.credentialService = credentialService;
        this.encryptionService = encryptionService;
    }

    @GetMapping()
    public String homeView(Model model, Authentication authentication) {
        Integer userId = userService.getUser(authentication.getName()).getUserId();

        logger.error("at Home");
        // credential list
        List<Credential> credentialList = credentialService.getCredentials(userId);
        model.addAttribute("encryptionService", encryptionService);
        model.addAttribute("credentials", credentialList);

        //note list
        List<Note> noteList = noteService.getNoteFromUser(userId);
        model.addAttribute("noteList", noteList);

        // file list
        List<File> fileList = fileService.getFileFromUser(userId);
        model.addAttribute("fileList", fileList);

        return "home";
    }

}
