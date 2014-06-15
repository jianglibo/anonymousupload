package com.m3958.apps.anonymousupload.unit;


import org.apache.commons.lang.RandomStringUtils;
import org.junit.Assert;
import org.junit.Test;

import com.m3958.apps.anonymousupload.PostFileHandler;

public class CharsTest {

  @Test
  public void t1(){
    char[] chars = PostFileHandler.chars;
    StringBuilder sb = new StringBuilder();
    
    for(int i=0;i<chars.length;i++){
      sb.append(chars[i]);
    }
    Assert.assertEquals(PostFileHandler.availableChar, sb.toString());
    
    for(int i=0;i<10;i++){
      System.out.println(RandomStringUtils.random(7, chars));
    }
  }
}
