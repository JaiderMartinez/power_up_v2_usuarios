package com.reto.usuario.domain.gateways;

import com.reto.usuario.domain.dto.EmployeeRestaurantClientRequestDto;

public interface IEmployeeRestaurantClientPlazoleta {

    void saveUserEmployeeToARestaurant(EmployeeRestaurantClientRequestDto employeeRestaurantClientRequestDto, String tokenWithPrefixBearer);
}
