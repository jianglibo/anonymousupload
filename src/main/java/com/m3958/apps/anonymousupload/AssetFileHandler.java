package com.m3958.apps.anonymousupload;

import org.vertx.java.core.Vertx;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.logging.Logger;
import org.vertx.mods.web.StaticFileHandler;

public class AssetFileHandler extends StaticFileHandler {

  private Logger logger;

  public AssetFileHandler(Vertx vertx, Logger logger, String webRootPrefix, String indexPage,
      boolean gzipFiles, boolean caching) {
    super(vertx, webRootPrefix, indexPage, gzipFiles, caching);
    this.logger = logger;
  }

  @Override
  public void handle(HttpServerRequest req) {
    logger.info(req.absoluteURI());
    logger.info(System.getProperty("user.dir"));
    super.handle(req);
  }

}
