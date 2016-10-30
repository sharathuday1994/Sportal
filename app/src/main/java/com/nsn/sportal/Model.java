package com.nsn.sportal;

/**
 * Created by nihalpradeep on 10/05/16.
 */
public class Model {
    private String title;
    private String counter;

    private boolean isGroupHeader = false;

    public Model(String title) {
        this(title,null);
        isGroupHeader = true;
    }
    public Model(String title, String counter) {
        super();
        this.title = title;
        this.counter = counter;
    }


    public String getTitle() {
        return title;
    }

    public String getCounter() {
        return counter;
    }

    public boolean isGroupHeader() {
        return isGroupHeader;
    }

//gettters & setters...
}
