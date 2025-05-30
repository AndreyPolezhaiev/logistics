package com.polezhaiev.logistics.frontend;

import com.polezhaiev.logistics.frontend.model.PanelName;
import com.polezhaiev.logistics.frontend.panel.broker.BrokerPanel;
import com.polezhaiev.logistics.frontend.panel.dispatcher.DispatcherPanel;
import com.polezhaiev.logistics.frontend.panel.driver.DriverPanel;
import com.polezhaiev.logistics.frontend.panel.LoginPanel;
import com.polezhaiev.logistics.frontend.panel.admin.AdminManagePanel;

import javax.swing.*;
import java.awt.CardLayout;

import com.polezhaiev.logistics.frontend.register.RegisterBrokerPanel;
import com.polezhaiev.logistics.frontend.register.RegisterDispatcherPanel;
import com.polezhaiev.logistics.frontend.register.RegisterDriverPanel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MainFrame extends JFrame {
    private final CardLayout cardLayout = new CardLayout();

    @Autowired
    public MainFrame(
            LoginPanel loginPanel,
            AdminManagePanel registerUserPanel,
            DispatcherPanel dispatcherPanel,
            BrokerPanel brokerPanel,
            DriverPanel driverPanel,
            RegisterBrokerPanel registerBrokerPanel,
            RegisterDispatcherPanel registerDispatcherPanel,
            RegisterDriverPanel registerDriverPanel
    ) {
        setTitle("Logistics System");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);
        setLayout(cardLayout);

        add(loginPanel, PanelName.LOGIN.name());
        add(registerUserPanel, PanelName.ADMIN.name());
        add(dispatcherPanel, PanelName.DISPATCHER.name());
        add(brokerPanel, PanelName.BROKER.name());
        add(driverPanel, PanelName.DRIVER.name());
        add(registerBrokerPanel, PanelName.REGISTER_BROKER.name());
        add(registerDriverPanel, PanelName.REGISTER_DRIVER.name());
        add(registerDispatcherPanel, PanelName.REGISTER_DISPATCHER.name());

        showPanel(PanelName.LOGIN.name());
    }

    public void showPanel(String name) {
        cardLayout.show(getContentPane(), name.toUpperCase());
        revalidate();
        repaint();
    }
}
