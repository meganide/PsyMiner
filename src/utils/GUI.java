package utils;

import org.osbot.rs07.api.map.Position;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI {
    private final JDialog mainDialog;
    private final JComboBox<Ore> oreSelector;
    private final JComboBox<Location> locationSelector;

    private boolean started;

    private Mode activeMode = Mode.Powermine;


    public GUI() {
        mainDialog = new JDialog();
        mainDialog.setTitle("PsyMiner");
        mainDialog.setModal(true);
        mainDialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        mainDialog.getContentPane().add(mainPanel);

        JPanel oreSelectionPanel = new JPanel();
        oreSelectionPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JLabel oreSelectionLabel = new JLabel("Select ore:");
        oreSelectionPanel.add(oreSelectionLabel);

        oreSelector = new JComboBox<>(Ore.values());
        oreSelectionPanel.add(oreSelector);

        mainPanel.add(oreSelectionPanel);

        JPanel locationSelectionPanel = new JPanel();
        locationSelectionPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JLabel locationSelectionLabel = new JLabel("Select location:");
        locationSelectionPanel.add(locationSelectionLabel);

        locationSelector = new JComboBox<>(Location.values());
        locationSelectionPanel.add(locationSelector);

        mainPanel.add(locationSelectionPanel);

        JRadioButton powerButton = new JRadioButton(Mode.Powermine.toString());
        powerButton.setSelected(true);
        JRadioButton bankButton = new JRadioButton(Mode.Bank.toString());

        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(powerButton);
        buttonGroup.add(bankButton);

        powerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setActiveMode(Mode.Powermine);
            }
        });

        bankButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setActiveMode(Mode.Bank);
            }
        });

        mainPanel.add(powerButton);
        mainPanel.add(bankButton);

        JButton startButton = new JButton("Start");
        startButton.addActionListener(e -> {
            started = true;
            close();
        });

        mainPanel.add(startButton);
        mainDialog.pack();
        mainDialog.setLocationRelativeTo(null);

    }

    public boolean isStarted() {
        return started;
    }

    public Ore getSelectedOre() {
        return (Ore) oreSelector.getSelectedItem();
    }

    public Location getSelectedLocation() {
        return (Location) locationSelector.getSelectedItem();
    }

    public void open() {
        mainDialog.setVisible(true);
    }

    public void close() {
        mainDialog.setVisible(false);
        mainDialog.dispose();
    }

    public Mode getActiveMode() {
        return activeMode;
    }

    private void setActiveMode(Mode mode) {
        activeMode = mode;
    }


    public enum Ore {
        Clay,
        Copper,
        Tin,
        Silver,
        Iron,
        Coal,
        Gold,
        Mithril,
        Adamantite,
        Runite,
        Amethyst,
    }

    public enum Location {
        Al_Kharid(new Position(3300, 3314, 0)),
        Varrock_West(new Position(3179, 3368, 0)),
        Varrock_East(new Position(3285, 3365, 0)),
        Rimmington(new Position(2977, 3240, 0)),
        Lumbridge(new Position(3226, 3147, 0));

        private final Position position;

        Location(Position position) {
            this.position = position;
        }

        public Position getPosition() {
            return position;
        }
    }


    public enum Mode {
        Powermine,
        Bank
    }
}






