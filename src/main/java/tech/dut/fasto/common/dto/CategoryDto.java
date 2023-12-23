package tech.dut.fasto.common.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto implements Serializable {


    @Serial
    private static final long serialVersionUID = -4647546833236996220L;
    Long id;

    String name;
    
    String categoryImage;

    boolean deleteFlag;

}
