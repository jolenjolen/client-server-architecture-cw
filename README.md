# Smart Campus API

A RESTful API built with JAX-RS (Jakarta EE 10) and GlassFish for managing Rooms and Sensors across a university campus.

---

## API Overview

This API provides a backend system for the university's Smart Campus initiative. It allows facilities managers to manage physical rooms and the sensors deployed within them (e.g. temperature monitors, CO2 sensors, occupancy trackers). The API follows RESTful principles and is built using JAX-RS on GlassFish 7.

**Base URL:** `http://localhost:8080/SmartCampusAPI/api/v1`

---

## Endpoints Summary

| Method | Endpoint | Description | Status |
|--------|----------|-------------|--------|
| GET | `/api/v1` | Discovery — API metadata and links | 200 |
| GET | `/api/v1/rooms` | List all rooms | 200 |
| POST | `/api/v1/rooms` | Create a new room | 201 |
| GET | `/api/v1/rooms/{roomId}` | Get a specific room | 200 / 404 |
| DELETE | `/api/v1/rooms/{roomId}` | Delete a room (blocked if sensors exist) | 204 / 409 |
| GET | `/api/v1/sensors` | List all sensors (optional `?type=` filter) | 200 |
| POST | `/api/v1/sensors` | Create a new sensor (validates roomId) | 201 / 422 |
| GET | `/api/v1/sensors/{sensorId}` | Get a specific sensor | 200 / 404 |
| GET | `/api/v1/sensors/{sensorId}/readings` | Get all readings for a sensor | 200 / 404 |
| POST | `/api/v1/sensors/{sensorId}/readings` | Add a new reading (blocked if MAINTENANCE) | 201 / 403 |

---

## How to Build and Run

### Prerequisites
- NetBeans 26
- GlassFish 7 (bundled with NetBeans)
- JDK 21
- Maven

### Steps
1. Clone the repository:
   ```
   git clone https://github.com/jolenjolen/client-server-architecture-cw.git
   ```
2. Open NetBeans → **File → Open Project** → select the cloned folder
3. Right-click the project → **Clean and Build**
4. Right-click the project → **Run** (this starts GlassFish and deploys automatically)
5. API is available at: `http://localhost:8080/SmartCampusAPI/api/v1`

---

## Sample curl Commands

```bash
# 1. Discovery endpoint
curl -X GET http://localhost:8080/SmartCampusAPI/api/v1

# 2. Create a room
curl -X POST http://localhost:8080/SmartCampusAPI/api/v1/rooms \
  -H "Content-Type: application/json" \
  -d '{"id":"LIB-301","name":"Library Quiet Study","capacity":50}'

# 3. Get all rooms
curl -X GET http://localhost:8080/SmartCampusAPI/api/v1/rooms

# 4. Create a sensor
curl -X POST http://localhost:8080/SmartCampusAPI/api/v1/sensors \
  -H "Content-Type: application/json" \
  -d '{"id":"TEMP-001","type":"Temperature","status":"ACTIVE","currentValue":21.5,"roomId":"LIB-301"}'

# 5. Get sensors filtered by type
curl -X GET "http://localhost:8080/SmartCampusAPI/api/v1/sensors?type=Temperature"

# 6. Post a sensor reading
curl -X POST http://localhost:8080/SmartCampusAPI/api/v1/sensors/TEMP-001/readings \
  -H "Content-Type: application/json" \
  -d '{"value":22.3}'

# 7. Try deleting a room that has sensors (returns 409)
curl -X DELETE http://localhost:8080/SmartCampusAPI/api/v1/rooms/LIB-301
```

---

## Seed Data — Populate a Sample Campus

Run these curl commands in order to populate the API with 10 rooms and 20 sensors across different types. This gives you a realistic dataset to explore and test with.

> **Note:** Run all room commands first, then the sensor commands. On Windows PowerShell use double quotes and escape inner quotes, or use Postman's bulk import instead.

### Step 1 — Create 10 Rooms

