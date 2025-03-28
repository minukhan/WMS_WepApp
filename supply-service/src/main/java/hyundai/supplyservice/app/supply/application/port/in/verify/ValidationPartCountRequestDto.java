package hyundai.supplyservice.app.supply.application.port.in.verify;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.util.List;

public record ValidationPartCountRequestDto(
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate dueDate,

        List<String> partIds
) {

}
