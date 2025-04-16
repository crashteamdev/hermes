package dev.crashteam.hermes.grpc;

import dev.crashteam.crm.*;
import dev.crashteam.hermes.exception.ContactNotFoundException;
import dev.crashteam.hermes.exception.LeadAlreadyExistsException;
import dev.crashteam.hermes.exception.integration.crm.CrmIntegrationException;
import dev.crashteam.hermes.exception.pipeline.PipelineException;
import dev.crashteam.hermes.mapper.GrpcMapper;
import dev.crashteam.hermes.model.domain.UserContactEntity;
import dev.crashteam.hermes.model.dto.lead.LeadRequest;
import dev.crashteam.hermes.service.analytics.KeAnalyticsService;
import dev.crashteam.hermes.service.contact.ContactService;
import dev.crashteam.hermes.service.demo.DemoAccessService;
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
    private final DemoAccessService demoAccessService;
    private final KeAnalyticsService keAnalyticsService;

    @Override
    public void createLead(CreateLeadRequest request, StreamObserver<CreateLeadResponse> responseObserver) {
        try {
            CreateLeadRequest.UtmTag utmTag = request.getUtmTag();
            if (request.hasCreateDemoLead()) {
                log.info("Start create DEMO Lead");
                leadService.createDemoLead(GrpcMapper.mapDemoLead(request.getCreateDemoLead(), utmTag));
            } else if (request.hasCreateFeedbackLead()) {
                log.info("Start create FEEDBACK Lead");
                leadService.createFeedbackLead(GrpcMapper.mapFeedbackLead(request.getCreateFeedbackLead(), utmTag));
            } else if (request.hasCreateServiceLead()) {
                log.info("Start create SERVICE Lead");
                leadService.createServiceLead(GrpcMapper.mapServiceLead(request.getCreateServiceLead(), utmTag));
            }
            responseObserver.onNext(CreateLeadResponse.newBuilder().build());
        } catch (LeadAlreadyExistsException e) {
            responseObserver.onNext(
                    fillCreateLeadResponseError(CreateLeadResponse.ErrorResponse.ErrorCode.ERROR_CODE_USER_LEAD_ALREADY_EXISTS, e));
        } catch (CrmIntegrationException e) {
            if (e.getMessage().contains("422") && e.getMessage().contains("уже занято")) {
                responseObserver.onNext(
                        fillCreateLeadResponseError(CreateLeadResponse.ErrorResponse.ErrorCode.ERROR_CODE_USER_LEAD_ALREADY_EXISTS, e));
            } else {
                responseObserver.onNext(fillCreateLeadResponseError(CreateLeadResponse.ErrorResponse.ErrorCode.ERROR_CODE_UNKNOWN, e));
            }
            log.error("Lead not created: [{}]", e.getMessage());
        } catch (PipelineException e) {
            responseObserver.onNext(fillCreateLeadResponseError(CreateLeadResponse.ErrorResponse.ErrorCode.ERROR_CODE_UNKNOWN, e));
            log.error("Lead not created: [{}]", e.getMessage());
        }

        responseObserver.onCompleted();
        log.info("Done create Lead");
    }

    @Override
    public void getUserContactInfo(GetUserContactInfoRequest request, StreamObserver<GetUserContactInfoResponse> responseObserver) {
        try {
            log.info("Start get user contact info");
            UserContactEntity userContact = contactService.getContact(request.getUserId());
            responseObserver.onNext(GrpcMapper.mapToUserContact(userContact));
        } catch (ContactNotFoundException e) {
            responseObserver.onNext(
                    fillUserContactInfoResponseError(GetUserContactInfoResponse.ErrorResponse.ErrorCode.ERROR_CODE_USER_NOT_FOUND, e));
            log.error("Contact not created\n" +
                    "Stack trace: %s".formatted(e));
        } catch (RuntimeException e) {
            responseObserver.onNext(
                    fillUserContactInfoResponseError(GetUserContactInfoResponse.ErrorResponse.ErrorCode.ERROR_CODE_UNKNOWN, e));
            log.error("Contact not created\n" +
                    "Stack trace: %s".formatted(e));
        }
        responseObserver.onCompleted();
    }

    @Override
    public void updateUserContactInfo(UpdateUserContactInfoRequest request, StreamObserver<UpdateUserContactInfoResponse> responseObserver) {
        String userId = request.getUserId();
        if (request.hasInitialUpdateContactInfoPayload()) {
            try {
                log.info("Start update user contact info");
                UserContactEntity userContact = contactService.updateContact(userId, GrpcMapper.map(request.getInitialUpdateContactInfoPayload()));
                String smsCode = smsService.generateApproveCode();
                smsService.smsSend(userContact.getPhone(), smsCode);
                responseObserver.onNext(UpdateUserContactInfoResponse.newBuilder()
                        .setSuccessResponse(UpdateUserContactInfoResponse.SuccessResponse.newBuilder()
                                .setUpdateUserContactInfoState(UpdateUserContactInfoState.UPDATE_USER_CONTACT_INFO_STATE_NOT_VERIFIED)
                                .build())
                        .build());
            } catch (ContactNotFoundException e) {
                responseObserver.onNext(
                        fillUpdateUserContactInfoError(UpdateUserContactInfoResponse.ErrorResponse.ErrorCode.ERROR_CODE_INVALID_SEQUENCE, e));
            }
        } else if (request.hasApproveUpdateContactInfoPayload()) {
            String sentApproveCode = request.getApproveUpdateContactInfoPayload().getApproveCode();
            UserContactEntity userContact = contactService.getContact(request.getUserId());
            String expectedApproveCode = userContact.getApproveCode();
            if (expectedApproveCode.equals(sentApproveCode)) {
                contactService.verifyContact(userId);
                responseObserver.onNext(UpdateUserContactInfoResponse.newBuilder()
                        .setSuccessResponse(UpdateUserContactInfoResponse.SuccessResponse.newBuilder()
                                .setUserContact(GrpcMapper.map(userContact))
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

    @Override
    public void requestDemoAccess(RequestDemoAccess request, StreamObserver<RequestDemoAccessResponse> responseObserver) {
        LeadRequest leadRequest = GrpcMapper.mapDemoLead(request);
        try {
            leadService.createDemoLead(leadRequest);
        } catch (Exception e) {
            // Ignore error
            log.warn("Failed to create demo lead", e);
        }
        String userId = request.getTelegramUsername();
        if (userId.isEmpty()) {
            userId = request.getUserEmail();
        }
        try {
            String demoToken = demoAccessService.createDemoAccess(userId);
            responseObserver.onNext(RequestDemoAccessResponse.newBuilder()
                    .setSuccessResponse(
                            RequestDemoAccessResponse.SuccessResponse.newBuilder()
                                    .setDemoAccessToken(demoToken)
                                    .build())
                    .build());
        } catch (Exception ex) {
            responseObserver.onNext(RequestDemoAccessResponse.newBuilder()
                    .setErrorResponse(RequestDemoAccessResponse.ErrorResponse.newBuilder()
                            .setErrorCode(RequestDemoAccessResponse.ErrorResponse.ErrorCode.ERROR_CODE_DUPLICATE)
                            .build())
                    .build());
        } finally {
            responseObserver.onCompleted();
        }
    }

    @Override
    public void checkDemoToken(CheckDemoTokenRequest
                                       request, StreamObserver<CheckDemoTokenResponse> responseObserver) {
        boolean isDemoAccess = demoAccessService.giveDemoByToken(request.getGeneralUserId(), request.getDemoAccessToken());
        if (isDemoAccess) {
            responseObserver.onNext(CheckDemoTokenResponse.newBuilder().setSuccessResponse(
                            CheckDemoTokenResponse.SuccessResponse.newBuilder().build())
                    .build());
        } else {
            responseObserver.onNext(CheckDemoTokenResponse.newBuilder()
                    .setErrorResponse(
                            CheckDemoTokenResponse.ErrorResponse.newBuilder()
                                    .setErrorCode(CheckDemoTokenResponse.ErrorResponse.ErrorCode.ERROR_CODE_INVALID)
                                    .build())
                    .build());
        }
    }

    private CreateLeadResponse fillCreateLeadResponseError(CreateLeadResponse.ErrorResponse.ErrorCode errorCode,
                                                           Exception e) {
        return CreateLeadResponse.newBuilder()
                .setErrorResponse(CreateLeadResponse.ErrorResponse.newBuilder()
                        .setErrorCode(errorCode)
                        .setDescription(e.getMessage())
                        .build())
                .build();
    }

    private GetUserContactInfoResponse fillUserContactInfoResponseError
            (GetUserContactInfoResponse.ErrorResponse.ErrorCode errorCode,
             Exception e) {
        return GetUserContactInfoResponse.newBuilder()
                .setErrorResponse(GetUserContactInfoResponse.ErrorResponse.newBuilder()
                        .setErrorCode(errorCode)
                        .setDescription(e.getMessage())
                        .build())
                .build();
    }

    private static UpdateUserContactInfoResponse fillUpdateUserContactInfoError
            (UpdateUserContactInfoResponse.ErrorResponse.ErrorCode errorCode,
             ContactNotFoundException e) {
        return UpdateUserContactInfoResponse.newBuilder()
                .setErrorResponse(UpdateUserContactInfoResponse.ErrorResponse.newBuilder()
                        .setErrorCode(errorCode)
                        .setDescription(e.getMessage())
                        .build())
                .build();
    }

}
