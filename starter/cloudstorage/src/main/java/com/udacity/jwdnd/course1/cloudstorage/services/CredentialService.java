package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.Controller.HomeController;
import com.udacity.jwdnd.course1.cloudstorage.Model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

import org.slf4j.Logger;

@Service
public class CredentialService {
    private CredentialMapper credentialMapper;
    private UserService userService;
    private EncryptionService encryptionService;

    private Logger logger = LoggerFactory.getLogger(CredentialService.class);

    public CredentialService(CredentialMapper credentialMapper, UserService userService, EncryptionService encryptionService) {
        this.credentialMapper = credentialMapper;
        this.userService = userService;
        this.encryptionService = encryptionService;
    }

    public List<Credential> getCredentials(Integer userId) {
        return credentialMapper.credentialList(userId);
    }

    public void encryptPassword(Credential credential) {
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);

        credential.setKey(encodedKey);

        String encodedPassword = encryptionService.encryptValue(credential.getPassword(), encodedKey);

        credential.setPassword(encodedPassword);
        return;
    }

    public Integer createCredential(Credential credential) {
        logger.error("now ready to insert the credential in service " + credential.getUrl() + " " + credential.getUsername()+" " + credential.getPassword());
        return credentialMapper.insertCredential(credential);
    }

    public Integer getLastCredentialId() {
        return credentialMapper.getLastCredentialId();
    }

    public void updateCredentialWithKey(Credential credential) {
        credential.setKey(credentialMapper.getKey(credential.getCredentialId()));
    }

    public Integer updateCredential(Credential credential) {
        return credentialMapper.updateCredential(credential);
    }

    public Integer deleteCredential(Integer credentialId) {
        return credentialMapper.deleteCredential(credentialId);
    }

    public String getKeyById(Integer id) {return credentialMapper.getKey(id);}
}
