package com.upiiz.examen_eohs_01;


public final class Message {
    public final String text;
    public final String time;
    public final boolean outgoing;
    public Message(String text, String time, boolean outgoing) {
        this.text = text;
        this.time = time;
        this.outgoing = outgoing;
    }
}