```bash
curl -X POST http://localhost:8080/SmartCampusAPI/api/v1/rooms -H "Content-Type: application/json" -d "{\"id\":\"LIB-301\",\"name\":\"Library Quiet Study\",\"capacity\":50}"

curl -X POST http://localhost:8080/SmartCampusAPI/api/v1/rooms -H "Content-Type: application/json" -d "{\"id\":\"LAB-101\",\"name\":\"Computer Science Lab\",\"capacity\":30}"

curl -X POST http://localhost:8080/SmartCampusAPI/api/v1/rooms -H "Content-Type: application/json" -d "{\"id\":\"LECT-201\",\"name\":\"Main Lecture Hall\",\"capacity\":200}"

curl -X POST http://localhost:8080/SmartCampusAPI/api/v1/rooms -H "Content-Type: application/json" -d "{\"id\":\"CAFE-001\",\"name\":\"Student Cafeteria\",\"capacity\":120}"

curl -X POST http://localhost:8080/SmartCampusAPI/api/v1/rooms -H "Content-Type: application/json" -d "{\"id\":\"GYM-001\",\"name\":\"Sports Hall\",\"capacity\":80}"

curl -X POST http://localhost:8080/SmartCampusAPI/api/v1/rooms -H "Content-Type: application/json" -d "{\"id\":\"OFF-401\",\"name\":\"Faculty Offices\",\"capacity\":20}"

curl -X POST http://localhost:8080/SmartCampusAPI/api/v1/rooms -H "Content-Type: application/json" -d "{\"id\":\"MED-101\",\"name\":\"Medical Centre\",\"capacity\":15}"

curl -X POST http://localhost:8080/SmartCampusAPI/api/v1/rooms -H "Content-Type: application/json" -d "{\"id\":\"STUDY-501\",\"name\":\"Postgraduate Study Room\",\"capacity\":40}"

curl -X POST http://localhost:8080/SmartCampusAPI/api/v1/rooms -H "Content-Type: application/json" -d "{\"id\":\"SERVER-001\",\"name\":\"Server Room\",\"capacity\":5}"

curl -X POST http://localhost:8080/SmartCampusAPI/api/v1/rooms -H "Content-Type: application/json" -d "{\"id\":\"HALL-001\",\"name\":\"Main Entrance Hall\",\"capacity\":100}"
```

### Step 2 — Create 20 Sensors

