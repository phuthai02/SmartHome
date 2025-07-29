package project.smarthome.common.dto.request;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PageFilterRequest {
    private int page;
    private int size;
    private String sortField;
    private String sortDir;
    private List<FilterRequest> filters;

    public PageFilterRequest() {
        this.page = 0;
        this.size = 10;
        this.sortField = "id";
        this.sortDir = "ASC";
        this.filters = new ArrayList<>();
    }
}
