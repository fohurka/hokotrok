import java.util.*;

/**
 * Reconstructs the live domain object graph from a JSON game state string
 * produced by StateSerializer.
 *
 * Usage:
 * new StateParser().parse(jsonString, controller);
 *
 * Implementation will follow a two-phase approach:
 * Phase 1 — JSON tokenizer/parser → populates GameState DTOs
 * Phase 2 — DTO graph → domain objects, wired in dependency order
 */
public class StateParser {

    /**
     * Parses {@code json} and loads the resulting game state into
     * {@code controller}.
     *
     * @param json       the full JSON string produced by StateSerializer
     * @param controller the GameController to populate
     */
    public void parse(String json, GameController controller) {
        // Step 1: tokenize + parse JSON into GameState.GameStateDto
        GameState.GameStateDto dto = parseJsonToDto(json);

        // Step 2: reconstruct domain objects from DTOs
        reconstruct(dto, controller);
    }

    // =========================================================================
    // Phase 1: AST to DTO
    // =========================================================================

    private GameState.GameStateDto parseJsonToDto(String json) {
        Node rootNode = new JsonParser(json).parse();
        if (!(rootNode instanceof JObj)) {
            throw new RuntimeException("Root must be an object");
        }
        JObj root = (JObj) rootNode;
        JObj gs = (JObj) root.fields.get("gameState");
        if (gs == null) {
            throw new RuntimeException("Missing gameState object");
        }

        GameState.GameStateDto dto = new GameState.GameStateDto();

        // Bank
        if (gs.fields.containsKey("bank")) {
            JObj bNode = (JObj) gs.fields.get("bank");
            dto.bank = new GameState.BankDto();
            dto.bank.accounts = new HashMap<>();
            JObj accNode = (JObj) bNode.fields.get("accounts");
            if (accNode != null) {
                for (String k : accNode.keyOrder) {
                    dto.bank.accounts.put(k, (int) ((JNum) accNode.fields.get(k)).v);
                }
            }
        }

        // Warehouse
        if (gs.fields.containsKey("warehouse")) {
            JObj wNode = (JObj) gs.fields.get("warehouse");
            dto.warehouse = new GameState.WarehouseDto();
            dto.warehouse.id = ((JStr) wNode.fields.get("id")).v;
            dto.warehouse.stock = new ArrayList<>();
            JArr stockNode = (JArr) wNode.fields.get("stock");
            if (stockNode != null) {
                for (Node e : stockNode.elems) {
                    dto.warehouse.stock.add(((JStr) e).v);
                }
            }
        }

        // Recoverer
        if (gs.fields.containsKey("recoverer")) {
            JObj rNode = (JObj) gs.fields.get("recoverer");
            dto.recoverer = new GameState.RecovererDto();
            dto.recoverer.recoveryQueue = new ArrayList<>();
            JArr reqNode = (JArr) rNode.fields.get("recoveryQueue");
            if (reqNode != null) {
                for (Node e : reqNode.elems) {
                    dto.recoverer.recoveryQueue.add(((JStr) e).v);
                }
            }
        }

        // Map
        JObj mapNode = (JObj) gs.fields.get("map");
        if (mapNode != null) {
            dto.map = new GameState.MapDto();
            dto.map.junctions = new ArrayList<>();
            dto.map.buildings = new ArrayList<>();
            dto.map.lanes = new ArrayList<>();

            JArr jArr = (JArr) mapNode.fields.get("junctions");
            if (jArr != null) {
                for (Node n : jArr.elems) {
                    JObj jo = (JObj) n;
                    GameState.JunctionDto j = new GameState.JunctionDto();
                    j.id = ((JStr) jo.fields.get("id")).v;
                    j.startingLaneIds = parseStringList((JArr) jo.fields.get("startingLaneIds"));
                    j.endingLaneIds = parseStringList((JArr) jo.fields.get("endingLaneIds"));
                    j.buildingIds = parseStringList((JArr) jo.fields.get("buildingIds"));
                    dto.map.junctions.add(j);
                }
            }

            JArr bArr = (JArr) mapNode.fields.get("buildings");
            if (bArr != null) {
                for (Node n : bArr.elems) {
                    JObj bo = (JObj) n;
                    GameState.BuildingDto b = new GameState.BuildingDto();
                    b.id = ((JStr) bo.fields.get("id")).v;
                    b.connectionId = ((JStr) bo.fields.get("connectionId")).v;
                    b.type = ((JStr) bo.fields.get("type")).v;
                    dto.map.buildings.add(b);
                }
            }

            JArr lArr = (JArr) mapNode.fields.get("lanes");
            if (lArr != null) {
                for (Node n : lArr.elems) {
                    JObj lo = (JObj) n;
                    Node idNode = lo.fields.get("id");
                    if (idNode instanceof JStr && lo.fields.get("length") instanceof JNum && lo.fields.get("startJunctionId") instanceof JStr && lo.fields.get("endJunctionId") instanceof JStr) {
                        GameState.LaneDto l = new GameState.LaneDto();
                        l.id = ((JStr) idNode).v;
                        l.length = (int) ((JNum) lo.fields.get("length")).v;
                        l.startJunctionId = ((JStr) lo.fields.get("startJunctionId")).v;
                        l.endJunctionId = ((JStr) lo.fields.get("endJunctionId")).v;
                        l.leftNeighborId = lo.fields.get("leftNeighborId") instanceof JStr
                                ? ((JStr) lo.fields.get("leftNeighborId")).v
                                : null;
                        l.rightNeighborId = lo.fields.get("rightNeighborId") instanceof JStr
                                ? ((JStr) lo.fields.get("rightNeighborId")).v
                                : null;

                        l.vehicles = new ArrayList<>();
                        JArr vArr = (JArr) lo.fields.get("vehicles");
                        if (vArr != null) {
                            for (Node vn : vArr.elems) {
                                JObj vObj = (JObj) vn;
                                Map<String, Integer> prog = new HashMap<>();
                                for (String vk : vObj.keyOrder) {
                                    Node val = vObj.fields.get(vk);
                                    if (val instanceof JNum) {
                                        prog.put(vk, (int) ((JNum) val).v);
                                    }
                                }
                                if (!prog.isEmpty()) {
                                    l.vehicles.add(prog);
                                }
                            }
                        }

                        if (lo.fields.containsKey("isCrashed") && lo.fields.get("isCrashed") instanceof JBool) {
                            l.isCrashed = ((JBool) lo.fields.get("isCrashed")).v;
                        }

                        JObj sNode = (JObj) lo.fields.get("surface");
                        if (sNode != null && sNode.fields.get("type") instanceof JStr && sNode.fields.get("snowAmount") instanceof JNum && sNode.fields.get("iceAmount") instanceof JNum && sNode.fields.get("modifier") instanceof JStr) {
                            GameState.SurfaceDto s = new GameState.SurfaceDto();
                            s.type = ((JStr) sNode.fields.get("type")).v;
                            s.snowAmount = (int) ((JNum) sNode.fields.get("snowAmount")).v;
                            s.iceAmount = (int) ((JNum) sNode.fields.get("iceAmount")).v;
                            s.modifier = ((JStr) sNode.fields.get("modifier")).v;
                            l.surface = s;
                        }
                        dto.map.lanes.add(l);
                    }
            }
        }

        // Players
        dto.carPlayers = new ArrayList<>();
        dto.busPlayers = new ArrayList<>();
        dto.snowplowPlayers = new ArrayList<>();
        JArr pArr = (JArr) gs.fields.get("players");
        if (pArr != null) {
            for (Node n : pArr.elems) {
                JObj po = (JObj) n;
                String type = ((JStr) po.fields.get("type")).v;
                if ("CarPlayer".equals(type)) {
                    GameState.CarPlayerDto cp = new GameState.CarPlayerDto();
                    cp.id = ((JStr) po.fields.get("id")).v;
                    cp.vehicleId = ((JStr) po.fields.get("vehicleId")).v;
                    cp.homeId = po.fields.get("homeId") instanceof JStr ? ((JStr) po.fields.get("homeId")).v : null;
                    cp.workId = po.fields.get("workId") instanceof JStr ? ((JStr) po.fields.get("workId")).v : null;
                    dto.carPlayers.add(cp);
                } else if ("BusPlayer".equals(type)) {
                    GameState.BusPlayerDto bp = new GameState.BusPlayerDto();
                    bp.id = ((JStr) po.fields.get("id")).v;
                    bp.vehicleId = ((JStr) po.fields.get("vehicleId")).v;
                    bp.stop1 = po.fields.get("stop1") instanceof JStr ? ((JStr) po.fields.get("stop1")).v : null;
                    bp.stop2 = po.fields.get("stop2") instanceof JStr ? ((JStr) po.fields.get("stop2")).v : null;
                    dto.busPlayers.add(bp);
                } else if ("SnowplowPlayer".equals(type)) {
                    GameState.SnowplowPlayerDto sp = new GameState.SnowplowPlayerDto();
                    sp.id = ((JStr) po.fields.get("id")).v;
                    sp.vehicleIds = parseStringList((JArr) po.fields.get("vehicleIds"));
                    sp.warehouseId = po.fields.get("warehouseId") instanceof JStr
                            ? ((JStr) po.fields.get("warehouseId")).v
                            : null;
                    dto.snowplowPlayers.add(sp);
                }
            }
        }

        // Vehicles
        dto.cars = new ArrayList<>();
        dto.buses = new ArrayList<>();
        dto.snowplows = new ArrayList<>();
        JArr vArr = (JArr) gs.fields.get("vehicles");
        if (vArr != null) {
            for (Node n : vArr.elems) {
                JObj vo = (JObj) n;
                String type = ((JStr) vo.fields.get("type")).v;
                if ("Car".equals(type)) {
                    GameState.CarDto c = new GameState.CarDto();
                    c.id = ((JStr) vo.fields.get("id")).v;
                    c.ownerId = ((JStr) vo.fields.get("ownerId")).v;
                    c.location = parseLocation(vo.fields.get("location"));
                    c.isCrashed = ((JBool) vo.fields.get("isCrashed")).v;
                    dto.cars.add(c);
                } else if ("Bus".equals(type)) {
                    GameState.BusDto b = new GameState.BusDto();
                    b.id = ((JStr) vo.fields.get("id")).v;
                    b.ownerId = ((JStr) vo.fields.get("ownerId")).v;
                    b.location = parseLocation(vo.fields.get("location"));
                    b.isCrashed = ((JBool) vo.fields.get("isCrashed")).v;
                    dto.buses.add(b);
                } else if ("Snowplow".equals(type)) {
                    GameState.SnowplowDto s = new GameState.SnowplowDto();
                    s.id = ((JStr) vo.fields.get("id")).v;
                    s.ownerId = ((JStr) vo.fields.get("ownerId")).v;
                    s.location = parseLocation(vo.fields.get("location"));
                    s.equipmentId = vo.fields.get("equipmentId") instanceof JStr
                            ? ((JStr) vo.fields.get("equipmentId")).v
                            : null;
                    dto.snowplows.add(s);
                }
            }
        }

        // Equipments
        dto.equipments = new ArrayList<>();
        JArr eArr = (JArr) gs.fields.get("equipments");
        if (eArr != null) {
            for (Node n : eArr.elems) {
                JObj eo = (JObj) n;
                GameState.EquipmentDto e = new GameState.EquipmentDto();
                e.id = ((JStr) eo.fields.get("id")).v;
                e.type = ((JStr) eo.fields.get("type")).v;
                e.ownerId = eo.fields.get("ownerId") instanceof JStr ? ((JStr) eo.fields.get("ownerId")).v : null;
                e.stateData = new HashMap<>();
                JObj sdNode = (JObj) eo.fields.get("stateData");
                if (sdNode != null) {
                    for (String k : sdNode.keyOrder) {
                        e.stateData.put(k, (int) ((JNum) sdNode.fields.get(k)).v);
                    }
                }
                dto.equipments.add(e);
            }
        }
        }
        return dto;
    }

