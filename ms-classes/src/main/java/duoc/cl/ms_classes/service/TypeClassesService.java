package duoc.cl.ms_classes.service;

import duoc.cl.ms_classes.dto.TypeClassesRequestDto;
import duoc.cl.ms_classes.dto.TypeClassesResponseDto;

import java.util.List;

public interface TypeClassesService {

    List<TypeClassesResponseDto> findAll();
    TypeClassesResponseDto findById(Long id);
    TypeClassesResponseDto create(TypeClassesRequestDto type);
    TypeClassesResponseDto update(Long id, TypeClassesRequestDto type);
    boolean deleteById(Long id);
}
