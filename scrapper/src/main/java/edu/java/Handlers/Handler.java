package edu.java.Handlers;

public interface Handler<T> {

    String getData(T dto);

    T getInfo(String url);
}
