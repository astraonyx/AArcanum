# Specification: Auralys Music Chamber Integration

## Problem Statement

Users of Arcanum requiring strong plausible deniability currently lack an innocent, fully functional daily-driver audio application disguise. While Arcanum provides a secure cryptographic vault, an MT file explorer, and a media downloader, accessing private audio media under scrutiny requires a plausible cover. Furthermore, users who consume both local private audio (recordings, unreleased tracks, and sound packages) and online music streaming currently must toggle between disjoint applications, creating digital footprints and risking accidental exposure.

## Solution

Arcanum embeds the complete **Auralys Music Chamber** natively into the application as an isolated stealth music player persona. Auralys is triggered directly from the primary Security Gate via the 3-layer default Skip-Setup PIN `000000-000000-000000` (or custom credentials configured in Onboarding Setup or Isolated Vault Settings).

The Auralys suite preserves 100% of its authentic UI and feature depth:
1. **Four Canonical Navigation Tabs**: Home (recommendations & quick picks), Explore (trending, genres & charts), Library (playlists, local audio, and saved albums), and Search (real-time tracks, artists, and albums).
2. **Persistent Bottom Mini-Player & Expanding Now Playing Sheet**: Expanding smoothly into a full-screen deck featuring album art, seek scrubber, synchronized streaming and local lyrics, audio waveform visualizer, sleep timer, playback speed control (0.75x–2.0x), and repeat/shuffle controls.
3. **Hybrid Playback & Queue Isolation**: Local encrypted vault audio enforces strict *Folder-Scoped Playlist* isolation (preventing cross-folder track leakage), whereas online streaming engages dynamic algorithmic queue progression.
4. **Disguised Stealth Exit**: A discreet profile action securely locks the session and returns immediately to the Arcanum Security Gate.

## User Stories

1. As a privacy-conscious user under duress, I want to enter `000000-000000-000000` at the security gate, so that the application seamlessly opens an innocent, fully featured music streaming player without revealing any secret vault files.
2. As a first-time user opting to skip onboarding setup, I want the system to assign `000000-000000-000000` as the default credentials for Auralys, so that I can immediately explore the app without tedious initial configuration.
3. As a methodical user completing normal onboarding, I want to configure a custom 3-layer 6-digit PIN for the Auralys Music Chamber, so that my music disguise remains unique and secured against common guesses.
4. As an active user, I want to change my Auralys 3-layer PIN inside Isolated Vault Settings, so that I can update my access credentials at any time without resetting other vaults.
5. As an audiophile, I want the Auralys user interface to remain completely identical to the original standalone experience, so that no controls, buttons, or aesthetic cues feel broken or foreign.
6. As a listener browsing trending tracks, I want to use the Explore tab to discover songs and genres, so that I can discover new music directly within the application.
7. As a music lover, I want to search for songs, artists, and albums in the Search tab, so that I can find and stream tracks on demand.
8. As a listener enjoying private voice notes or music stored in my encrypted vault, I want Auralys to play these files with full fidelity, so that I do not need to export them to unencrypted storage.
9. As a privacy user listening to vault audio packages, I want playback to strictly enforce a Folder-Scoped Queue, so that tracks from unrelated vault folders do not accidentally auto-advance into my ears.
10. As a streaming user, I want online tracks to dynamically generate recommended next tracks in the queue, so that I enjoy continuous, uninterrupted listening.
11. As a karaoke enthusiast, I want to view synchronized lyrics scrolling in real-time on the expanding player sheet, so that I can sing along to current tracks.
12. As a nighttime listener, I want to activate a sleep timer directly from the player controls, so that music playback automatically stops after a designated duration.
13. As a listener studying music or podcasts, I want to adjust playback speed between 0.75x, 1.0x, 1.25x, 1.5x, and 2.0x, so that I can consume audio at my preferred pace.
14. As a user viewing the player deck, I want to observe a real-time reactive waveform visualizer, so that the player feels visually dynamic and responsive to sound.
15. As a multitasking listener, I want a persistent bottom mini-player bar visible while navigating tabs, so that I retain instant play/pause and progress monitoring throughout the app.
16. As a user finishing my listening session, I want a subtle lock option in the profile/header menu, so that I can instantly terminate the music session and return safely to the Arcanum Security Gate.

## Implementation Decisions

### Modules & Subsystems

1. **Auralys Music Chamber Controller & UI Surface**:
   - Implements the complete 4-tab container (`Home`, `Explore`, `Library`, `Search`) alongside the persistent bottom mini-player and expanding player sheet.
   - Built natively with Jetpack Compose, preserving the authentic typography, touch targets, and motion dynamics.

2. **Hybrid Playback & Queue Engine**:
   - Manages playback state for both local vault audio streams and online media.
   - Enforces two queue modes: `FolderScopedQueue` (strict lock to active vault folder/archive) and `DynamicStreamingQueue` (online track progression and radio recommendations).

3. **Security Registry & Candidate Resolution**:
   - Introduces `ISOLATED_AURALYS_CANDIDATE_ID` to the multi-vault candidate router.
   - Updates `SecurityConfig` to persist `auralysPin1`, `auralysPin2`, and `auralysPin3` in the preferences datastore.
   - Provides explicit whitelist bypass for the default `000000` sequence when routing to the Auralys candidate, preserving weak-PIN protection for other vaults.

4. **Onboarding & Isolated Settings Integration**:
   - Adds the Auralys default credentials payload (`000000-000000-000000`) to the Quick Skip Setup handler.
   - Adds manual Auralys PIN input card to the Onboarding Wizard sequence.
   - Adds an isolated "Ubah PIN Auralys Music Chamber" card to Isolated Vault Settings (`EncryptionScriptEngineScreen`).

### Schema & Data Contracts

- Preferences Datastore keys for Auralys 3-layer PIN (`auralys_pin_1`, `auralys_pin_2`, `auralys_pin_3`).
- Candidate ID constant: `ISOLATED_AURALYS_CANDIDATE_ID = -105`.
- Playback queue model with `QueueSourceMode.VAULT_LOCAL_FOLDER` and `QueueSourceMode.ONLINE_STREAMING`.

## Testing Decisions

- **Good Test Criteria**: Tests must verify externally observable behavior (PIN resolution, queue progression without folder leakage, playback state transitions, and skip-setup defaults) rather than private internal widget state.
- **Modules Tested**:
  - `SecurityConfig` and `VaultRepository` PIN persistence and default values.
  - `VaultViewModel` candidate resolution when inputting `000000-000000-000000`.
  - `AuralysController` queue isolation and folder-scoping boundaries.
- **Prior Art**: Modeled after existing unit tests in `ExampleUnitTest.kt` and `ExampleRobolectricTest.kt`.

## Out of Scope

- Modifying existing ChaCha20/AES cryptographic primitives for files outside the audio category.
- Replacing the primary Cosmic Dragon mascot with external branding.
- Removing existing video theater capabilities in Arcanum.

## Further Notes

- Maintains complete offline availability for local vault playback even when network streaming APIs are unreachable.
