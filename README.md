# codealpha-java3
# 📈 Stock Trading Platform

A modern **Stock Trading Platform built using Java Swing**, designed with a **web-dashboard-style user interface** for managing stocks, buying and selling shares, tracking portfolios, viewing transaction history, and monitoring investment performance.

The project combines core **Java Object-Oriented Programming concepts, collections, file handling, and GUI development** into an attractive desktop trading application.

---

## ✨ Features

### 📊 Trading Dashboard

The application provides a modern dashboard containing:

* 💰 Cash Balance
* 📈 Stock Value
* 💎 Total Portfolio Value
* 📦 Number of Holdings
* Market Overview
* Quick Trading Actions
* User account information

---

### 📈 Market Overview

The application provides market information for:

| Symbol   | Company                   |  Price |
| -------- | ------------------------- | -----: |
| TCS      | Tata Consultancy Services | ₹3,500 |
| INFY     | Infosys                   | ₹1,800 |
| RELIANCE | Reliance Industries       | ₹2,900 |
| HDFC     | HDFC Bank                 | ₹1,650 |

The market data is stored using a Java `HashMap`.

---

### 🟢 Buy Stocks

Users can:

* Select a stock
* Enter the quantity
* View the current price
* Calculate the total purchase value
* Confirm the transaction
* Automatically deduct the amount from the available balance
* Add purchased shares to the portfolio

The original project validates the quantity and checks whether the user has sufficient balance before completing the purchase.

---

### 🔴 Sell Stocks

Users can sell stocks from their portfolio.

The system:

* Checks the entered quantity
* Checks the number of shares owned
* Calculates the selling amount
* Adds the amount to the cash balance
* Updates the portfolio
* Records the transaction

The original implementation prevents users from selling more shares than they own.

---

### 💼 Portfolio Management

The portfolio section displays:

* Stock symbol
* Quantity
* Current price
* Current value
* Total portfolio value
* Available cash balance

The portfolio value is calculated using the current stock price and the number of shares held.

---

### 📊 Portfolio Performance

The application calculates:

* Cash Balance
* Stock Value
* Total Portfolio Value

This gives the user an overview of their current investment position.

---

### 🔄 Transaction History

Every successful buy or sell operation is stored as a transaction.

Transaction details include:

```text
Transaction Type
Stock Symbol
Quantity
Price
Total Amount
```

The original project maintains transactions using an `ArrayList<Transaction>`.

---

### 💾 Save Portfolio

Users can save their portfolio information to:

```text
portfolio.txt
```

The saved information includes:

* User name
* Account balance
* Portfolio holdings
* Transaction information

The original code uses Java `FileWriter` for saving portfolio data.

---

## 🎨 Modern UI

The application has been redesigned from the original console-based interface into a **web-inspired Java Swing dashboard**.

### UI Highlights

* 🌑 Dark navigation sidebar
* 🟣 Purple modern theme
* 📊 Dashboard cards
* 💳 Financial summary cards
* 📈 Market stock cards
* 🟢 Buy buttons
* 🔴 Sell buttons
* 💼 Portfolio dashboard
* ↔ Transaction history
* 💾 Save portfolio option
* ✨ Hover effects
* 🖥️ Resizable desktop interface

---

## 🖥️ Dashboard Preview

```text
┌────────────────────┬───────────────────────────────────────────────┐
│                    │ Stock Trading Platform       ● Market Open  │
│  📈 TradeFlow      ├───────────────────────────────────────────────┤
│  Smart Investment  │                                               │
│                    │ ┌───────────────────────────────────────────┐ │
│  MAIN MENU         │ │ Good evening, Rajasri 👋  View Market → │ │
│                    │ │ Track your investments and portfolio.   │ │
│  ⌂ Dashboard       │ └───────────────────────────────────────────┘ │
│                    │                                               │
│  📊 Market         │ ┌────────┐ ┌────────┐ ┌────────┐ ┌────────┐ │
│                    │ │  CASH  │ │ STOCK  │ │ TOTAL  │ │HOLDINGS│ │
│  💼 Portfolio      │ │₹100,000│ │₹0      │ │₹100,000│ │   0    │ │
│                    │ └────────┘ └────────┘ └────────┘ └────────┘ │
│  ↔ Transactions    │                                               │
│                    │ ┌─────────────────────┐ ┌─────────────────┐ │
│  💾 Save Portfolio │ │  MARKET OVERVIEW    │ │ QUICK ACTIONS   │ │
│                    │ │                     │ │                 │ │
│                    │ │ TCS       INFY      │ │ 📊 Browse Market│ │
│  👤 Rajasri        │ │ ₹3500     ₹1800     │ │ 💼 Portfolio    │ │
│  Investor Account  │ │ [BUY]     [BUY]     │ │ ↔ Transactions  │ │
│                    │ │                     │ │ 💾 Save Data     │ │
│                    │ │ RELIANCE  HDFC      │ │                 │ │
│                    │ │ ₹2900     ₹1650      │ │                 │ │
│                    │ │ [BUY]     [BUY]      │ │                 │ │
│                    │ └─────────────────────┘ └─────────────────┘ │
└────────────────────┴───────────────────────────────────────────────┘
```

