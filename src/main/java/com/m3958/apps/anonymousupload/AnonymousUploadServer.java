package com.m3958.apps.anonymousupload;

/*
 * 
 * @author <a href="http://tfox.org">Tim Fox</a>
 */

import org.vertx.java.core.Handler;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.http.RouteMatcher;
import org.vertx.mods.web.WebServerBase;


public class AnonymousUploadServer extends WebServerBase {

  @Override
  protected RouteMatcher routeMatcher() {
    RouteMatcher matcher = new RouteMatcher();
    matcher.get("/abc", getFileHandler());
    matcher.postWithRegEx("/*", postFileHandler());
    matcher.noMatch(staticHandler());
    return matcher;
  }
  
  
  private Handler<HttpServerRequest> getFileHandler(){
    return new GetFileHandler();
  }
  
  private Handler<HttpServerRequest> postFileHandler(){
    return new PostFileHandler();
  }
}
