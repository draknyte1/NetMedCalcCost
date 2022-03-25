package net.laurus.calc.gui;

import static net.laurus.calc.gui.MainApp.sMedCache;

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

import net.laurus.calc.med.Medicine;
import net.laurus.calc.packaging.PackageType;
import net.laurus.calc.packaging.types.BasePackage;
import net.laurus.calc.stock.MedicineStock;
import net.laurus.calc.supplier.Supplier;
import net.laurus.calc.util.Money;
import net.laurus.calc.util.Utils;

public class AddMedStockToSupplier extends JDialog {

	private JFrame parent;
	private final JPanel contentPanel = new JPanel();
	private JTextField textCost;
	private JTextField textPackageAmount;
	private Supplier supplier;
	private JComboBox<Medicine> comboMedType;
	private JComboBox<PackageType> comboPackaging;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			AddMedStockToSupplier dialog = new AddMedStockToSupplier();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public AddMedStockToSupplier(JFrame aParent, Supplier aSupplier) {
		this();
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setVisible(true);
		this.parent = aParent;
		this.supplier = aSupplier;
	}

	/**
	 * Create the dialog.
	 */
	private AddMedStockToSupplier() {
		setBounds(100, 100, 540, 240);
		getContentPane().setLayout(new BorderLayout());
		this.contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(this.contentPanel, BorderLayout.CENTER);
		this.contentPanel.setLayout(null);

		JLabel lblBadData = new JLabel("You have not correctly entered details for adding a new Stock");
		lblBadData.setVisible(false);
		lblBadData.setForeground(Color.RED);
		lblBadData.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblBadData.setBounds(10, 141, 506, 13);
		this.contentPanel.add(lblBadData);
		lblBadData.setHorizontalAlignment(SwingConstants.LEFT);

		JLabel lblMedType = new JLabel("Med Type:");
		lblMedType.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblMedType.setBounds(10, 10, 110, 30);
		this.contentPanel.add(lblMedType);

		JLabel lblPackaging = new JLabel("Packaging:");
		lblPackaging.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblPackaging.setBounds(10, 55, 110, 30);
		this.contentPanel.add(lblPackaging);

		JLabel lblCost = new JLabel("Cost \u00A3:");
		lblCost.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblCost.setBounds(10, 95, 110, 30);
		this.contentPanel.add(lblCost);

		this.textCost = new JTextField();
		this.textCost.setColumns(10);
		this.textCost.setBounds(120, 98, 300, 30);
		this.contentPanel.add(this.textCost);

		this.comboMedType = new JComboBox<Medicine>();
		this.comboMedType.setModel(new DefaultComboBoxModel<Medicine>(sMedCache.toArray(new Medicine[sMedCache.size()])));
		this.comboMedType.setBounds(120, 17, 300, 21);
		this.contentPanel.add(this.comboMedType);

		this.comboPackaging = new JComboBox<PackageType>();
		this.comboPackaging.setModel(new DefaultComboBoxModel<PackageType>(PackageType.values()));
		this.comboPackaging.setBounds(120, 62, 150, 21);
		this.contentPanel.add(this.comboPackaging);

		this.textPackageAmount = new JTextField();
		this.textPackageAmount.setColumns(10);
		this.textPackageAmount.setBounds(280, 55, 140, 30);
		this.contentPanel.add(this.textPackageAmount);
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
		Medicine med = (Medicine) this.comboMedType.getSelectedItem();
		Money cost = Utils.createMoneyObject(Float.parseFloat(this.textCost.getText()));
		BasePackage packaging = Utils.getBasePackageFromType((PackageType) this.comboPackaging.getSelectedItem(), Integer.parseInt(this.textPackageAmount.getText()));
		MedicineStock aNewStock = new MedicineStock(med, packaging, cost);
		boolean added = this.supplier.addStock(aNewStock);
		Utils.log(added ? "Added MedicineStock to given Supplier" : "Failed to add MedicineStock to given Supplier.");
		return added;
	}

	private void closeFrame() {
		//MainApp.INSTANCE.tryRefreshGUI();
		//this.parent.update(this.parent.getGraphics());
		//this.parent.revalidate();
		//this.parent.repaint();
		MainApp.INSTANCE.listSupplierStock.update(this.parent.getGraphics());
		MainApp.INSTANCE.listSupplierStock.revalidate();
		MainApp.INSTANCE.listSupplierStock.repaint();
		this.setVisible(false);
		this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
	}
}
