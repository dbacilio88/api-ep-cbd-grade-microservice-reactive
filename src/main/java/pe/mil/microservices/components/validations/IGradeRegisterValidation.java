package pe.mil.microservices.components.validations;

import pe.mil.microservices.components.enums.GradesValidationResult;
import pe.mil.microservices.dto.requests.RegisterGradeRequest;

import java.util.function.Function;
import java.util.function.Predicate;

@FunctionalInterface
public interface IGradeRegisterValidation
    extends Function<RegisterGradeRequest, GradesValidationResult> {

    static IGradeRegisterValidation isGradeNameValidation() {
        return request ->
            request.getName() != null
                && !request.getName().isEmpty()
                && !request.getName().isBlank()

                ? GradesValidationResult.GRADES_VALID
                : GradesValidationResult.INVALID_GRADES_NAME;
    }

    static IGradeRegisterValidation isGradeIdValidation() {
        return request ->
            request.getGradeId() != null
                ? GradesValidationResult.GRADES_VALID
                : GradesValidationResult.INVALID_GRADES_ID;
    }

    static IGradeRegisterValidation isGradeDescriptionValidation() {
        return request ->
            request.getDescription() != null
                && !request.getDescription().isBlank()
                && !request.getDescription().isEmpty()
                ? GradesValidationResult.GRADES_VALID
                : GradesValidationResult.INVALID_GRADES_DESCRIPTION;
    }

    static IGradeRegisterValidation customValidation(Predicate<RegisterGradeRequest> validate) {
        return request -> validate.test(request)
            ? GradesValidationResult.GRADES_VALID
            : GradesValidationResult.INVALID_GRADES_NAME;
    }

    default IGradeRegisterValidation and(IGradeRegisterValidation andValidation) {
        return request -> {
            GradesValidationResult validation = this.apply(request);
            return validation.equals(GradesValidationResult.GRADES_VALID)
                ? andValidation.apply(request)
                : validation;
        };
    }

    default IGradeRegisterValidation or(IGradeRegisterValidation orValidation) {
        return request -> {
            GradesValidationResult validation = this.apply(request);
            return validation.equals(GradesValidationResult.GRADES_VALID)
                ? orValidation.apply(request)
                : validation;
        };
    }
}