    private List<String> parseStringList(JArr arr) {
        List<String> list = new ArrayList<>();
        if (arr != null) {
            for (Node n : arr.elems) {
                list.add(((JStr) n).v);
            }
        }
        return list;
    }

    private GameState.LocationDto parseLocation(Node locNode) {
        if (!(locNode instanceof JObj))
            return null;
        JObj lo = (JObj) locNode;
        GameState.LocationDto loc = new GameState.LocationDto();
        loc.type = ((JStr) lo.fields.get("type")).v;
        loc.id = ((JStr) lo.fields.get("id")).v;
        return loc;
    }

    // =========================================================================
    // Phase 2: DTO to Domain Objects
    // =========================================================================

    private void reconstruct(GameState.GameStateDto dto, GameController controller) {
        controller.clearAll();

        RoadNetwork rn = new RoadNetwork();
        controller.setRoadNetwork(rn);

        Map<String, Junction> junctionMap = new HashMap<>();
        Map<String, Building> buildingMap = new HashMap<>();
        Map<String, Lane> laneMap = new HashMap<>();
        Map<String, Player> playerMap = new HashMap<>();
        Map<String, Equipment> equipmentMap = new HashMap<>();
        Map<String, Vehicle> vehicleMap = new HashMap<>();
        Map<String, MapComponent> locationMap = new HashMap<>();

        // 1. Junctions
        if (dto.map != null && dto.map.junctions != null) {
            for (GameState.JunctionDto jDto : dto.map.junctions) {
                Junction j = new Junction();
                j.setId(jDto.id);
                j.setRoadNetwork(rn);
                rn.addJunction(j);
                junctionMap.put(j.getId(), j);
                locationMap.put(j.getId(), j);
                controller.addJunction(j);
            }
        }

        // 2. Buildings
        if (dto.map != null && dto.map.buildings != null) {
            for (GameState.BuildingDto bDto : dto.map.buildings) {
                Junction conn = junctionMap.get(bDto.connectionId);
                Building b;
                if ("Warehouse".equals(bDto.type)) {
                    b = new Warehouse(conn);
                    controller.setWarehouse((Warehouse) b);
                } else {
                    b = new Building(conn);
                }
                b.setId(bDto.id);
                buildingMap.put(b.getId(), b);
                locationMap.put(b.getId(), b);
                controller.addBuilding(b);
                if (conn != null)
                    conn.addBuilding(b);
            }
        }

        // 3. Lanes
        if (dto.map != null && dto.map.lanes != null) {
            // Pass 1: Instantiate and map
            for (GameState.LaneDto lDto : dto.map.lanes) {
                Junction start = junctionMap.get(lDto.startJunctionId);
                Junction end = junctionMap.get(lDto.endJunctionId);
                Lane l = new Lane(start, end, lDto.length);
                l.setId(lDto.id);
                l.setRoadNetwork(rn);
                rn.addLane(l);
                laneMap.put(l.getId(), l);
                locationMap.put(l.getId(), l);
                controller.addLane(l);

                if (start != null)
                    start.addStarting(l);
                if (end != null)
                    end.addEnding(l);

                if (lDto.isCrashed) {
                    l.crashHappened();
                }

                if (lDto.surface != null) {
                    Surface s;
                    switch (lDto.surface.type) {
                        case "DeepSnow":
                            s = new DeepSnow(l);
                            break;
                        case "Ice":
                            s = new Ice(l);
                            break;
                        case "Grit":
                            s = new Grit(l);
                            break;
                        case "SmallSnow":
                        default:
                            s = new SmallSnow(l);
                            break;
                    }
                    s.snowAmount = lDto.surface.snowAmount;
                    s.iceAmount = lDto.surface.iceAmount;
                    if ("Salted".equals(lDto.surface.modifier)) {
                        s.salt();
                    }
                    l.setSurface(s);
                }
            }
            // Pass 2: Neighbors
            for (GameState.LaneDto lDto : dto.map.lanes) {
                Lane l = laneMap.get(lDto.id);
                Lane left = laneMap.get(lDto.leftNeighborId);
                Lane right = laneMap.get(lDto.rightNeighborId);
                l.addNeighbors(left, right);
            }
        }

        // 4. Players
        if (dto.carPlayers != null) {
            for (GameState.CarPlayerDto cpDto : dto.carPlayers) {
                Building home = buildingMap.get(cpDto.homeId);
                Building work = buildingMap.get(cpDto.workId);
                CarPlayer cp = new CarPlayer(home, work);
                cp.setId(cpDto.id);
                playerMap.put(cp.getId(), cp);
                controller.addCarPlayer(cp);

                Car car = cp.getCar();
                car.setId(cpDto.vehicleId);
                vehicleMap.put(car.getId(), car);
                controller.addVehicle(car);
            }
        }
        if (dto.busPlayers != null) {
            for (GameState.BusPlayerDto bpDto : dto.busPlayers) {
                BusPlayer bp = new BusPlayer(null); // start junction is overridden later
                bp.setId(bpDto.id);
                playerMap.put(bp.getId(), bp);
                controller.addBusPlayer(bp);

                Bus bus = bp.getBus();
                bus.setId(bpDto.vehicleId);
                vehicleMap.put(bus.getId(), bus);
                controller.addVehicle(bus);
            }
        }
        if (dto.snowplowPlayers != null) {
            for (GameState.SnowplowPlayerDto spDto : dto.snowplowPlayers) {
                SnowplowPlayer sp = SnowplowPlayer.createEmpty();
                sp.setId(spDto.id);
                playerMap.put(sp.getId(), sp);
                controller.addSnowplowPlayer(sp);
            }
        }

        // 5. Equipments
        if (dto.equipments != null) {
            for (GameState.EquipmentDto eqDto : dto.equipments) {
                SnowplowPlayer owner = (SnowplowPlayer) playerMap.get(eqDto.ownerId);
                Equipment eq;
                switch (eqDto.type) {
                    case "Salter":
                        eq = new Salter(owner);
                        if (eqDto.stateData.containsKey("ammo"))
                            eq.setAmmo((Integer) eqDto.stateData.get("ammo"));
                        break;
                    case "DragonBlade":
                        eq = new DragonBlade(owner);
                        if (eqDto.stateData.containsKey("ammo"))
                            eq.setAmmo((Integer) eqDto.stateData.get("ammo"));
                        break;
                    case "Gritter":
                        eq = new Gritter(owner);
                        if (eqDto.stateData.containsKey("ammo"))
                            eq.setAmmo((Integer) eqDto.stateData.get("ammo"));
                        break;
                    case "Sweeper":
                        eq = new Sweeper(owner);
                        break;
                    case "Impeller":
                        eq = new Impeller(owner);
                        break;
                    case "IceBreaker":
                        eq = new IceBreaker(owner);
                        break;
                    default:
                        eq = new IceBreaker(owner);
                        break;
                }
                eq.setId(eqDto.id);
                equipmentMap.put(eq.getId(), eq);
                controller.addEquipment(eq);
            }
        }

        // 6. Vehicles
        if (dto.cars != null) {
            for (GameState.CarDto cDto : dto.cars) {
                Car car = (Car) vehicleMap.get(cDto.id);
                if (car != null && cDto.location != null) {
                    MapComponent loc = locationMap.get(cDto.location.id);
                    if (loc != null)
                        car.setLocation(loc);
                }
            }
        }
        if (dto.buses != null) {
            for (GameState.BusDto bDto : dto.buses) {
                Bus bus = (Bus) vehicleMap.get(bDto.id);
                if (bus != null) {
                    if (bDto.location != null) {
                        MapComponent loc = locationMap.get(bDto.location.id);
                        if (loc != null)
                            bus.setLocation(loc);
                    }
                    if (bDto.isCrashed)
                        bus.crash();

                    // Look up BusPlayerDto again to find stop1/stop2
                    for (GameState.BusPlayerDto bpDto : dto.busPlayers) {
                        if (bpDto.vehicleId.equals(bDto.id)) {
                            List<Building> stations = new ArrayList<>();
                            if (bpDto.stop1 != null)
                                stations.add(buildingMap.get(bpDto.stop1));
                            if (bpDto.stop2 != null)
                                stations.add(buildingMap.get(bpDto.stop2));
                            bus.setStations(stations);
                            break;
                        }
                    }
                }
            }
        }
        if (dto.snowplows != null) {
            for (GameState.SnowplowDto sDto : dto.snowplows) {
                SnowplowPlayer owner = (SnowplowPlayer) playerMap.get(sDto.ownerId);
                Snowplow sp = new Snowplow();
                sp.setId(sDto.id);
                vehicleMap.put(sp.getId(), sp);
                controller.addVehicle(sp);
                if (owner != null)
                    owner.addSnowplow(sp);

                if (sDto.location != null) {
                    MapComponent loc = locationMap.get(sDto.location.id);
                    if (loc != null)
                        sp.setLocation(loc);
                }
                if (sDto.equipmentId != null) {
                    Equipment eq = equipmentMap.get(sDto.equipmentId);
                    if (eq != null)
                        sp.setEquipment(eq);
                }
            }
        }

        // Restore Lane Progress
        if (dto.map != null && dto.map.lanes != null) {
            for (GameState.LaneDto lDto : dto.map.lanes) {
                Lane l = laneMap.get(lDto.id);
                if (l != null && lDto.vehicles != null) {
                    for (Map<String, Integer> progMap : lDto.vehicles) {
                        for (Map.Entry<String, Integer> entry : progMap.entrySet()) {
                            Vehicle v = vehicleMap.get(entry.getKey());
                            if (v != null) {
                                l.setProgress(v, entry.getValue());
                            }
                        }
                    }
                }
            }
        }

        // 7. Recoverer
        if (dto.recoverer != null) {
            Recoverer rec = new Recoverer();
            controller.setRecoverer(rec);

            // Rebuild queue exactly
            for (String carId : dto.recoverer.recoveryQueue) {
                Car c = (Car) vehicleMap.get(carId);
                if (c != null) {
                    rec.getRecoveryQueue().add(c);
                }
            }

            // Wire Car.setRecoverer
            for (Vehicle v : vehicleMap.values()) {
                if (v instanceof Car) {
                    ((Car) v).setRecoverer(rec);
                }
            }
        }

        // 8. Bank
        if (dto.bank != null) {
            Bank bank = new Bank();
            controller.setBank(bank);
            rn.setBank(bank);
            if (dto.bank.accounts != null) {
                for (Map.Entry<String, Integer> entry : dto.bank.accounts.entrySet()) {
                    Player p = playerMap.get(entry.getKey());
                    if (p != null) {
                        bank.getAccounts().put(p, entry.getValue());
                    }
                }
            }
        }

        // 9. Warehouse
        if (dto.warehouse != null) {
            Warehouse wh = (Warehouse) buildingMap.get(dto.warehouse.id);
            if (wh != null) {
                // Wire Bank and Warehouse
                Bank bank = controller.getBank();
                if (bank != null) {
                    wh.setBank(bank);
                    bank.setWarehouse(wh);
                }

                if (dto.warehouse.stock != null) {
                    for (String eqId : dto.warehouse.stock) {
                        Equipment eq = equipmentMap.get(eqId);
                        if (eq != null) {
                            wh.getStock().add(eq);
                        }
                    }
                }

                // Wire SnowplowPlayers to Warehouse
                if (dto.snowplowPlayers != null) {
                    for (GameState.SnowplowPlayerDto spDto : dto.snowplowPlayers) {
                        if (spDto.warehouseId != null && spDto.warehouseId.equals(wh.getId())) {
                            SnowplowPlayer sp = (SnowplowPlayer) playerMap.get(spDto.id);
                            if (sp != null)
                                sp.setWarehouse(wh);
                        }
                    }
                }
            }
        }
    }

