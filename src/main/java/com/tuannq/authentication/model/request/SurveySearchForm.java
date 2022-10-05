package com.tuannq.authentication.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class SurveySearchForm extends SearchPage{
    private String id;
    private String surveyName;
}
