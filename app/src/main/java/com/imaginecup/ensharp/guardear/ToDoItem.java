package com.imaginecup.ensharp.guardear;

import android.util.Log;

/**
 * Represents an item in a ToDo list
 */
public class ToDoItem {

    /**
     * Item text
     */
    @com.google.gson.annotations.SerializedName("password")
    private String mPassword; // 비밀번호

    @com.google.gson.annotations.SerializedName("name")
    private String mName; // 닉네임

    @com.google.gson.annotations.SerializedName("id")
    private String mId;   // 아이디

    @com.google.gson.annotations.SerializedName("age")
    private String mAge;  // 나이
    @com.google.gson.annotations.SerializedName("sex")
    private String mSex;  // 성별

    /**
     * Indicates if the item is completed
     */
    @com.google.gson.annotations.SerializedName("complete")
    private boolean mComplete;

    /**
     * ToDoItem constructor
     */

    public ToDoItem() {
        Log.d("태그", "TodoItem()");
    }

    //@Override
    public String toString() {
        //return getText();
        Log.d("태그", "toString()");

        return getId() + "/" + getPassword() + "/" + getName() + "/" + getAge() + "/" + getSex(); // 임시 변환
    }


    /**
     * Initializes a new ToDoItem
     *
     * @param password
     *            The item textf
     * @param id
     *            The item id
     * @param name
     */
    public ToDoItem(String password, String id, String name, String age, String sex) {
        this.setPassword(password);
        this.setId(id);
        this.setName(name);
        this.setAge(age);
        this.setSex(sex);
        Log.d("태그", "this.반환값");
    }

    /**
     * Returns the item text
     */
    public String getPassword() {
        return mPassword;
    }

    /**
     * Sets the item text
     *
     * @param password
     *            text to set
     */
    public final void setPassword(String password) {
        mPassword = password;
    }

    /**
     * Returns the item id
     */
    public String getId() {
        return mId;
    }

    /**
     * Sets the item id
     *
     * @param id
     *            id to set
     */
    public final void setId(String id) {
        mId = id;
    }

    public String getName() {
        Log.d("태그", "getName() 실행 ");
        return mName;}
    public final void setName(String name) {
        Log.d("태그", "setName 실행");
        mName = name;}

    public String getAge() {return mAge;}
    public final void setAge(String age) {mAge = age;}

    public String getSex() {return mSex;}
    public final void setSex(String sex) {mSex = sex;}


    /**
     * Indicates if the item is marked as completed
     */
    public boolean isComplete() {
        return mComplete;
    }

    /**
     * Marks the item as completed or incompleted
     */
    public void setComplete(boolean complete) {
        mComplete = complete;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof ToDoItem && ((ToDoItem) o).mId == mId;
    }
}