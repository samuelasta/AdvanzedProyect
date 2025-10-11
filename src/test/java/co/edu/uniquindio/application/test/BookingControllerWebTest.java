package co.edu.uniquindio.application.test;

import co.edu.uniquindio.application.controller.BookingController;
import co.edu.uniquindio.application.dto.bookingDTO.CreateBookingDTO;
import co.edu.uniquindio.application.services.BookingService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
@AutoConfigureMockMvc(addFilters = false) // desactiva filtros de seguridad/JWT en el slice de MVC
class BookingControllerWebTest {

    @Autowired private MockMvc mvc;

    @MockBean private BookingService bookingService;

    @Test
    void create_returns201() throws Exception {
        // tu BookingService.create tiene 2 parámetros: (id, CreateBookingDTO)
        Mockito.doNothing().when(bookingService).create(eq("acc-1"), any(CreateBookingDTO.class));

        String body = """
          {"check_in":"%s","check_out":"%s","guest_number":2}
        """.formatted(
                LocalDateTime.now().plusDays(2).withNano(0),
                LocalDateTime.now().plusDays(3).withNano(0)
        );

        mvc.perform(post("/api/bookings/{id}", "acc-1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.error").value(false));
    }

    @Test
    void delete_returns200() throws Exception {
        Mockito.doNothing().when(bookingService).delete("booking-1");

        mvc.perform(delete("/api/bookings/{id}", "booking-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error").value(false));
    }
}
