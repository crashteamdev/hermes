package dev.crashteam.hermes.grpc;

import dev.crashteam.crm.CreateLeadRequest;
import dev.crashteam.crm.CreateLeadResponse;
import dev.crashteam.crm.CrmServiceGrpc;
import dev.crashteam.crm.GetUserContactInfoRequest;
import dev.crashteam.crm.GetUserContactInfoResponse;
import dev.crashteam.crm.UpdateUserContactInfoRequest;
import dev.crashteam.crm.UpdateUserContactInfoResponse;
import dev.crashteam.crm.UpdateUserContactInfoState;
import dev.crashteam.hermes.mapper.GrpcMapper;
import dev.crashteam.hermes.model.domain.Contact;
import dev.crashteam.hermes.model.dto.contact.ContactResponse;
import dev.crashteam.hermes.service.contact.ContactService;
import dev.crashteam.hermes.service.lead.LeadService;
import dev.crashteam.hermes.service.sms.SmsService;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
public class CrmGrpc extends CrmServiceGrpc.CrmServiceImplBase {

    private final GrpcMapper grpcMapper;
    private final LeadService leadService;
    private final ContactService contactService;
    private final SmsService smsService;

    @Override
    public void createLead(CreateLeadRequest request, StreamObserver<CreateLeadResponse> responseObserver) {
        leadService.createLead(grpcMapper.map(request));
        responseObserver.onNext(CreateLeadResponse.newBuilder().build());
        responseObserver.onCompleted();
    }

    @Override
    public void getUserContactInfo(GetUserContactInfoRequest request, StreamObserver<GetUserContactInfoResponse> responseObserver) {
        ContactResponse contact = contactService.getContact(request.getUserId());
        responseObserver.onNext(grpcMapper.map(contact));
        responseObserver.onCompleted();
    }

    @Override
    public void updateUserContactInfo(UpdateUserContactInfoRequest request, StreamObserver<UpdateUserContactInfoResponse> responseObserver) {
        String userId = request.getUserId();
        Contact contact = new Contact();
        String smsCode = smsService.generateSmsCode();
        if (request.hasInitialUpdateContactInfoPayload()) {
            contact = contactService.updateContact(userId, grpcMapper.map(request.getInitialUpdateContactInfoPayload()));
            smsService.smsSend(contact.getPhone(), smsCode);
            responseObserver.onNext(UpdateUserContactInfoResponse.newBuilder()
                    .setSuccessResponse(UpdateUserContactInfoResponse.SuccessResponse.newBuilder()
                            .setUpdateUserContactInfoState(UpdateUserContactInfoState.UPDATE_USER_CONTACT_INFO_STATE_NOT_VERIFIED)
                            .build())
                    .build());
        } else if (request.hasApproveUpdateContactInfoPayload()) {
            String approveCode = request.getApproveUpdateContactInfoPayload().getApproveCode();
            if (smsCode.equals(approveCode)) {
                responseObserver.onNext(UpdateUserContactInfoResponse.newBuilder()
                        .setSuccessResponse(UpdateUserContactInfoResponse.SuccessResponse.newBuilder()
                                .setUserContact(grpcMapper.map(contact))
                                .setUpdateUserContactInfoState(UpdateUserContactInfoState.UPDATE_USER_CONTACT_INFO_STATE_VERIFIED)
                                .build())
                        .build());
            }
        }
        responseObserver.onCompleted();
    }

}
