package com.buffalo.thevoid.data;

import lombok.Data;

public @Data class Tuple3<T1, T2, T3>
{
    private T1 item1;
    private T2 item2;
    private T3 item3;

    /**
     * This is a tuple that has three items
     * @param item1 What do you want me to say about this?
     * @param item2
     * @param item3
     */
    public Tuple3(T1 item1, T2 item2, T3 item3)
    {
        this.item1 = item1;
        this.item2 = item2;
        this.item3 = item3;
    }
}
