package com.tuannq.authentication.repository.survey;

import com.tuannq.authentication.entity.survey.Questionnaire;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

//@RepositoryRestResource(collectionResourceRel = "questionnaire", path = "questionnaire")
public interface QuestionnaireRepository  extends JpaRepository<Questionnaire, Long> {

    Questionnaire findById(long id);

    List<Questionnaire> findAllByOrderByIdAsc();
}
