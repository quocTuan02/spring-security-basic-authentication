package com.tuannq.authentication.entity.survey;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Open end questions.
 */
@Entity
public class OpenEnd extends Question{
    //open-ended questions

    @ElementCollection
    private List<String> answers = new ArrayList<>();

    /**
     * Instantiates a new Open end.
     */
    public OpenEnd(){
        super();
    }

    /**
     * Instantiates a new Open end.
     *
     * @param question the question\
     */
    public OpenEnd(String question) {
        super(question);
    }

    /**
     * Set answer.
     *
     * @param answer the answer
     */
    public void setAnswer(ArrayList<String> answer){
        this.answers = answer;
    }

    /**
     * Get answer list.
     *
     * @return the list
     */
    public List<String> getAnswers(){
        return answers;
    }

    /**
     * Add answer.
     *
     * @param answer the answer
     */
    public void addAnswer(String answer){
        this.answers.add(answer);
    }
}
