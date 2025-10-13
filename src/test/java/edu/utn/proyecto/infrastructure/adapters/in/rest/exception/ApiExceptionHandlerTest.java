package edu.utn.proyecto.infrastructure.adapters.in.rest.exception;
import edu.utn.proyecto.common.exception.DomainException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import static org.assertj.core.api.Assertions.assertThat;

class ApiExceptionHandlerTest {

    @Test
    void handle_traduceDomainException_aProblemDetail_conStatusMensajeYKey() {
        MessageSource messages = Mockito.mock(MessageSource.class);
        ApiExceptionHandler handler = new ApiExceptionHandler(messages);

        String key = "desaparecido.dni.duplicado";
        String msg = "DNI ya registrado";
        var ex = DomainException.of(key, HttpStatus.CONFLICT, msg);

        ProblemDetail pd = handler.handle(ex);

        assertThat(pd.getStatus()).isEqualTo(HttpStatus.CONFLICT.value());
        assertThat(pd.getDetail()).isEqualTo(msg);
        assertThat(pd.getProperties()).containsEntry("key", key);
    }

    @Test
    void handle_respetaStatusYKey_paraOtroCaso() {
        MessageSource messages = Mockito.mock(MessageSource.class);
        ApiExceptionHandler handler = new ApiExceptionHandler(messages);

        String key = "avistador.padron.not_found";
        String msg = "No existe en padrón (RENAPER).";
        var ex = DomainException.of(key, HttpStatus.NOT_FOUND, msg);

        ProblemDetail pd = handler.handle(ex);

        assertThat(pd.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(pd.getDetail()).isEqualTo(msg);
        assertThat(pd.getProperties().get("key")).isEqualTo(key);
    }
}
