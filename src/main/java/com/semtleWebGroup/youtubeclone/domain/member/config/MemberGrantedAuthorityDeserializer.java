package com.semtleWebGroup.youtubeclone.domain.member.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.semtleWebGroup.youtubeclone.domain.member.domain.MemberGrantedAuthority;

import java.io.IOException;

/**
 * package :  com.semtleWebGroup.youtubeclone.domain.member.config
 * fileName : MemberGrantedAuthorityDeserializer
 * author :  ShinYeaChan
 * date : 2023-05-12
 */
public class MemberGrantedAuthorityDeserializer extends StdDeserializer<MemberGrantedAuthority> {
    public MemberGrantedAuthorityDeserializer() {
        this(null);
    }
    
    public MemberGrantedAuthorityDeserializer(Class<?> vc) {
        super(vc);
    }
    
    @Override
    public MemberGrantedAuthority deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        JsonNode node = jp.getCodec().readTree(jp);
        String authority = node.get("authority").asText();
        return new MemberGrantedAuthority(authority);
    }
}
