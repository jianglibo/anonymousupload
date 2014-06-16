package com.m3958.apps.anonymousupload;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.vertx.java.core.Vertx;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.logging.Logger;
import org.vertx.mods.web.StaticFileHandler;

public class AssetFileHandler extends StaticFileHandler {

  private Logger logger;

  private String assetRoot;

  public AssetFileHandler(Vertx vertx, Logger logger, String assetRoot, boolean gzipFiles,
      boolean caching) {
    super(vertx, assetRoot, gzipFiles, caching);
    this.logger = logger;
  }

  @Override
  public void handle(HttpServerRequest req) {
    Path p = Paths.get(assetRoot, req.path().substring(1));
    if (Files.exists(p)) {
      super.handle(req);
    } else {
      //redirect to other url.
    }
  }
}
