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
import dev.crashteam.hermes.service.contact.ContactService;
import dev.crashteam.hermes.service.lead.LeadService;
import dev.crashteam.hermes.service.sms.SmsService;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

@Slf4j
@GrpcService
@RequiredArgsConstructor
public class CrmGrpc extends CrmServiceGrpc.CrmServiceImplBase {

    private final LeadService leadService;
    private final ContactService contactService;
    private final SmsService smsService;

    @Override
    public void createLead(CreateLeadRequest request, StreamObserver<CreateLeadResponse> responseObserver) {
        try {
            leadService.createLead(GrpcMapper.map(request));
            responseObserver.onNext(CreateLeadResponse.newBuilder().build());
        } catch (Exception e) {
            responseObserver.onNext(CreateLeadResponse.newBuilder()
                    .setErrorResponse(CreateLeadResponse.ErrorResponse.newBuilder()
                            .setErrorCode(CreateLeadResponse.ErrorResponse.ErrorCode.ERROR_CODE_USER_LEAD_ALREADY_EXISTS)
                            .build())
                    .build());
        }
        responseObserver.onCompleted();
    }

    @Override
    public void getUserContactInfo(GetUserContactInfoRequest request, StreamObserver<GetUserContactInfoResponse> responseObserver) {
        Contact contact = contactService.getContact(request.getUserId());
        if (contact != null) {
            responseObserver.onNext(GrpcMapper.mapToUserContact(contact));
        } else {
            responseObserver.onNext(GetUserContactInfoResponse.newBuilder()
                    .setErrorResponse(GetUserContactInfoResponse.ErrorResponse.newBuilder()
                            .setErrorCode(GetUserContactInfoResponse.ErrorResponse.ErrorCode.ERROR_CODE_USER_NOT_FOUND)
                            .build())
                    .build());
        }
        responseObserver.onCompleted();
    }

    @Override
    public void updateUserContactInfo(UpdateUserContactInfoRequest request, StreamObserver<UpdateUserContactInfoResponse> responseObserver) {
        String userId = request.getUserId();
        if (request.hasInitialUpdateContactInfoPayload()) {
            Contact contact = contactService.updateContact(userId, GrpcMapper.map(request.getInitialUpdateContactInfoPayload()));
            String smsCode = smsService.generateApproveCode();
            smsService.smsSend(contact.getPhone(), smsCode);
            responseObserver.onNext(UpdateUserContactInfoResponse.newBuilder()
                    .setSuccessResponse(UpdateUserContactInfoResponse.SuccessResponse.newBuilder()
                            .setUpdateUserContactInfoState(UpdateUserContactInfoState.UPDATE_USER_CONTACT_INFO_STATE_NOT_VERIFIED)
                            .build())
                    .build());
        } else if (request.hasApproveUpdateContactInfoPayload()) {
            String sentApproveCode = request.getApproveUpdateContactInfoPayload().getApproveCode();
            Contact contact = contactService.getContact(request.getUserId());
            String expectedApproveCode = contact.getApproveCode();
            if (expectedApproveCode.equals(sentApproveCode)) {
                contactService.verifyContact(userId);
                responseObserver.onNext(UpdateUserContactInfoResponse.newBuilder()
                        .setSuccessResponse(UpdateUserContactInfoResponse.SuccessResponse.newBuilder()
                                .setUserContact(GrpcMapper.map(contact))
                                .setUpdateUserContactInfoState(UpdateUserContactInfoState.UPDATE_USER_CONTACT_INFO_STATE_VERIFIED)
                                .build())
                        .build());
            } else {
                responseObserver.onNext(UpdateUserContactInfoResponse.newBuilder()
                        .setErrorResponse(UpdateUserContactInfoResponse.ErrorResponse.newBuilder()
                                .setErrorCode(UpdateUserContactInfoResponse.ErrorResponse.ErrorCode.ERROR_CODE_INVALID_APPROVAL_CODE)
                                .setDescription("Код подтверждения: %s. Ожидаемый код: %s".formatted(sentApproveCode, expectedApproveCode))
                                .build())
                        .build());
            }
        }
        responseObserver.onCompleted();
    }

}