---

## 🛠️ Technologies Used

* **Java**
* **Java Swing**
* **AWT**
* **JFrame**
* **JPanel**
* **JTable**
* **HashMap**
* **ArrayList**
* **FileWriter**
* **Event Handling**
* **Object-Oriented Programming**

---

## 🧠 Java Concepts Demonstrated

### 1. Encapsulation

Private fields and public getter/setter methods are used in classes such as `Stock`.

```java
private String symbol;
private String companyName;
private double price;
```

The project uses classes for stocks, transactions, and users.

### 2. Classes and Objects

Main classes include:

```text
Stock
Transaction
User
StockTradingPlatform
```

### 3. Collections

The application uses:

```java
HashMap<String, Stock>
HashMap<String, Integer>
ArrayList<Transaction>
```

### 4. File Handling

Portfolio information is saved using:

```java
FileWriter
```

### 5. Exception Handling

The application handles file-related errors using:

```java
try {
    ...
} catch (IOException e) {
    ...
}
```

### 6. GUI Event Handling

Swing event listeners are used for:

* Buy actions
* Sell actions
* Navigation
* Saving data
* Search/input interactions

---

## 📂 Project Structure

```text
Stock-Trading-Platform/
│
├── StockTradingPlatform.java
├── portfolio.txt
└── README.md
```

---

## 🚀 How to Run

### Step 1: Clone the Repository

```bash
git clone https://github.com/your-username/stock-trading-platform.git
```

### Step 2: Open the Project

Open the project using:

* IntelliJ IDEA
* Eclipse
* NetBeans
* VS Code

### Step 3: Compile

```bash
javac StockTradingPlatform.java
```

### Step 4: Run

```bash
java StockTradingPlatform
```

---

## 💰 Initial Account

The demo application starts with:

```text
User: Rajasri
Initial Balance: ₹100,000
```

The initial user and balance are defined in the original project.

---

## 🔄 Application Workflow

```text
              ┌─────────────────┐
              │     Dashboard   │
              └────────┬────────┘
                       │
              ┌────────▼────────┐
              │  Market Overview│
              └────────┬────────┘
                       │
             ┌─────────┴─────────┐
             │                   │
        ┌────▼────┐         ┌────▼────┐
        │   BUY   │         │   SELL  │
        └────┬────┘         └────┬────┘
             │                   │
             └─────────┬─────────┘
                       │
                ┌──────▼──────┐
                │  Portfolio  │
                └──────┬──────┘
                       │
             ┌─────────▼─────────┐
             │ Transaction History│
             └─────────┬─────────┘
                       │
                ┌──────▼──────┐
                │ Save Portfolio│
                └─────────────┘
```

---

## 📌 Key Functionalities

| Feature               | Status |
| --------------------- | :----: |
| Market Data           |    ✅   |
| Buy Stocks            |    ✅   |
| Sell Stocks           |    ✅   |
| Portfolio Management  |    ✅   |
| Portfolio Performance |    ✅   |
| Transaction History   |    ✅   |
| Save Portfolio        |    ✅   |
| Modern Dashboard      |    ✅   |
| Web-Style UI          |    ✅   |
| Input Validation      |    ✅   |

---

## 🔮 Future Enhancements

The project can be extended with:

* 🔐 User authentication
* 🗄️ MySQL database
* 📊 Real-time stock charts
* 📈 Live market API integration
* 📉 Profit/Loss charts
* 🔔 Price alerts
* 🧾 PDF transaction reports
* 📱 Mobile-friendly version
* ☁️ Cloud deployment
* 👥 Multiple user accounts
* 🏦 Watchlist functionality
* 🌙 Dark/Light theme
* 📊 Advanced portfolio analytics

---

## 🎯 Project Objective

The main objective of this project is to build a simple stock trading simulation platform while demonstrating:

* Java OOP
* Collections
* File handling
* Exception handling
* GUI development
* Event-driven programming
* Financial data management

The original application was menu-driven with options for market data, buying, selling, portfolio viewing, performance, transaction history, saving, and exit.

---

## 👩‍💻 Author

**Rajasri R K**

Computer Science & Engineering

**Skills:**
Java • React.js • MERN Stack • REST APIs • MongoDB • SQL • AWS • Python

---

## ⭐ Support

If you found this project useful, please consider giving the repository a ⭐ on GitHub.

---

## 📜 License

This project is developed for **educational and academic purposes**.
