package com.project.android.helper.remotemanagement;

import android.os.AsyncTask;

import java.util.concurrent.ExecutionException;

/**
 * Created by ManishaKedia on 04/07/2015.
 */
public class AndroidTaskExecutor extends AsyncTask<Void, String, Object>{

    private UserListener userListener;
    private TaskListener taskListener;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        userListener.onPreExecute();
    }

    @Override
    protected Object doInBackground(Void... params) {
        return taskListener.execute();
    }

    @Override
    protected void onPostExecute(Object result) {
        super.onPostExecute(result);
        userListener.onPostExecute(result);
    }

    public Object getResult(TaskListener taskListener) throws InterruptedException, ExecutionException {
        this.userListener = new UserListener() {
            @Override
            public void onPreExecute() {
                //Do nothing
            }

            @Override
            public void onPostExecute(Object result) {
                //Do nothing
            }
        };
        this.taskListener = taskListener;
        return this.execute().get();
    }

    public void execute(UserListener userListener, TaskListener taskListener) {
        this.userListener = userListener;
        this.taskListener = taskListener;
        this.execute();
    }

}
