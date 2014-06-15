package com.m3958.apps.anonymousupload;

import java.util.Map;

import org.vertx.java.core.Handler;
import org.vertx.java.core.http.HttpServerRequest;

public class GetFileHandler implements Handler<HttpServerRequest> {

  @Override
  public void handle(HttpServerRequest request) {
    StringBuilder sb = new StringBuilder();
    for (Map.Entry<String, String> header: request.headers().entries()) {
        sb.append(header.getKey()).append(": ").append(header.getValue()).append("\n");
    }
    
    sb.append("absoluteUrl:").append(request.absoluteURI()).append("\n");
    request.response().putHeader("content-type", "text/plain");
    request.response().end(sb.toString());
  }
}
