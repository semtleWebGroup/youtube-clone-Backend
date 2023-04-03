package com.semtleWebGroup.youtubeclone.domain.channel.domain;
import com.semtleWebGroup.youtubeclone.domain.member.domain.Member;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@Entity
@Table(name = "channel")
public class Channel {
    @Id
    @Column(name = "channelid", nullable = false)
    private Integer id;

    @Size(max = 15)
    @NotNull
    @Column(name = "title", nullable = false, length = 15)
    private String title;

    @Size(max = 70)
    @Column(name = "description", length = 70)
    private String description;

    @Size(max = 60)
    @NotNull
    @Column(name = "profile_img", nullable = false, length = 60)
    private String profileImg;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_memberid", nullable = false)
    private Member memberMemberid;

}