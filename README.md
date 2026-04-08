# Autonomous VM Scaling System

A CloudSim Plus-based simulation of an autonomous VM scaling system using ensemble ML models.

## Project Structure

```
src/main/java/org/autonomous/
├── agent/
│   └── AutonomousAgent.java       # Scaling decision logic
├── models/
│   ├── AnomalyModel.java          # Variance-based anomaly detection
│   ├── AutoencoderModel.java      # Reconstruction error scoring
│   ├── EnsemblePredictor.java     # Combines all models
│   └── KMeansModel.java           # Cluster-based load classification
├── CSVLogger.java                 # Logs metrics to metrics.csv
├── FeatureBuilder.java            # Builds feature vectors from metrics
├── MainSimulation.java            # Entry point
├── MetricConfig.java              # Metric configuration
└── MetricMonitor.java             # Collects VM metrics
```

## Prerequisites

- Java JDK 11+
- Maven 3.6+

## How to Run

```bash
mvn clean compile
mvn exec:java
```

## Output

- Console logs with scaling decisions (SCALE_OUT / SCALE_IN / NONE)
- `metrics.csv` generated in project root with columns: `Time, VM, CPU, RAM, BW, Score, Action`
