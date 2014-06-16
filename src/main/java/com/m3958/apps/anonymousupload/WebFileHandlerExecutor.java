package com.m3958.apps.anonymousupload;

import java.nio.file.Path;
import java.nio.file.Paths;

public class WebFileHandlerExecutor {
  
  private Path rootPath;
  
  public WebFileHandlerExecutor(Path rootPath,String uri){
    this.rootPath = rootPath;
  }

  public String prepareContent(){
    return null;
  }
}
