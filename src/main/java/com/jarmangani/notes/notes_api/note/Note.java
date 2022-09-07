package com.jarmangani.notes.notes_api.note;

import java.sql.Time;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;

import org.hibernate.annotations.Type;

import com.jarmangani.notes.notes_api.tag.Tag;
import com.jarmangani.notes.notes_api.user.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Note {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;

    @ManyToMany
    private Set<Tag> tags;

    @OneToOne
    private User author;

    private Time creationTime;
    
    private String topic;

    @Type(type="text")
    private String content;
}
