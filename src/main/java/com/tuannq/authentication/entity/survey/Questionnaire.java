package com.tuannq.authentication.entity.survey;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Questionnaire.
 */
@Entity
public class Questionnaire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    private boolean isClosed;

    @OneToMany(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
    @JoinTable(name = "questionnaire_question",
            joinColumns = {@JoinColumn(name="questionnaire_id")},
            inverseJoinColumns = {@JoinColumn (name="question_id")})
    private List<Question> questionList = new ArrayList<>();

    /**
     * Instantiates a new Questionnaire.
     */
    public Questionnaire(){
    }

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
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets question list.
     *
     * @return the question list
     */
    public List<Question> getQuestionList() {
        return questionList;
    }

    /**
     * Sets question list.
     *
     * @param questionList the question list
     */
    public void setQuestionList(ArrayList<Question> questionList) {
        this.questionList = questionList;
    }

    /**
     * Is closed boolean.
     *
     * @return the boolean
     */
    public boolean isClosed() {
        return isClosed;
    }

    /**
     * Sets closed.
     *
     * @param closed the closed
     */
    public void setClosed(boolean closed) {
        isClosed = closed;
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
