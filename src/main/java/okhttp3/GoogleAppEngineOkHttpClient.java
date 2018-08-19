/*
  Copyright 2017 Andrew Kelly

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
*/
package okhttp3;

import com.swizel.okhttp3.GoogleAppEngineCall;

import javax.annotation.Nullable;

public class GoogleAppEngineOkHttpClient extends OkHttpClient {

  @Nullable private final Headers headers;

  @SuppressWarnings("WeakerAccess")
  GoogleAppEngineOkHttpClient(GoogleAppEngineOkHttpClient.Builder builder) {
    super(builder.builder);
    this.headers = builder.headers.build();
  }

  @Override
  public Call newCall(Request request) {
    if (headers != null) {
      request = new Request.Builder(request).headers(headers).build();
    }
    return new GoogleAppEngineCall(request);
  }

  public static final class Builder {
    private final OkHttpClient.Builder builder;
    private Headers.Builder headers = null;

    public Builder(OkHttpClient.Builder builder) {
      this.builder = builder;
    }

    /** Removes all headers on this builder and adds {@code headers}. */
    public GoogleAppEngineOkHttpClient.Builder headers(Headers headers) {
      this.headers = headers.newBuilder();
      return this;
    }

    public OkHttpClient build() {
      return new GoogleAppEngineOkHttpClient(this);
    }
  }
}
