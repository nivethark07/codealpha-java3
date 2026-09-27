import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/*
 * ============================================================
 *              STOCK TRADING PLATFORM
 *              Modern Web-Style Java UI
 * ============================================================
 */

public class StockTradingPlatform extends JFrame {

    // ============================================================
    // COLORS
    // ============================================================

    private static final Color BG =
            new Color(245, 247, 251);

    private static final Color WHITE =
            Color.WHITE;

    private static final Color SIDEBAR =
            new Color(20, 24, 38);

    private static final Color SIDEBAR_HOVER =
            new Color(40, 45, 65);

    private static final Color PRIMARY =
            new Color(99, 76, 255);

    private static final Color PRIMARY_DARK =
            new Color(78, 58, 220);

    private static final Color GREEN =
            new Color(22, 163, 74);

    private static final Color RED =
            new Color(220, 38, 38);

    private static final Color BLUE =
            new Color(37, 99, 235);

    private static final Color ORANGE =
            new Color(234, 128, 25);

    private static final Color TEXT =
            new Color(31, 36, 48);

    private static final Color MUTED =
            new Color(107, 114, 128);

    private static final Color BORDER =
            new Color(226, 229, 237);

    // ============================================================
    // DATA CLASSES
    // ============================================================

    static class Stock {

        private String symbol;
        private String companyName;
        private double price;

        public Stock(
                String symbol,
                String companyName,
                double price
        ) {
            this.symbol = symbol;
            this.companyName = companyName;
            this.price = price;
        }

        public String getSymbol() {
            return symbol;
        }

        public String getCompanyName() {
            return companyName;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }
    }

    static class Transaction {

        private String type;
        private String stockSymbol;
        private int quantity;
        private double price;

        public Transaction(
                String type,
                String stockSymbol,
                int quantity,
                double price
        ) {
            this.type = type;
            this.stockSymbol = stockSymbol;
            this.quantity = quantity;
            this.price = price;
        }

        public String getType() {
            return type;
        }

        public String getStockSymbol() {
            return stockSymbol;
        }

        public int getQuantity() {
            return quantity;
        }

        public double getPrice() {
            return price;
        }

        @Override
        public String toString() {

            return type + "," +
                    stockSymbol + "," +
                    quantity + "," +
                    price;
        }
    }

    static class User {

        private String name;
        private double balance;

        private HashMap<String, Integer> portfolio;
        private ArrayList<Transaction> transactions;

        public User(
                String name,
                double balance
        ) {
            this.name = name;
            this.balance = balance;

            portfolio =
                    new HashMap<>();

            transactions =
                    new ArrayList<>();
        }

        public String getName() {
            return name;
        }

        public double getBalance() {
            return balance;
        }

        public HashMap<String, Integer>
        getPortfolio() {
            return portfolio;
        }

        public ArrayList<Transaction>
        getTransactions() {
            return transactions;
        }

        public boolean buyStock(
                Stock stock,
                int quantity
        ) {

            if (quantity <= 0) {
                return false;
            }

            double totalCost =
                    stock.getPrice() *
                            quantity;

            if (totalCost > balance) {
                return false;
            }

            balance -= totalCost;

            portfolio.put(
                    stock.getSymbol(),
                    portfolio.getOrDefault(
                            stock.getSymbol(),
                            0
                    ) + quantity
            );

            transactions.add(
                    new Transaction(
                            "BUY",
                            stock.getSymbol(),
                            quantity,
                            stock.getPrice()
                    )
            );

            return true;
        }

        public boolean sellStock(
                Stock stock,
                int quantity
        ) {

            if (quantity <= 0) {
                return false;
            }

            int owned =
                    portfolio.getOrDefault(
                            stock.getSymbol(),
                            0
                    );

            if (owned < quantity) {
                return false;
            }

            double totalAmount =
                    stock.getPrice() *
                            quantity;

            balance += totalAmount;

            if (owned == quantity) {

                portfolio.remove(
                        stock.getSymbol()
                );

            } else {

                portfolio.put(
                        stock.getSymbol(),
                        owned - quantity
                );
            }

            transactions.add(
                    new Transaction(
                            "SELL",
                            stock.getSymbol(),
                            quantity,
                            stock.getPrice()
                    )
            );

            return true;
        }

        public double getPortfolioValue(
                HashMap<String, Stock> market
        ) {

            double value = 0;

            for (String symbol :
                    portfolio.keySet()) {

                Stock stock =
                        market.get(symbol);

                if (stock != null) {

                    value +=
                            portfolio.get(symbol)
                                    * stock.getPrice();
                }
            }

            return value;
        }

        public double getTotalValue(
                HashMap<String, Stock> market
        ) {

            return balance +
                    getPortfolioValue(market);
        }

