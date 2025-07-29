package project.smarthome.dataservice.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.smarthome.common.dto.request.PageFilterRequest;
import project.smarthome.common.dto.response.PageFilterResponse;
import project.smarthome.common.utils.JsonUtils;
import project.smarthome.dataservice.service.BaseService;

import java.util.List;

@Slf4j
public abstract class BaseController<T, ID> {

    protected abstract BaseService<T, ID> getBaseService();

    protected abstract String getEntityName();

    protected String getLogPrefix() {
        return "[" + getEntityName().toUpperCase() + "]";
    }

    @GetMapping("/{id}")
    public ResponseEntity<T> findById(@PathVariable ID id) {
        log.info("{} GET {} BY ID: {}", getLogPrefix(), getEntityName().toUpperCase(), id);
        return getBaseService().findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<T>> findAll() {
        log.info("{} GET ALL {}", getLogPrefix(), getEntityName().toUpperCase());
        return ResponseEntity.ok(getBaseService().findAll());
    }

    @PostMapping
    public ResponseEntity<T> create(@RequestBody T entity) {
        log.info("{} CREATE NEW {}: {}", getLogPrefix(), getEntityName().toUpperCase(), entity);
        return ResponseEntity.ok(getBaseService().save(entity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<T> update(@PathVariable ID id, @RequestBody T entity) {
        log.info("{} UPDATE {} ID: {}, DATA: {}", getLogPrefix(), getEntityName().toUpperCase(), id, entity);
        try {
            return ResponseEntity.ok(getBaseService().update(id, entity));
        } catch (RuntimeException ex) {
            log.warn("{} FAILED TO UPDATE {} ID: {} (NOT FOUND)", getLogPrefix(), getEntityName().toUpperCase(), id);
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable ID id) {
        log.info("{} DELETE {} BY ID: {}", getLogPrefix(), getEntityName().toUpperCase(), id);
        getBaseService().deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/page-filter")
    public ResponseEntity<PageFilterResponse<T>> findByPageFilter(@RequestBody PageFilterRequest request) {
        log.info("{} GET {} BY PAGE: page={}, size={}, sortField={}, sortDir={}, filters={}",
                getLogPrefix(), getEntityName().toUpperCase(),
                request.getPage(), request.getSize(),
                request.getSortField(), request.getSortDir(),
                JsonUtils.toJson(request.getFilters())
        );
        Sort sort = Sort.by(Sort.Direction.fromString(request.getSortDir()), request.getSortField());
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);
        PageFilterResponse<T> result = getBaseService().findByPageFilter(request.getFilters(), pageable);
        return ResponseEntity.ok(result);
    }
}