    // =========================================================================
    // Minimal JSON AST & Parser (Zero Dependencies)
    // =========================================================================

    abstract static class Node {
    }

    static class JNull extends Node {
        static final JNull I = new JNull();
    }

    static class JBool extends Node {
        final boolean v;

        JBool(boolean v) {
            this.v = v;
        }
    }

    static class JNum extends Node {
        final double v;

        JNum(double v) {
            this.v = v;
        }
    }

    static class JStr extends Node {
        final String v;

        JStr(String v) {
            this.v = v;
        }
    }

    static class JArr extends Node {
        final List<Node> elems;

        JArr(List<Node> e) {
            elems = e;
        }
    }

    static class JObj extends Node {
        final List<String> keyOrder;
        final Map<String, Node> fields;

        JObj(List<String> ko, Map<String, Node> f) {
            keyOrder = ko;
            fields = f;
        }
    }

    private static class JsonParser {
        private final String s;
        private int pos;

        JsonParser(String s) {
            this.s = s;
        }

        Node parse() {
            skipWs();
            Node n = parseValue();
            skipWs();
            return n;
        }

        private Node parseValue() {
            char c = s.charAt(pos);
            if (c == '{')
                return parseObject();
            if (c == '[')
                return parseArray();
            if (c == '"')
                return parseString();
            if (c == 't' || c == 'f')
                return parseBool();
            if (c == 'n')
                return parseNull();
            return parseNumber();
        }

