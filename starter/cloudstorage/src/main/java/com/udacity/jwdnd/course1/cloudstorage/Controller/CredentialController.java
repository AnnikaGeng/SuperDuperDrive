package com.udacity.jwdnd.course1.cloudstorage.Controller;

import com.udacity.jwdnd.course1.cloudstorage.Model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.Model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.AuthenticationService;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import static com.udacity.jwdnd.course1.cloudstorage.constant.ConstantMsgs.*;

@Controller
public class CredentialController {
    private CredentialService credentialService;
    private AuthenticationService authenticationService;
    private UserService userService;

    private Logger logger = LoggerFactory.getLogger(CredentialController.class);

    public CredentialController(CredentialService credentialService, AuthenticationService authenticationService, UserService userService) {
        this.credentialService = credentialService;
        this.authenticationService = authenticationService;
        this.userService = userService;
    }

    @PostMapping("home/credentials")
    public String createCredential(@ModelAttribute Credential credential, RedirectAttributes redirectAttributes) {
        logger.error("at credential controller");
        String credential_err=null;
        String credential_ok=null;
        String username = authenticationService.getUserName();
        Integer credentialId = credential.getCredentialId();

        if (username == null) {
            credential_err=CREDENTIAL_INVALIDSESSION_ERR;
        }

        if(credential_err==null) {
            User user = userService.getUser(username);
            if (user != null) {
                credential.setUserId(user.getUserId());
            }
            else {
                credential_err=CREDENTIAL_INVALIDSESSION_ERR;
            }
        }

        if(credential_err==null) {
            if (credentialId==null) {
                logger.error("insert new credential");
                credentialService.encryptPassword(credential);
                int rowAdded = credentialService.createCredential(credential);
                if (rowAdded < 0) {
                    logger.error("insert failed");
                    credential_err=CREDENTIAL_CREATE_ERR;
                } else {
                    credentialId=credentialService.getLastCredentialId();
                    credential_ok=CREDENTIAL_CREATE_SUCCESS;
                }
            }
            else {
                credentialService.updateCredentialWithKey(credential);
                credentialService.encryptPassword(credential);

                int rowAdded = credentialService.updateCredential(credential);
                if (rowAdded < 0) {
                    logger.error("insert failed");
                    credential_err=CREDENTIAL_CREATE_ERR;
                } else {
                    credentialId=credentialService.getLastCredentialId();
                    credential_ok=CREDENTIAL_CREATE_SUCCESS;
                }

            }
        }
        if(credential_err==null) {
            redirectAttributes.addAttribute("opCredOk", true);
            redirectAttributes.addAttribute("opCredMsg", credential_ok+"- ID:"+credentialId.toString());
        } else {
            redirectAttributes.addAttribute("opCredNotOk", true);
            redirectAttributes.addAttribute("opCredMsg", credential_err+"- ID:"+credentialId.toString());
        }
        return "redirect:/home";
    }

    @GetMapping("home/credentials/delete/{credentialId}")
    public String deleteCredential(@PathVariable("credentialId") Integer credentialId, RedirectAttributes redirectAttributes) {
        String credential_err=null;
        String credential_ok=null;

        int rowDeleted=credentialService.deleteCredential(credentialId);
        if(rowDeleted < 0) {
            credential_err=CREDENTIAL_DELETE_ERR;
        } else {
            credential_ok=CREDENTIAL_DELETE_SUCCESS;
        }

        if(credential_err==null) {
            redirectAttributes.addAttribute("opCredOk", true);
            redirectAttributes.addAttribute("opCredMsg", credential_ok+"- ID:"+credentialId.toString());
        } else {
            redirectAttributes.addAttribute("opCredNotOk", true);
            redirectAttributes.addAttribute("opCredMsg", credential_err+"- ID:"+credentialId.toString());
        }

        return "redirect:/home";
    }

}











