package com.collathon.jamukja.owner.BlackList;

public class BlackData {
    private String ID;
    private String count;

    public BlackData(String ID, String count) {
        this.ID = ID;
        this.count = count;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}