        private JObj parseObject() {
            consume('{');
            List<String> keyOrder = new ArrayList<>();
            Map<String, Node> fields = new LinkedHashMap<>();
            skipWs();
            if (peek() == '}') {
                pos++;
                return new JObj(keyOrder, fields);
            }
            while (true) {
                skipWs();
                String key = ((JStr) parseString()).v;
                skipWs();
                consume(':');
                skipWs();
                Node val = parseValue();
                keyOrder.add(key);
                fields.put(key, val);
                skipWs();
                if (peek() == ',') {
                    pos++;
                } else
                    break;
            }
            skipWs();
            consume('}');
            return new JObj(keyOrder, fields);
        }

        private JArr parseArray() {
            consume('[');
            List<Node> elems = new ArrayList<>();
            skipWs();
            if (peek() == ']') {
                pos++;
                return new JArr(elems);
            }
            while (true) {
                skipWs();
                elems.add(parseValue());
                skipWs();
                if (peek() == ',') {
                    pos++;
                } else
                    break;
            }
            skipWs();
            consume(']');
            return new JArr(elems);
        }

        private JStr parseString() {
            consume('"');
            StringBuilder sb = new StringBuilder();
            while (pos < s.length()) {
                char c = s.charAt(pos++);
                if (c == '"')
                    break;
                if (c == '\\') {
                    char e = s.charAt(pos++);
                    switch (e) {
                        case '"':
                            sb.append('"');
                            break;
                        case '\\':
                            sb.append('\\');
                            break;
                        case '/':
                            sb.append('/');
                            break;
                        case 'n':
                            sb.append('\n');
                            break;
                        case 'r':
                            sb.append('\r');
                            break;
                        case 't':
                            sb.append('\t');
                            break;
                        default:
                            sb.append(e);
                    }
                } else {
                    sb.append(c);
                }
            }
            return new JStr(sb.toString());
        }

