package com.semtleWebGroup.youtubeclone.domain.channel.domain;

import com.semtleWebGroup.youtubeclone.domain.channel.domain.Channel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Entity
@Table(name = "subscribe")
public class Subscribe {
    @Id
    @Column(name = "subscribeid", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "subscriber", nullable = false)
    private Channel subscriber;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "channel", nullable = false)
    private Channel channel;

}