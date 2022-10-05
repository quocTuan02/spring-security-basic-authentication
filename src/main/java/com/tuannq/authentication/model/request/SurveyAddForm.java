package com.tuannq.authentication.model.request;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Data
public class SurveyAddForm {
    @NotBlank(message = "not-null")
    private String surveyName;

    @NotNull(message = "not-null")
    @Size(min = 1, message = "not-null")
    private List<@Valid @NotBlank(message = "not-null") String> essayQuestions = new ArrayList<>();
}
