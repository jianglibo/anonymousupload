package com.m3958.apps.anonymousupload;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.vertx.java.core.Handler;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.logging.Logger;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.m3958.apps.anonymousupload.util.Utils;

public class WebFileHandler implements Handler<HttpServerRequest> {

  private Logger logger;

  private String webRootPrefix;

  private String indexPage;


  private static String[] ctary = new String[] {"text/javascript; charset=UTF-8",
      "text/css; charset=UTF-8", "text/html; charset=UTF-8"};

  public WebFileHandler(Vertx vertx, Logger logger, String webRootPrefix, String indexPage) {
    this.webRootPrefix = webRootPrefix;
    this.indexPage = indexPage;
    this.logger = logger;
  }

  /**
   * text/javascript text/css text/html; charset=GBK
   */

  @Override
  public void handle(HttpServerRequest req) {
    Path webRoot;
    if(java.nio.file.Files.exists(Paths.get("mods"))){
      webRoot = Paths.get("mods").resolve(System.getProperty("vertx.modulename")).resolve(webRootPrefix);
    } else if(java.nio.file.Files.exists(Paths.get("target/mods"))){
      webRoot = Paths.get("target/mods").resolve(System.getProperty("vertx.modulename")).resolve(webRootPrefix);
    }else {
      webRoot = Paths.get(webRootPrefix);
    }
    
//    if(cwdPath.endsWith("anonymousupload/web")){
//      cwdPath  = cwdPath.getParent().resolve("mods").resolve(System.getProperty("vertx.modulename")).resolve("web");
//    }
    String uri = req.path().substring(1);
    if (uri.isEmpty()){
      uri = indexPage;
    }
    String ext = Utils.getFileExtWithDot(uri);
    if (".js".equals(ext) || ".css".equals(ext) || ".html".equals(ext)) {
      try {
        String c = Files.toString(webRoot.resolve(uri).toFile(), Charsets.UTF_8);
        int i = 0;
        if(".css".equals(ext)){
          i = 1;
        }else {
          i = 2;
        }
        req.response().headers().set("Content-Type", ctary[i]);
        req.response().end(c, Charsets.UTF_8.toString());
      } catch (IOException e) {
        req.response().end("File Not Found!");
      }
    } else {
      req.response().sendFile(webRoot.resolve(uri).toString());
    }
  }

}
