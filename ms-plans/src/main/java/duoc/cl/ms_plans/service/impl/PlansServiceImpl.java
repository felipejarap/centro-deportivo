package duoc.cl.ms_plans.service.impl;

import duoc.cl.ms_plans.dto.PlansRequestDto;
import duoc.cl.ms_plans.dto.PlansResponseDto;
import duoc.cl.ms_plans.model.Plans;
import duoc.cl.ms_plans.repository.PlansRepository;
import duoc.cl.ms_plans.service.PlansService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlansServiceImpl implements PlansService {

    private final PlansRepository repository;

    private Plans toEntity(PlansResponseDto dto) {
        return new Plans(dto.getId(),dto.getName(),dto.getPrice(),dto.getDurationDays());
    }

    private Plans toEntity(PlansRequestDto dto) {
        return new Plans(dto.getId(),dto.getName(),dto.getPrice(),dto.getDurationDays());

    }

    private PlansResponseDto toDto(Plans entity) {
        return new PlansResponseDto(entity.getId(),entity.getName(),entity.getPrice(),entity.getDurationDays());
    }


    @Override
    public List<PlansResponseDto> findAll() {
        return repository.findAll().stream().map(this::toDto).toList();
    }

    @Override
    public PlansResponseDto findById(Long id) {
        return repository.findById(id).map(this::toDto).orElse(null);
    }

    @Override
    public PlansResponseDto create(PlansRequestDto plans) {
        return toDto(repository.save(toEntity(plans)));
    }

    @Override
    public PlansResponseDto update(Long id, PlansRequestDto plans) {
        if (repository.existsById(id)) {
            Plans entity = toEntity(plans);
            entity.setId(id);
            return toDto(repository.save(entity));
        }
        return null;
    }

    @Override
    public boolean deleteById(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}
