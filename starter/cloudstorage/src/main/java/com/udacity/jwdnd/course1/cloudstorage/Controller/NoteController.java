package com.udacity.jwdnd.course1.cloudstorage.Controller;

import com.udacity.jwdnd.course1.cloudstorage.Model.Note;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static com.udacity.jwdnd.course1.cloudstorage.constant.ConstantMsgs.*;
@Controller
public class NoteController {
    private NoteService noteService;
    private UserService userService;

    public NoteController(NoteService noteService, UserService userService) {
        this.noteService = noteService;
        this.userService = userService;
    }

    private Logger logger = LoggerFactory.getLogger(NoteService.class);

    /**
     * handle note addition
     */
    @PostMapping("/home/addNote")
    public String addNewNote(@ModelAttribute Note note, Authentication authentication, RedirectAttributes redirectAttributes) {

        logger.error("noteController, add a note");
        String note_err = null;
        String note_ok = null;
        Integer noteId = null;
        try {
            Integer userId = userService.getUser(authentication.getName()).getUserId();
            note.setUserId(userId);

            noteId = note.getNoteId();

            logger.error("noteId"+note.getNoteId());
            if(noteId==null) {
                // means new note
                Integer rowAdded = noteService.insertNote(note);
                logger.error("note row added="+rowAdded.toString());

                if (rowAdded < 0) {
                    note_err = NOTE_ERR_CREATION_FAILURE;
                    logger.error("insert note row < 0");
                }
                else {
                    note_ok = NOTE_NEW_SUCCESS;
                    // the latest created note
                    noteId=noteService.getLastNoteId();
                }
            } else {
                // edit note
                logger.error("updating note");
                Integer rowUpdated = noteService.updateNote(note);
                if (rowUpdated < 0) {
                    note_err=NOTE_ERR_UPDATE_FAILURE;
                }
                else {
                    note_ok=NOTE_EDIT_SUCCESS;
                }
            }
        } catch (Exception a) {
            logger.error("note exception");
            note_err=NOTE_ERR_INVALIDSESSION;
            logger.error(a.toString());
        }

        if(note_err==null) {
            redirectAttributes.addAttribute("opNoteOk", true);
            redirectAttributes.addAttribute("opNoteMsg", note_ok+"-ID:"+noteId.toString());
        }
        else {
            redirectAttributes.addAttribute("opNoteNotOk", true);
            redirectAttributes.addAttribute("opNoteMsg", note_err+"-ID:"+noteId.toString());
        }
        return "redirect:/home";
    }

    @GetMapping("home/note/delete/{noteId}")
    public String deleteNote(@PathVariable("noteId") Integer noteId, RedirectAttributes redirectAttributes) {
        String note_err=null;
        String note_ok=null;
        int rowDeleted=noteService.deleteNote(noteId);
        if(rowDeleted < 0) {
            note_err=NOTE_DELETE_ERR;
        }
        else {
            note_ok=NOTE_DELETE_SUCCESS;
        }
        if (note_err==null) {
            redirectAttributes.addAttribute("opNoteOk", true);
            redirectAttributes.addAttribute("opNoteMsg", note_ok+"-ID:"+noteId.toString());
        }
        else {
            redirectAttributes.addAttribute("opNoteNotOk", true);
            redirectAttributes.addAttribute("opNoteMsg", note_err+"-ID:"+noteId.toString());
        }
        return "redirect:/home";
    }
}










