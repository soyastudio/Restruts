package com.albertsons.workshop.kafka;

public class KafkaPubSubTestCase {

    private String bod;
    private String name;
    private String produceTopic;
    private String consumeTopic;
    private String input;

    public String getBod() {
        return bod;
    }

    public void setBod(String bod) {
        this.bod = bod;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProduceTopic() {
        return produceTopic;
    }

    public void setProduceTopic(String produceTopic) {
        this.produceTopic = produceTopic;
    }

    public String getConsumeTopic() {
        return consumeTopic;
    }

    public void setConsumeTopic(String consumeTopic) {
        this.consumeTopic = consumeTopic;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }
}
