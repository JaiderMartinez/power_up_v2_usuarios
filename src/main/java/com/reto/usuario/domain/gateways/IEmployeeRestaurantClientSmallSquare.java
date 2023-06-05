package com.reto.usuario.domain.gateways;

import com.reto.usuario.domain.dto.EmployeeRestaurantClientRequestDto;

public interface IEmployeeRestaurantClientSmallSquare {

    void saveUserEmployeeToARestaurant(EmployeeRestaurantClientRequestDto employeeRestaurantClientRequestDto, String tokenWithPrefixBearer);
}
