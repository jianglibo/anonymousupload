package com.m3958.apps.anonymousupload;

import java.nio.file.Paths;

import org.apache.commons.lang.RandomStringUtils;
import org.vertx.java.core.Handler;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.VoidHandler;
import org.vertx.java.core.file.FileSystem;
import org.vertx.java.core.http.HttpServerFileUpload;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.http.HttpServerResponse;
import org.vertx.java.core.logging.Logger;

import com.m3958.apps.anonymousupload.util.Utils;

public class PostFileHandler implements Handler<HttpServerRequest> {

  public static String availableChar =
      "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789$@.-~";

  public static char[] chars = new char[67];

  static {
    for (int i = 0; i < 67; i++) {
      chars[i] = availableChar.charAt(i);
    }
  }

  private Logger logger;

  private FileSystem fileSystem;

  private String assetRoot;

  public PostFileHandler(Vertx vertx, Logger logger, String assetRoot) {
    this.logger = logger;
    this.fileSystem = vertx.fileSystem();
    this.assetRoot = assetRoot;
  }

  /**
   * curl --form upload=@localfilename --form press=OK localhost
   */
  @Override
  public void handle(final HttpServerRequest request) {
    request.expectMultiPart(true);
    final HttpServerResponse response = request.response();
    final String host = request.headers().get("Host");

    final StringBuilder sb = new StringBuilder();

    request.uploadHandler(new Handler<HttpServerFileUpload>() {
      public void handle(HttpServerFileUpload upload) {
        String rs = RandomStringUtils.random(7, chars) + Utils.getFileExtWithDot(upload.filename());
        upload.streamToFileSystem(Paths.get(assetRoot, rs).toString());
        sb.append("http://").append(host).append("/").append(rs).append("\n");
      }
    });

    // request.uploadHandler(new Handler<HttpServerFileUpload>() {
    // public void handle(HttpServerFileUpload upload) {
    // String rs = RandomStringUtils.random(7, chars) + Utils.getFileExtWithDot(upload.filename());
    // String fn = Paths.get(assetRoot, rs).toString();
    // upload.dataHandler(new Handler<Buffer>() {
    // @Override
    // public void handle(Buffer event) {
    //
    //
    // }});
    //
    // sb.append("http://").append(host).append("/").append(rs).append("\n");
    // }
    // });

    request.endHandler(new VoidHandler() {
      public void handle() {
        response.putHeader("content-type", "text/plain");
        response.end(sb.toString());
      }
    });
  }
}
