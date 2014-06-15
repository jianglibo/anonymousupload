package com.m3958.apps.anonymousupload.util;

public class Utils {

  public static String getFileExtWithDot(String fn){
    int idx = fn.lastIndexOf('.');
    if(idx == -1){
      return "";
    }else{
      return fn.substring(idx);
    }
  }
}
