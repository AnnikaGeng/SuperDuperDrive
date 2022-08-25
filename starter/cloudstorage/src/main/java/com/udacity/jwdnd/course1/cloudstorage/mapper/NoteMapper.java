package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.Model.Note;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Mapper
public interface NoteMapper {
    @Insert("INSERT INTO NOTES(notetitle, notedescription, userid) VALUES(#{noteTitle}, #{noteDescription}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "noteId")
    Integer insertNote(Note note);

    @Select("SELECT * FROM NOTES WHERE notetitle=#{noteTitle} and userid=#{userId}")
    Note selectNoteFromName(String noteTitle, Integer userId);

    @Select("SELECT * FROM NOTES WHERE userid=#{userId}")
    List<Note> noteList(Integer userId);

    @Select("SELECT notetitle FROM NOTES WHERE userid=#{userId}")
    List<String> noteNameList(Integer userId);

    @Delete("DELETE FROM NOTES WHERE noteid=#{noteId}")
    Integer deleteNote(Integer noteId);

    @Select("SELECT max(noteid) from NOTES")
    Integer getLastNoteId();

    @Update("UPDATE NOTES SET notetitle=#{noteTitle}, notedescription=#{noteDescription}, userid=#{userId} WHERE noteid=#{noteId}")
    Integer updateNote(Note note);
}
