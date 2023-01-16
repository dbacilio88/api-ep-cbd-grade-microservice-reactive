package pe.mil.microservices.services.implementations;

import com.google.common.collect.Lists;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.mil.microservices.components.enums.GradesValidationResult;
import pe.mil.microservices.components.mappers.contracts.IGradesMapperByMapstruct;
import pe.mil.microservices.components.validations.IGradeRegisterValidation;
import pe.mil.microservices.dto.Grade;
import pe.mil.microservices.dto.requests.RegisterGradeRequest;
import pe.mil.microservices.dto.responses.RegisterGradeResponse;
import pe.mil.microservices.repositories.contracts.IGradeRepository;
import pe.mil.microservices.repositories.entities.GradesEntity;
import pe.mil.microservices.services.contracts.IGradeServices;
import pe.mil.microservices.utils.components.enums.ResponseCode;
import pe.mil.microservices.utils.components.exceptions.CommonBusinessProcessException;
import pe.mil.microservices.utils.components.helpers.ObjectMapperHelper;
import pe.mil.microservices.utils.dtos.base.GenericBusinessResponse;
import pe.mil.microservices.utils.dtos.process.BusinessProcessResponse;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Log4j2
@Service
public class GradeService implements IGradeServices {

    private final IGradeRepository gradeRepository;
    private final String gradeServiceId;


    @Autowired
    public GradeService(
        final IGradeRepository gradeRepository
    ) {
        this.gradeRepository = gradeRepository;
        gradeServiceId = UUID.randomUUID().toString();
        log.debug("gradeServiceIdv {}", gradeServiceId);
        log.debug("CustomerService loaded successfully");
    }

    @Override
    public Mono<BusinessProcessResponse> getById(Long id) {

        log.info("this is in services getById method");
        log.debug("gradeServiceId {}", gradeServiceId);

        GenericBusinessResponse<Grade> genericMessagesBusinessResponse = new GenericBusinessResponse<>();

        return Mono
            .just(genericMessagesBusinessResponse)
            .flatMap(generic -> {
                final Optional<GradesEntity> entity = this.gradeRepository.findById(id);
                if (entity.isEmpty()) {
                    return Mono.error(() -> new CommonBusinessProcessException(ResponseCode.ERROR_IN_REQUESTED_DATA));
                } else {
                    return Mono.just(entity.get());
                }
            })
            .flatMap(entity -> {
                final Grade target = ObjectMapperHelper.map(entity, Grade.class);
                log.info("target {} ", target.toString());
                GenericBusinessResponse<Grade> data = new GenericBusinessResponse<>(target);
                return Mono.just(data);
            })
            .flatMap(response -> {
                return Mono.just(BusinessProcessResponse.setEntitySuccessfullyResponse(response));
            })
            .doOnSuccess(success ->
                log.info("finish process getById, success: {}", success)
            )
            .doOnError(throwable ->
                log.error("exception error in process getById, error: {}", throwable.getMessage())
            );
    }

    @Override
    public Mono<BusinessProcessResponse> getAll() {
        log.info("this is in services getAll method");
        log.debug("gradeServiceId {}", gradeServiceId);
        GenericBusinessResponse<List<Grade>> genericMessagesBusinessResponse = new GenericBusinessResponse<>();

        return Mono.just(genericMessagesBusinessResponse)
            .flatMap(generic -> {
                generic.setData(ObjectMapperHelper.mapAll(Lists.newArrayList(this.gradeRepository.findAll()), Grade.class));
                return Mono.just(generic);
            })
            .flatMap(response -> {
                log.info("response {} ", response.getData().toString());
                return Mono.just(BusinessProcessResponse
                    .setEntitySuccessfullyResponse(response));
            }).flatMap(process -> Mono.just(BusinessProcessResponse.setEntitySuccessfullyResponse(process.getBusinessResponse())))
            .doOnSuccess(success ->
                log.info("finish process getById, success: {}", success.toString())
            )
            .doOnError(throwable ->
                log.error("exception error in process getById, error: {}", throwable.getMessage())
            );
    }

