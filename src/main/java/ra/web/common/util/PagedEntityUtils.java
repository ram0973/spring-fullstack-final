package ra.web.common.util;

import jakarta.validation.constraints.NotNull;
import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class PagedEntityUtils {
    private static Sort.Direction getSortDirection(@NotNull String direction) {
        if (direction.equals("asc")) {
            return Sort.Direction.ASC;
        } else if (direction.equals("desc")) {
            return Sort.Direction.DESC;
        }
        return Sort.Direction.DESC;
    }

    public static @NotNull List<Sort.Order> getSortOrders(@NotNull String[] sort) {
        List<Sort.Order> orders = new ArrayList<>();
        // sorting single column
        // ?sort=column1,asc или ?sort=column1,desc
        if (!sort[0].contains(",")) {
            orders.add(new Sort.Order(getSortDirection(sort[1]), sort[0]));
            return orders;
        }
        // sort more than 2 columns
        // ?sort=column1,asc&sort=column2,desc
        for (String sortOrder : sort) {
            String[] sortArray = sortOrder.split(",");
            orders.add(new Sort.Order(getSortDirection(sortArray[1]), sortArray[0]));
        }
        return orders;
    }
}
