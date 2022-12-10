package soya.framework.action;

public enum ParameterType {
    PATH_PARAM, QUERY_PARAM, HEADER_PARAM, COOKIE_PARAM, FORM_PARAM, MATRIX_PARAM, BEAN_PARAM, PAYLOAD;

    private static final ParameterType[] SEQUENCE
            = {PATH_PARAM, QUERY_PARAM, HEADER_PARAM, COOKIE_PARAM, FORM_PARAM, MATRIX_PARAM, BEAN_PARAM, PAYLOAD};

    public static final int index(ParameterType type) {
        int i = 0;
        for (ParameterType p : SEQUENCE) {
            if (p.equals(type)) {
                return i;
            } else {
                i++;
            }
        }

        return -1;
    }
}
