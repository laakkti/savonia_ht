package com.thl.gtwmob;

import android.util.Log;

public class TaskQueue {

    private int taskCnt;
    private int taskTotal;

    public TaskQueue(int cnt,int total){

       set(cnt,total);
    }

    public void set(int cnt,int total) {

        taskCnt=cnt;
        taskTotal=total;
    }

    public void clear(){

        taskCnt=0;
    }
    public void add(){

        taskCnt++;
    }

    public Boolean status(){

        Log.v("xxx",Integer.toString(taskCnt)+"=="+Integer.toString(taskTotal));
        return (taskCnt==taskTotal);
    }

}
