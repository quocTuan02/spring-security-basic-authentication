package com.tuannq.authentication.model.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SearchPage {
    private Integer page = 1;
    private String order = "";
    private String direction = "";
}
