package nodomain.mijnmooiewereld.orders.logger;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;
import nodomain.mijnmooiewereld.orders.logger.order.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public enum OrderDao {
    ORDER_DAO;

    private record OrdersDTO(
            int iteration,
            List<BoardDTO> boards,
            List<OrderDTO> orders
    ) {
    }

    private record BoardDTO(
            int timeline,
            int year,
            String Phase,
            List<Integer> childTimelines,
            Map<String, String> centers,
            Map<String, Unit> units
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record OrderDTO(
            String $type,
            LocationDTO destination,
            String status,
            Unit unit,
            LocationDTO location,
            LocationDTO supportLocation,
            LocationDTO convoyLocation
    ) {
        private Order toOrder() {
            return switch ($type) {
                case "Move" -> new MoveOrder(destination.toLocation(), status, unit, location.toLocation());
                case "Support" -> new SupportOrder(destination.toLocation(), status, unit, location.toLocation(), supportLocation.toLocation());
                case "Convoy" -> new ConvoyOrder(destination.toLocation(), status, unit, location.toLocation(), convoyLocation.toLocation());
                case "Hold" -> new HoldOrder(status, unit, location.toLocation());
                case "Disband" -> new Disband(status, unit, location.toLocation());
                case "Build" -> new Build(status, unit, location.toLocation());
                default -> throw new IllegalArgumentException("Invalid order type: "+ $type);
            };
        }
    }

    private record LocationDTO(
            int year,
            String phase,
            int timeline,
            String region
    ) {
        Location toLocation() {
            return new Location(new Location.Board(year, phase, timeline), region);
        }
    }

    public List<Order> getAllFromSource(Path path) {
        var mapper = new ObjectMapper();
        try (InputStream inputStream = Files.newInputStream(path)) {
            return mapper.readValue(inputStream, new TypeReference<OrdersDTO>() {})
                    .orders().reversed().stream().map(OrderDTO::toOrder).toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
