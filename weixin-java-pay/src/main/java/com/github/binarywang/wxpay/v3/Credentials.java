package com.github.binarywang.wxpay.v3;

import org.apache.http.client.methods.HttpRequestWrapper;

import java.io.IOException;

public interface Credentials {

  String getSchema();

  String getToken(HttpRequestWrapper request) throws IOException;
}
