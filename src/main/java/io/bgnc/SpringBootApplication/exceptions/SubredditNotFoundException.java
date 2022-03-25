package io.bgnc.SpringBootApplication.exceptions;

public class SubredditNotFoundException extends Throwable {
    public SubredditNotFoundException(String s) {
        super(s);
    }
}
