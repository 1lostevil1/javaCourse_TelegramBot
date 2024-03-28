package edu.java.Request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AddLinkRequest(@JsonProperty("link") String link) {
}
