package com.tuannq.authentication.entity.survey;

import com.tuannq.authentication.entity.core.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Questionnaire.
 */
@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Questionnaire extends BaseEntity {
    private String name;

    private boolean isClosed;

    @OneToMany(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
    @JoinTable(name = "questionnaire_question",
            joinColumns = {@JoinColumn(name="questionnaire_id")},
            inverseJoinColumns = {@JoinColumn (name="question_id")})
    private List<Question> questionList = new ArrayList<>();
    /**
     * Instantiates a new Questionnaire.
     *
     * @param name the name
     */
    public Questionnaire(String name){
        this.name = name;
    }

    /**
     * Instantiates a new Questionnaire.
     *
     * @param name         the name
     * @param questionList the question list
     */
    public Questionnaire(String name, List<Question> questionList){
        this.name = name;
        this.questionList = questionList;
    }

    /**
     * Add question.
     *
     * @param question the question
     */
    public void addQuestion(Question question){
        questionList.add(question);
    }
}