    @Override
    public Mono<BusinessProcessResponse> save(Mono<RegisterGradeRequest> entity) {
        log.info("this is in services save  method");
        log.debug("gradeServiceId {}", gradeServiceId);

        return entity
            .flatMap(create -> {
                log.debug("this is in services save demo method");
                final GradesValidationResult result =
                    IGradeRegisterValidation
                        .isGradeIdValidation()
                        .and(IGradeRegisterValidation.isGradeNameValidation())
                        .and(IGradeRegisterValidation.isGradeDescriptionValidation())
                        .apply(create);
                log.info("result {} ", result);
                if (!GradesValidationResult.GRADES_VALID.equals(result)) {
                    return Mono.error(() -> new CommonBusinessProcessException(ResponseCode.ERROR_IN_REQUESTED_DATA));
                }
                return Mono.just(create);
            })
            .flatMap(request -> {
                log.debug("log in flatMap context #2");
                final GradesEntity save =
                    IGradesMapperByMapstruct.GRADES_MAPPER.mapGradeEntityByRegisterGradeRequest(request);
                log.info("save entity {} ", save);
                boolean exists = this.gradeRepository.existsByGradeId(save.getGradeId());
                log.info("exists entity {} ", exists);

                if (exists) {
                    return Mono.error(() -> new CommonBusinessProcessException(ResponseCode.ERROR_IN_REQUESTED_DATA_EXISTS));
                }

                final GradesEntity saved = this.gradeRepository.save(save);
                log.info("saved entity {} ", saved);

                if (Objects.isNull(saved.getGradeId())) {
                    return Mono.error(() -> new CommonBusinessProcessException(ResponseCode.ERROR_IN_REQUESTED_DATA));
                }

                return Mono.just(saved);
            })
            .flatMap(grade -> {
                log.info("entity {} ", grade);
                final RegisterGradeResponse response = ObjectMapperHelper
                    .map(grade, RegisterGradeResponse.class);
                return Mono.just(BusinessProcessResponse
                    .setEntitySuccessfullyResponse(new GenericBusinessResponse<>(response)));
            })
            .doOnSuccess(success ->
                log.info("finish process save, success: {}", success)
            )
            .doOnError(throwable ->
                log.error("exception error in process save, error: {}", throwable.getMessage())
            );
    }

    @Override
    public Boolean delete(Grade entity) {

        log.debug("this is in services delete method");
        log.debug("gradeServiceId {}", gradeServiceId);

        final Optional<GradesEntity> result = this.gradeRepository.findById(entity.getGradeId());

        if (result.isEmpty()) {
            return false;
        }

        this.gradeRepository.delete(result.get());

        return true;
    }

    @Override
    public Mono<BusinessProcessResponse> update(Mono<RegisterGradeRequest> entity) {

        log.info("this is in services update method");
        log.debug("gradeServiceId {}", gradeServiceId);

        return entity.
            flatMap(update -> {
                log.debug("this is in services update method");
                final GradesValidationResult result = IGradeRegisterValidation
                    .isGradeNameValidation().apply(update);
                if (!GradesValidationResult.GRADES_VALID.equals(result)) {
                    return Mono.error(() -> new CommonBusinessProcessException(ResponseCode.ERROR_IN_REQUESTED_DATA));
                }
                return Mono.just(update);
            })
            .flatMap(request -> {
                log.debug("log in flatMap context #2");
                final GradesEntity update = IGradesMapperByMapstruct
                    .GRADES_MAPPER
                    .mapGradeEntityByRegisterGradeRequest(request);

                final GradesEntity updated = this.gradeRepository.save(update);

                if (Objects.isNull(updated.getGradeId())) {
                    return Mono.error(() -> new CommonBusinessProcessException(ResponseCode.ERROR_IN_REQUESTED_DATA));
                }
                return Mono.just(updated);

            })
            .flatMap(grade -> {
                final RegisterGradeResponse response = ObjectMapperHelper
                    .map(grade, RegisterGradeResponse.class);
                return Mono.just(BusinessProcessResponse
                    .setEntitySuccessfullyResponse(new GenericBusinessResponse<>(response)));
            }).doOnSuccess(success ->
                log.info("finish process save, success: {}", success)
            )
            .doOnError(throwable ->
                log.error("exception error in process save, error: {}", throwable.getMessage())
            );
    }
}
