package com.buffalo.thevoid.db;

import lombok.Getter;

public abstract class AbstractDBManager
{
    protected @Getter String url;
    protected @Getter String user;
    protected @Getter String password;

    public AbstractDBManager(String url, String user, String password)
    {
        this.url = url;
        this.user = user;
        this.password = password;
    }
}
