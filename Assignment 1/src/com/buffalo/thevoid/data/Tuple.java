package com.buffalo.thevoid.data;

import lombok.Data;

public @Data class Tuple<T1, T2>
{
    private T1 item1;
    private T2 item2;

    /**
     * This is a tuple that has two items.
     * @param item1
     * @param item2
     */
    public Tuple(T1 item1, T2 item2)
    {
        this.item1 = item1;
        this.item2 = item2;
    }
}