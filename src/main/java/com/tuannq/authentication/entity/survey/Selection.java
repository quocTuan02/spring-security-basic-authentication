package com.tuannq.authentication.entity.survey;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Selection Question.
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Selection extends Question {
    @ElementCollection
    @Column(name = "options")
    private List<String> options = new ArrayList<>();

    @ElementCollection
    @Column(name = "answers")
    private List<String> answers = new ArrayList<>();

    public Selection(String question, List<String> options){
        super(question);
        this.options = options;
    }

    /**
     * Add answer to answers
     *
     * @param answer the new answer
     */
    public void addAnswer(String answer){
        this.answers.add(answer);
    }

    /**
     * Add option.
     *
     * @param option the option
     */
    public void addOption(String option){
        if(this.options.contains(option)) return;
        this.options.add(option);
    }
}
