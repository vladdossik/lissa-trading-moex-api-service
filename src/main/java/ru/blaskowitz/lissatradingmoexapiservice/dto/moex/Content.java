package ru.blaskowitz.lissatradingmoexapiservice.dto.moex;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Content {
    private List<String> columns;
    private List<List<Object>> data;
}
