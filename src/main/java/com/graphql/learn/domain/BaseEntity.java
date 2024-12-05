package com.graphql.learn.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.index.Indexed;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BaseEntity {

    @Id
    private String id ;

    @Indexed(unique = true)
    private String guid;

    private Boolean isActive ;

    @CreatedDate
    private Long createdOn ;

    @LastModifiedDate
    private Long lastUpdatedOn ;

    @CreatedBy
    private String createdBy ;

    @LastModifiedBy
    private String lastUpdatedBy ;
}
