package com.jarmangani.notes.notes_api.tag;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class Tag {
    @Id
    private String tag;
}
