package com.semtleWebGroup.youtubeclone.domain.auth.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChannelCandidateResponse {

    private List<Entry> users;

    public ChannelCandidateResponse(List<Entry> users) {
        this.users = users;
    }

    @Getter
    public static class Entry {
        private final String name;
        private final Long key;

        public Entry(String name, Long key) {
            this.name = name;
            this.key = key;
        }
    }
}
