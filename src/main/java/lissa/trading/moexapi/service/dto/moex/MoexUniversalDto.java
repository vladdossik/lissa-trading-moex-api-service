package lissa.trading.moexapi.service.dto.moex;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MoexUniversalDto {

    @JsonAnySetter
    private Map<String, ContentDto> contentMap = new HashMap<>();
}
