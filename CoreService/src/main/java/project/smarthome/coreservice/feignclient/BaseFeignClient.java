package project.smarthome.coreservice.feignclient;

import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface BaseFeignClient<T, ID> {

    @GetMapping("/{id}")
    T findById(@PathVariable("id") ID id);

    @GetMapping
    List<T> findAll();

    @PostMapping
    T create(@RequestBody T entity);

    @PutMapping("/{id}")
    T update(@PathVariable("id") ID id, @RequestBody T entity);

    @DeleteMapping("/{id}")
    void delete(@PathVariable("id") ID id);
}
