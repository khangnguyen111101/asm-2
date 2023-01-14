package com.example.asm2.engine;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class Producer {
    private static final Logger logger = LoggerFactory.getLogger(Producer.class);

    private static final String TOPIC_CUSTOMER = "SAVE_CUSTOMER";
    private static final String TOPIC_ITEM = "SAVE_ITEM";
    private static final String TOPIC_MO = "SAVE_MO";
    private static final String TOPIC_BOM = "SAVE_BOM";
    private static final String TOPIC_MPO = "SAVE_MPO";
    @Autowired
    private  KafkaTemplate<String, String> kafkaTemplate;

    public <T> void sendSaveRequest(T t, String topic) {
        String json = parseJson(t);
        this.kafkaTemplate.send(topic, json);
    }

    public <T> String parseJson(T t) {
        logger.info(String.format("#### -> Producing message -> %s", t));
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        Gson gson = gsonBuilder.create();
        return gson.toJson(t);
    }
}
