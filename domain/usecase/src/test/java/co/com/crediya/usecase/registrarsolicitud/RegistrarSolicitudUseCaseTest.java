package co.com.crediya.usecase.registrarsolicitud;

import co.com.crediya.model.Solicitud;
import co.com.crediya.model.Usuario;
import co.com.crediya.model.exceptions.SolicitudException;
import co.com.crediya.model.exceptions.TipoError;
import co.com.crediya.model.gateways.SolicitudRepository;
import co.com.crediya.model.gateways.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegistrarSolicitudUseCaseTest {

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private SolicitudRepository solicitudRepository;

    private RegistrarSolicitudUseCase useCase;

    private static final String DOCUMENTO_IDENTIDAD = "12345678";
    private static final BigDecimal MONTO = BigDecimal.valueOf(100000);
    private static final Integer PLAZO = 12;
    private static final Integer ID_TIPO_PRESTAMO = 1;
    private static final String EMAIL_USUARIO = "test@example.com";

    @BeforeEach
    void setUp() {
        useCase = new RegistrarSolicitudUseCase(usuarioService, solicitudRepository);
    }

    @Test
    void registrar_CuandoUsuarioExisteYTipoPrestamoValido_DeberiaRetornarMensajeExitoso() {

        Usuario usuario = new Usuario(EMAIL_USUARIO);
        when(usuarioService.consultarUsuarioDocumento(DOCUMENTO_IDENTIDAD))
                .thenReturn(Mono.just(usuario));
        when(solicitudRepository.existeIdTipoPrestamo(ID_TIPO_PRESTAMO))
                .thenReturn(Mono.just(true));
        when(solicitudRepository.registrarSolicitud(any(Solicitud.class)))
                .thenReturn(Mono.empty());

        StepVerifier.create(useCase.registrar(DOCUMENTO_IDENTIDAD, MONTO, PLAZO, ID_TIPO_PRESTAMO))
                .expectNext("Pendiente de revisión")
                .verifyComplete();

        verify(usuarioService).consultarUsuarioDocumento(DOCUMENTO_IDENTIDAD);
        verify(solicitudRepository).existeIdTipoPrestamo(ID_TIPO_PRESTAMO);
        verify(solicitudRepository).registrarSolicitud(any(Solicitud.class));
    }

    @Test
    void registrar_CuandoUsuarioNoExiste_DeberiaLanzarSolicitudException() {

        when(usuarioService.consultarUsuarioDocumento(DOCUMENTO_IDENTIDAD))
                .thenReturn(Mono.empty());

        StepVerifier.create(useCase.registrar(DOCUMENTO_IDENTIDAD, MONTO, PLAZO, ID_TIPO_PRESTAMO))
                .expectErrorMatches(error -> 
                    error instanceof SolicitudException &&
                    ((SolicitudException) error).getTipoError() == TipoError.USUARIO_NO_ENCONTRADO &&
                    error.getMessage().contains(DOCUMENTO_IDENTIDAD)
                )
                .verify();

        verify(usuarioService).consultarUsuarioDocumento(DOCUMENTO_IDENTIDAD);
    }

    @Test
    void registrar_CuandoTipoPrestamoNoValido_DeberiaLanzarSolicitudException() {

        Usuario usuario = new Usuario(EMAIL_USUARIO);
        when(usuarioService.consultarUsuarioDocumento(DOCUMENTO_IDENTIDAD))
                .thenReturn(Mono.just(usuario));
        when(solicitudRepository.existeIdTipoPrestamo(ID_TIPO_PRESTAMO))
                .thenReturn(Mono.just(false));

        StepVerifier.create(useCase.registrar(DOCUMENTO_IDENTIDAD, MONTO, PLAZO, ID_TIPO_PRESTAMO))
                .expectErrorMatches(error -> 
                    error instanceof SolicitudException &&
                    ((SolicitudException) error).getTipoError() == TipoError.TIPO_PRESTAMO_INVALIDO &&
                    error.getMessage().contains("ID: " + ID_TIPO_PRESTAMO)
                )
                .verify();

        verify(usuarioService).consultarUsuarioDocumento(DOCUMENTO_IDENTIDAD);
        verify(solicitudRepository).existeIdTipoPrestamo(ID_TIPO_PRESTAMO);
    }

    @Test
    void registrar_CuandoRepositoryFalla_DeberiaPropagarlError() {

        Usuario usuario = new Usuario(EMAIL_USUARIO);
        RuntimeException repositoryError = new RuntimeException("Database error");
        
        when(usuarioService.consultarUsuarioDocumento(DOCUMENTO_IDENTIDAD))
                .thenReturn(Mono.just(usuario));
        when(solicitudRepository.existeIdTipoPrestamo(ID_TIPO_PRESTAMO))
                .thenReturn(Mono.just(true));
        when(solicitudRepository.registrarSolicitud(any(Solicitud.class)))
                .thenReturn(Mono.error(repositoryError));

        StepVerifier.create(useCase.registrar(DOCUMENTO_IDENTIDAD, MONTO, PLAZO, ID_TIPO_PRESTAMO))
                .expectError(RuntimeException.class)
                .verify();

        verify(usuarioService).consultarUsuarioDocumento(DOCUMENTO_IDENTIDAD);
        verify(solicitudRepository).existeIdTipoPrestamo(ID_TIPO_PRESTAMO);
        verify(solicitudRepository).registrarSolicitud(any(Solicitud.class));
    }

    @Test
    void registrar_CuandoUsuarioServiceFalla_DeberiaPropagarlError() {

        RuntimeException serviceError = new RuntimeException("Service error");
        when(usuarioService.consultarUsuarioDocumento(DOCUMENTO_IDENTIDAD))
                .thenReturn(Mono.error(serviceError));

        StepVerifier.create(useCase.registrar(DOCUMENTO_IDENTIDAD, MONTO, PLAZO, ID_TIPO_PRESTAMO))
                .expectError(RuntimeException.class)
                .verify();

        verify(usuarioService).consultarUsuarioDocumento(DOCUMENTO_IDENTIDAD);
    }

    @Test
    void registrar_CuandoValidacionTipoPrestamoFalla_DeberiaPropagarlError() {

        Usuario usuario = new Usuario(EMAIL_USUARIO);
        RuntimeException validationError = new RuntimeException("Validation error");
        
        when(usuarioService.consultarUsuarioDocumento(DOCUMENTO_IDENTIDAD))
                .thenReturn(Mono.just(usuario));
        when(solicitudRepository.existeIdTipoPrestamo(ID_TIPO_PRESTAMO))
                .thenReturn(Mono.error(validationError));

        StepVerifier.create(useCase.registrar(DOCUMENTO_IDENTIDAD, MONTO, PLAZO, ID_TIPO_PRESTAMO))
                .expectError(RuntimeException.class)
                .verify();

        verify(usuarioService).consultarUsuarioDocumento(DOCUMENTO_IDENTIDAD);
        verify(solicitudRepository).existeIdTipoPrestamo(ID_TIPO_PRESTAMO);
    }

    @Test
    void registrar_DeberiaCrearSolicitudConDatosCorrectos() {

        Usuario usuario = new Usuario(EMAIL_USUARIO);
        when(usuarioService.consultarUsuarioDocumento(DOCUMENTO_IDENTIDAD))
                .thenReturn(Mono.just(usuario));
        when(solicitudRepository.existeIdTipoPrestamo(ID_TIPO_PRESTAMO))
                .thenReturn(Mono.just(true));
        when(solicitudRepository.registrarSolicitud(any(Solicitud.class)))
                .thenReturn(Mono.empty());

        StepVerifier.create(useCase.registrar(DOCUMENTO_IDENTIDAD, MONTO, PLAZO, ID_TIPO_PRESTAMO))
                .expectNext("Pendiente de revisión")
                .verifyComplete();

        verify(solicitudRepository).registrarSolicitud(any(Solicitud.class));
    }
}