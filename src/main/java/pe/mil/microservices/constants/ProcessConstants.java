package pe.mil.microservices.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ProcessConstants {

    public static final String PROCESS_TYPE_STRING = "String";
    public static final String PARAM_COMPONENT_UUID = "String";
    public static final String PARAMETER_EMPTY_VALUE = "";
    public static final String PARAMETER_ACTUATOR_PATH_CONTAIN_VALUE = "actuator";
    public static final String MICROSERVICE_PATH_CONTEXT = "";
    public static final String MICROSERVICE_GRADE_PATH = MICROSERVICE_PATH_CONTEXT + "/grades";
    public static final String GET_GRADE_PATH = "";
    public static final String SAVE_GRADE_PATH = "";
    public static final String GET_GRADE_ID_PATH = "/{gradeId}";
    public static final String FIND_ALL_GRADE_LOG_METHOD = "find.grades.method";
    public static final String FIND_BY_ID_GRADE_LOG_METHOD = "findById.grades.method";
    public static final String SAVE_GRADE_LOG_METHOD = "save.grades.method";
    public static final String UPDATE_GRADE_LOG_METHOD = "update.grades.method";
    public static final String MAPSTRUCT_COMPONENT_MODEL_CONFIGURATION = "spring";
}