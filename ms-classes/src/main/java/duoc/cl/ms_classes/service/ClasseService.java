package duoc.cl.ms_classes.service;

import duoc.cl.ms_classes.dto.ClasseRequestDto;
import duoc.cl.ms_classes.dto.ClasseResponseDto;

import java.util.List;

public interface ClasseService {

    List<ClasseResponseDto> findAll();
    ClasseResponseDto findById(Long id);
    ClasseResponseDto create(ClasseRequestDto classe);
    ClasseResponseDto update(Long id,ClasseRequestDto classe);
    boolean deleteById(Long id);


}
