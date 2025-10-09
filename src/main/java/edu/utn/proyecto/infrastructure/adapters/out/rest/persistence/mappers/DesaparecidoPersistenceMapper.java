package edu.utn.proyecto.infrastructure.adapters.out.rest.persistence.mappers;
import edu.utn.proyecto.domain.model.concreta.Desaparecido;
import edu.utn.proyecto.infrastructure.adapters.out.rest.persistence.entities.DesaparecidoEntity;
import org.springframework.stereotype.Component;

@Component
public class DesaparecidoPersistenceMapper {

    public DesaparecidoEntity domainToEntity(Desaparecido desaparecidoDominio) {
        DesaparecidoEntity desaparecidoEntity = new DesaparecidoEntity();
        desaparecidoEntity.setNombre(desaparecidoDominio.getNombre());
        desaparecidoEntity.setId(desaparecidoDominio.getId());
        desaparecidoEntity.setApellido(desaparecidoDominio.getApellido());
        desaparecidoEntity.setDni(desaparecidoDominio.getDni());
        desaparecidoEntity.setDescripcion(desaparecidoDominio.getDescripcion());
        desaparecidoEntity.setFotoUrl(desaparecidoDominio.getFoto());
        desaparecidoEntity.setFechaDesaparicion(desaparecidoDominio.getFechaDesaparicion());

        return desaparecidoEntity;
    }
    public Desaparecido entityToDomain(DesaparecidoEntity desaparecidoEntity) {
        Desaparecido desaparecido = new Desaparecido(
                desaparecidoEntity.getNombre(),
                desaparecidoEntity.getApellido(),
                desaparecidoEntity.getDni(),
                desaparecidoEntity.getDescripcion(),
                desaparecidoEntity.getFotoUrl()
        );
        desaparecido.setId(desaparecidoEntity.getId());
        desaparecido.setFechaDesaparicion(desaparecidoEntity.getFechaDesaparicion());
        return desaparecido;
    }
}
