package com.reto.usuario.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeRestaurantClientRequestDto {

    private Long idOwnerRestaurant;
    private Long employeeUserId;
}
