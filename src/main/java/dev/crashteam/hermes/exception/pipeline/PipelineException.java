package dev.crashteam.hermes.exception.pipeline;

public class PipelineException extends RuntimeException {
    public PipelineException() {
    }

    public PipelineException(String message) {
        super(message);
    }
}
