# Courier Company Simulation

A multithreaded Java simulation of a courier company's daily operations. The system models the full lifecycle of shipments — from creation and warehouse storage to delivery and client notification.

## Design Patterns Used

- **Builder** — `Address` construction with `AddressBuilder`
- **Strategy** — interchangeable pricing algorithms via `PriceStrategy`
- **Observer** — clients receive delivery notifications via `DeliveryObserver`
- **Singleton** — centralized logging through `DeliveryLogger`
- **Factory** — shipment creation and logging via `ShipmentFactory`

## Overview

The goal is to simulate how a courier company receives, stores, routes, and delivers shipments. The simulation emphasizes correct use of OOP principles, design patterns, and concurrent programming with threads.

## Domain Model

### Shipments

The core entity in the system. Each shipment has:

- A unique auto-generated identifier
- A weight (in grams)
- A type that determines both its priority in processing queues and its delivery duration:
  - **Express** — highest priority, fastest delivery
  - **Fragile** — medium priority, slowest delivery (requires careful handling)
  - **Standard** — lowest priority, moderate delivery time
- A label containing the destination address, receiver name, and barcode
- A price calculated at creation time using a configurable pricing strategy
- A reference to the client who sent it

### Clients

Clients send shipments and receive real-time notifications when their deliveries are completed. They are uniquely identified by a personal identification number (PIN), which is never exposed externally.

### Warehouses

Warehouses act as intermediate storage before shipments are picked up by couriers. Each warehouse:

- Is located at a specific address
- Has a fixed capacity (maximum number of shipments it can hold)
- Stores shipments in a priority queue, so express shipments are always processed first
- When full, incoming shipments are rerouted to another warehouse. If no warehouse has space, the shipment is rejected

### Couriers

Each courier operates on a dedicated thread and is responsible for a specific route (city). A courier:

- Has a weight limit determining how many shipments they can carry
- Picks up shipments from a warehouse that match their route and fit within their weight capacity
- Delivers shipments sequentially, with delivery time proportional to the shipment type
- Notifies the client upon successful delivery

### Company

The company orchestrates the entire operation. It manages a list of warehouses and handles shipment routing logic — first attempting to place a shipment in a warehouse in the destination city, then falling back to any available warehouse, and finally rejecting the shipment if all warehouses are full.

## Pricing Strategies

The system supports multiple interchangeable pricing strategies (Strategy pattern):

- **Weight-based** — base cost plus a rate proportional to shipment weight
- **Priority-based** — cost scales with shipment priority level
- **Destination-based** — price determined by the destination city, with a default for unlisted cities

## Logging

All significant events are logged in real time to both the console and a log file. Logged events include:

- Shipment creation
- Shipment added to a warehouse
- Shipment rerouted to a different warehouse
- Shipment rejected (no available warehouse)
- Delivery started and completed
- Client notification

## Simulation

The simulation creates at least 100 shipments at regular intervals. Multiple couriers operate in parallel, each on their own thread, processing shipments from their assigned warehouses. The simulation ends when all shipments have been either delivered or rejected.

## Tech

- Java 21+
- Concurrency: `ExecutorService`, `ScheduledExecutorService`, `ConcurrentLinkedQueue`, `PriorityQueue` with synchronization, `AtomicInteger`
- No external dependencies
