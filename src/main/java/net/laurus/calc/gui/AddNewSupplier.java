package net.laurus.calc.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import net.laurus.calc.supplier.Supplier;
import net.laurus.calc.util.GsonUtils;
import net.laurus.calc.util.Utils;

public class AddNewSupplier extends JDialog {

	private JFrame parent;
	private final JPanel contentPanel = new JPanel();
	private JTextField textName;
	private JTextField textLocation;
	private JTextField textEmail;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			AddNewSupplier dialog = new AddNewSupplier();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public AddNewSupplier(JFrame aParent) {
		this();
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setVisible(true);
		this.parent = aParent;
	}

	/**
	 * Create the dialog.
	 */
	private AddNewSupplier() {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setResizable(false);
		setType(Type.POPUP);
		setTitle("Add new supplier");
		setBounds(100, 100, 540, 240);
		getContentPane().setLayout(new BorderLayout());
		this.contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(this.contentPanel, BorderLayout.CENTER);
		this.contentPanel.setLayout(null);

		JLabel lblName = new JLabel("Name:");
		lblName.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblName.setBounds(10, 10, 85, 30);
		this.contentPanel.add(lblName);
		JLabel lblLocation = new JLabel("Location:");
		lblLocation.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblLocation.setBounds(10, 50, 85, 30);
		this.contentPanel.add(lblLocation);
		JLabel lblEmail = new JLabel("Email:");
		lblEmail.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblEmail.setBounds(10, 90, 85, 30);
		this.contentPanel.add(lblEmail);

		this.textName = new JTextField();
		this.textName.setBounds(105, 13, 300, 30);
		this.contentPanel.add(this.textName);
		this.textName.setColumns(10);

		this.textLocation = new JTextField();
		this.textLocation.setText("Unknown");
		this.textLocation.setColumns(10);
		this.textLocation.setBounds(105, 58, 300, 30);
		this.contentPanel.add(this.textLocation);

		this.textEmail = new JTextField();
		this.textEmail.setColumns(10);
		this.textEmail.setBounds(105, 98, 300, 30);
		this.contentPanel.add(this.textEmail);

		JLabel lblNewLabel = new JLabel("You have not correctly entered details for adding a new supplier.");
		lblNewLabel.setVisible(false);
		lblNewLabel.setForeground(Color.RED);
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel.setBounds(10, 140, 506, 13);
		this.contentPanel.add(lblNewLabel);
		lblNewLabel.setHorizontalAlignment(SwingConstants.LEFT);
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		JButton okButton = new JButton("OK");
		okButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (!tryAddNewSupplier()) {
					lblNewLabel.setVisible(true);
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

	private boolean tryAddNewSupplier() {
		String aSupplierName = this.textName.getText();
		String aSupplierRegion = this.textLocation.getText();
		String aSupplierEmail = this.textEmail.getText();
		if (aSupplierName != null && aSupplierName.length() > 0) {
			if (aSupplierRegion != null && aSupplierRegion.length() > 0) {
				if (aSupplierEmail != null && aSupplierEmail.length() > 0 && aSupplierEmail.contains("@")) {
					Supplier aNewSupplier = new Supplier();
					aNewSupplier.setName(aSupplierName);
					aNewSupplier.setRegion(aSupplierRegion);
					aNewSupplier.setEmail(aSupplierEmail);
					boolean aSuccess = GsonUtils.writeObjectToFile(aNewSupplier);
					if (aSuccess) {
						Utils.log("Wrote Supplier via Gson.");
						return true;
					}
					else {
						Utils.log("Failed to write Supplier via Gson.");
						return false;
					}
				}
			}
		}


		return false;
	}

	private void closeFrame() {
		//MainApp.INSTANCE.tryRefreshGUI();
		this.parent.update(this.parent.getGraphics());
		this.parent.revalidate();
		this.parent.repaint();
		this.setVisible(false);
		this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
	}
}