        public void saveToFile() {

            try {

                FileWriter writer =
                        new FileWriter(
                                "portfolio.txt"
                        );

                writer.write(
                        "User: " +
                                name +
                                "\n"
                );

                writer.write(
                        "Balance: " +
                                balance +
                                "\n"
                );

                writer.write(
                        "\nPortfolio:\n"
                );

                for (String symbol :
                        portfolio.keySet()) {

                    writer.write(
                            symbol +
                                    " : " +
                                    portfolio.get(symbol) +
                                    "\n"
                    );
                }

                writer.write(
                        "\nTransactions:\n"
                );

                for (Transaction t :
                        transactions) {

                    writer.write(
                            t.toString() +
                                    "\n"
                    );
                }

                writer.close();

            } catch (IOException e) {

                e.printStackTrace();
            }
        }
    }

    // ============================================================
    // GLOBAL DATA
    // ============================================================

    private final HashMap<String, Stock>
            market =
            new HashMap<>();

    private final User user =
            new User(
                    "Rajasri",
                    100000
            );

    // ============================================================
    // UI
    // ============================================================

    private JPanel contentPanel;

    private JLabel balanceLabel;
    private JLabel portfolioLabel;
    private JLabel totalLabel;
    private JLabel holdingsLabel;

    private JTable portfolioTable;
    private JTable transactionTable;

    private DefaultTableModel
            portfolioModel;

    private DefaultTableModel
            transactionModel;

    // ============================================================
    // CONSTRUCTOR
    // ============================================================

    public StockTradingPlatform() {

        setTitle(
                "TradeFlow - Stock Trading Platform"
        );

        setSize(
                1400,
                850
        );

        setMinimumSize(
                new Dimension(
                        1100,
                        700
                )
        );

        setLocationRelativeTo(null);

        setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        initializeMarket();

        createUI();
    }

    // ============================================================
    // MARKET DATA
    // ============================================================

    private void initializeMarket() {

        market.put(
                "TCS",
                new Stock(
                        "TCS",
                        "Tata Consultancy Services",
                        3500
                )
        );

        market.put(
                "INFY",
                new Stock(
                        "INFY",
                        "Infosys",
                        1800
                )
        );

        market.put(
                "RELIANCE",
                new Stock(
                        "RELIANCE",
                        "Reliance Industries",
                        2900
                )
        );

        market.put(
                "HDFC",
                new Stock(
                        "HDFC",
                        "HDFC Bank",
                        1650
                )
        );
    }

    // ============================================================
    // MAIN UI
    // ============================================================

    private void createUI() {

        JPanel root =
                new JPanel(
                        new BorderLayout()
                );

        root.setBackground(BG);

        root.add(
                createSidebar(),
                BorderLayout.WEST
        );

        contentPanel =
                new JPanel(
                        new BorderLayout()
                );

        contentPanel.setBackground(BG);

        contentPanel.add(
                createHeader(),
                BorderLayout.NORTH
        );

        contentPanel.add(
                createDashboard(),
                BorderLayout.CENTER
        );

        root.add(
                contentPanel,
                BorderLayout.CENTER
        );

        add(root);
    }

    // ============================================================
    // SIDEBAR
    // ============================================================

