package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.Model.File;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FileMapper {

    @Insert("INSERT INTO FILES(filename,contenttype,filesize,userid,filedata) VALUES(#{fileName},#{contentType},#{fileSize},#{userId},#{fileData})")
    @Options(useGeneratedKeys = true,keyProperty = "key")
    Integer insertFile(File file);

    @Select("SELECT * FROM FILES WHERE filename = #{filename} and userid = #{userId}")
    File selectFileFromName(String filename, Integer userId);

    @Select("SELECT * FROM FILES WHERE userid = #{userId}")
    List<File> selectFilesForUser(Integer userId);

    @Select("SELECT filename FROM FILES WHERE userid = #{userId}")
    List<String> selectFileNamesForUser(Integer userId);

    @Delete("DELETE FROM FILES WHERE filename=#{fileName}")
    Integer deleteFile(String fileName);

}


















