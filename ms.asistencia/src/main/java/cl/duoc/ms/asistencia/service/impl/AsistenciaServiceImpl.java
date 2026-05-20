package cl.duoc.ms.asistencia.service.impl;

import cl.duoc.ms.asistencia.dto.AsistenciaRequestDto;
import cl.duoc.ms.asistencia.dto.AsistenciaResponseDto;
import cl.duoc.ms.asistencia.dto.ClasseResponseDto;
import cl.duoc.ms.asistencia.dto.UserResponseDto;
import cl.duoc.ms.asistencia.model.Asistencia;
import cl.duoc.ms.asistencia.repository.AsistenciaRepository;
import cl.duoc.ms.asistencia.service.AsistenciaService;
import cl.duoc.ms.asistencia.service.api.ClasseClient;
import cl.duoc.ms.asistencia.service.api.UsuarioClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AsistenciaServiceImpl implements AsistenciaService {
    private final AsistenciaRepository repository;
    private final UsuarioClient usuarioClient;
    private final ClasseClient classeClient;

    private Asistencia toEntity(AsistenciaRequestDto dto) {
        Asistencia a = new Asistencia();
        a.setIdUsuario(dto.getIdUsuario());
        a.setIdClasse(dto.getIdClasse());
        a.setHoraLlegada(dto.getHoraLlegada());
        a.setAsistio(dto.getAsistio());
        return a;
    }

    private AsistenciaResponseDto toDto(Asistencia entity) {
        AsistenciaResponseDto dto = new AsistenciaResponseDto();
        dto.setIdAsistencia(entity.getIdAsistencia());
        dto.setIdUsuario(entity.getIdUsuario());
        dto.setIdClasse(entity.getIdClasse());
        dto.setHoraLlegada(entity.getHoraLlegada());
        dto.setAsistio(entity.getAsistio());

        try {
                UserResponseDto usuario = usuarioClient.findById(entity.getIdUsuario());
                dto.setUsuario(usuario);
        } catch (Exception e) {
            dto.setUsuario(null);
        }



        try {
            ClasseResponseDto classe = classeClient.findById(entity.getIdClasse());
            dto.setClasse(classe);
        } catch (Exception e) {
            dto.setClasse(null);
        }

        return dto;
    }

    @Override
    public List<AsistenciaResponseDto> findAll() {
        return repository.findAll().stream().map(this::toDto).toList();
    }

    @Override
    public AsistenciaResponseDto findById(Long id) {
        return repository.findById(id).map(this::toDto).orElse(null);
    }

    @Override
    public AsistenciaResponseDto create(AsistenciaRequestDto asistencia) {

        // Validar que el usuario exista en su microservicio
            UserResponseDto userFind = usuarioClient.findById(asistencia.getIdUsuario());
            if (userFind == null || userFind.getId() == null) {
                throw new IllegalArgumentException("El usuario con ID " + asistencia.getIdUsuario() + " no existe.");
            }

            // Validar que la clase exista en su microservicio

            ClasseResponseDto classeFind = classeClient.findById(asistencia.getIdClasse());
            if (classeFind == null || classeFind.getId() == null) {
                throw new IllegalArgumentException("La clase con ID " + asistencia.getIdClasse() + " no existe.");
            }

        return toDto(repository.save(toEntity(asistencia)));
    }

    @Override
    public AsistenciaResponseDto update(Long id, AsistenciaRequestDto asistencia) {

        // Validar que el usuario exista en su microservicio
        UserResponseDto userFind = usuarioClient.findById(asistencia.getIdUsuario());
        if (userFind == null || userFind.getId() == null) {
            throw new IllegalArgumentException("El usuario con ID " + asistencia.getIdUsuario() + " no existe.");
        }

        // Validar que la clase exista en su microservicio

        ClasseResponseDto classeFind = classeClient.findById(asistencia.getIdClasse());
        if (classeFind == null || classeFind.getId() == null) {
            throw new IllegalArgumentException("La clase con ID " + asistencia.getIdClasse() + " no existe.");
        }


        if (repository.existsById(id)) {
            Asistencia entity = toEntity(asistencia);
            entity.setIdAsistencia(id);
            return toDto(repository.save(entity));
        }
        return null;
    }

    @Override
    public boolean delete(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public List<AsistenciaResponseDto> findByUserId(Long userId) throws Exception {
        try {
            UserResponseDto userFind = usuarioClient.findById(userId);
            if (userFind == null) {
                return null;
            }
            return repository.findByIdUsuario(userId).stream().map(this::toDto).toList();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public List<AsistenciaResponseDto> findByClasseId(Long classeId) throws Exception {
        try {
            ClasseResponseDto classeFind = classeClient.findById(classeId);
            if (classeFind == null) {
                return null;
            }
            return repository.findByIdClasse(classeId).stream().map(this::toDto).toList();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}
