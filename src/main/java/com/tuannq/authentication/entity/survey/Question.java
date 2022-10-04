package com.tuannq.authentication.entity.survey;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.List;

/**
 * abstract class for all questions
 */
@Entity
public abstract class Question {

    /**
     * The Id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected long id;

    /**
     * The Question.
     */
    protected String question;

    /**
     * Instantiates a new Question.
     */
    public Question() {
    }

    /**
     * Instantiates a new Question.
     *
     * @param question the question
     */
    public Question(String question) {
        this.question = question;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Gets question.
     *
     * @return the question
     */
    public String getQuestion() {
        return question;
    }

    /**
     * Sets question.
     *
     * @param question the question
     */
    public void setQuestion(String question) {
        this.question = question;
    }

    abstract public void addAnswer(String answer);

    abstract public List<String> getAnswers();
}
