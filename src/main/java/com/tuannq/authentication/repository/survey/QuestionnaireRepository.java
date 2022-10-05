package com.tuannq.authentication.repository.survey;

import com.tuannq.authentication.entity.Users;
import com.tuannq.authentication.entity.survey.Questionnaire;
import com.tuannq.authentication.model.request.SurveySearchForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

//@RepositoryRestResource(collectionResourceRel = "questionnaire", path = "questionnaire")
public interface QuestionnaireRepository extends JpaRepository<Questionnaire, Long> {

    Questionnaire findById(long id);

    @Query(value = "select x1 from Questionnaire x1 " +
            "where x1.createdBy.id = :#{#createdBy.id} " +
            "and (:#{#form.id} is null or :#{#form.id} = '' or concat(x1.id, '') = :#{#form.id} ) " +
            "and (:#{#form.surveyName} is null or :#{#form.surveyName} = '' or lower(coalesce(x1.name, '') ) like lower(concat('%',:#{#form.surveyName},'%'))) "
    )
    Page<Questionnaire> findByCreatedBy(Users createdBy, SurveySearchForm form, Pageable pageable);

    List<Questionnaire> findAllByOrderByIdAsc();
}
