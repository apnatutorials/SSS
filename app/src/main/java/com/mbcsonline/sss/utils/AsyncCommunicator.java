package com.mbcsonline.sss.utils;

import android.content.Context;

public interface AsyncCommunicator {
    public void onProgressUpdate(String progress);
 
    public void onProcessResult(String result);
 
    public String getStringById(int id);

    public Context getContext();

    public void cancelTask();
}