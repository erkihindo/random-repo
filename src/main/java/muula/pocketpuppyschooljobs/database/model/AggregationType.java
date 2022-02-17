package muula.pocketpuppyschooljobs.database.model;

import lombok.Getter;

public enum AggregationType {

    LIKE_COUNT_V1(Number.class),
    POST_COUNT_V1(Number.class),
    COMMENT_COUNT_V1(Number.class),
    REPORT_COUNT_V1(Number.class),
    LATEST_LIKE_DATETIME(String.class),
    LATEST_COMMENT_DATETIME(String.class);

    AggregationType(Class<?> aClass) {
        this.classType = aClass;
    }

    @Getter
    private Class classType;
}
