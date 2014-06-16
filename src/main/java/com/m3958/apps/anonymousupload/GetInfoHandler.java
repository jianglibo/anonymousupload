package com.m3958.apps.anonymousupload;

import java.nio.file.Paths;
import java.util.Map;
import java.util.Map.Entry;

import org.vertx.java.core.Handler;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.platform.Container;

public class GetInfoHandler implements Handler<HttpServerRequest> {

  private Container container;


  public GetInfoHandler(Container container) {
    this.container = container;
  }

  @Override
  public void handle(HttpServerRequest request) {
    StringBuilder sb = new StringBuilder();
    for (Map.Entry<String, String> header : request.headers().entries()) {
      sb.append(header.getKey()).append(": ").append(header.getValue()).append("\n");
    }

    sb.append("absoluteUrl:").append(request.absoluteURI()).append("\n");
    sb.append("cwd:").append(Paths.get(".").toAbsolutePath().toString()).append("\n");
    sb.append("moddir:").append(System.getProperty("vertx.mods")).append("\n");



    for (Entry<String, String> entry : container.env().entrySet()) {
      sb.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
    }

    request.response().putHeader("content-type", "text/plain");
    request.response().end(sb.toString());
  }
}
