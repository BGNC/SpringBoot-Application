package io.bgnc.SpringBootApplication.model;

import io.bgnc.SpringBootApplication.exceptions.SpringBootApplicationException;

import java.util.Arrays;

public enum VoteType {
    UPVOTE(1), DOWNVOTE(-1),
    ;

    private int direction;

    VoteType(int direction) {
    }

    public static VoteType lookup(Integer direction) {
        return Arrays.stream(VoteType.values())
                .filter(value -> value.getDirection().equals(direction))
                .findAny()
                .orElseThrow(() -> new SpringBootApplicationException("Type did not find"));
    }

    public Integer getDirection() {
        return direction;
    }
}