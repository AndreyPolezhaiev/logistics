package com.polezhaiev.logistics.frontend.panel;

import com.polezhaiev.logistics.dto.admin.AdminLoginRequestDto;
import com.polezhaiev.logistics.dto.broker.BrokerLoginRequestDto;
import com.polezhaiev.logistics.dto.dispather.DispatcherLoginRequestDto;
import com.polezhaiev.logistics.dto.driver.DriverLoginRequestDto;
import com.polezhaiev.logistics.frontend.MainFrame;
import com.polezhaiev.logistics.frontend.context.AuthContext;
import com.polezhaiev.logistics.frontend.util.JwtUtils;
import com.polezhaiev.logistics.service.auth.admin.AdminAuthService;

import javax.swing.*;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Map;

import com.polezhaiev.logistics.service.auth.broker.BrokerAuthService;
import com.polezhaiev.logistics.service.auth.dispatcher.DispatcherAuthService;
import com.polezhaiev.logistics.service.auth.driver.DriverAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class LoginPanel extends JPanel {
    private final MainFrame mainFrame;
    private final AdminAuthService adminAuthService;
    private final DispatcherAuthService dispatcherAuthService;
    private final BrokerAuthService brokerAuthService;
    private final DriverAuthService driverAuthService;
    private final AuthContext authContext;
    private final JTextField emailField = new JTextField(20);
    private final JPasswordField passwordField = new JPasswordField(20);
    private final JComboBox<String> roleComboBox
            = new JComboBox<>(new String[]{"ADMIN", "DISPATCHER", "BROKER", "DRIVER"});

    @Autowired
    public LoginPanel(@Lazy MainFrame mainFrame,
                      AdminAuthService adminAuthService,
                      DispatcherAuthService dispatcherAuthService,
                      BrokerAuthService brokerAuthService,
                      DriverAuthService driverAuthService,
                      AuthContext authContext
    ) {
        this.mainFrame = mainFrame;
        this.adminAuthService = adminAuthService;
        this.dispatcherAuthService = dispatcherAuthService;
        this.brokerAuthService = brokerAuthService;
        this.driverAuthService = driverAuthService;
        this.authContext = authContext;

        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel titleLabel = new JLabel("Login page");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Email:"), gbc);

        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridx = 1;
        add(emailField, gbc);

        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Password:"), gbc);

        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridx = 1;
        add(passwordField, gbc);

        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(new JLabel("Role:"), gbc);

        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.gridx = 1;
        add(roleComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(login(), gbc);
    }

    private JButton login() {
        JButton button = new JButton("Login");
        button.setPreferredSize(new Dimension(120, 35));
        button.setFont(new Font("Arial", Font.BOLD, 14));

        button.addActionListener(l -> handleLogin());

        return button;
    }

    private void handleLogin() {
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());
        String role = (String) roleComboBox.getSelectedItem();

        try {
            String token = "";
            switch (role) {
                case "ADMIN" -> {
                    var dto = new AdminLoginRequestDto(email, password);
                    token = adminAuthService.authenticateAdmin(dto).getAccessToken();
                }
                case "DISPATCHER" -> {
                    var dto = new DispatcherLoginRequestDto(email, password);
                    token = dispatcherAuthService.authenticateDispatcher(dto).getAccessToken();
                }
                case "BROKER" -> {
                    var dto = new BrokerLoginRequestDto(email, password);
                    token = brokerAuthService.authenticateBroker(dto).getAccessToken();
                }
                case "DRIVER" -> {
                    var dto = new DriverLoginRequestDto(email, password);
                    token = driverAuthService.authenticateDriver(dto).getAccessToken();
                }
                case null -> {
                    JOptionPane.showMessageDialog(this, "Choose your role to sign in");
                }
                default -> throw new IllegalStateException("Unexpected value: " + role);
            }

            authContext.setAccessToken(token);
            Map<String, Object> claims = JwtUtils.decode(token);
            authContext.setEmail((String) claims.get("email"));
            authContext.setUserId((String) claims.get("sub"));
            authContext.setRole(role);

            mainFrame.showPanel(role);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Login failed: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
