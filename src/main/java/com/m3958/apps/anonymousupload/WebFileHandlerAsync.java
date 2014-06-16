package com.m3958.apps.anonymousupload;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.vertx.java.core.AsyncResult;
import org.vertx.java.core.AsyncResultHandler;
import org.vertx.java.core.Handler;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.VoidHandler;
import org.vertx.java.core.file.AsyncFile;
import org.vertx.java.core.file.FileSystem;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.http.HttpServerResponse;
import org.vertx.java.core.logging.Logger;
import org.vertx.java.core.streams.Pump;

import com.m3958.apps.anonymousupload.util.Utils;

public class WebFileHandlerAsync implements Handler<HttpServerRequest> {

  private Logger logger;

  private Path webRootPath = Paths.get("web");

  private String indexPage;

  private FileSystem fileSystem;


  private static String[] ctary = new String[] {"text/javascript; charset=UTF-8",
      "text/css; charset=UTF-8", "text/html; charset=UTF-8"};

  public WebFileHandlerAsync(Vertx vertx, Logger logger, String indexPage) {
    this.indexPage = indexPage;
    this.logger = logger;
    this.fileSystem = vertx.fileSystem();
  }

  /**
   * text/javascript text/css text/html; charset=GBK
   */

  @Override
  public void handle(final HttpServerRequest req) {

    final HttpServerResponse res = req.response();

    String uri = req.path().substring(1);

    if (uri.startsWith("web")) {
      uri = uri.substring(4);
    }
    if (uri.isEmpty()) {
      uri = indexPage;
    }
    String ext = Utils.getFileExtWithDot(uri);
    if (".js".equals(ext) || ".css".equals(ext) || ".html".equals(ext)) {
      int i = 0;
      if (".css".equals(ext)) {
        i = 1;
      } else {
        i = 2;
      }
      res.headers().set("content-type", ctary[i]);
      String finalFile = webRootPath.resolve(uri).toString();
      
//      if(fileSystem.existsSync(finalFile)){
//        fileSystem.readFile(finalFile, new AsyncResultHandler<Buffer>() {
//          public void handle(AsyncResult<Buffer> ar) {
//            if (ar.succeeded()) {
//              res.headers().set("content-length", ar.result().length() + "");
//              res.end(ar.result());
//            } else {
//              logger.error("Failed to open file", ar.cause());
//              res.end("File Read Error!");
//            }
//          }
//        });
//      } else {
//        res.end("File Not Found!");
//      }


      fileSystem.open(finalFile, new AsyncResultHandler<AsyncFile>() {
        public void handle(AsyncResult<AsyncFile> ar) {
          if (ar.succeeded()) {
            AsyncFile asyncFile = ar.result();
            res.setChunked(true);
            Pump.createPump(asyncFile, res).start();
            asyncFile.endHandler(new VoidHandler() {
              public void handle() {
                res.end();
              }
            });
          } else {
            logger.error("Failed to open file", ar.cause());
            res.end("File Not Found!");
          }
        }
      });
    } else {
      res.sendFile(webRootPath.resolve(uri).toString());
    }
  }
}
