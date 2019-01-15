/*
 * Copyright 2018 Red Hat, Inc.
 *
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Apache License v2.0 which accompanies this distribution.
 *
 *  The Eclipse Public License is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  The Apache License v2.0 is available at
 *  http://www.opensource.org/licenses/apache2.0.php
 *
 *  You may elect to redistribute this code under either of these licenses.
 */
import com.oracle.svm.core.annotate.*;
import org.graalvm.nativeimage.*;

import io.netty.handler.codec.compression.*;
import io.netty.util.internal.logging.*;
import io.vertx.core.Vertx;
import io.vertx.core.dns.AddressResolverOptions;
import io.vertx.core.impl.resolver.DefaultResolverProvider;
import io.vertx.core.spi.resolver.ResolverProvider;

/**
 * This substitution avoid having loggers added to the build
 */
@TargetClass(className = "io.netty.util.internal.logging.InternalLoggerFactory")
final class TargetInternalLoggerFactory {
  @Substitute
  private static InternalLoggerFactory newDefaultFactory(String name) {
    return JdkLoggerFactory.INSTANCE;
  }
}

/**
 * This substitution allows the usage of platform specific code to do low level buffer related tasks
 */
@TargetClass(className = "io.netty.util.internal.CleanerJava6")
final class TargetCleanerJava6 {
  @Alias
  @RecomputeFieldValue(kind = RecomputeFieldValue.Kind.FieldOffset, declClassName = "java.nio.DirectByteBuffer", name = "cleaner")
  private static long CLEANER_FIELD_OFFSET;
}

/**
 * This substitution allows the usage of platform specific code to do low level buffer related tasks
 */
@TargetClass(className = "io.netty.util.internal.PlatformDependent0")
final class TargetPlatformDependent0 {
  @Alias
  @RecomputeFieldValue(kind = RecomputeFieldValue.Kind.FieldOffset, declClassName = "java.nio.Buffer", name = "address")
  private static long ADDRESS_FIELD_OFFSET;
}

/**
 * This substitution allows the usage of platform specific code to do low level buffer related tasks
 */
@TargetClass(className = "io.netty.util.internal.shaded.org.jctools.util.UnsafeRefArrayAccess")
final class TargetUnsafeRefArrayAccess {
  @Alias
  @RecomputeFieldValue(kind = RecomputeFieldValue.Kind.ArrayIndexShift, declClass = Object[].class)
  public static int REF_ELEMENT_SHIFT;
}

/**
 * This substitution avoid having jcraft zlib added to the build
 */
@TargetClass(className = "io.netty.handler.codec.compression.ZlibCodecFactory")
final class TargetZlibCodecFactory {

  @Substitute
  public static ZlibEncoder newZlibEncoder(int compressionLevel) {
    return new JdkZlibEncoder(compressionLevel);
  }

  @Substitute
  public static ZlibEncoder newZlibEncoder(ZlibWrapper wrapper) {
    return new JdkZlibEncoder(wrapper);
  }

  @Substitute
  public static ZlibEncoder newZlibEncoder(ZlibWrapper wrapper, int compressionLevel) {
    return new JdkZlibEncoder(wrapper, compressionLevel);
  }

  @Substitute
  public static ZlibEncoder newZlibEncoder(ZlibWrapper wrapper, int compressionLevel, int windowBits, int memLevel) {
    return new JdkZlibEncoder(wrapper, compressionLevel);
  }

  @Substitute
  public static ZlibEncoder newZlibEncoder(byte[] dictionary) {
    return new JdkZlibEncoder(dictionary);
  }

  @Substitute
  public static ZlibEncoder newZlibEncoder(int compressionLevel, byte[] dictionary) {
    return new JdkZlibEncoder(compressionLevel, dictionary);
  }

  @Substitute
  public static ZlibEncoder newZlibEncoder(int compressionLevel, int windowBits, int memLevel, byte[] dictionary) {
    return new JdkZlibEncoder(compressionLevel, dictionary);
  }

  @Substitute
  public static ZlibDecoder newZlibDecoder() {
    return new JdkZlibDecoder(true);
  }

  @Substitute
  public static ZlibDecoder newZlibDecoder(ZlibWrapper wrapper) {
    return new JdkZlibDecoder(wrapper, true);
  }

  @Substitute
  public static ZlibDecoder newZlibDecoder(byte[] dictionary) {
    return new JdkZlibDecoder(dictionary);
  }
}

/**
 * This substitution forces the usage of the blocking DNS resolver
 */
@TargetClass(className = "io.vertx.core.spi.resolver.ResolverProvider")
final class TargetResolverProvider {

  @Substitute
  public static ResolverProvider factory(Vertx vertx, AddressResolverOptions options) {
    return new DefaultResolverProvider();
  }
}

@AutomaticFeature
class RuntimeReflectionRegistrationFeature implements Feature {
  public void beforeAnalysis(BeforeAnalysisAccess access) {
    try {
      RuntimeReflection.register(java.util.LinkedHashMap.class.getDeclaredConstructor());
    } catch (NoSuchMethodException e) {
      throw new RuntimeException(e);
    }
  }
}
