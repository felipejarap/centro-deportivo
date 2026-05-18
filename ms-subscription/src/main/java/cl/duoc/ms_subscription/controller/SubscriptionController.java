package cl.duoc.ms_subscription.controller;

import cl.duoc.ms_subscription.dto.SubscriptionRequestDto;
import cl.duoc.ms_subscription.dto.SubscriptionResponseDto;
import cl.duoc.ms_subscription.model.Subscription;
import cl.duoc.ms_subscription.service.SubscriptionService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/subscription")
@RequiredArgsConstructor
public class SubscriptionController {
    private final SubscriptionService service;

    @GetMapping
    public ResponseEntity<List<SubscriptionResponseDto>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubscriptionResponseDto> findById(@PathVariable Long id) {
        SubscriptionResponseDto subscription = service.findById(id);

        if (subscription == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(subscription);
    }

    @GetMapping("/by-user/{userId}")
    public ResponseEntity<List<SubscriptionResponseDto>> findByUserId(@PathVariable Long userId) throws Exception {

        try {
            return ResponseEntity.ok(service.findByUserId(userId));

        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/by-plans/{plansId}")
    public ResponseEntity<List<SubscriptionResponseDto>> findByPlansId(@PathVariable Long plansId) throws Exception {

        try {
            return ResponseEntity.ok(service.findByPlansId(plansId));

        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<SubscriptionResponseDto> create(@Valid @RequestBody SubscriptionRequestDto subscription) {
        SubscriptionResponseDto addSubscription = service.create(subscription);
        return ResponseEntity.status(HttpStatus.CREATED).body(addSubscription);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SubscriptionResponseDto> update(@PathVariable Long id, @Valid @RequestBody SubscriptionRequestDto subscription){
        SubscriptionResponseDto updatedSubscription = service.update(id, subscription);
        if(updatedSubscription == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedSubscription);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        boolean deleted = service.deleteById(id);
        if(deleted){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

}
