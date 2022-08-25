package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.Model.File;
import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileService {
    private FileMapper fileMapper;

    public FileService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    public Integer insertFile(File file) {return fileMapper.insertFile(file);}

    public Integer deleteFile(String fileName) {
        return fileMapper.deleteFile(fileName);
    }
    public List<File> getFileFromUser(Integer userId) {return fileMapper.selectFilesForUser(userId);}

    public boolean isFileDuplicate(Integer userId, String fileName) {
        List<String> fileNames = fileMapper.selectFileNamesForUser(userId);
        return fileNames.contains(fileName);
    }

    public File getFileByNameForUser(Integer userId, String fileName) {
        return fileMapper.selectFileFromName(fileName, userId);
    }
}
