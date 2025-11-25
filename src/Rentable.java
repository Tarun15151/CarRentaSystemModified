package com.carrental;

// Interface demonstrates abstraction and defines rental contract
public interface Rentable {
    void rent() throws CarNotAvailableException;
    void returns();
}
