package project.smarthome.dataservice.service;

import org.springframework.data.domain.Pageable;
import project.smarthome.common.dto.request.FilterRequest;
import project.smarthome.common.dto.response.PageFilterResponse;

import java.util.List;
import java.util.Optional;

public interface BaseService<T, ID> {
    Optional<T> findById(ID id);
    List<T> findAll();
    T save(T entity);
    T update(ID id, T entity);
    void deleteById(ID id);
    PageFilterResponse<T> findByPageFilter(List<FilterRequest> filters, Pageable pageable);
}