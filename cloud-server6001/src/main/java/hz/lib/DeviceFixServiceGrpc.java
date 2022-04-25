package hz.lib;

import io.grpc.stub.ClientCalls;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 * <pre>
 * The device hz.service definition.
 * </pre>
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.6.1)",
    comments = "Source: hello.proto")
public final class DeviceFixServiceGrpc {

  private DeviceFixServiceGrpc() {}

  public static final String SERVICE_NAME = "device.DeviceFixService";

  // Static method descriptors that strictly reflect the proto.
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<deviceFix,
          booleanReply> METHOD_INSERT_DEVICE_FIX =
      io.grpc.MethodDescriptor.<deviceFix, booleanReply>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "device.DeviceFixService", "insertDeviceFix"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              deviceFix.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              booleanReply.getDefaultInstance()))
          .build();
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<deviceFix,
          booleanReply> METHOD_UPDATE_DEVICE_FIX =
      io.grpc.MethodDescriptor.<deviceFix, booleanReply>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "device.DeviceFixService", "updateDeviceFix"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              deviceFix.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              booleanReply.getDefaultInstance()))
          .build();
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<conditionsRequest,
          deviceFix> METHOD_SEARCH_DEVICE_FIX =
      io.grpc.MethodDescriptor.<conditionsRequest, deviceFix>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "device.DeviceFixService", "searchDeviceFix"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              conditionsRequest.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              deviceFix.getDefaultInstance()))
          .build();
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<conditionsRequest,
          booleanReply> METHOD_DELETE_DEVICE_FIX =
      io.grpc.MethodDescriptor.<conditionsRequest, booleanReply>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "device.DeviceFixService", "deleteDeviceFix"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              conditionsRequest.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              booleanReply.getDefaultInstance()))
          .build();

  /**
   * Creates a new async stub that supports all call types for the hz.service
   */
  public static DeviceFixServiceStub newStub(io.grpc.Channel channel) {
    return new DeviceFixServiceStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the hz.service
   */
  public static DeviceFixServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new DeviceFixServiceBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the hz.service
   */
  public static DeviceFixServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new DeviceFixServiceFutureStub(channel);
  }

  /**
   * <pre>
   * The device hz.service definition.
   * </pre>
   */
  public static abstract class DeviceFixServiceImplBase implements io.grpc.BindableService {

    /**
     * <pre>
     * Sends a message
     * </pre>
     */
    public void insertDeviceFix(deviceFix request,
                                io.grpc.stub.StreamObserver<booleanReply> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_INSERT_DEVICE_FIX, responseObserver);
    }

    /**
     */
    public void updateDeviceFix(deviceFix request,
                                io.grpc.stub.StreamObserver<booleanReply> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_UPDATE_DEVICE_FIX, responseObserver);
    }

    /**
     */
    public void searchDeviceFix(conditionsRequest request,
                                io.grpc.stub.StreamObserver<deviceFix> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_SEARCH_DEVICE_FIX, responseObserver);
    }

    /**
     */
    public void deleteDeviceFix(conditionsRequest request,
                                io.grpc.stub.StreamObserver<booleanReply> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_DELETE_DEVICE_FIX, responseObserver);
    }

    @Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            METHOD_INSERT_DEVICE_FIX,
            asyncUnaryCall(
              new MethodHandlers<
                      deviceFix,
                      booleanReply>(
                  this, METHODID_INSERT_DEVICE_FIX)))
          .addMethod(
            METHOD_UPDATE_DEVICE_FIX,
            asyncUnaryCall(
              new MethodHandlers<
                      deviceFix,
                      booleanReply>(
                  this, METHODID_UPDATE_DEVICE_FIX)))
          .addMethod(
            METHOD_SEARCH_DEVICE_FIX,
            asyncUnaryCall(
              new MethodHandlers<
                      conditionsRequest,
                      deviceFix>(
                  this, METHODID_SEARCH_DEVICE_FIX)))
          .addMethod(
            METHOD_DELETE_DEVICE_FIX,
            asyncUnaryCall(
              new MethodHandlers<
                      conditionsRequest,
                      booleanReply>(
                  this, METHODID_DELETE_DEVICE_FIX)))
          .build();
    }
  }

  /**
   * <pre>
   * The device hz.service definition.
   * </pre>
   */
  public static final class DeviceFixServiceStub extends io.grpc.stub.AbstractStub<DeviceFixServiceStub> {
    private DeviceFixServiceStub(io.grpc.Channel channel) {
      super(channel);
    }

    private DeviceFixServiceStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @Override
    protected DeviceFixServiceStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new DeviceFixServiceStub(channel, callOptions);
    }

    /**
     * <pre>
     * Sends a message
     * </pre>
     */
    public void insertDeviceFix(deviceFix request,
                                io.grpc.stub.StreamObserver<booleanReply> responseObserver) {
      ClientCalls.asyncUnaryCall(
          getChannel().newCall(METHOD_INSERT_DEVICE_FIX, getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void updateDeviceFix(deviceFix request,
                                io.grpc.stub.StreamObserver<booleanReply> responseObserver) {
      ClientCalls.asyncUnaryCall(
          getChannel().newCall(METHOD_UPDATE_DEVICE_FIX, getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void searchDeviceFix(conditionsRequest request,
                                io.grpc.stub.StreamObserver<deviceFix> responseObserver) {
      ClientCalls.asyncUnaryCall(
          getChannel().newCall(METHOD_SEARCH_DEVICE_FIX, getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void deleteDeviceFix(conditionsRequest request,
                                io.grpc.stub.StreamObserver<booleanReply> responseObserver) {
      ClientCalls.asyncUnaryCall(
          getChannel().newCall(METHOD_DELETE_DEVICE_FIX, getCallOptions()), request, responseObserver);
    }
  }

  /**
   * <pre>
   * The device hz.service definition.
   * </pre>
   */
  public static final class DeviceFixServiceBlockingStub extends io.grpc.stub.AbstractStub<DeviceFixServiceBlockingStub> {
    private DeviceFixServiceBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private DeviceFixServiceBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @Override
    protected DeviceFixServiceBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new DeviceFixServiceBlockingStub(channel, callOptions);
    }

    /**
     * <pre>
     * Sends a message
     * </pre>
     */
    public booleanReply insertDeviceFix(deviceFix request) {
      return blockingUnaryCall(
          getChannel(), METHOD_INSERT_DEVICE_FIX, getCallOptions(), request);
    }

    /**
     */
    public booleanReply updateDeviceFix(deviceFix request) {
      return blockingUnaryCall(
          getChannel(), METHOD_UPDATE_DEVICE_FIX, getCallOptions(), request);
    }

    /**
     */
    public deviceFix searchDeviceFix(conditionsRequest request) {
      return blockingUnaryCall(
          getChannel(), METHOD_SEARCH_DEVICE_FIX, getCallOptions(), request);
    }

    /**
     */
    public booleanReply deleteDeviceFix(conditionsRequest request) {
      return blockingUnaryCall(
          getChannel(), METHOD_DELETE_DEVICE_FIX, getCallOptions(), request);
    }
  }

  /**
   * <pre>
   * The device hz.service definition.
   * </pre>
   */
  public static final class DeviceFixServiceFutureStub extends io.grpc.stub.AbstractStub<DeviceFixServiceFutureStub> {
    private DeviceFixServiceFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private DeviceFixServiceFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @Override
    protected DeviceFixServiceFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new DeviceFixServiceFutureStub(channel, callOptions);
    }

    /**
     * <pre>
     * Sends a message
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<booleanReply> insertDeviceFix(
        deviceFix request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_INSERT_DEVICE_FIX, getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<booleanReply> updateDeviceFix(
        deviceFix request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_UPDATE_DEVICE_FIX, getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<deviceFix> searchDeviceFix(
        conditionsRequest request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_SEARCH_DEVICE_FIX, getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<booleanReply> deleteDeviceFix(
        conditionsRequest request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_DELETE_DEVICE_FIX, getCallOptions()), request);
    }
  }

  private static final int METHODID_INSERT_DEVICE_FIX = 0;
  private static final int METHODID_UPDATE_DEVICE_FIX = 1;
  private static final int METHODID_SEARCH_DEVICE_FIX = 2;
  private static final int METHODID_DELETE_DEVICE_FIX = 3;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final DeviceFixServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(DeviceFixServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_INSERT_DEVICE_FIX:
          serviceImpl.insertDeviceFix((deviceFix) request,
              (io.grpc.stub.StreamObserver<booleanReply>) responseObserver);
          break;
        case METHODID_UPDATE_DEVICE_FIX:
          serviceImpl.updateDeviceFix((deviceFix) request,
              (io.grpc.stub.StreamObserver<booleanReply>) responseObserver);
          break;
        case METHODID_SEARCH_DEVICE_FIX:
          serviceImpl.searchDeviceFix((conditionsRequest) request,
              (io.grpc.stub.StreamObserver<deviceFix>) responseObserver);
          break;
        case METHODID_DELETE_DEVICE_FIX:
          serviceImpl.deleteDeviceFix((conditionsRequest) request,
              (io.grpc.stub.StreamObserver<booleanReply>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @Override
    @SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static final class DeviceFixServiceDescriptorSupplier implements io.grpc.protobuf.ProtoFileDescriptorSupplier {
    @Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return DeviceFixProto.getDescriptor();
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (DeviceFixServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new DeviceFixServiceDescriptorSupplier())
              .addMethod(METHOD_INSERT_DEVICE_FIX)
              .addMethod(METHOD_UPDATE_DEVICE_FIX)
              .addMethod(METHOD_SEARCH_DEVICE_FIX)
              .addMethod(METHOD_DELETE_DEVICE_FIX)
              .build();
        }
      }
    }
    return result;
  }
}
