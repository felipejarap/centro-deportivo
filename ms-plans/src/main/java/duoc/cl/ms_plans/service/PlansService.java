package duoc.cl.ms_plans.service;

import duoc.cl.ms_plans.dto.PlansRequestDto;
import duoc.cl.ms_plans.dto.PlansResponseDto;

import java.util.List;

public interface PlansService {
    List<PlansResponseDto> findAll();
    PlansResponseDto findById(Long id);
    PlansResponseDto create(PlansRequestDto plans);
    PlansResponseDto update(Long id,PlansRequestDto plans);
    boolean deleteById(Long id);
}
