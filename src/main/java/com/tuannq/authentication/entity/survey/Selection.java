package com.tuannq.authentication.entity.survey;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Selection Question.
 */
@Entity
public class Selection extends Question{
    @ElementCollection
    @Column(name = "options")
    private List<String> options = new ArrayList<>();

    @ElementCollection
    @Column(name = "answers")
    private List<String> answers = new ArrayList<>();

    /**
     * Instantiates a new Selection question.
     */
    public Selection() {
        super();
    }

    /**
     * Instantiates a new Selection question.
     *
     * @param question the question
     * @param options  the options
     */
    public Selection(String question, List<String> options){
        super(question);
        this.options = options;
    }

    /**
     * Gets options.
     *
     * @return the options
     */
    public List<String> getOptions() {
        return options;
    }

    /**
     * Sets options.
     *
     * @param options the options
     */
    public void setOptions(List<String> options) {
        this.options = options;
    }

    /**
     * Gets answers.
     *
     * @return the answers
     */
    public List<String> getAnswers() {
        return answers;
    }

    /**
     * Sets answers.
     *
     * @param answers the answers
     */
    public void setAnswers(List<String> answers) {
        this.answers = answers;
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
