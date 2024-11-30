package ru.blaskowitz.lissatradingmoexapiservice.dto.Company;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyListDto {
    private List<String> names;
}
