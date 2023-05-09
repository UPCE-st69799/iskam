package upce.cz.iskam.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import upce.cz.iskam.entity.Role;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppUserResponseDtoV1 {
    private Long id;

    private String username;

    private String password;

    private Boolean active;

    private LocalDateTime creationDate;

    private LocalDateTime updateDate;

    private List<Role> roles = Collections.emptyList();
}