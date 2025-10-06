package edu.utn.proyecto.infrastructure.adapters.out.rest.persistence.mappers;
import edu.utn.proyecto.domain.model.concretas.Avistador;
import edu.utn.proyecto.infrastructure.adapters.out.rest.persistence.entities.AvistadorEntity;
import org.springframework.stereotype.Component;

@Component
public class AvistadorPersistenceMapper {

    public AvistadorEntity domainToEntity(Avistador d) {
        AvistadorEntity e = new AvistadorEntity();
        e.setId(d.getId());
        e.setDni(d.getDni());
        e.setNombre(d.getNombre());
        e.setApellido(d.getApellido());
        e.setEdad(d.getEdad());
        e.setDireccion(d.getDireccion());
        e.setEmail(d.getEmail());
        e.setTelefono(d.getTelefono());
        e.setCreadoEn(d.getCreadoEn());
        return e;
    }

    public Avistador entityToDomain(AvistadorEntity e) {
        Avistador d = new Avistador();
        d.setId(e.getId());
        d.setDni(e.getDni());
        d.setNombre(e.getNombre());
        d.setApellido(e.getApellido());
        d.setEdad(e.getEdad());
        d.setDireccion(e.getDireccion());
        d.setEmail(e.getEmail());
        d.setTelefono(e.getTelefono());
        d.setCreadoEn(e.getCreadoEn());
        return d;
    }
}