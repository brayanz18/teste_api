package api_teste.ds.models.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

@NoArgsConstructor
@AllArgsConstructor
@Data

public class UserUpdateDTO {

    private Long id;

    @NotBlank 
    @Size (min = 8, max =60)
    private String password;
}
