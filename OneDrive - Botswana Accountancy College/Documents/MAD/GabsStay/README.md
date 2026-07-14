# GabsStay — Android Accommodation Finder for Gaborone Students

Reference build for BAC MAD Assignment (Semester 2, 2026), Student ID CSE22-025.

## What's implemented (Part A, 100 marks)

| Feature | Where | Marks |
|---|---|---|
| **A. User Management** | `ui/login`, `ui/register`, `data/Student.kt`, `data/StudentDao.kt` — registration, login, role field (STUDENT/PROVIDER), 50 seeded students | 20 |
| **B. Listings** | `data/Listing.kt`, `ui/listings`, `ui/detail` — title, price (BWP), location, type, amenities, availability date, deposit, image, 50 seeded records | 25 |
| **C. Smart Filtering & Alerts** | `ui/filter/FilterActivity.kt`, `ListingDao.filterListings()`, `util/NotificationHelper.kt` — price range, location, availability date filter + local notification on match | 20 |
| **D. Deposit & Reservation** | `ui/reservation/ReservationActivity.kt`, `data/Reservation.kt` — simulated payment, generated reference number receipt, status flips to RESERVED, race-condition guard prevents double booking | 15 |
| **E. Extension — Chat** | `ui/chat/ChatActivity.kt`, `data/ChatMessage.kt` — persisted chat thread per listing between student and landlord | 20 |

## Database schema (Room, 4 tables)

- **students**(studentId PK, fullName, email, password, role, campus)
- **listings**(listingId PK, title, priceBWP, location, type, amenities, availableFrom, depositBWP, imageResId, status)
- **reservations**(reservationId PK, listingId FK, studentId FK, amountPaidBWP, referenceNumber, dateBooked, status)
- **chat_messages**(messageId PK, listingId FK, senderId, senderName, message, timestamp, isFromStudent)

Seed data (50 students + 50 listings) is generated in `data/SeedData.kt` and inserted automatically the first time the app creates the database (`AppDatabase.seedCallback`).

## Screens (5 required minimum — this build has 7)

1. Login
2. Register
3. Listings (home)
4. Filter
5. Listing Detail
6. Reservation / Payment receipt
7. Chat

## Tech stack

- Kotlin, Android Studio, Gradle Kotlin DSL
- minSdk 26, targetSdk/compileSdk 34
- Room (`room-runtime`, `room-ktx`, KSP compiler) for persistence
- Coroutines + LiveData for async DB access and reactive UI
- View Binding (no findViewById)
- Material Components (TextInputLayout, MaterialButton, MaterialCardView)

## Opening the project

This was authored outside Android Studio (no Android SDK / emulator available in this
environment), so it has **not** been compiled here. To build it:

1. Unzip and open the `GabsStay` folder in Android Studio (tested for Giraffe; AGP 8.1.4 / Gradle 8.4 / Kotlin 1.9.10 are pinned in the build files for that version — bump them if you're on a newer Android Studio).
2. Let Gradle sync — it will download the Android Gradle Plugin, Kotlin, KSP, Room,
   and AndroidX libraries listed in `app/build.gradle.kts`.
3. Run on an emulator or device with API 26+.
4. First launch auto-seeds 50 students (`student1@studentnest.bw` … `student50@studentnest.bw`,
   password `pass123` … `pass5023`, i.e. `pass{n}23`) and 50 listings — check `SeedData.kt`
   for the exact pattern if login testing is needed.

Since Gradle sync needs network access to Google's/Maven Central's repositories, expect the
first sync to take a few minutes.

## What you still need to do yourself (Part B, 30% of the assignment)

I can't produce these — they need you, your voice, and your own submission:

- **3–6 page PowerPoint report**: purpose, design, database schema (table above), screenshots,
  software products used. I can help you build this slide-by-slide whenever you're ready.
- **10-minute screencast (MP4)**: record with Screencast-O-Matic or similar, following the
  Guidance section in the brief (intro → purpose → design/schema/screenshots → software
  products → functionality demo).
- **Cover sheet** (Appendix 1) filled in and signed.
- **APK + zipped project + optional GitHub link** for submission.

## Suggested next step

Since this is meant as a study reference rather than your submitted work, I'd suggest reading
through each file, running it yourself in Android Studio, and rebuilding pieces you're less sure
about (e.g. the filter query, the reservation race-condition guard) in your own words before you
submit anything under your name.
