package pe.mil.microservices.controllers.implementations;

import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.support.WebExchangeBindException;
import pe.mil.microservices.controllers.contracts.IGradeController;
import pe.mil.microservices.dto.requests.RegisterGradeRequest;
import pe.mil.microservices.services.contracts.IGradeServices;
import pe.mil.microservices.utils.components.exceptions.CommonBusinessProcessException;
import pe.mil.microservices.utils.constants.LoggerConstants;
import pe.mil.microservices.utils.dtos.base.BusinessResponse;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static pe.mil.microservices.constants.ProcessConstants.*;

@Log4j2
@RestController
public class GradeController implements IGradeController {
    private final IGradeServices gradeServices;
    private final BusinessResponse businessResponse;
    private final String gradeControllerId;

    public GradeController(
        IGradeServices gradeServices,
        BusinessResponse businessResponse
    ) {
        this.gradeServices = gradeServices;
        this.businessResponse = businessResponse;
        this.gradeControllerId = UUID.randomUUID().toString();
        log.debug("gradeControllerId {}", gradeControllerId);
        log.debug("GradeController loaded successfully");
    }

    @Override
    @GetMapping(path = GET_GRADE_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Object>> findAll() {

        log.debug("method findAll initialized successfully");
        log.debug("gradeControllerId {}", gradeControllerId);
        ThreadContext.put(LoggerConstants.CONSTANTS_LOG_METHOD, FIND_ALL_GRADE_LOG_METHOD);

        return this.gradeServices
            .getAll()
            .flatMap(processResponse -> {

                if (processResponse.isErrorProcessResponse() || processResponse.isEmptySuccessfullyResponse()) {
                    return Mono.just(businessResponse
                        .getResponse(processResponse.getResponseCode().getResponseCodeValue()));
                }

                return Mono.just(businessResponse
                    .getResponse(processResponse.getBusinessResponse(),
                        processResponse.getResponseCode().getResponseCodeValue()));
            })
            .doOnSuccess(success ->
                log.info("finish process findAll")
            )
            .onErrorResume(WebExchangeBindException.class, Mono::error)

            .onErrorResume(CommonBusinessProcessException.class, e -> Mono
                .just(
                    businessResponse
                        .getResponse(e.getResponseCode().getResponseCodeValue())))
            .doOnError(throwable ->
                log.error("exception error in process findAll, error: {}", throwable.getMessage())
            );
    }

    @Override
    @GetMapping(path = GET_GRADE_ID_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Object>> getById(Long gradeId) {

        log.debug("method getById initialized successfully");
        log.debug("gradeControllerId {}", gradeControllerId);
        ThreadContext.put(LoggerConstants.CONSTANTS_LOG_METHOD, FIND_BY_ID_GRADE_LOG_METHOD);

        return this.gradeServices.getById(gradeId)
            .flatMap(processResponse -> {
                if (processResponse.isErrorProcessResponse() || processResponse.isEmptySuccessfullyResponse()) {
                    return Mono.just(businessResponse
                        .getResponse(processResponse.getResponseCode().getResponseCodeValue()));
                }
                return Mono.just(businessResponse
                    .getResponse(processResponse.getBusinessResponse(),
                        processResponse.getResponseCode().getResponseCodeValue()));
            }).doOnSuccess(success ->
                log.info("finish process getById")
            )
            .onErrorResume(WebExchangeBindException.class, Mono::error)
            .onErrorResume(CommonBusinessProcessException.class, e -> Mono
                .just(
                    businessResponse
                        .getResponse(e.getResponseCode().getResponseCodeValue())))
            .doOnError(throwable ->
                log.error("exception error in process getById, error: {}", throwable.getMessage())
            );
    }

    @Override
    @PostMapping(value = SAVE_GRADE_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Object>> save(Mono<RegisterGradeRequest> request) {

        log.debug("method save initialized successfully");
        log.debug("gradeControllerId {}", gradeControllerId);
        ThreadContext.put(LoggerConstants.CONSTANTS_LOG_METHOD, SAVE_GRADE_LOG_METHOD);

        return this.gradeServices.save(request)
            .flatMap(processResponse -> {
                if (processResponse.isErrorProcessResponse() || processResponse.isEmptySuccessfullyResponse()) {
                    return Mono.just(businessResponse
                        .getResponse(processResponse.getResponseCode().getResponseCodeValue()));
                }
                return Mono.just(businessResponse
                    .getResponse(processResponse.getBusinessResponse(),
                        processResponse.getResponseCode().getResponseCodeValue()));
            }).doOnSuccess(success ->
                log.info("finish process save")
            )
            .onErrorResume(WebExchangeBindException.class, Mono::error)
            .onErrorResume(CommonBusinessProcessException.class, e -> Mono
                .just(
                    businessResponse
                        .getResponse(e.getResponseCode().getResponseCodeValue())))
            .doOnError(throwable ->
                log.error("exception error in process save, error: {}", throwable.getMessage())
            );
    }

    @Override
    @PutMapping(value = MICROSERVICE_GRADE_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Object>> update(Mono<RegisterGradeRequest> request) {
        log.debug("method update initialized successfully");
        log.debug("gradeControllerId {}", gradeControllerId);
        ThreadContext.put(LoggerConstants.CONSTANTS_LOG_METHOD, UPDATE_GRADE_LOG_METHOD);

        return this.gradeServices.update(request)
            .flatMap(processResponse -> {
                if (processResponse.isErrorProcessResponse() || processResponse.isEmptySuccessfullyResponse()) {
                    return Mono.just(businessResponse
                        .getResponse(processResponse.getResponseCode().getResponseCodeValue()));
                }
                return Mono.just(businessResponse
                    .getResponse(processResponse.getBusinessResponse(),
                        processResponse.getResponseCode().getResponseCodeValue()));
            }).doOnSuccess(success ->
                log.info("finish process update")
            )
            .onErrorResume(WebExchangeBindException.class, Mono::error)
            .onErrorResume(CommonBusinessProcessException.class, e -> Mono
                .just(
                    businessResponse
                        .getResponse(e.getResponseCode().getResponseCodeValue())))
            .doOnError(throwable ->
                log.error("exception error in process update, error: {}", throwable.getMessage())
            );
    }
}
