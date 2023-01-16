package pe.mil.microservices.services.contracts;

import pe.mil.microservices.dto.Grade;
import pe.mil.microservices.dto.requests.RegisterGradeRequest;
import pe.mil.microservices.utils.dtos.process.BusinessProcessResponse;
import pe.mil.microservices.utils.service.interfaces.*;
import reactor.core.publisher.Mono;

public interface IGradeServices
    extends
    IGetDomainEntityById<Mono<BusinessProcessResponse>, Long>,
    IGetAllDomainEntity<Mono<BusinessProcessResponse>>,
    ISaveDomainEntity<Mono<BusinessProcessResponse>, Mono<RegisterGradeRequest>>,
    IUpdateDomainEntity<Mono<BusinessProcessResponse>, Mono<RegisterGradeRequest>>,
    IDeleteDomainEntity<Grade>{
}
