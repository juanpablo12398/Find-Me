package edu.utn.proyecto.infrastructure.adapters.in.rest.exception;
import edu.utn.proyecto.common.exception.DomainException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import java.util.Locale;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

class ApiExceptionHandlerTest {

    @Test
    void handle_traduceDomainException_aProblemDetail_conStatusMensajeYKey() {
        MessageSource messages = Mockito.mock(MessageSource.class);
        ApiExceptionHandler handler = new ApiExceptionHandler(messages);

        String key = "desaparecido.dni.duplicado";
        String defaultMsg = "DNI ya registrado";
        String translated = "DNI ya registrado (traducido)";
        var ex = DomainException.of(key, HttpStatus.CONFLICT, defaultMsg);

        // mock: devuelve traducción desde messages.properties
        when(messages.getMessage(eq(key), isNull(), eq(defaultMsg), any(Locale.class)))
                .thenReturn(translated);

        ProblemDetail pd = handler.handle(ex);

        assertThat(pd.getStatus()).isEqualTo(HttpStatus.CONFLICT.value());
        assertThat(pd.getDetail()).isEqualTo(translated); // usa la traducción
        assertThat(pd.getProperties()).containsEntry("key", key);
    }

    @Test
    void handle_respetaStatusYKey_yHaceFallbackAlDefaultMessage() {
        MessageSource messages = Mockito.mock(MessageSource.class);
        ApiExceptionHandler handler = new ApiExceptionHandler(messages);

        String key = "avistador.padron.not_found";
        String defaultMsg = "No existe en padrón (RENAPER).";
        var ex = DomainException.of(key, HttpStatus.NOT_FOUND, defaultMsg);

        // mock: no hay traducción; retorna el defaultMessage
        when(messages.getMessage(eq(key), isNull(), eq(defaultMsg), any(Locale.class)))
                .thenReturn(defaultMsg);

        ProblemDetail pd = handler.handle(ex);

        assertThat(pd.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(pd.getDetail()).isEqualTo(defaultMsg); // fallback al default
        assertThat(pd.getProperties().get("key")).isEqualTo(key);
    }
}