```bash
# Library
curl -X POST http://localhost:8080/SmartCampusAPI/api/v1/sensors -H "Content-Type: application/json" -d "{\"id\":\"TEMP-001\",\"type\":\"Temperature\",\"status\":\"ACTIVE\",\"currentValue\":21.5,\"roomId\":\"LIB-301\"}"
curl -X POST http://localhost:8080/SmartCampusAPI/api/v1/sensors -H "Content-Type: application/json" -d "{\"id\":\"OCC-001\",\"type\":\"Occupancy\",\"status\":\"ACTIVE\",\"currentValue\":34.0,\"roomId\":\"LIB-301\"}"

# CS Lab
curl -X POST http://localhost:8080/SmartCampusAPI/api/v1/sensors -H "Content-Type: application/json" -d "{\"id\":\"CO2-001\",\"type\":\"CO2\",\"status\":\"ACTIVE\",\"currentValue\":412.0,\"roomId\":\"LAB-101\"}"
curl -X POST http://localhost:8080/SmartCampusAPI/api/v1/sensors -H "Content-Type: application/json" -d "{\"id\":\"TEMP-002\",\"type\":\"Temperature\",\"status\":\"ACTIVE\",\"currentValue\":23.1,\"roomId\":\"LAB-101\"}"

# Lecture Hall
curl -X POST http://localhost:8080/SmartCampusAPI/api/v1/sensors -H "Content-Type: application/json" -d "{\"id\":\"OCC-002\",\"type\":\"Occupancy\",\"status\":\"ACTIVE\",\"currentValue\":180.0,\"roomId\":\"LECT-201\"}"
curl -X POST http://localhost:8080/SmartCampusAPI/api/v1/sensors -H "Content-Type: application/json" -d "{\"id\":\"LIGHT-001\",\"type\":\"Lighting\",\"status\":\"ACTIVE\",\"currentValue\":75.0,\"roomId\":\"LECT-201\"}"

# Cafeteria
curl -X POST http://localhost:8080/SmartCampusAPI/api/v1/sensors -H "Content-Type: application/json" -d "{\"id\":\"CO2-002\",\"type\":\"CO2\",\"status\":\"ACTIVE\",\"currentValue\":600.0,\"roomId\":\"CAFE-001\"}"
curl -X POST http://localhost:8080/SmartCampusAPI/api/v1/sensors -H "Content-Type: application/json" -d "{\"id\":\"TEMP-003\",\"type\":\"Temperature\",\"status\":\"MAINTENANCE\",\"currentValue\":19.0,\"roomId\":\"CAFE-001\"}"

# Sports Hall
curl -X POST http://localhost:8080/SmartCampusAPI/api/v1/sensors -H "Content-Type: application/json" -d "{\"id\":\"OCC-003\",\"type\":\"Occupancy\",\"status\":\"ACTIVE\",\"currentValue\":45.0,\"roomId\":\"GYM-001\"}"
curl -X POST http://localhost:8080/SmartCampusAPI/api/v1/sensors -H "Content-Type: application/json" -d "{\"id\":\"HUMID-001\",\"type\":\"Humidity\",\"status\":\"ACTIVE\",\"currentValue\":62.0,\"roomId\":\"GYM-001\"}"

# Faculty Offices
curl -X POST http://localhost:8080/SmartCampusAPI/api/v1/sensors -H "Content-Type: application/json" -d "{\"id\":\"TEMP-004\",\"type\":\"Temperature\",\"status\":\"ACTIVE\",\"currentValue\":22.0,\"roomId\":\"OFF-401\"}"
curl -X POST http://localhost:8080/SmartCampusAPI/api/v1/sensors -H "Content-Type: application/json" -d "{\"id\":\"LIGHT-002\",\"type\":\"Lighting\",\"status\":\"OFFLINE\",\"currentValue\":0.0,\"roomId\":\"OFF-401\"}"

# Medical Centre
curl -X POST http://localhost:8080/SmartCampusAPI/api/v1/sensors -H "Content-Type: application/json" -d "{\"id\":\"TEMP-005\",\"type\":\"Temperature\",\"status\":\"ACTIVE\",\"currentValue\":20.5,\"roomId\":\"MED-101\"}"
curl -X POST http://localhost:8080/SmartCampusAPI/api/v1/sensors -H "Content-Type: application/json" -d "{\"id\":\"HUMID-002\",\"type\":\"Humidity\",\"status\":\"ACTIVE\",\"currentValue\":45.0,\"roomId\":\"MED-101\"}"

# Postgraduate Study Room
curl -X POST http://localhost:8080/SmartCampusAPI/api/v1/sensors -H "Content-Type: application/json" -d "{\"id\":\"CO2-003\",\"type\":\"CO2\",\"status\":\"ACTIVE\",\"currentValue\":390.0,\"roomId\":\"STUDY-501\"}"
curl -X POST http://localhost:8080/SmartCampusAPI/api/v1/sensors -H "Content-Type: application/json" -d "{\"id\":\"OCC-004\",\"type\":\"Occupancy\",\"status\":\"ACTIVE\",\"currentValue\":12.0,\"roomId\":\"STUDY-501\"}"

# Server Room
curl -X POST http://localhost:8080/SmartCampusAPI/api/v1/sensors -H "Content-Type: application/json" -d "{\"id\":\"TEMP-006\",\"type\":\"Temperature\",\"status\":\"ACTIVE\",\"currentValue\":18.0,\"roomId\":\"SERVER-001\"}"
curl -X POST http://localhost:8080/SmartCampusAPI/api/v1/sensors -H "Content-Type: application/json" -d "{\"id\":\"HUMID-003\",\"type\":\"Humidity\",\"status\":\"ACTIVE\",\"currentValue\":30.0,\"roomId\":\"SERVER-001\"}"

# Entrance Hall
curl -X POST http://localhost:8080/SmartCampusAPI/api/v1/sensors -H "Content-Type: application/json" -d "{\"id\":\"OCC-005\",\"type\":\"Occupancy\",\"status\":\"ACTIVE\",\"currentValue\":55.0,\"roomId\":\"HALL-001\"}"
curl -X POST http://localhost:8080/SmartCampusAPI/api/v1/sensors -H "Content-Type: application/json" -d "{\"id\":\"LIGHT-003\",\"type\":\"Lighting\",\"status\":\"ACTIVE\",\"currentValue\":90.0,\"roomId\":\"HALL-001\"}"
```

