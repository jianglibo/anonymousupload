package com.m3958.apps.anonymousupload;

import org.vertx.java.core.json.JsonObject;
import org.vertx.java.platform.Verticle;

import com.m3958.apps.anonymousupload.util.Utils;

public class AnonymousUpload extends Verticle {

  @Override
  public void start() {
    JsonObject config = container.config();
    if (config.toMap().isEmpty()) {
      String js = Utils.readResouce("/conf.json");
      config = new JsonObject(js);
    }
    container.deployVerticle("com.m3958.apps.anonymousupload.AnonymousUploadServer", config, 1);
  }
}
