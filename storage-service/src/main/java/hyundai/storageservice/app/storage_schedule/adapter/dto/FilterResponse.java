package hyundai.storageservice.app.storage_schedule.adapter.dto;


import java.util.List;

public record FilterResponse(
        List<DayTodoDto> dayTodoDtos
) {
    public static FilterResponse from(List<DayTodoDto> dayTodoDtos){
        return new FilterResponse(dayTodoDtos);
    }
}