### Step 3 — Verify
```bash
# Should return 10 rooms
curl -X GET http://localhost:8080/SmartCampusAPI/api/v1/rooms

# Should return 20 sensors
curl -X GET http://localhost:8080/SmartCampusAPI/api/v1/sensors

# Filter by type examples
curl -X GET "http://localhost:8080/SmartCampusAPI/api/v1/sensors?type=Temperature"
curl -X GET "http://localhost:8080/SmartCampusAPI/api/v1/sensors?type=CO2"
curl -X GET "http://localhost:8080/SmartCampusAPI/api/v1/sensors?type=Occupancy"
```

---

## Sample curl Commands

### Part 1 — Service Architecture & Setup

**Q1.1: What is the default lifecycle of a JAX-RS Resource class? How does this impact in-memory data management?**

By default, JAX-RS creates a **new instance of each resource class for every incoming HTTP request**. This is known as the per-request lifecycle. While this approach is thread-safe at the instance level since no two requests share the same object, it presents a significant challenge for managing in-memory data: if each resource instance held its own HashMap or ArrayList, data would be lost after every request since the object is discarded once the response is sent.

To address this, a **singleton DataStore class** was implemented using the Singleton design pattern. The DataStore holds all shared HashMaps (rooms, sensors, readings) as a single static instance accessible across all resource classes. This ensures data persists across requests. In a production system, thread safety would also need to be considered using `ConcurrentHashMap` or synchronised blocks to prevent race conditions when multiple requests modify the data simultaneously.

**Q1.2: Why is HATEOAS considered a hallmark of advanced RESTful design? How does it benefit client developers?**

HATEOAS (Hypermedia as the Engine of Application State) is the principle that API responses should include hyperlinks that guide the client on what actions are available next, rather than requiring the client to construct URLs manually. For example, a response returning a room object might also include links such as `GET /rooms/LIB-301/sensors` or `DELETE /rooms/LIB-301`.

This benefits client developers in several ways. First, it reduces coupling between the client and server — clients do not need to hardcode URL structures, so if the API changes its paths, clients following the links will still work. Second, it makes the API self-documenting and discoverable at runtime, meaning a developer can explore the API by following links rather than relying solely on static documentation. This is analogous to browsing a website by clicking links rather than memorising every URL.

---

### Part 2 — Room Management

**Q2.1: What are the implications of returning only IDs versus full room objects in a list response?**

Returning only IDs is lightweight and reduces network bandwidth significantly, which is beneficial when there are thousands of rooms. However, it forces the client to make a separate `GET /rooms/{id}` request for each room it wants details on — known as the N+1 problem — which increases latency and the number of round trips.

Returning full room objects increases the response payload size but gives the client everything it needs in a single request, reducing round trips and client-side complexity. For this API, returning full objects is the better choice since the room data is relatively small and clients are likely to need the full details immediately.

**Q2.2: Is DELETE idempotent in your implementation?**

Yes, the DELETE operation is idempotent in this implementation. Idempotency means that sending the same request multiple times produces the same result as sending it once. If a client sends `DELETE /rooms/LIB-301` and the room exists, it is deleted and a `204 No Content` is returned. If the same request is sent again, the room no longer exists and the API returns `404 Not Found`. The end state of the system — the room not existing — is the same regardless of how many times the request is sent. This aligns with the HTTP specification which states DELETE must be idempotent, even if subsequent calls return different status codes.

---

### Part 3 — Sensor Operations & Linking

**Q3.1: What happens if a client sends data in a format other than application/json to a method annotated with @Consumes(MediaType.APPLICATION_JSON)?**

If a client sends a request with a `Content-Type` header of `text/plain` or `application/xml` to an endpoint annotated with `@Consumes(MediaType.APPLICATION_JSON)`, JAX-RS will automatically reject the request before it even reaches the resource method. It returns an **HTTP 415 Unsupported Media Type** response, indicating that the server cannot process the request body in the format provided. This is handled entirely by the JAX-RS runtime — no custom code is needed. It acts as a contract enforcement mechanism, ensuring the API only processes data it knows how to deserialise.

