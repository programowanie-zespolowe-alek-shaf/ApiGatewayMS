package pl.agh.gateway.application.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class RequestDTO {

    @NotNull
    private Long shoppingCardID;

    @NotNull
    private String address;

    @NotNull
    @FutureOrPresent
    private LocalDate shipDate;

    private String couponCode;

}
