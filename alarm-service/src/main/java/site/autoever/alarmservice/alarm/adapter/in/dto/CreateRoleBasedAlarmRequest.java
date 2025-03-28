package site.autoever.alarmservice.alarm.adapter.in.dto;

public record CreateRoleBasedAlarmRequest(
        String role,
        String message,
        String type
) {

}
