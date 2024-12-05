package com.graphql.learn.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BaseEntityDTO {
    @JsonIgnore
    private String id;
    private String guid;
    private Boolean isActive ;
    private String createdOn ;
    private String lastUpdatedOn ;
    private String createdBy ;
    private String lastUpdatedBy ;
    private String operation;
}
