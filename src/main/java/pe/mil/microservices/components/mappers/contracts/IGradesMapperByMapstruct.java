package pe.mil.microservices.components.mappers.contracts;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import pe.mil.microservices.dto.requests.RegisterGradeRequest;
import pe.mil.microservices.repositories.entities.GradesEntity;

import static pe.mil.microservices.components.mappers.contracts.IGradesMapperByMapstruct.CustomerFields.*;

@Mapper
public interface IGradesMapperByMapstruct {

    IGradesMapperByMapstruct GRADES_MAPPER = Mappers
        .getMapper(IGradesMapperByMapstruct.class);

      @Condition
      default boolean isNotEmpty(String value) {
          return value != null && value.length() > 0;
      }

    @Mapping(source = FIELD_GRADE_ID, target = FIELD_GRADE_ID)
    @Mapping(source = FIELD_GRADE_NAME, target = FIELD_GRADE_NAME)
    @Mapping(source = FIELD_GRADE_DESCRIPTION, target = FIELD_GRADE_DESCRIPTION)
    GradesEntity mapGradeEntityByRegisterGradeRequest(RegisterGradeRequest source);


    class CustomerFields {
        public static final String FIELD_GRADE_ID = "gradeId";
        public static final String FIELD_GRADE_NAME = "name";
        public static final String FIELD_GRADE_DESCRIPTION = "description";
    }
}