        private JBool parseBool() {
            if (s.startsWith("true", pos)) {
                pos += 4;
                return new JBool(true);
            }
            pos += 5;
            return new JBool(false);
        }

        private JNull parseNull() {
            pos += 4;
            return JNull.I;
        }

        private JNum parseNumber() {
            int start = pos;
            if (peek() == '-')
                pos++;
            while (pos < s.length() && Character.isDigit(s.charAt(pos)))
                pos++;
            if (pos < s.length() && s.charAt(pos) == '.') {
                pos++;
                while (pos < s.length() && Character.isDigit(s.charAt(pos)))
                    pos++;
            }
            if (pos < s.length() && (s.charAt(pos) == 'e' || s.charAt(pos) == 'E')) {
                pos++;
                if (pos < s.length() && (s.charAt(pos) == '+' || s.charAt(pos) == '-'))
                    pos++;
                while (pos < s.length() && Character.isDigit(s.charAt(pos)))
                    pos++;
            }
            return new JNum(Double.parseDouble(s.substring(start, pos)));
        }

        private void skipWs() {
            while (pos < s.length() && s.charAt(pos) <= ' ')
                pos++;
        }

        private char peek() {
            return pos < s.length() ? s.charAt(pos) : 0;
        }

        private void consume(char c) {
            if (pos >= s.length() || s.charAt(pos) != c)
                throw new RuntimeException("Expected '" + c + "' at pos " + pos);
            pos++;
        }
    }
}