    private JPanel createSidebar() {

        JPanel sidebar =
                new JPanel(
                        new BorderLayout()
                );

        sidebar.setPreferredSize(
                new Dimension(
                        250,
                        0
                )
        );

        sidebar.setBackground(
                SIDEBAR
        );

        // Logo

        JPanel logoPanel =
                new JPanel(
                        new BorderLayout()
                );

        logoPanel.setBackground(
                SIDEBAR
        );

        logoPanel.setBorder(
                new EmptyBorder(
                        28,
                        22,
                        28,
                        20
                )
        );

        JLabel icon =
                new JLabel("📈");

        icon.setFont(
                new Font(
                        "Segoe UI Emoji",
                        Font.PLAIN,
                        32
                )
        );

        JLabel logo =
                new JLabel(
                        "<html>" +
                                "<b style='font-size:20px;color:white'>" +
                                "Trade</b>" +
                                "<b style='font-size:20px;color:#8b7cff'>" +
                                "Flow</b>" +
                                "<br>" +
                                "<font color='#9ca3af' size='2'>" +
                                "Smart Investment" +
                                "</font>" +
                                "</html>"
                );

        logo.setBorder(
                new EmptyBorder(
                        0,
                        12,
                        0,
                        0
                )
        );

        logoPanel.add(
                icon,
                BorderLayout.WEST
        );

        logoPanel.add(
                logo,
                BorderLayout.CENTER
        );

        sidebar.add(
                logoPanel,
                BorderLayout.NORTH
        );

        // Menu

        JPanel menu =
                new JPanel();

        menu.setBackground(
                SIDEBAR
        );

        menu.setBorder(
                new EmptyBorder(
                        10,
                        15,
                        10,
                        15
                )
        );

        menu.setLayout(
                new BoxLayout(
                        menu,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel menuTitle =
                new JLabel(
                        "MAIN MENU"
                );

        menuTitle.setForeground(
                new Color(
                        130,
                        136,
                        155
                )
        );

        menuTitle.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        10
                )
        );

        menuTitle.setBorder(
                new EmptyBorder(
                        5,
                        12,
                        15,
                        0
                )
        );

        menu.add(menuTitle);

        JButton dashboard =
                createMenuButton(
                        "⌂",
                        "Dashboard"
                );

        JButton marketButton =
                createMenuButton(
                        "📊",
                        "Market"
                );

        JButton portfolioButton =
                createMenuButton(
                        "💼",
                        "Portfolio"
                );

        JButton transactionsButton =
                createMenuButton(
                        "↔",
                        "Transactions"
                );

        JButton saveButton =
                createMenuButton(
                        "💾",
                        "Save Portfolio"
                );

        menu.add(dashboard);
        menu.add(marketButton);
        menu.add(portfolioButton);
        menu.add(transactionsButton);
        menu.add(saveButton);

        dashboard.addActionListener(
                e -> showDashboard()
        );

        marketButton.addActionListener(
                e -> showMarket()
        );

        portfolioButton.addActionListener(
                e -> showPortfolio()
        );

        transactionsButton.addActionListener(
                e -> showTransactions()
        );

        saveButton.addActionListener(
                e -> savePortfolio()
        );

        sidebar.add(
                menu,
                BorderLayout.CENTER
        );

        // Bottom user

        JPanel profile =
                new JPanel(
                        new BorderLayout()
                );

        profile.setBackground(
                new Color(
                        27,
                        31,
                        48
                )
        );

        profile.setBorder(
                new EmptyBorder(
                        15,
                        15,
                        15,
                        15
                )
        );

        JLabel avatar =
                new JLabel("R");

        avatar.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        avatar.setForeground(
                Color.WHITE
        );

        avatar.setBackground(
                PRIMARY
        );

        avatar.setOpaque(true);

        avatar.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        16
                )
        );

        avatar.setPreferredSize(
                new Dimension(
                        42,
                        42
                )
        );

        JLabel userInfo =
                new JLabel(
                        "<html>" +
                                "<b style='color:white'>" +
                                "Rajasri" +
                                "</b><br>" +
                                "<font color='#9ca3af'>" +
                                "Investor Account" +
                                "</font>" +
                                "</html>"
                );

        userInfo.setBorder(
                new EmptyBorder(
                        0,
                        10,
                        0,
                        0
                )
        );

        profile.add(
                avatar,
                BorderLayout.WEST
        );

        profile.add(
                userInfo,
                BorderLayout.CENTER
        );

        sidebar.add(
                profile,
                BorderLayout.SOUTH
        );

        return sidebar;
    }

    // ============================================================
    // MENU BUTTON
    // ============================================================

    private JButton createMenuButton(
            String icon,
            String text
    ) {

        JButton button =
                new JButton();

        button.setLayout(
                new BorderLayout()
        );

        button.setPreferredSize(
                new Dimension(
                        215,
                        50
                )
        );

        button.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        50
                )
        );

        button.setBackground(
                SIDEBAR
        );

        button.setBorder(
                new EmptyBorder(
                        0,
                        14,
                        0,
                        10
                )
        );

        button.setFocusPainted(false);
        button.setBorderPainted(false);

        JLabel iconLabel =
                new JLabel(icon);

        iconLabel.setFont(
                new Font(
                        "Segoe UI Emoji",
                        Font.PLAIN,
                        18
                )
        );

        JLabel textLabel =
                new JLabel(text);

        textLabel.setForeground(
                new Color(
                        215,
                        218,
                        230
                )
        );

        textLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        13
                )
        );

        button.add(
                iconLabel,
                BorderLayout.WEST
        );

        button.add(
                textLabel,
                BorderLayout.CENTER
        );

        button.addMouseListener(
                new MouseAdapter() {

                    @Override
                    public void mouseEntered(
                            MouseEvent e
                    ) {

                        button.setBackground(
                                SIDEBAR_HOVER
                        );

                        textLabel.setForeground(
                                Color.WHITE
                        );
                    }

                    @Override
                    public void mouseExited(
                            MouseEvent e
                    ) {

                        button.setBackground(
                                SIDEBAR
                        );

                        textLabel.setForeground(
                                new Color(
                                        215,
                                        218,
                                        230
                                )
                        );
                    }
                }
        );

        return button;
    }

    // ============================================================
    // HEADER
    // ============================================================

    private JPanel createHeader() {

        JPanel header =
                new JPanel(
                        new BorderLayout()
                );

        header.setBackground(
                WHITE
        );

        header.setBorder(
                new EmptyBorder(
                        18,
                        30,
                        18,
                        30
                )
        );

        JLabel title =
                new JLabel(
                        "Stock Trading Platform"
                );

        title.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        23
                )
        );

        title.setForeground(TEXT);

        JLabel marketStatus =
                new JLabel(
                        "● Market Open"
                );

        marketStatus.setForeground(
                GREEN
        );

        marketStatus.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        12
                )
        );

        header.add(
                title,
                BorderLayout.WEST
        );

        header.add(
                marketStatus,
                BorderLayout.EAST
        );

        return header;
    }

    // ============================================================
    // DASHBOARD
    // ============================================================

    private JPanel createDashboard() {

        JPanel page =
                new JPanel(
                        new BorderLayout()
                );

        page.setBackground(BG);

        page.setBorder(
                new EmptyBorder(
                        25,
                        30,
                        30,
                        30
                )
        );

        // Hero

        JPanel hero =
                new JPanel(
                        new BorderLayout()
                );

        hero.setBackground(
                new Color(
                        40,
                        35,
                        90
                )
        );

        hero.setBorder(
                new EmptyBorder(
                        28,
                        32,
                        28,
                        32
                )
        );

        JPanel heroText =
                new JPanel();

        heroText.setOpaque(false);

        heroText.setLayout(
                new BoxLayout(
                        heroText,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel welcome =
                new JLabel(
                        "Good evening, Rajasri 👋"
                );

        welcome.setForeground(
                Color.WHITE
        );

        welcome.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        25
                )
        );

        JLabel description =
                new JLabel(
                        "Track your investments and manage your portfolio."
                );

        description.setForeground(
                new Color(
                        210,
                        207,
                        235
                )
        );

        description.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        heroText.add(welcome);

        heroText.add(
                Box.createVerticalStrut(7)
        );

        heroText.add(description);

        hero.add(
                heroText,
                BorderLayout.WEST
        );

        JButton market =
                new JButton(
                        "View Market →"
                );

        styleButton(
                market,
                Color.WHITE,
                PRIMARY
        );

        market.addActionListener(
                e -> showMarket()
        );

        hero.add(
                market,
                BorderLayout.EAST
        );

        // Stats

        JPanel stats =
                new JPanel(
                        new GridLayout(
                                1,
                                4,
                                18,
                                0
                        )
                );

        stats.setBackground(BG);

        stats.setBorder(
                new EmptyBorder(
                        20,
                        0,
                        20,
                        0
                )
        );

        balanceLabel =
                new JLabel();

        portfolioLabel =
                new JLabel();

        totalLabel =
                new JLabel();

        holdingsLabel =
                new JLabel();

        stats.add(
                statCard(
                        "CASH BALANCE",
                        "💰",
                        balanceLabel,
                        PRIMARY
                )
        );

        stats.add(
                statCard(
                        "STOCK VALUE",
                        "📈",
                        portfolioLabel,
                        BLUE
                )
        );

        stats.add(
                statCard(
                        "TOTAL VALUE",
                        "💎",
                        totalLabel,
                        GREEN
                )
        );

        stats.add(
                statCard(
                        "HOLDINGS",
                        "📦",
                        holdingsLabel,
                        ORANGE
                )
        );

        // Main body

        JPanel body =
                new JPanel(
                        new BorderLayout(
                                20,
                                0
                        )
                );

        body.setBackground(BG);

        body.add(
                createMarketPreview(),
                BorderLayout.CENTER
        );

        body.add(
                createQuickActions(),
                BorderLayout.EAST
        );

        page.add(
                hero,
                BorderLayout.NORTH
        );

        JPanel center =
                new JPanel(
                        new BorderLayout()
                );

        center.setBackground(BG);

        center.add(
                stats,
                BorderLayout.NORTH
        );

        center.add(
                body,
                BorderLayout.CENTER
        );

        page.add(
                center,
                BorderLayout.CENTER
        );

        updateDashboardStats();

        return page;
    }

    // ============================================================
    // STAT CARD
    // ============================================================

    private JPanel statCard(
            String title,
            String icon,
            JLabel value,
            Color accent
    ) {

        JPanel card =
                new JPanel(
                        new BorderLayout()
                );

        card.setBackground(WHITE);

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER
                        ),
                        new EmptyBorder(
                                16,
                                18,
                                16,
                                18
                        )
                )
        );

        JPanel top =
                new JPanel(
                        new BorderLayout()
                );

        top.setBackground(WHITE);

        JLabel titleLabel =
                new JLabel(title);

        titleLabel.setForeground(MUTED);

        titleLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        10
                )
        );

        JLabel iconLabel =
                new JLabel(icon);

        iconLabel.setFont(
                new Font(
                        "Segoe UI Emoji",
                        Font.PLAIN,
                        20
                )
        );

        top.add(
                titleLabel,
                BorderLayout.WEST
        );

        top.add(
                iconLabel,
                BorderLayout.EAST
        );

        value.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        25
                )
        );

        value.setForeground(TEXT);

        card.add(
                top,
                BorderLayout.NORTH
        );

        card.add(
                value,
                BorderLayout.CENTER
        );

        JPanel bottom =
                new JPanel();

        bottom.setBackground(accent);

        bottom.setPreferredSize(
                new Dimension(
                        0,
                        4
                )
        );

        card.add(
                bottom,
                BorderLayout.SOUTH
        );

        return card;
    }

    // ============================================================
    // MARKET PREVIEW
    // ============================================================

    private JPanel createMarketPreview() {

        JPanel card =
                new JPanel(
                        new BorderLayout()
                );

        card.setBackground(WHITE);

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER
                        ),
                        new EmptyBorder(
                                18,
                                18,
                                18,
                                18
                        )
                )
        );

        JLabel title =
                new JLabel(
                        "Market Overview"
                );

        title.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        18
                )
        );

        title.setForeground(TEXT);

        JLabel subtitle =
                new JLabel(
                        "Popular stocks"
                );

        subtitle.setForeground(MUTED);

        JPanel heading =
                new JPanel(
                        new BorderLayout()
                );

        heading.setBackground(WHITE);

        heading.add(
                title,
                BorderLayout.WEST
        );

        heading.add(
                subtitle,
                BorderLayout.EAST
        );

        card.add(
                heading,
                BorderLayout.NORTH
        );

        JPanel stockList =
                new JPanel(
                        new GridLayout(
                                2,
                                2,
                                12,
                                12
                        )
                );

        stockList.setBackground(WHITE);

        for (Stock stock :
                market.values()) {

            stockList.add(
                    createStockCard(stock)
            );
        }

        card.add(
                stockList,
                BorderLayout.CENTER
        );

        return card;
    }

    // ============================================================
    // STOCK CARD
    // ============================================================

    private JPanel createStockCard(
            Stock stock
    ) {

        JPanel card =
                new JPanel(
                        new BorderLayout()
                );

        card.setBackground(
                new Color(
                        249,
                        250,
                        253
                )
        );

        card.setBorder(
                new EmptyBorder(
                        14,
                        14,
                        14,
                        14
                )
        );

        JPanel left =
                new JPanel();

        left.setBackground(
                card.getBackground()
        );

        left.setLayout(
                new BoxLayout(
                        left,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel symbol =
                new JLabel(
                        stock.getSymbol()
                );

        symbol.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        16
                )
        );

        symbol.setForeground(TEXT);

        JLabel company =
                new JLabel(
                        stock.getCompanyName()
                );

        company.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        10
                )
        );

        company.setForeground(MUTED);

        left.add(symbol);

        left.add(
                Box.createVerticalStrut(3)
        );

        left.add(company);

        JPanel right =
                new JPanel();

        right.setBackground(
                card.getBackground()
        );

        right.setLayout(
                new BoxLayout(
                        right,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel price =
                new JLabel(
                        String.format(
                                "₹%.2f",
                                stock.getPrice()
                        )
                );

        price.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        15
                )
        );

        price.setForeground(
                new Color(
                        22,
                        163,
                        74
                )
        );

        JButton buy =
                new JButton("BUY");

        buy.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        10
                )
        );

        buy.setForeground(Color.WHITE);

        buy.setBackground(GREEN);

        buy.setFocusPainted(false);

        buy.setBorderPainted(false);

        buy.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        buy.addActionListener(
                e -> showTradeDialog(
                        stock,
                        true
                )
        );

        right.add(price);

        right.add(
                Box.createVerticalStrut(6)
        );

        right.add(buy);

        card.add(
                left,
                BorderLayout.WEST
        );

        card.add(
                right,
                BorderLayout.EAST
        );

        return card;
    }

    // ============================================================
    // QUICK ACTIONS
    // ============================================================

    private JPanel createQuickActions() {

        JPanel card =
                new JPanel();

        card.setPreferredSize(
                new Dimension(
                        250,
                        0
                )
        );

        card.setBackground(WHITE);

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER
                        ),
                        new EmptyBorder(
                                20,
                                20,
                                20,
                                20
                        )
                )
        );

        card.setLayout(
                new BoxLayout(
                        card,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel title =
                new JLabel(
                        "Quick Actions"
                );

        title.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        18
                )
        );

        title.setForeground(TEXT);

        card.add(title);

        card.add(
                Box.createVerticalStrut(20)
        );

        JButton market =
                actionButton(
                        "📊  Browse Market",
                        PRIMARY
                );

        JButton portfolio =
                actionButton(
                        "💼  My Portfolio",
                        BLUE
                );

        JButton history =
                actionButton(
                        "↔  Transactions",
                        ORANGE
                );

        JButton save =
                actionButton(
                        "💾  Save Data",
                        GREEN
                );

        market.addActionListener(
                e -> showMarket()
        );

        portfolio.addActionListener(
                e -> showPortfolio()
        );

        history.addActionListener(
                e -> showTransactions()
        );

        save.addActionListener(
                e -> savePortfolio()
        );

        card.add(market);
        card.add(
                Box.createVerticalStrut(12)
        );

        card.add(portfolio);
        card.add(
                Box.createVerticalStrut(12)
        );

        card.add(history);
        card.add(
                Box.createVerticalStrut(12)
        );

        card.add(save);

        return card;
    }

    // ============================================================
    // ACTION BUTTON
    // ============================================================

    private JButton actionButton(
            String text,
            Color color
    ) {

        JButton button =
                new JButton(text);

        button.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        45
                )
        );

        button.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        button.setBackground(
                new Color(
                        248,
                        249,
                        252
                )
        );

        button.setForeground(TEXT);

        button.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        12
                )
        );

        button.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER
                        ),
                        new EmptyBorder(
                                10,
                                12,
                                10,
                                12
                        )
                )
        );

        button.setFocusPainted(false);

        button.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        return button;
    }

    // ============================================================
    // MARKET PAGE
    // ============================================================

    private void showMarket() {

        JPanel page =
                basePage(
                        "Market",
                        "Explore available stocks"
                );

        JPanel grid =
                new JPanel(
                        new GridLayout(
                                2,
                                2,
                                20,
                                20
                        )
                );

        grid.setBackground(BG);

        for (Stock stock :
                market.values()) {

            grid.add(
                    createLargeStockCard(stock)
            );
        }

        page.add(
                grid,
                BorderLayout.CENTER
        );

        replaceContent(page);
    }

    // ============================================================
    // LARGE STOCK CARD
    // ============================================================

    private JPanel createLargeStockCard(
            Stock stock
    ) {

        JPanel card =
                new JPanel(
                        new BorderLayout()
                );

        card.setBackground(WHITE);

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER
                        ),
                        new EmptyBorder(
                                25,
                                25,
                                25,
                                25
                        )
                )
        );

        JLabel symbol =
                new JLabel(
                        stock.getSymbol()
                );

        symbol.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        25
                )
        );

        symbol.setForeground(TEXT);

        JLabel company =
                new JLabel(
                        stock.getCompanyName()
                );

        company.setForeground(MUTED);

        JLabel price =
                new JLabel(
                        String.format(
                                "₹%.2f",
                                stock.getPrice()
                        )
                );

        price.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        30
                )
        );

        price.setForeground(GREEN);

        JPanel info =
                new JPanel();

        info.setBackground(WHITE);

        info.setLayout(
                new BoxLayout(
                        info,
                        BoxLayout.Y_AXIS
                )
        );

        info.add(symbol);

        info.add(
                Box.createVerticalStrut(5)
        );

        info.add(company);

        info.add(
                Box.createVerticalStrut(20)
        );

        info.add(price);

        JPanel buttons =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT
                        )
                );

        buttons.setBackground(WHITE);

        JButton buy =
                new JButton("Buy Stock");

        JButton sell =
                new JButton("Sell Stock");

        styleButton(
                buy,
                GREEN,
                Color.WHITE
        );

        styleButton(
                sell,
                RED,
                Color.WHITE
        );

        buy.addActionListener(
                e -> showTradeDialog(
                        stock,
                        true
                )
        );

        sell.addActionListener(
                e -> showTradeDialog(
                        stock,
                        false
                )
        );

        buttons.add(buy);
        buttons.add(sell);

        card.add(
                info,
                BorderLayout.CENTER
        );

        card.add(
                buttons,
                BorderLayout.SOUTH
        );

        return card;
    }

    // ============================================================
    // TRADE DIALOG
    // ============================================================

    private void showTradeDialog(
            Stock stock,
            boolean buying
    ) {

        JDialog dialog =
                new JDialog(
                        this,
                        buying
                                ? "Buy " + stock.getSymbol()
                                : "Sell " + stock.getSymbol(),
                        true
                );

        dialog.setSize(
                430,
                360
        );

        dialog.setLocationRelativeTo(this);

        JPanel panel =
                new JPanel(
                        new BorderLayout()
                );

        panel.setBackground(WHITE);

        panel.setBorder(
                new EmptyBorder(
                        25,
                        30,
                        25,
                        30
                )
        );

        JLabel title =
                new JLabel(
                        buying
                                ? "Buy " + stock.getSymbol()
                                : "Sell " + stock.getSymbol()
                );

        title.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        23
                )
        );

        title.setForeground(TEXT);

        JLabel company =
                new JLabel(
                        stock.getCompanyName()
                );

        company.setForeground(MUTED);

        JPanel heading =
                new JPanel();

        heading.setBackground(WHITE);

        heading.setLayout(
                new BoxLayout(
                        heading,
                        BoxLayout.Y_AXIS
                )
        );

        heading.add(title);

        heading.add(
                Box.createVerticalStrut(4)
        );

        heading.add(company);

        panel.add(
                heading,
                BorderLayout.NORTH
        );

        JPanel form =
                new JPanel(
                        new GridLayout(
                                4,
                                1,
                                8,
                                8
                        )
                );

        form.setBackground(WHITE);

        JLabel price =
                new JLabel(
                        "Current Price: ₹" +
                                String.format(
                                        "%.2f",
                                        stock.getPrice()
                                )
                );

        price.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        14
                )
        );

        JTextField quantity =
                new JTextField();

        quantity.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER
                        ),
                        new EmptyBorder(
                                8,
                                10,
                                8,
                                10
                        )
                )
        );

        form.add(price);

        form.add(
                new JLabel(
                        "Enter Quantity"
                )
        );

        form.add(quantity);

        JLabel total =
                new JLabel(
                        "Total: ₹0.00"
                );

        total.setForeground(
                PRIMARY
        );

        total.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        16
                )
        );

        form.add(total);

        quantity.addKeyListener(
                new KeyAdapter() {

                    @Override
                    public void keyReleased(
                            KeyEvent e
                    ) {

                        try {

                            int q =
                                    Integer.parseInt(
                                            quantity
                                                    .getText()
                                                    .trim()
                                    );

                            total.setText(
                                    "Total: ₹" +
                                            String.format(
                                                    "%.2f",
                                                    q *
                                                            stock.getPrice()
                                            )
                            );

                        } catch (Exception ex) {

                            total.setText(
                                    "Total: ₹0.00"
                            );
                        }
                    }
                }
        );

        panel.add(
                form,
                BorderLayout.CENTER
        );

        JButton action =
                new JButton(
                        buying
                                ? "✓ Confirm Buy"
                                : "✓ Confirm Sell"
                );

        styleButton(
                action,
                buying
                        ? GREEN
                        : RED,
                Color.WHITE
        );

        action.addActionListener(
                e -> {

                    try {

                        int q =
                                Integer.parseInt(
                                        quantity
                                                .getText()
                                                .trim()
                                );

                        if (q <= 0) {

                            showError(
                                    "Quantity must be greater than zero."
                            );

                            return;
                        }

                        boolean success;

                        if (buying) {

                            success =
                                    user.buyStock(
                                            stock,
                                            q
                                    );

                        } else {

                            success =
                                    user.sellStock(
                                            stock,
                                            q
                                    );
                        }

                        if (!success) {

                            showError(
                                    buying
                                            ? "Insufficient balance or invalid quantity."
                                            : "You don't own enough stocks."
                            );

                            return;
                        }

                        dialog.dispose();

                        updateDashboardStats();

                        JOptionPane.showMessageDialog(
                                this,
                                buying
                                        ? "Stock purchased successfully!"
                                        : "Stock sold successfully!",
                                "Transaction Successful",
                                JOptionPane.INFORMATION_MESSAGE
                        );

                    } catch (
                            NumberFormatException ex
                    ) {

                        showError(
                                "Please enter a valid quantity."
                        );
                    }
                }
        );

        JPanel bottom =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT
                        )
                );

        bottom.setBackground(WHITE);

        bottom.add(action);

        panel.add(
                bottom,
                BorderLayout.SOUTH
        );

        dialog.add(panel);

        dialog.setVisible(true);
    }

    // ============================================================
    // PORTFOLIO PAGE
    // ============================================================

    private void showPortfolio() {

        JPanel page =
                basePage(
                        "My Portfolio",
                        "Track your current holdings"
                );

        portfolioModel =
                new DefaultTableModel(
                        new String[]{
                                "STOCK",
                                "QUANTITY",
                                "PRICE",
                                "CURRENT VALUE"
                        },
                        0
                );

        portfolioTable =
                new JTable(
                        portfolioModel
                );

        styleTable(
                portfolioTable
        );

        refreshPortfolioTable();

        JScrollPane scroll =
                new JScrollPane(
                        portfolioTable
                );

        scroll.setBorder(null);

        JPanel card =
                new JPanel(
                        new BorderLayout()
                );

        card.setBackground(WHITE);

        card.setBorder(
                new EmptyBorder(
                        18,
                        18,
                        18,
                        18
                )
        );

        card.add(
                scroll,
                BorderLayout.CENTER
        );

        page.add(
                card,
                BorderLayout.CENTER
        );

        replaceContent(page);
    }

    // ============================================================
    // REFRESH PORTFOLIO
    // ============================================================

    private void refreshPortfolioTable() {

        if (portfolioModel == null)
            return;

        portfolioModel.setRowCount(0);

        for (String symbol :
                user.getPortfolio().keySet()) {

            Stock stock =
                    market.get(symbol);

            if (stock == null)
                continue;

            int quantity =
                    user.getPortfolio()
                            .get(symbol);

            double value =
                    quantity *
                            stock.getPrice();

            portfolioModel.addRow(
                    new Object[]{
                            symbol,
                            quantity,
                            String.format(
                                    "₹%.2f",
                                    stock.getPrice()
                            ),
                            String.format(
                                    "₹%.2f",
                                    value
                            )
                    }
            );
        }
    }

    // ============================================================
    // TRANSACTIONS PAGE
    // ============================================================

    private void showTransactions() {

        JPanel page =
                basePage(
                        "Transaction History",
                        "Your recent trading activity"
                );

        transactionModel =
                new DefaultTableModel(
                        new String[]{
                                "TYPE",
                                "STOCK",
                                "QUANTITY",
                                "PRICE",
                                "TOTAL"
                        },
                        0
                );

        transactionTable =
                new JTable(
                        transactionModel
                );

        styleTable(
                transactionTable
        );

        refreshTransactionTable();

        JScrollPane scroll =
                new JScrollPane(
                        transactionTable
                );

        scroll.setBorder(null);

        JPanel card =
                new JPanel(
                        new BorderLayout()
                );

        card.setBackground(WHITE);

        card.setBorder(
                new EmptyBorder(
                        18,
                        18,
                        18,
                        18
                )
        );

        card.add(
                scroll,
                BorderLayout.CENTER
        );

        page.add(
                card,
                BorderLayout.CENTER
        );

        replaceContent(page);
    }

    // ============================================================
    // REFRESH TRANSACTIONS
    // ============================================================

    private void refreshTransactionTable() {

        if (transactionModel == null)
            return;

        transactionModel.setRowCount(0);

        for (
                Transaction t :
                user.getTransactions()
        ) {

            double total =
                    t.getQuantity()
                            * t.getPrice();

            transactionModel.addRow(
                    new Object[]{
                            t.getType(),
                            t.getStockSymbol(),
                            t.getQuantity(),
                            String.format(
                                    "₹%.2f",
                                    t.getPrice()
                            ),
                            String.format(
                                    "₹%.2f",
                                    total
                            )
                    }
            );
        }
    }

    // ============================================================
    // SAVE
    // ============================================================

    private void savePortfolio() {

        user.saveToFile();

        JOptionPane.showMessageDialog(
                this,
                "Portfolio saved successfully!\n\n" +
                        "File: portfolio.txt",
                "Saved",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    // ============================================================
    // BASE PAGE
    // ============================================================

    private JPanel basePage(
            String title,
            String subtitle
    ) {

        JPanel page =
                new JPanel(
                        new BorderLayout()
                );

        page.setBackground(BG);

        page.setBorder(
                new EmptyBorder(
                        25,
                        30,
                        30,
                        30
                )
        );

        JPanel header =
                new JPanel(
                        new BorderLayout()
                );

        header.setBackground(BG);

        JPanel text =
                new JPanel();

        text.setBackground(BG);

        text.setLayout(
                new BoxLayout(
                        text,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel titleLabel =
                new JLabel(title);

        titleLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        27
                )
        );

        titleLabel.setForeground(TEXT);

        JLabel subtitleLabel =
                new JLabel(subtitle);

        subtitleLabel.setForeground(MUTED);

        subtitleLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        text.add(titleLabel);

        text.add(
                Box.createVerticalStrut(5)
        );

        text.add(subtitleLabel);

        header.add(
                text,
                BorderLayout.WEST
        );

        page.add(
                header,
                BorderLayout.NORTH
        );

        return page;
    }

    // ============================================================
    // TABLE STYLE
    // ============================================================

    private void styleTable(
            JTable table
    ) {

        table.setRowHeight(50);

        table.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        table.setForeground(TEXT);

        table.setBackground(WHITE);

        table.setGridColor(
                new Color(
                        240,
                        242,
                        247
                )
        );

        table.setShowVerticalLines(false);

        table.setSelectionBackground(
                new Color(
                        239,
                        235,
                        255
                )
        );

        table.setSelectionForeground(TEXT);

        JTableHeader header =
                table.getTableHeader();

        header.setPreferredSize(
                new Dimension(
                        0,
                        45
                )
        );

        header.setBackground(
                new Color(
                        249,
                        250,
                        252
                )
        );

        header.setForeground(MUTED);

        header.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        11
                )
        );
    }

    // ============================================================
    // UPDATE DASHBOARD
    // ============================================================

    private void updateDashboardStats() {

        if (balanceLabel == null)
            return;

        double stockValue =
                user.getPortfolioValue(
                        market
                );

        double totalValue =
                user.getTotalValue(
                        market
                );

        int holdings =
                user.getPortfolio()
                        .size();

        balanceLabel.setText(
                String.format(
                        "₹%,.2f",
                        user.getBalance()
                )
        );

        portfolioLabel.setText(
                String.format(
                        "₹%,.2f",
                        stockValue
                )
        );

        totalLabel.setText(
                String.format(
                        "₹%,.2f",
                        totalValue
                )
        );

        holdingsLabel.setText(
                String.valueOf(
                        holdings
                )
        );

        if (portfolioTable != null) {

            refreshPortfolioTable();
        }

        if (transactionTable != null) {

            refreshTransactionTable();
        }
    }

    // ============================================================
    // SHOW DASHBOARD
    // ============================================================

    private void showDashboard() {

        contentPanel.remove(1);

        contentPanel.add(
                createDashboard(),
                BorderLayout.CENTER
        );

        contentPanel.revalidate();

        contentPanel.repaint();
    }

    // ============================================================
    // REPLACE CONTENT
    // ============================================================

    private void replaceContent(
            JPanel panel
    ) {

        contentPanel.remove(1);

        contentPanel.add(
                panel,
                BorderLayout.CENTER
        );

        contentPanel.revalidate();

        contentPanel.repaint();
    }

    // ============================================================
    // BUTTON STYLE
    // ============================================================

    private void styleButton(
            JButton button,
            Color background,
            Color foreground
    ) {

        button.setBackground(
                background
        );

        button.setForeground(
                foreground
        );

        button.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        12
                )
        );

        button.setBorder(
                new EmptyBorder(
                        11,
                        18,
                        11,
                        18
                )
        );

        button.setFocusPainted(false);

        button.setBorderPainted(false);

        button.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );
    }

    // ============================================================
    // ERROR
    // ============================================================

    private void showError(
            String message
    ) {

        JOptionPane.showMessageDialog(
                this,
                message,
                "Trading Error",
                JOptionPane.ERROR_MESSAGE
        );
    }

    // ============================================================
    // MAIN
    // ============================================================

    public static void main(
            String[] args
    ) {

        try {

            UIManager.setLookAndFeel(
                    UIManager
                            .getSystemLookAndFeelClassName()
            );

        } catch (Exception ignored) {
        }

        SwingUtilities.invokeLater(
                () -> {

                    StockTradingPlatform app =
                            new StockTradingPlatform();

                    app.setVisible(true);
                }
        );
    }
}
