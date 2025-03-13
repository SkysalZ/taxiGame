# ShadowTaxi 🚕  
*A Survival Taxi Simulation Game*  

---

## 📜 Overview  
Navigate an endless road as a taxi driver in a struggling economy! Pick up passengers, dodge enemy cars, collect coins, and race against time to reach the target score of **500** before health depletes or the clock runs out. Built with Java and the Bagel engine.  

---

## 🎯 Objectives  
- **Earn 500 points** by completing passenger trips.  
- **Avoid collisions** with cars, enemy cars, and fireballs to protect your taxi, driver, and passengers.  
- **Survive** for 15,000 frames (≈2.5 minutes at 120Hz) without losing all health.  

---

## 🕹️ Controls  
- **↑ Arrow**: Move taxi forward (or driver upward when outside the taxi).  
- **←/→ Arrow**: Steer taxi left/right.  
- **Enter**: Start game/submit player name.  
- **Space**: Restart after game over.  
- **Backspace**: Delete characters in name input.  

---

## 🚦 How to Play  
1. **Pick Up Passengers**:  
   - Stop near a passenger (distance ≤ 100 pixels).  
   - Deliver them to their **trip end flag** to earn money.  

2. **Avoid Obstacles**:  
   - Collisions with cars/enemy cars reduce health.  
   - **Invincible Power**: Grants 1000 frames of immunity.  
   - **Coins**: Increase passenger priority (higher earnings).  

3. **Manage Health**:  
   - Taxi, driver, and passengers start with 100 health.  
   - Health drops to 0? Game over!  

4. **Beat the Clock**:  
   - Complete trips before 15,000 frames expire.  

---

## ⚙️ Game Elements  
- **Taxi**: Move left/right/up. Health displayed as "TAXI [health]".  
- **Passengers**: Priority 1 (highest) to 3. Collect coins to boost priority.  
- **Cars/Enemy Cars**: Move upward randomly. Enemy cars shoot fireballs!  
- **Weather**: Rain lowers passenger priority (unless they have an umbrella).  

---

## 💰 Scoring & Penalties  
- **Earnings** = `(Distance × 0.1) + Priority Fee`  
  - Priority Fees: 50 (1), 20 (2), 10 (3).  
- **Penalty**: Stopping past the trip end flag reduces earnings by `0.05 × overshoot distance`.  

---

## 🖥️ Screens  
1. **Home Screen**: Press Enter to start.  
2. **Player Info**: Enter your name.  
3. **Game Play**: Navigate the endless road with dynamic weather.  
4. **Game End**: View top 5 scores and win/loss message.  

---

## 🛠️ Installation  
1. **Requirements**:  
   - Java JDK 11+  
   - Maven  
   - IntelliJ (recommended)  

2. **Run**:  
   ```bash  
   git clone [your-repo-link]  
   mvn clean package  
   java -jar target/shadowtaxi.jar
## 💡 Tips
- Stay in lanes (360, 480, 620 x-coordinates) to avoid cars.

- Use invincibility power strategically during heavy traffic.

- Prioritize high-priority passengers for faster earnings!
1. **Home Screen**: Press Enter to start.  
2. **Player Info**: Enter your name.  
3. **Game Play**: Navigate the endless road with dynamic weather.  
4. **Game End**: View top 5 scores and win/loss message.  