**Q3.2: Why is @QueryParam preferred over path-based filtering for searching collections?**

Using a query parameter such as `GET /sensors?type=CO2` is preferred over a path-based approach like `GET /sensors/type/CO2` for several reasons. Firstly, query parameters are semantically designed for filtering, searching, and sorting — they are optional by nature and do not imply a hierarchical resource relationship. A path segment like `/type/CO2` implies that `type` is a sub-resource of `sensors`, which is architecturally misleading.

Secondly, query parameters are more flexible and composable — multiple filters can be combined easily, for example `?type=CO2&status=ACTIVE`, whereas path-based filtering becomes increasingly awkward with multiple criteria. Finally, the REST convention is that paths represent resource identities (e.g. `/sensors/TEMP-001`) while query strings represent optional modifiers on a collection.

---

### Part 4 — Deep Nesting with Sub-Resources

**Q4.1: What are the architectural benefits of the Sub-Resource Locator pattern?**

The Sub-Resource Locator pattern allows a resource class to delegate handling of a nested path to a separate, dedicated class rather than defining all endpoints in one large controller. In this API, `SensorResource` delegates `/sensors/{sensorId}/readings` to a dedicated `SensorReadingResource` class.

The key benefits are separation of concerns and maintainability. Each class has a single, clear responsibility — `SensorResource` handles sensor-level operations while `SensorReadingResource` handles reading-level operations. This makes the codebase easier to navigate, test, and extend. In large APIs with many nested resources, defining every path in one class would result in an unmanageable, monolithic controller. The locator pattern also allows the sub-resource to receive contextual data (in this case the `sensorId`) via its constructor, keeping the logic clean and focused.

---

### Part 5 — Error Handling, Exception Mapping & Logging

**Q5.2: Why is HTTP 422 more semantically accurate than 404 when the issue is a missing reference inside a valid JSON payload?**

A `404 Not Found` response typically means the requested **URL resource** does not exist — for example, trying to access `/rooms/FAKE-ROOM` directly. However, when a client sends a valid POST request to `/sensors` with a well-formed JSON body, but the `roomId` field references a room that doesn't exist, the URL itself is valid and the request was understood. The problem is a **semantic validation failure** within the request body.

HTTP 422 Unprocessable Entity is more accurate in this case because it signals that the request was syntactically correct and the server understood it, but could not process it due to a logical or referential error in the content. It communicates clearly that the issue is with the data provided, not with the URL or the format of the request.

**Q5.4: What are the cybersecurity risks of exposing Java stack traces to external API consumers?**

Exposing raw Java stack traces in API error responses is a significant security risk for several reasons. Stack traces reveal the internal structure of the application — including package names, class names, method names, and line numbers — which gives an attacker a detailed map of the codebase. This information can be used to identify specific frameworks, libraries, and their versions, allowing the attacker to look up known vulnerabilities (CVEs) for those exact versions.

Additionally, stack traces may expose file system paths, database query details, or configuration information. An attacker performing reconnaissance could use repeated error triggers to build a comprehensive picture of the system's internals and craft targeted exploits. The Global Exception Mapper in this API prevents this by catching all unexpected exceptions and returning a generic 500 response with no internal details exposed.

**Q5.5: Why is it better to use JAX-RS filters for cross-cutting concerns like logging rather than inserting Logger.info() calls into every resource method?**

Using JAX-RS filters for logging is far superior to manually inserting log statements into each resource method for several reasons. Firstly, it follows the **DRY principle** (Don't Repeat Yourself) — the logging logic is written once in the filter and automatically applied to every request and response across the entire API, rather than being duplicated across dozens of methods.

Secondly, it enforces **consistency** — manually inserted log statements are prone to being forgotten or written differently by different developers, leading to inconsistent log formats. A filter guarantees uniform logging for every endpoint.

Thirdly, it achieves **separation of concerns** — resource methods should focus solely on business logic. Mixing in logging, authentication, or metrics code makes methods harder to read and maintain. Filters handle these cross-cutting concerns at the infrastructure level, keeping resource classes clean and focused.
