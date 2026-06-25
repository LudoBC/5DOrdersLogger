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
            String phase,
            List<Integer> childTimelines,
            Map<String, String> centers,
            Map<String, Unit> units
    ) {
        private boolean match(LocationDTO location) {
            return timeline == location.timeline && year == location.year && phase.equals(location.phase);
        }
    }

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
        private Order toOrder(List<BoardDTO> boards) {
            return switch ($type) {
                case "Move" -> new MoveOrder(destination.toLocation(), status, unit, location.toLocation());
                case "Support" -> new SupportOrder(destination.toLocation(), status, unit,
                        location.toLocation(), supportLocation.toLocation(), findSupportedUnit(boards));
                case "Convoy" -> new ConvoyOrder(destination.toLocation(), status, unit, location.toLocation(), convoyLocation.toLocation());
                case "Hold" -> new HoldOrder(status, unit, location.toLocation());
                case "Disband" -> new Disband(status, unit, location.toLocation());
                case "Build" -> new Build(status, unit, location.toLocation());
                default -> throw new IllegalArgumentException("Invalid order type: "+ $type);
            };
        }

        private Unit findSupportedUnit(List<BoardDTO> boards) {
            return boards.stream()
                    .filter(b -> b.match(supportLocation))
                    .findFirst()
                    .map(b -> b.units.get(supportLocation.region))
                    .orElse(Province.associatedUnit(supportLocation.region(), destination().region()));
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
            OrdersDTO orders = mapper.readValue(inputStream, new TypeReference<>() {});
            return orders.orders().reversed().stream().map(o -> o.toOrder(orders.boards)).toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
