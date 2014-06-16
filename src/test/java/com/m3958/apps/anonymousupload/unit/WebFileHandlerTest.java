package com.m3958.apps.anonymousupload.unit;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.base.Charsets;

public class WebFileHandlerTest {
  
  @Test
  public void t1(){
    Assert.assertEquals("UTF-8", Charsets.UTF_8.toString());
  }
}
