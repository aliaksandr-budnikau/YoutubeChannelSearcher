package ru.itchannel.ycsearcher.dao.exception;

public class FastException extends Exception {
    public FastException(String message) {
        super(message, null, true, false);
    }
}
