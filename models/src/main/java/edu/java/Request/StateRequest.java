package edu.java.Request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record StateRequest(@JsonProperty("state") String state) {
}
