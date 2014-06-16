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

  // Multiple matches can be specified for each HTTP verb. In the case there
  // are more than one matching patterns for a particular request, the first
  // matching one will be used.
  @Override
  protected RouteMatcher routeMatcher() {
    RouteMatcher matcher = new RouteMatcher();
    matcher.get("/", getWebFileHandler());
    matcher.get("/abc", getInfoHandler());
    matcher.getWithRegEx("/web/.+", getWebFileHandler());
    matcher.getWithRegEx("/.+", getAssetFileHandler());
    matcher.post("/", postFileHandler());
    System.out.println(System.getProperty("user.dir"));
    return matcher;
  }

  private Handler<HttpServerRequest> getInfoHandler() {
    return new GetInfoHandler(container);
  }

  private Handler<HttpServerRequest> postFileHandler() {
    return new PostFileHandler(vertx, container.logger(), config.getString("asset_root", "asset/"));
  }

  private Handler<HttpServerRequest> getWebFileHandler() {
    return new WebFileHandler(vertx, container.logger(), "web", "index.html");
  }

  private Handler<HttpServerRequest> getAssetFileHandler() {
    String assetRoot = config.getString("asset_root", "asset/");
    return new AssetFileHandler(vertx, container.logger(), assetRoot, "", false, false);
  }
}
