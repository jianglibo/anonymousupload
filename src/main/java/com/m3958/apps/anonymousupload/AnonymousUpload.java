package com.m3958.apps.anonymousupload;

import org.vertx.java.core.json.JsonObject;
import org.vertx.java.platform.Verticle;

public class AnonymousUpload extends Verticle {

  @Override
  public void start() {
    JsonObject config = container.config();
    container.deployVerticle("com.m3958.apps.anonymousupload.AnonymousUploadServer", config, 1);
  }
}
