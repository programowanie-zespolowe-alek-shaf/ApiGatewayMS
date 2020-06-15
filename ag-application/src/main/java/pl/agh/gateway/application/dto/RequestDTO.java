package pl.agh.gateway.application.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
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

    @NotNull
    @PositiveOrZero
    private Float amount;

    private String couponCode;

}
