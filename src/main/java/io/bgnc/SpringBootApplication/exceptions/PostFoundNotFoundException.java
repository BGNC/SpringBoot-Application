package io.bgnc.SpringBootApplication.exceptions;

public class PostFoundNotFoundException extends Throwable {
    public PostFoundNotFoundException(String s) {
        super(s);
    }
}
