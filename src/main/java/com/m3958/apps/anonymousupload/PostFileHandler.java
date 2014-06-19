package com.m3958.apps.anonymousupload;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.apache.commons.lang.RandomStringUtils;
import org.vertx.java.core.Handler;
import org.vertx.java.core.MultiMap;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.VoidHandler;
import org.vertx.java.core.file.FileSystem;
import org.vertx.java.core.http.HttpServerFileUpload;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.http.HttpServerResponse;
import org.vertx.java.core.logging.Logger;

import com.google.common.collect.Lists;
import com.m3958.apps.anonymousupload.util.Utils;

public class PostFileHandler implements Handler<HttpServerRequest> {

  public static String availableChar =
      "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789$@.-~_";

  public static char[] chars = new char[68];

  static {
    for (int i = 0; i < 68; i++) {
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
   * curl --form upload=@localfilename --form press=OK localhost allow upload one file per form.
   */
  @Override
  public void handle(final HttpServerRequest request) {
    request.expectMultiPart(true);
    final HttpServerResponse response = request.response();
    final String host = request.headers().get("Host");
    final List<String> upfiles = Lists.newArrayList();
    request.uploadHandler(new Handler<HttpServerFileUpload>() {
      public void handle(HttpServerFileUpload upload) {
        if (upfiles.size() == 0) {
          String rs = getNextFn(upload.filename());
          Path fpath = Paths.get(assetRoot, rs);
          upload.streamToFileSystem(fpath.toString());
          upfiles.add(rs);
        }
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
        String restr;
        if (upfiles.size() > 0) {
          MultiMap attrs = request.formAttributes();

          String randomfn = upfiles.get(0);

          String fn = attrs.get("fn");

          if (fn == null || fn.length() < 3) {
            fn = randomfn;
          } else {
            String randomPath = Paths.get(assetRoot, randomfn).toString();
            String fnPath = Paths.get(assetRoot, fn).toString();
            if (fileSystem.existsSync(fnPath)) {
              fileSystem.deleteSync(fnPath);
            }
            fileSystem.moveSync(randomPath, fnPath);
          }
          restr = "http://" + host + "/" + fn;
        } else {
          restr = "1";
        }
        response.putHeader("content-type", "text/plain");
        response.end(restr);
      }
    });
  }


  private String getNextFn(String fn) {
    String rs = RandomStringUtils.random(7, chars) + Utils.getFileExtWithDot(fn);
    Path fpath = Paths.get(assetRoot, rs);
    if (fileSystem.existsSync(fpath.toString())) {
      return getNextFn(fn);
    } else {
      return rs;
    }
  }
}
