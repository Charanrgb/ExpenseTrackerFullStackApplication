package in.saicharan.expensetracker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

//The DTO only includes the data that the client provides. json to dto to entity, dto ensures security (only includes necessary data fields)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileDto {
    private Long id;
    private String fullName;
    private String email;
    private String password;
    private String imageURL;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
