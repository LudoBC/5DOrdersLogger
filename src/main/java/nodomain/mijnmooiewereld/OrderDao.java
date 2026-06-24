package nodomain.mijnmooiewereld;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import nodomain.mijnmooiewereld.order.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public enum OrderDao {
    ORDER_DAO;

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record OrdersDTO(
            int iteration,
            List<OrderDTO> orders
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record OrderDTO(
            String $type,
            Location destination,
            String status,
            Unit unit,
            Location location,
            Location supportLocation,
            Location convoyLocation
    ) {
        private Order toOrder() {
            return switch ($type) {
                case "Move" -> new MoveOrder(destination, status, unit, location);
                case "Support" -> new SupportOrder(destination, status, unit, location, supportLocation);
                case "Convoy" -> new ConvoyOrder(destination, status, unit, location, convoyLocation);
                case "Hold" -> new HoldOrder(status, unit, location);
                case "Disband" -> new Disband(status, unit, location);
                case "Build" -> new Build(status, unit, location);
                default -> throw new IllegalArgumentException("Invalid order type: "+ $type);
            };
        }
    }

    public Stream<Order> getAllFromSource(Path path) {
        var mapper = new ObjectMapper();
        try (InputStream inputStream = Files.newInputStream(path)) {
            return mapper.readValue(inputStream, new TypeReference<OrdersDTO>() {})
                    .orders().reversed().stream().map(OrderDTO::toOrder);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
