package com.semtleWebGroup.youtubeclone.domain.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.lang.Nullable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotEmpty;

@Getter
@ToString(of = {"value"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class Email {

    @javax.validation.constraints.Email
    @Column(name = "email", length = 50)
    @NotEmpty
    private String value;

    private Email(String value) {
        this.value = value;
    }

    public static Email of(String value){
        return new Email(value);
    }

    @Nullable
    public String getHost() {
        final int index = value.indexOf("@");
        return index == -1 ? null : value.substring(index + 1);
    }

    @Nullable
    public String getId(){
        final int index = value.indexOf("@");
        return index == -1 ? null : value.substring(0,index);
    }
}
