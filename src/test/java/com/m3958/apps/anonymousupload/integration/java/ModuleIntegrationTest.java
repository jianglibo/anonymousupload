package com.m3958.apps.anonymousupload.integration.java;

/*
 * Copyright 2013 Red Hat, Inc.
 * 
 * Red Hat licenses this file to you under the Apache License, version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 * 
 * @author <a href="http://tfox.org">Tim Fox</a>
 */

import static org.vertx.testtools.VertxAssert.assertNotNull;
import static org.vertx.testtools.VertxAssert.assertTrue;
import static org.vertx.testtools.VertxAssert.testComplete;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.charset.Charset;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.junit.Assert;
import org.junit.Test;
import org.vertx.java.core.AsyncResult;
import org.vertx.java.core.AsyncResultHandler;
import org.vertx.java.core.Handler;
import org.vertx.java.core.VoidHandler;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.http.HttpClient;
import org.vertx.java.core.http.HttpClientResponse;
import org.vertx.testtools.TestVerticle;

import com.google.common.io.CharSource;
import com.google.common.io.CharStreams;
import com.google.common.io.InputSupplier;

/**
 * Example Java integration test that deploys the module that this project builds.
 * 
 * Quite often in integration tests you want to deploy the same module for all tests and you don't
 * want tests to start before the module has been deployed.
 * 
 * This test demonstrates how to do that.
 */
public class ModuleIntegrationTest extends TestVerticle {

  // @Test
  // public void testGetIndex() {
  // HttpClient client = vertx.createHttpClient().setHost("localhost").setPort(8080);
  // container.logger().info(System.getProperty("user.dir"));
  // client.getNow("/", new Handler<HttpClientResponse>() {
  // public void handle(final HttpClientResponse resp) {
  //
  // final Buffer body = new Buffer(0);
  //
  // resp.dataHandler(new Handler<Buffer>() {
  // public void handle(Buffer data) {
  // body.appendBuffer(data);
  // }
  // });
  // resp.endHandler(new VoidHandler() {
  // public void handle() {
  // // The entire response body has been received
  // container.logger().info(body.toString("UTF-8"));
  // Assert.assertEquals(17, body.length());
  // Assert.assertEquals("hello web server.", body.toString());
  // Assert.assertEquals("text/html; charset=UTF-8",resp.headers().get("Content-Type"));
  // testComplete();
  // }
  // });
  // }
  // });
  // }

  @Test
  public void testGetIndex() throws ClientProtocolException, IOException, URISyntaxException {

    File f = new File("README.md");

    Assert.assertTrue(f.exists());


    HttpResponse r =
        Request.Get(new URIBuilder().setScheme("http").setHost("localhost").setPort(8080).build())
            .execute().returnResponse();

    String c =
        CharStreams.toString(new InputStreamReader(r.getEntity().getContent(), Charset
            .forName("UTF-8")));
    container.logger().info(c);
    Assert.assertEquals(17, c.length());
    Assert.assertEquals("hello web server.", c);
    Assert.assertEquals("text/html; charset=UTF-8", r.getEntity().getContentType().getValue());
    testComplete();
  }

  // @Test
  // public void testGetChinese() {
  // HttpClient client = vertx.createHttpClient().setHost("localhost").setPort(8080);
  //
  // client.getNow("/web/chinese.html", new Handler<HttpClientResponse>() {
  // public void handle(final HttpClientResponse resp) {
  //
  // final Buffer body = new Buffer(0);
  //
  // resp.dataHandler(new Handler<Buffer>() {
  // public void handle(Buffer data) {
  // body.appendBuffer(data);
  // }
  // });
  // resp.endHandler(new VoidHandler() {
  // public void handle() {
  // // The entire response body has been received
  // Assert.assertEquals(15, body.length());
  // Assert.assertEquals("你好，中文", body.toString());
  // Assert.assertEquals("text/html; charset=UTF-8", resp.headers().get("Content-Type"));
  // testComplete();
  // }
  // });
  // }
  // });
  // }

  public void testGetChinese() throws ClientProtocolException, IOException, URISyntaxException {

    File f = new File("README.md");

    Assert.assertTrue(f.exists());


    HttpResponse r =
        Request
            .Get(
                new URIBuilder().setScheme("http").setHost("localhost")
                    .setPath("/web/chinese.html").setPort(8080).build()).execute().returnResponse();

    String c =
        CharStreams.toString(new InputStreamReader(r.getEntity().getContent(), Charset
            .forName("UTF-8")));

    container.logger().info(c);

    Assert.assertEquals(15, c.length());
    Assert.assertEquals("你好，中文", c);
    Assert.assertEquals("text/html; charset=UTF-8", r.getEntity().getContentType().getValue());

    testComplete();

  }

  // @Test
  // public void testOthers() {
  // HttpClient client = vertx.createHttpClient().setHost("localhost").setPort(8080);
  //
  // client.getNow("/web/README.md", new Handler<HttpClientResponse>() {
  // public void handle(final HttpClientResponse resp) {
  //
  // final Buffer body = new Buffer(0);
  //
  // resp.dataHandler(new Handler<Buffer>() {
  // public void handle(Buffer data) {
  // body.appendBuffer(data);
  // }
  // });
  // resp.endHandler(new VoidHandler() {
  // public void handle() {
  // // The entire response body has been received
  // Assert.assertEquals("text/html", resp.headers().get("Content-Type"));
  // testComplete();
  // }
  // });
  // }
  // });
  // }


  public void testOthers() throws ClientProtocolException, IOException, URISyntaxException {

    File f = new File("README.md");

    Assert.assertTrue(f.exists());


    HttpResponse r =
        Request
            .Get(
                new URIBuilder().setScheme("http").setHost("localhost").setPath("/web/README.md")
                    .setPort(8080).build()).execute().returnResponse();

    Assert.assertEquals("text/html", r.getEntity().getContentType().getValue());

    testComplete();

  }

  @Test
  public void tt() {
    testComplete();
  }

  @Override
  public void start() {
    // Make sure we call initialize() - this sets up the assert stuff so
    // assert functionality works
    // correctly
    initialize();
    // Deploy the module - the System property `vertx.modulename` will
    // contain the name of the
    // module so you
    // don't have to hardecode it in your tests
    container.logger().info(System.getProperty("vertx.modulename"));
    container.deployModule(System.getProperty("vertx.modulename"),
        new AsyncResultHandler<String>() {
          @Override
          public void handle(AsyncResult<String> asyncResult) {
            // Deployment is asynchronous and this this handler will
            // be called when it's complete
            // (or failed)
            assertTrue(asyncResult.succeeded());
            assertNotNull("deploymentID should not be null", asyncResult.result());
            // If deployed correctly then start the tests!
            startTests();
          }
        });
  }

}
