package org.project.social_account_business.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@MappedSuperclass
@ToString
@RequiredArgsConstructor
@Setter
@Getter
public abstract class Auditable<T> extends ReuseId {
    @CreatedBy
    @Column(name = "created_by", nullable = false, updatable = false)
    private T createdBy;

    @CreatedDate
    @Column(name = "created_date", nullable = false, updatable = false)
    private Date createdDate;

    @LastModifiedBy
    @Column(name = "modified_by", nullable = false)
    private T modifiedBy;

    @LastModifiedDate
    @Column(name = "modified_date", nullable = false)
    private Date modifiedDate;

    private int status = 1;
}
