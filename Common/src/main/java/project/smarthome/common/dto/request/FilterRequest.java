package project.smarthome.common.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FilterRequest {
    private String field;
    private String operator;
    private Object value;
    private Object valueFrom;
    private Object valueTo;

    public FilterRequest(String field, String operator, Object value) {
        this.field = field;
        this.operator = operator;
        this.value = value;
    }

    public FilterRequest(String field, String operator, Object valueFrom, Object valueTo) {
        this.field = field;
        this.operator = operator;
        this.valueFrom = valueFrom;
        this.valueTo = valueTo;
    }
}
