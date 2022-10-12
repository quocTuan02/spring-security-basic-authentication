package com.tuannq.authentication.model.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
public class DoSurvey {
    @Size(min = 1, message = "not-null")
    private List<@Valid @NotNull(message = "not-null") Answer> question;

    @Getter
    @Setter
    public static class Answer {
        @NotNull(message = "not-null")
        private long id;
        @NotBlank(message = "not-null")
        private String answer;
    }
}
