package edu.java.Response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ListLinksResponse(@JsonProperty("links") LinkResponse[] links,
                                @JsonProperty("size") int size) {
}
