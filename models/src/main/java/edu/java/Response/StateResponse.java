package edu.java.Response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record StateResponse(@JsonProperty("id") Long id,
                            @JsonProperty("state") String state) {
}
