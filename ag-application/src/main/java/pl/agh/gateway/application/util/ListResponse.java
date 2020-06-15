package pl.agh.gateway.application.util;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ListResponse {
    private List<?> list;
    private int count;
    private Double totalValue;
}
