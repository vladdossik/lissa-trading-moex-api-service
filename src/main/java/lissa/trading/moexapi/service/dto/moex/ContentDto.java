package lissa.trading.moexapi.service.dto.moex;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContentDto {
    private List<String> columns = new ArrayList<>();
    private List<List<Object>> data = new ArrayList<>();

}
