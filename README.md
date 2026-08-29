# 2D Android Racing Game (Under Development) 🏎️

This is an early-stage, initial prototype of a 2D Android racing game built entirely using modern UI development with **Jetpack Compose**. 

⚠️ **IMPORTANT NOTE:** This project is currently **under active development** and is **NOT** a completed game. More features will be added in future updates.

## Current Features Implemented:
* **Car Model Rendering:** The red car asset is rendered and managed dynamically inside a `Canvas` using custom transformations (translation, rotation, and scaling).
* **Interactive Steering Wheel:** A fully functional on-screen steering wheel UI component that detects user drag gestures.
* **Rotation & Control Logic:** Implemented math logic using `atan2` to calculate drag angles, allowing the player to rotate the steering wheel, which directly and smoothly influences the car's orientation.
* **Auto-Centering Wheel:** The steering wheel automatically resets to its default position (`0f`) once the user stops dragging.

## Tech Stack & Concepts Used:
* **Kotlin** & **Jetpack Compose**
* **Compose Canvas & Canvas Transformations** (`withTransform`, `rotate`, `translate`)
* **Pointer Input & Gesture Detection** (`detectDragGestures`)
* **Trigonometric Math** (`atan2` for angle calculation)

## Screenshots
Racing car 1: ![main car](images/car_red_1_m_model.png)  
The steering wheel: ![steering_wheel](images/steering_wheel.png)  

Normal accelerator pedal setting: ![accelerator_pedal_off](images/throttle_off.png) 

Pressing the accelerator pedal: ![accelerator_pedal_on](images/throttle_on.png)  

⚠️ **IMPORTANT NOTE:**
     The track image size is **too big** to be uploading here , 
     But you can get All THE TRACK's HERE FREE : ![Coda_Car_Racing_Tracks]([https://github.com/Coda-Level-Up/Car_Racing](https://www.patreon.com/codaLevelUp/posts/racing-tracks-168027608)

The path of track 1: ![ready path of track 1](images/ready_path_of_track_1.png)  

---
*Stay tuned for more updates as the game logic expands!*
