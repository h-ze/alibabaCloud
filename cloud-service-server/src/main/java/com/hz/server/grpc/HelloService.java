/*
package com.hz.grpc;

import hz.lib.DeviceFixServiceGrpc;
import hz.lib.booleanReply;
import hz.lib.conditionsRequest;
import hz.lib.deviceFix;
import io.grpc.stub.StreamObserver;
import net.devh.springboot.autoconfigure.grpc.server.GrpcService;

@GrpcService(DeviceFixServiceGrpc.class)
public class HelloService extends DeviceFixServiceGrpc.DeviceFixServiceImplBase{


//    @Autowired
//    private IDevicesFixService deviceService;

    @Override
    public void insertDeviceFix(deviceFix request, StreamObserver<booleanReply> responseObserver) {
//        DevicesFix deviceFix = DevicesFix.builder().id(request.getId())
//                .serialNum(request.getSerialNum())
//                .address(request.getAddress())
//                .createtime(DateUtil.toDate(request.getCreatetime(), DatePattern.TIMESTAMP))
//                .updatetime(DateUtil.toDate(request.getUpdatetime(), DatePattern.TIMESTAMP))
//                .userNum(request.getUserNum())
//                .status(request.getStatus())
//                .type(request.getType())
//                .build();
        //log.info(deviceFix.toString());
        //boolean replyTag = deviceService.insert(deviceFix);

        booleanReply reply = booleanReply.newBuilder().setReply(true).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

    @Override
    public void updateDeviceFix(deviceFix request, StreamObserver<booleanReply> responseObserver) {
        super.updateDeviceFix(request, responseObserver);
    }

    @Override
    public void searchDeviceFix(conditionsRequest request, StreamObserver<deviceFix> responseObserver) {

        deviceFix builder = deviceFix.newBuilder().setId("1").setAddress("mac地址").build();
        responseObserver.onNext(builder);
        responseObserver.onCompleted();
        //super.searchDeviceFix(request, responseObserver);
    }

    @Override
    public void deleteDeviceFix(conditionsRequest request, StreamObserver<booleanReply> responseObserver) {
        super.deleteDeviceFix(request, responseObserver);
    }
}
*/
