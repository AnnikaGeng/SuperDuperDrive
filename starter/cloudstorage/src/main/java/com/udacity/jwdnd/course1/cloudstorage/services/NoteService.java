package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.Model.Note;
import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {

    private NoteMapper noteMapper;

    public NoteService(NoteMapper noteMapper) {
        this.noteMapper = noteMapper;
    }

    public List<Note> getNoteFromUser(Integer userId) {
        return noteMapper.noteList(userId);
    }

    public Integer insertNote(Note note) {
        return noteMapper.insertNote(note);
    }

    public Integer getLastNoteId() {
        return noteMapper.getLastNoteId();
    }

    public Integer updateNote(Note note) {
        return noteMapper.updateNote(note);
    }

    public Integer deleteNote(Integer noteId) {
        return noteMapper.deleteNote(noteId);
    }
}
