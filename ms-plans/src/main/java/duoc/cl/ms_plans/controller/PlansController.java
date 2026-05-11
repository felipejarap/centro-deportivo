package duoc.cl.ms_plans.controller;

import duoc.cl.ms_plans.dto.PlansRequestDto;
import duoc.cl.ms_plans.dto.PlansResponseDto;
import duoc.cl.ms_plans.service.PlansService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/plans")
public class PlansController {
    private final PlansService service;

    @GetMapping
    public ResponseEntity<List<PlansResponseDto>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlansResponseDto> findById(@PathVariable Long id) {
        PlansResponseDto plan = service.findById(id);

        if (plan == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(plan);
    }

    @PostMapping
    public ResponseEntity<PlansResponseDto> create(@Valid @RequestBody PlansRequestDto plan) {
        PlansResponseDto addPlan = service.create(plan);
        return ResponseEntity.status(HttpStatus.CREATED).body(addPlan);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlansResponseDto> update(@PathVariable Long id, @Valid @RequestBody PlansRequestDto plan) {
        PlansResponseDto updatePlan = service.update(id, plan);
        if (updatePlan == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatePlan);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<PlansResponseDto> delete(@PathVariable Long id) {
        boolean delPlan = service.deleteById(id);
        if (delPlan) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }


}
