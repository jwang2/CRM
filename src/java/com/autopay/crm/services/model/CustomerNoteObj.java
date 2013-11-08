package com.autopay.crm.services.model;

import java.io.Serializable;

/**
 *
 * @author Judy
 */
public final class CustomerNoteObj implements Serializable{
    private Long id;
    private String note;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return "CustomerNote{" + "id=" + id + ", note=" + note + '}';
    }
    
}
