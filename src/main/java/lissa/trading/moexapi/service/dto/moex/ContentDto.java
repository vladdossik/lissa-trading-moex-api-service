package lissa.trading.moexapi.service.dto.moex;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor

public class ContentDto {
    private List<String> columns;
    private List<List<Object>> data;

    public ContentDto() {
        this.columns = new ArrayList<>();
        this.data = new ArrayList<>();
    }
}
