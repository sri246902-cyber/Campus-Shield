# 🛡️ CampusShield

CampusShield is an Android cybersecurity application designed to help users identify potentially malicious URLs before opening them.

The app performs multiple security checks and classifies URLs as **Safe**, **Suspicious**, or **Phishing**.

---

## 🚀 Features

### 🌐 DNS Analysis

* Verifies whether the domain resolves correctly.
* Detects invalid or suspicious domains.

### 📅 WHOIS Analysis

* Checks domain registration information.
* Calculates domain age.
* Flags newly registered domains that may be risky.

### 🔒 SSL Verification

* Validates SSL certificates.
* Detects insecure connections.

### ↪ Redirect Analysis

* Identifies excessive redirects.
* Detects suspicious redirect chains.

### ☣ Threat Intelligence

* Checks URLs against threat intelligence databases.
* Detects known malicious websites.

### 📊 Risk Scoring

* Combines multiple security indicators.
* Generates a final security score.
* Classifies URLs as:

  * ✅ SAFE
  * ⚠️ SUSPICIOUS
  * 🚨 PHISHING

### 📱 Android Integration

* Built with Kotlin and Jetpack Compose.
* Supports URL analysis through Android sharing and deep links.

---

## 🛠️ Tech Stack

* Kotlin
* Jetpack Compose
* Android Studio
* Retrofit
* Gson
* Git & GitHub

---


## ⚙️ Installation

1. Clone the repository:

```bash
git clone https://github.com/sri246902-cyber/Campus-Shield.git
```

2. Open the project in Android Studio.

3. Create a `local.properties` file and add:

```properties
WHOIS_API_KEY=YOUR_KEY
THREAT_API_KEY=YOUR_KEY
```

4. Sync Gradle and run the application.

---

## 🔐 Security Note

API keys are stored using local configuration files and are not included in the public repository.

---

## 👨‍💻 Author

**SRI NIRANJANA J**

BE-COMPUTER SCIENCE AND ENGINEERING 
1st YEAR

---

## 📌 Version

CampusShield v1.0
