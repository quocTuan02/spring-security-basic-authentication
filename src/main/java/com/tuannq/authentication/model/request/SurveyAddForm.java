package com.tuannq.authentication.model.request;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
public class SurveyAddForm {
    @NotBlank(message = "not-null")
    private String surveyName;
    private List<@Valid @NotBlank(message = "not-null") String> essayQuestions = new ArrayList<>();
    private List<@Valid @NotNull(message = "not-null") MultipleChoiceQuestion> multipleChoiceQuestions = new ArrayList<>();
    private Instant startTime;
    private Instant endTime;
    private boolean isPublic = false;

    @Data
    public static class MultipleChoiceQuestion {
        @NotBlank(message = "not-null")
        private String name;
        private List<@Valid @NotBlank(message = "not-null") String> answers;
    }
}
