package project.smarthome.common.utils;

public class Constants {

    public static final String JWT_BEARER = "Bearer ";
    public static final String AUTHORIZATION = "Authorization";
    public static final String ACCESS_TOKEN = "Access-Token";
    public static final String REFRESH_TOKEN = "Refresh-Token";
    public static final String USER_INFO = "User-Info";

    public static class Message {
        public static final String SYSTEM_ERROR = "Đã có lỗi xảy ra";
    }

    public static class ClientType {
        public static final String ADMIN = "ADMIN";
        public static final String CUSTOMER = "CUSTOMER";
    }

    public static class Role {
        public static final String ADMIN = "ROLE_ADMIN";
        public static final String CUSTOMER = "ROLE_USER";
        public static final String ADMIN_TEXT = "Quản trị viên";
        public static final String CUSTOMER_TEXT = "Khách hàng";
    }

    public static class Format {
        public static final String TOPIC_COMMAND = "TOPIC-COMMAND/VHome+/%s";
        public static final String TOPIC_CONTENT = "TOPIC-CONTENT/VHome+/%s";
        public static final String TOPIC_ALERT = "TOPIC-ALERT/VHome+/%s";
        public static final String ACCESS_TOKEN = "jwt:access:%s";
        public static final String REFRESH_TOKEN = "jwt:refresh:%s";
        public static final String TEMPLATE_PAGE_PATH = "classpath:/templates/contents/%s.html";
    }

    public static class Pattern {
        public static final String ACCESS_TOKEN = "jwt:access:*";
    }

    public static class Prefix {
        public static final String ACCESS_TOKEN = "jwt:access:";
    }

    public static class Status {
        public static final String LOCKED = "LOCKED";
        public static final String ACTIVE = "ACTIVE";
    }

    public static class FilterOperator {
        public static final String EQUALS = "eq";
        public static final String NOT_EQUALS = "ne";
        public static final String LIKE = "like";
        public static final String GREATER_THAN = "gt";
        public static final String GREATER_THAN_OR_EQUAL = "gte";
        public static final String LESS_THAN = "lt";
        public static final String LESS_THAN_OR_EQUAL = "lte";
        public static final String BETWEEN = "between";
        public static final String IN = "in";
        public static final String NOT_IN = "not_in";
        public static final String IS_NULL = "is_null";
        public static final String IS_NOT_NULL = "is_not_null";
    }
}
