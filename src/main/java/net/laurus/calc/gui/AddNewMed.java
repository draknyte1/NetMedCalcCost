package net.laurus.calc.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import net.laurus.calc.med.MedType;
import net.laurus.calc.med.Medicine;
import net.laurus.calc.util.GsonUtils;
import net.laurus.calc.util.Measurement;
import net.laurus.calc.util.Utils;

public class AddNewMed extends JDialog {

	private JFrame parent;
	private final JPanel contentPanel = new JPanel();
	private JTextField textName;
	private JTextField textDose;
	private JComboBox<Measurement> comboMeasurement;
	private JComboBox<MedType> comboType;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			AddNewMed dialog = new AddNewMed();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public AddNewMed(JFrame aParent) {
		this();
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setVisible(true);
		this.parent = aParent;
	}

	/**
	 * Create the dialog.
	 */
	private AddNewMed() {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setResizable(false);
		setType(Type.POPUP);
		setTitle("Add new medicine");
		setBounds(100, 100, 540, 270);
		getContentPane().setLayout(new BorderLayout());
		this.contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(this.contentPanel, BorderLayout.CENTER);
		this.contentPanel.setLayout(null);

		JLabel lblName = new JLabel("Name:");
		lblName.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblName.setBounds(10, 10, 110, 30);
		this.contentPanel.add(lblName);
		JLabel lblDose = new JLabel("Dose");
		lblDose.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblDose.setBounds(10, 55, 110, 30);
		this.contentPanel.add(lblDose);
		JLabel lblMeasurement = new JLabel("Measurement:");
		lblMeasurement.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblMeasurement.setBounds(10, 95, 110, 30);
		this.contentPanel.add(lblMeasurement);

		this.textName = new JTextField();
		this.textName.setBounds(120, 13, 300, 30);
		this.contentPanel.add(this.textName);
		this.textName.setColumns(10);

		this.textDose = new JTextField();
		this.textDose.setColumns(10);
		this.textDose.setBounds(120, 58, 300, 30);
		this.contentPanel.add(this.textDose);

		JLabel lblBadData = new JLabel("You have not correctly entered details for adding a new Medicine.");
		lblBadData.setVisible(false);
		lblBadData.setForeground(Color.RED);
		lblBadData.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblBadData.setBounds(10, 175, 506, 13);
		this.contentPanel.add(lblBadData);
		lblBadData.setHorizontalAlignment(SwingConstants.LEFT);

		this.comboMeasurement = new JComboBox<Measurement>();
		this.comboMeasurement.setModel(new DefaultComboBoxModel<Measurement>(Measurement.values()));
		this.comboMeasurement.setBounds(120, 102, 300, 21);
		this.contentPanel.add(this.comboMeasurement);

		JLabel lblType = new JLabel("Type:");
		lblType.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblType.setBounds(10, 135, 110, 30);
		this.contentPanel.add(lblType);

		this.comboType = new JComboBox<MedType>();
		this.comboType.setModel(new DefaultComboBoxModel<MedType>(MedType.values()));
		this.comboType.setBounds(120, 142, 300, 21);
		this.contentPanel.add(this.comboType);
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		JButton okButton = new JButton("OK");
		okButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (!tryAddNewMed()) {
					lblBadData.setVisible(true);
				}
				else {
					closeFrame();
				}
			}
		});
		okButton.setActionCommand("OK");
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);

		JButton cancelButton = new JButton("Cancel");
		cancelButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				closeFrame();
			}
		});
		cancelButton.setActionCommand("Cancel");
		buttonPane.add(cancelButton);

	}

	private boolean tryAddNewMed() {
		String aMedName = this.textName.getText();
		String aMedDose = this.textDose.getText();
		Object aMedMeasurementObj = this.comboMeasurement.getSelectedItem();
		Object aMedTypeObj = this.comboType.getSelectedItem();
		if (!(aMedMeasurementObj instanceof Measurement)) {
			Utils.log("Bad Measurement selected.");
			return false;
		}
		if (!(aMedTypeObj instanceof MedType)) {
			Utils.log("Bad Type selected.");
			return false;
		}
		Measurement aMedMeasurement = (Measurement) aMedMeasurementObj;
		MedType aType = (MedType) aMedTypeObj;
		if (aMedName != null && aMedName.length() > 0) {
			if (aMedDose != null && aMedDose.length() > 0) {
				if (aMedMeasurement.get().length() > 0 && aType.get().length() > 0) {
					float dose = 0f;
					try {
						dose = Float.parseFloat(this.textDose.getText());
					}
					catch (NumberFormatException n) {
						n.printStackTrace();
						return false;
					}
					Medicine aMed = new Medicine();
					aMed.setName(aMedName);
					aMed.setDose(dose);
					aMed.setUnit(aMedMeasurement);
					aMed.setType(aType);
					boolean aSuccess = GsonUtils.writeObjectToFile(aMed);
					if (aSuccess) {
						Utils.log("Wrote Medicine via Gson.");
						return true;
					}
					else {
						Utils.log("Failed to write Medicine via Gson.");
						return false;
					}
				}
			}
		}
		return false;
	}

	private void closeFrame() {
		MainApp.INSTANCE.tryRefreshGUI();
		this.parent.update(this.parent.getGraphics());
		this.parent.revalidate();
		this.parent.repaint();
		this.setVisible(false);
		this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
	}
}
