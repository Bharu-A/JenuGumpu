# 🍯 JenuGumpu – Honey Tracking & Profit Management App

## Overview

**JenuGumpu** (meaning *"Honey Collective"* in Kannada) is an offline-first Android application designed for rural honey producers, beekeepers, and agricultural cooperatives. The application helps users digitally track honey harvests, manage inventory, monitor batch quality, and calculate profits while supporting both English and Kannada languages.

Built with modern Android development practices, JenuGumpu focuses on accessibility, simplicity, and usability for rural communities with limited internet connectivity.

---

## Problem Statement

Small-scale beekeepers often rely on paper records or manual tracking methods to manage honey production. This creates challenges in:

* Maintaining accurate harvest records
* Tracking honey quality across batches
* Calculating profits and inventory value
* Managing production across multiple locations
* Accessing digital tools in local languages

JenuGumpu addresses these challenges by providing a lightweight, offline-first mobile solution tailored for rural honey producers.

---

## Key Features

### 📊 Smart Dashboard

* Total stock overview
* Batch count tracking
* Market price insights
* Recent harvest activity
* Educational beekeeping tips

### 🍯 Harvest Logging

* Harvest date selection
* Location tracking
* Quantity management
* Floral source categorization
* Honey quality indicators
* Automatic batch ID generation

### 🗂 Batch Management

* View all recorded batches
* Edit existing records
* Delete outdated entries
* Automatic dashboard updates
* Empty-state guidance for new users

### 💰 Profit Calculator

* Production cost tracking
* Selling price estimation
* Profit-per-kilogram calculation
* Total earnings projection
* Business improvement suggestions

### 👤 User Profile

* Custom producer profile
* Contact information management
* Experience tracking
* Personalized branding details

### ⚙ Settings & Localization

* Dark and Light mode support
* Notification preferences
* English & Kannada language support
* Offline-first functionality

---

## Technology Stack

### Android Development

* Kotlin
* Jetpack Compose
* Material 3 Design

### Architecture

* MVVM Architecture
* Clean Architecture
* Repository Pattern

### Data Layer

* Room Database (SQLite)
* Shared Preferences

### Dependency Injection

* Dagger Hilt

### Asynchronous Programming

* Kotlin Coroutines
* StateFlow

### Localization

* Dynamic Runtime Language Switching
* Kannada & English Support

---

## Architecture

Data Layer → Domain Layer → Presentation Layer

The application follows Clean Architecture principles to ensure maintainability, scalability, and testability.

* Presentation Layer: Jetpack Compose UI + ViewModels
* Domain Layer: Business Logic & Use Cases
* Data Layer: Room Database, Preferences, Repository Implementations

---

## Database Schema

### HoneyBatchEntity

| Field           | Description                |
| --------------- | -------------------------- |
| id              | Auto-generated primary key |
| dateEpochMillis | Harvest date               |
| location        | Hive location              |
| quantityKg      | Honey quantity             |
| floralSource    | Honey source type          |
| gradeColor      | Honey color classification |
| moistureLevel   | Moisture classification    |
| batchId         | Generated batch identifier |

Example Batch ID:

HNY-2026-001

HNY-2026-002

HNY-2026-003

---

## Target Users

### Rural Honey Producers

Beekeepers who need a simple digital solution for recording harvests and monitoring inventory.

### Honey Cooperatives

Organizations managing honey collections from multiple producers and locations.

---

## Project Goals

* Digitize rural honey production workflows
* Improve traceability of honey batches
* Simplify inventory management
* Provide transparent profit calculations
* Support local language accessibility
* Enable 100% offline operation

---

## Future Enhancements

* QR-based batch labeling
* Export reports to PDF
* Cloud synchronization
* Multi-user cooperative management
* Market price API integration
* Sales and customer tracking
* Advanced analytics dashboard

---

## Impact

JenuGumpu aims to empower rural beekeepers with accessible digital tools, helping them improve record management, maintain product quality, and make informed business decisions while preserving accessibility through local language support.

Built with ❤️ for rural communities and sustainable agriculture.
