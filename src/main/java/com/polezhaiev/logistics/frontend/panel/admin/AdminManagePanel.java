package com.polezhaiev.logistics.frontend.panel.admin;

import com.polezhaiev.logistics.frontend.MainFrame;
import com.polezhaiev.logistics.frontend.model.PanelName;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

@Component
public class AdminManagePanel extends JPanel {

    private final MainFrame mainFrame;

    public AdminManagePanel(@Lazy MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Admin dashboard", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridy = 0;
        add(title, gbc);

        gbc.gridy++;
        add(createButton("Register driver", PanelName.REGISTER_DRIVER.name()), gbc);

        gbc.gridy++;
        add(createButton("Register dispatcher", PanelName.REGISTER_DISPATCHER.name()), gbc);

        gbc.gridy++;
        add(createButton("Register broker", PanelName.REGISTER_BROKER.name()), gbc);

        gbc.gridy++;
        add(createButton("Manage freights", PanelName.MANAGE_FREIGHTS.name()), gbc);

        gbc.gridy++;
        add(createButton("Manage users", PanelName.MANAGE_USERS.name()), gbc);

        gbc.gridy++;
        add(back(), gbc);
    }

    private JButton createButton(String label, String panelKey) {
        JButton button = new JButton(label);
        button.setPreferredSize(new Dimension(200, 40));
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.addActionListener(e -> mainFrame.showPanel(panelKey));
        return button;
    }

    private JButton back() {
        JButton button = new JButton("Back");
        button.addActionListener(l -> mainFrame.showPanel(PanelName.LOGIN.name()));

        return button;
    }
}
