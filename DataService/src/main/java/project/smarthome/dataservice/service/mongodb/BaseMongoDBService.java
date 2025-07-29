package project.smarthome.dataservice.service.mongodb;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import project.smarthome.common.dto.request.FilterRequest;
import project.smarthome.common.dto.response.PageFilterResponse;
import project.smarthome.common.utils.Constants;

import java.util.*;

@Slf4j
public abstract class BaseMongoDBService<T, ID> {

    @Autowired
    protected MongoTemplate mongoTemplate;

    protected abstract MongoRepository<T, ID> getRepository();

    protected abstract Class<T> getEntityClass();

    public Optional<T> findById(ID id) {
        return getRepository().findById(id);
    }

    public List<T> findAll() {
        return getRepository().findAll();
    }

    public T save(T entity) {
        return getRepository().save(entity);
    }

    public void deleteById(ID id) {
        getRepository().deleteById(id);
    }

    public PageFilterResponse<T> findByPageFilter(List<FilterRequest> filters, Pageable pageable) {
        Criteria criteria = buildCriteria(filters);
        Query query = new Query(criteria).with(pageable);

        List<T> content = mongoTemplate.find(query, getEntityClass());
        long total = mongoTemplate.count(Query.of(query).limit(-1).skip(-1), getEntityClass());

        Page<T> page = new PageImpl<>(content, pageable, total);

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

    private Criteria buildCriteria(List<FilterRequest> filters) {
        List<Criteria> criteriaList = new ArrayList<>();

        for (FilterRequest filter : filters) {
            if (filter.getField() == null || filter.getOperator() == null) continue;
            Criteria c = createCriteria(filter);
            if (c != null) criteriaList.add(c);
        }

        return criteriaList.isEmpty() ? new Criteria() : new Criteria().andOperator(criteriaList.toArray(new Criteria[0]));
    }

    @SuppressWarnings("unchecked")
    private Criteria createCriteria(FilterRequest filter) {
        String field = filter.getField();
        String operator = filter.getOperator();
        Object value = filter.getValue();
        Object valueFrom = filter.getValueFrom();
        Object valueTo = filter.getValueTo();

        try {
            return switch (operator) {
                case Constants.FilterOperator.EQUALS -> Criteria.where(field).is(value);
                case Constants.FilterOperator.NOT_EQUALS -> Criteria.where(field).ne(value);
                case Constants.FilterOperator.LIKE -> Criteria.where(field).regex(".*" + value.toString() + ".*", "i");
                case Constants.FilterOperator.GREATER_THAN -> Criteria.where(field).gt(value);
                case Constants.FilterOperator.GREATER_THAN_OR_EQUAL -> Criteria.where(field).gte(value);
                case Constants.FilterOperator.LESS_THAN -> Criteria.where(field).lt(value);
                case Constants.FilterOperator.LESS_THAN_OR_EQUAL -> Criteria.where(field).lte(value);
                case Constants.FilterOperator.BETWEEN -> Criteria.where(field).gte(valueFrom).lte(valueTo);
                case Constants.FilterOperator.IN -> (value instanceof Collection<?> values && !values.isEmpty()) ? Criteria.where(field).in(values) : null;
                case Constants.FilterOperator.NOT_IN -> (value instanceof Collection<?> values && !values.isEmpty()) ? Criteria.where(field).nin(values) : null;
                case Constants.FilterOperator.IS_NULL -> Criteria.where(field).is(null);
                case Constants.FilterOperator.IS_NOT_NULL -> Criteria.where(field).ne(null);
                default -> null;
            };
        } catch (Exception e) {
            log.warn("Could not create criteria for filter: {}", filter, e);
            return null;
        }
    }
}
