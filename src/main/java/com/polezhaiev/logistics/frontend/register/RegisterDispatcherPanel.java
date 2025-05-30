package com.polezhaiev.logistics.frontend.register;

import com.polezhaiev.logistics.frontend.MainFrame;
import com.polezhaiev.logistics.frontend.model.PanelName;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;

@Component
public class RegisterDispatcherPanel extends JPanel {
    private final MainFrame mainFrame;

    public RegisterDispatcherPanel(@Lazy MainFrame mainFrame) {
        this.mainFrame = mainFrame;

        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Register dispatcher", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridy = 0;
        add(title, gbc);

        gbc.gridy++;
        add(back(), gbc);
    }

    private JButton back() {
        JButton button = new JButton("Back");
        button.setPreferredSize(new Dimension(200, 40));
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.addActionListener(
                l -> mainFrame.showPanel(PanelName.ADMIN.name())
        );

        return button;
    }
}
