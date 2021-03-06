/*
 * Copyright The OpenTelemetry Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.opentelemetry.auto.instrumentation.lettuce.v4_0;

import com.lambdaworks.redis.RedisURI;
import io.opentelemetry.OpenTelemetry;
import io.opentelemetry.auto.bootstrap.instrumentation.decorator.DatabaseClientDecorator;
import io.opentelemetry.trace.Span;
import io.opentelemetry.trace.Tracer;
import io.opentelemetry.trace.attributes.SemanticAttributes;

public class LettuceClientDecorator extends DatabaseClientDecorator<RedisURI> {

  public static final LettuceClientDecorator DECORATE = new LettuceClientDecorator();

  public static final Tracer TRACER =
      OpenTelemetry.getTracerProvider().get("io.opentelemetry.auto.lettuce-4.0");

  @Override
  protected String dbType() {
    return "redis";
  }

  @Override
  protected String dbUser(final RedisURI connection) {
    return null;
  }

  @Override
  protected String dbInstance(final RedisURI connection) {
    return null;
  }

  @Override
  public Span onConnection(final Span span, final RedisURI connection) {
    if (connection != null) {
      span.setAttribute(SemanticAttributes.NET_PEER_NAME.key(), connection.getHost());
      span.setAttribute(SemanticAttributes.NET_PEER_PORT.key(), connection.getPort());

      span.setAttribute("db.redis.dbIndex", connection.getDatabase());
    }
    return super.onConnection(span, connection);
  }
}
