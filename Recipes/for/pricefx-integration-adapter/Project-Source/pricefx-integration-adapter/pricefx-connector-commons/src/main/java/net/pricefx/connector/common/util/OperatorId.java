package net.pricefx.connector.common.util;


public enum OperatorId {
    EQUALS("equals"),
    NOT_EQUAL("notEqual"),
    IEQUALS("iEquals"),
    INOT_EQUAL("iNotEqual"),
    GREATER_THAN("greaterThan"),
    LESS_THAN("lessThan"),
    GREATER_OR_EQUAL("greaterOrEqual"),
    LESS_OR_EQUAL("lessOrEqual"),
    CONTAINS("contains"),
    STARTS_WITH("startsWith"),
    ENDS_WITH("endsWith"),
    ICONTAINS("iContains"),
    ISTARTS_WITH("iStartsWith"),
    IENDS_WITH("iEndsWith"),
    NOT_CONTAINS("notContains"),
    NOT_STARTS_WITH("notStartsWith"),
    NOT_ENDS_WITH("notEndsWith"),
    INOT_CONTAINS("iNotContains"),
    INOT_STARTS_WITH("iNotStartsWith"),
    INOT_ENDS_WITH("iNotEndsWith"),
    REGEXP("regexp"),
    IREGEXP("iregexp"),
    IS_NULL("isNull"),
    NOT_NULL("notNull"),
    IN_SET("inSet"),
    NOT_IN_SET("notInSet"),
    EQUALS_FIELD("equalsField"),
    NOT_EQUAL_FIELD("notEqualField"),
    GREATER_THAN_FIELD("greaterThanField"),
    LESS_THAN_FIELD("lessThanField"),
    GREATER_OR_EQUAL_FIELD("greaterOrEqualField"),
    LESS_OR_EQUAL_FIELD("lessOrEqualField"),
    CONTAINS_FIELD("containsField"),
    STARTS_WITH_FIELD("startsWithField"),
    ENDS_WITH_FIELD("endsWithField"),
    AND("and"),
    NOT("not"),
    OR("or"),
    BETWEEN("between"),
    BETWEEN_INCLUSIVE("betweenInclusive");

    private final String value;

    OperatorId(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }


}
