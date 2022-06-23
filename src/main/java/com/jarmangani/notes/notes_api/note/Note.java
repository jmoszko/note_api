package com.jarmangani.notes.notes_api.note;

import java.sql.Time;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.annotations.Type;

import com.jarmangani.notes.notes_api.tag.Tag;
import com.jarmangani.notes.notes_api.user.User;

import lombok.Data;

@Entity
@Data
public class Note {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;

    @OneToMany
    private Set<Tag> tag;

    @OneToOne
    private User author;

    private Time crationTime;
    
    private String topic;

    @Type(type="text")
    private String content;
}
