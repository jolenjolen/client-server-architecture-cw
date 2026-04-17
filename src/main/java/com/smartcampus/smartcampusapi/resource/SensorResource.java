package com.smartcampus.smartcampusapi.resource;

import com.smartcampus.smartcampusapi.data.DataStore;
import com.smartcampus.smartcampusapi.model.Room;
import com.smartcampus.smartcampusapi.model.Sensor;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Path("/sensors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorResource {

    private final DataStore store = DataStore.getInstance();
    @DELETE
    @Path("/{sensorId}")
    public Response deleteSensor(@PathParam("sensorId") String sensorId) {
        Sensor sensor = store.getSensors().get(sensorId);

        if (sensor == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\":\"Sensor not found: " + sensorId + "\"}")
                    .build();
        }

        // Remove sensor from its room's sensorIds list
        Room room = store.getRooms().get(sensor.getRoomId());
        if (room != null) {
            room.getSensorIds().remove(sensor.getId());
        }

        store.getSensors().remove(sensorId);
        return Response.noContent().build();
    }
    // GET /api/v1/sensors — list all sensors, optional ?type= filter
    @GET
    public Response getAllSensors(@QueryParam("type") String type) {
        List<Sensor> sensorList = new ArrayList<>(store.getSensors().values());

        if (type != null && !type.isBlank()) {
            sensorList.removeIf(s -> !s.getType().equalsIgnoreCase(type));
        }

        return Response.ok(sensorList).build();
    }

    // POST /api/v1/sensors — create a sensor
    @POST
    public Response createSensor(Sensor sensor) {
        Map<String, Sensor> sensors = store.getSensors();
        Map<String, com.smartcampus.smartcampusapi.model.Room> rooms = store.getRooms();

        if (sensor.getId() == null || sensor.getId().isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"Sensor ID is required\"}")
                    .build();
        }

        if (sensors.containsKey(sensor.getId())) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("{\"error\":\"Sensor with this ID already exists\"}")
                    .build();
        }

        // Verify the roomId exists
        if (sensor.getRoomId() == null || !rooms.containsKey(sensor.getRoomId())) {
            return Response.status(422)
                    .entity("{\"error\":\"Room not found: " + sensor.getRoomId() + ". Create the room first.\"}")
                    .build();
        }

        // Register sensor and link it to the room
        sensors.put(sensor.getId(), sensor);
        rooms.get(sensor.getRoomId()).getSensorIds().add(sensor.getId());

        return Response.status(Response.Status.CREATED).entity(sensor).build();
    }

    // GET /api/v1/sensors/{sensorId} — get a specific sensor
    @GET
    @Path("/{sensorId}")
    public Response getSensor(@PathParam("sensorId") String sensorId) {
        Sensor sensor = store.getSensors().get(sensorId);

        if (sensor == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\":\"Sensor not found: " + sensorId + "\"}")
                    .build();
        }

        return Response.ok(sensor).build();
    }

    // Sub-resource locator — delegates to SensorReadingResource
    @Path("/{sensorId}/readings")
    public SensorReadingResource getReadingsResource(@PathParam("sensorId") String sensorId) {
        return new SensorReadingResource(sensorId);
    }
} 