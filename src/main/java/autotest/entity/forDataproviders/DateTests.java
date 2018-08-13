package autotest.entity.forDataproviders;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class DateTests {

    private String dateValue;
    private String errorText;
}
