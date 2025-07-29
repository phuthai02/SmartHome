package project.smarthome.dataservice.service.mysql;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import project.smarthome.common.dto.request.FilterRequest;
import project.smarthome.common.dto.response.PageFilterResponse;
import project.smarthome.common.utils.Constants;
import project.smarthome.dataservice.service.BaseService;

import java.util.*;

@Slf4j
public abstract class BaseMySQLService<T, ID> implements BaseService<T, ID> {

    protected abstract JpaRepository<T, ID> getRepository();

    protected abstract JpaSpecificationExecutor<T> getSpecificationExecutor();

    @Override
    public Optional<T> findById(ID id) {
        return getRepository().findById(id);
    }

    @Override
    public List<T> findAll() {
        return getRepository().findAll();
    }

    @Override
    public T save(T entity) {
        return getRepository().save(entity);
    }

    @Override
    public T update(ID id, T entity) {
        if (!getRepository().existsById(id)) {
            throw new RuntimeException("Entity with ID " + id + " not found");
        }
        return getRepository().save(entity);
    }

    @Override
    public void deleteById(ID id) {
        getRepository().deleteById(id);
    }

    @Override
    public PageFilterResponse<T> findByPageFilter(List<FilterRequest> filters, Pageable pageable) {
        Specification<T> spec = (root, query, cb) -> {
            List<Predicate> predicates = filters.stream()
                    .filter(f -> f.getField() != null && f.getOperator() != null)
                    .map(f -> createPredicate(root, cb, f))
                    .filter(Objects::nonNull)
                    .toList();
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<T> page = getSpecificationExecutor().findAll(spec, pageable);

        return PageFilterResponse.<T>builder()
                .content(page.getContent())
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .first(page.isFirst())
                .build();
    }


    @SuppressWarnings("unchecked")
    private Predicate createPredicate(Root<T> root, CriteriaBuilder cb, FilterRequest filter) {
        try {
            Path<Object> fieldPath = getFieldPath(root, filter.getField());
            String operator = filter.getOperator();
            Object value = filter.getValue();
            Object valueFrom = filter.getValueFrom();
            Object valueTo = filter.getValueTo();

            return switch (operator) {
                case Constants.FilterOperator.EQUALS -> value != null ? cb.equal(fieldPath, value) : null;
                case Constants.FilterOperator.NOT_EQUALS -> value != null ? cb.notEqual(fieldPath, value) : null;
                case Constants.FilterOperator.LIKE -> value != null ? cb.like(cb.lower(fieldPath.as(String.class)), "%" + value.toString().toLowerCase() + "%") : null;
                case Constants.FilterOperator.GREATER_THAN -> value instanceof Comparable ? cb.greaterThan(fieldPath.as(Comparable.class), (Comparable) value) : null;
                case Constants.FilterOperator.GREATER_THAN_OR_EQUAL -> value instanceof Comparable ? cb.greaterThanOrEqualTo(fieldPath.as(Comparable.class), (Comparable) value) : null;
                case Constants.FilterOperator.LESS_THAN -> value instanceof Comparable ? cb.lessThan(fieldPath.as(Comparable.class), (Comparable) value) : null;
                case Constants.FilterOperator.LESS_THAN_OR_EQUAL -> value instanceof Comparable ? cb.lessThanOrEqualTo(fieldPath.as(Comparable.class), (Comparable) value) : null;
                case Constants.FilterOperator.BETWEEN -> (valueFrom instanceof Comparable && valueTo instanceof Comparable) ? cb.between(fieldPath.as(Comparable.class), (Comparable) valueFrom, (Comparable) valueTo) : null;
                case Constants.FilterOperator.IN -> (value instanceof Collection<?> values && !values.isEmpty()) ? fieldPath.in(values) : null;
                case Constants.FilterOperator.NOT_IN -> (value instanceof Collection<?> values && !values.isEmpty()) ? cb.not(fieldPath.in(values)) : null;
                case Constants.FilterOperator.IS_NULL -> cb.isNull(fieldPath);
                case Constants.FilterOperator.IS_NOT_NULL -> cb.isNotNull(fieldPath);
                default -> null;
            };
        } catch (Exception e) {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    private Path<Object> getFieldPath(Root<T> root, String field) {
        return Arrays.stream(field.split("\\.")).reduce((Path<Object>) root, Path::get, (p1, p2) -> p2);
    }
}
