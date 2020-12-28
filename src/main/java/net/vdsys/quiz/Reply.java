package net.vdsys.quiz;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Reply {

    private String option;
    private boolean right;
    private int number;

    public Reply() {
        this.option = "";
        this.right = false;
        this.number = 0;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int n) {
        this.number = n;
    }


    public String getOption()
    {
        return option;
    }

    public void setOption(String q)
    {
        this.option = q;
    }

    public boolean getRight()
    {
        return right;
    }

    public void setRight(boolean r)
    {
        this.right = r;
    }


}