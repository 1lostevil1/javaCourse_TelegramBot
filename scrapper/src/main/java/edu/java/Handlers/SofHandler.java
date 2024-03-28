package edu.java.Handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.java.StackOverflow.StackOveflowData;
import edu.java.StackOverflow.StackOverflow;
import edu.java.clients.StackOverflowClient;
import io.swagger.v3.core.util.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SofHandler implements Handler<StackOverflow> {

    @Autowired
    private StackOverflowClient stackOverflowClient;

    @Override
    public String getData(StackOverflow dto) {
        try {
            return Json.mapper().writeValueAsString(
                new StackOveflowData(
                    dto.items().getFirst().isAnswered()
                )
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public StackOverflow getInfo(String url) {
        String[] splitUrl = url.split("/");
        return stackOverflowClient.getStackOverflow(splitUrl[splitUrl.length - 2]);
    }
}
