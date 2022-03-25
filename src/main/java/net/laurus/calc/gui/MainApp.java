package net.laurus.calc.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.laurus.calc.BackgroundDaemon;
import net.laurus.calc.DataHandler;
import net.laurus.calc.med.Medicine;
import net.laurus.calc.packaging.types.BasePackage;
import net.laurus.calc.stock.MedicineStock;
import net.laurus.calc.supplier.Supplier;
import net.laurus.calc.util.GsonUtils;
import net.laurus.calc.util.InterfaceAdapter;
import net.laurus.calc.util.Utils;

public class MainApp {

	public static MainApp INSTANCE;
	public static DataHandler DATA_INSTANCE;

	public static Vector<Medicine> sMedCache = new Vector<Medicine>();
	public static Vector<Supplier> sSupplierCache = new Vector<Supplier>();

	private HashSet<JComponent> mBits = new HashSet<JComponent>();
	public JFrame frmMedCalc;
	JList<Medicine> listMedTypes;
	JList<Supplier> listSuppliers;
	JList<MedicineStock> listSupplierStock;
	public static Gson GSON;
	private static final int X = 1024;
	private static final int Y = 768;

	private static String[] sTabNames = new String[] { "Main", "Suppliers", "Medication", "?" };

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		configureGson();
		Utils.log("Is Gson Valid? " + (GSON != null));
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					INSTANCE = new MainApp();
					INSTANCE.frmMedCalc.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public static void configureGson() {
		GsonBuilder aBuilder = new GsonBuilder();
		// Set Handling
		aBuilder.serializeNulls();
		aBuilder.setPrettyPrinting();

		// Add Interfaces for abstract classes
		aBuilder.registerTypeAdapter(BasePackage.class, new InterfaceAdapter<BasePackage>());
		GSON = aBuilder.create();
	}

	/**
	 * Create the application.
	 */
	public MainApp() {
		initialize();
		tryRefreshGUI();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		this.frmMedCalc = new JFrame();
		this.frmMedCalc.setResizable(false);
		this.frmMedCalc.setTitle("Med Calc");
		this.frmMedCalc.setBounds(100, 100, X, Y);
		this.frmMedCalc.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel panelRoot = new JPanel();
		// this.frmMedCalc.getContentPane().add(panelRoot, BorderLayout.CENTER);
		panelRoot.setLayout(null);
		this.frmMedCalc.setContentPane(panelRoot);

		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, this.frmMedCalc.getWidth(), 32);
		panelRoot.add(menuBar);

		JMenu mnNewMenu = new JMenu("File");
		menuBar.add(mnNewMenu);

		JMenuItem mntmNewMenuItem_2 = new JMenuItem("Open");
		mnNewMenu.add(mntmNewMenuItem_2);

		JMenuItem mntmNewMenuItem_1 = new JMenuItem("Save");
		mnNewMenu.add(mntmNewMenuItem_1);

		JMenuItem mntmNewMenuItem = new JMenuItem("Exit");
		mntmNewMenuItem.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (KeyEvent.VK_ENTER == e.getKeyCode()) {
					Utils.log("Pressed");
					close();
				}
			}
		});
		mntmNewMenuItem.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				Utils.log("Pressed");
				close();

			}
		});
		mnNewMenu.add(mntmNewMenuItem);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(0, 30, this.frmMedCalc.getWidth() - 18, this.frmMedCalc.getSize().height - mnNewMenu.getSize().height - 100);
		panelRoot.add(tabbedPane);

		JPanel panelMain = new JPanel();
		tabbedPane.addTab(sTabNames[0], null, panelMain, null);
		panelMain.setLayout(new BorderLayout(0, 0));

		JSplitPane splitMain = new InternalSplitPane();
		splitMain.setResizeWeight(0.1);
		panelMain.add(splitMain, BorderLayout.CENTER);

		JSplitPane splitMainInfo = new InternalSplitPane();
		splitMainInfo.setResizeWeight(1.0);
		splitMainInfo.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitMain.setRightComponent(splitMainInfo);

		JPanel panel_1 = new JPanel();
		splitMainInfo.setLeftComponent(panel_1);

		JPanel panel_2 = new JPanel();
		splitMainInfo.setRightComponent(panel_2);

		JPanel panel = new JPanel();
		splitMain.setLeftComponent(panel);

		JList list = new JList();
		panel.add(list);

		JPanel panelSuppliers = new JPanel();
		panelSuppliers.setToolTipText("");
		tabbedPane.addTab(sTabNames[1], null, panelSuppliers, null);
		panelSuppliers.setLayout(new BorderLayout(0, 0));

		JSplitPane splitSuppliersMain = new InternalSplitPane();
		splitSuppliersMain.setResizeWeight(0.1);
		// splitPane_1.setDividerSize(0);
		panelSuppliers.add(splitSuppliersMain);

		this.listSuppliers = new JList<Supplier>(sSupplierCache);
		this.listSuppliers.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (MainApp.this.listSuppliers.getModel().getSize() > 0) {
					if (MainApp.this.listSuppliers.getSelectedValue() != null) {
						Supplier aSupplier = MainApp.this.listSuppliers.getSelectedValue();
						MainApp.this.listSupplierStock.setListData(aSupplier.getStock());
						new BackgroundDaemon(MainApp.this.listSuppliers);
						new BackgroundDaemon(MainApp.this.listSupplierStock);
						//tryRefreshGUI();
					}
				}
			}
		});
		this.listSuppliers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		splitSuppliersMain.setLeftComponent(new JScrollPane(this.listSuppliers));

		JPanel panelSuppliersParent = new JPanel();
		panelSuppliersParent.setBorder(null);
		splitSuppliersMain.setRightComponent(panelSuppliersParent);
		panelSuppliersParent.setLayout(new BorderLayout(0, 0));

		JSplitPane splitSuppliersInfo = new InternalSplitPane();
		splitSuppliersInfo.setResizeWeight(1.0);
		splitSuppliersInfo.setOrientation(JSplitPane.VERTICAL_SPLIT);
		panelSuppliersParent.add(splitSuppliersInfo, BorderLayout.CENTER);

		JPanel panelSuppliersMain = new JPanel();
		panelSuppliersMain.setBorder(null);
		splitSuppliersInfo.setLeftComponent(panelSuppliersMain);
		panelSuppliersMain.setLayout(new BorderLayout(0, 0));

		JSplitPane splitStockInfo = new InternalSplitPane();
		panelSuppliersMain.add(splitStockInfo, BorderLayout.CENTER);

		JPanel panelSupplierMedList = new JPanel();
		splitStockInfo.setLeftComponent(panelSupplierMedList);

		JScrollPane scrollPane = new JScrollPane();
		panelSupplierMedList.add(scrollPane);

		this.listSupplierStock = new JList<MedicineStock>();
		this.listSupplierStock.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane.setViewportView(this.listSupplierStock);

		JPanel panelStockInfo = new JPanel();
		splitStockInfo.setRightComponent(panelStockInfo);
		panelStockInfo.setLayout(null);

		JPanel panelSuppliersButtons = new JPanel();
		FlowLayout fl_panelSuppliersButtons = (FlowLayout) panelSuppliersButtons.getLayout();
		fl_panelSuppliersButtons.setAlignment(FlowLayout.LEFT);
		panelSuppliersButtons.setBorder(null);
		splitSuppliersInfo.setRightComponent(panelSuppliersButtons);

		JButton buttonSuppliersNew = new JButton("New Supplier");
		buttonSuppliersNew.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				new AddNewSupplier(MainApp.this.frmMedCalc);
			}
		});
		panelSuppliersButtons.add(buttonSuppliersNew);

		JButton buttonSuppliersEdit = new JButton("Edit");
		panelSuppliersButtons.add(buttonSuppliersEdit);
		buttonSuppliersEdit.setToolTipText("Edit the currently selected supplier.");

		JButton buttonSuppliersDelete = new JButton("Delete");
		buttonSuppliersDelete.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (MainApp.this.listSuppliers.getModel().getSize() > 0) {
					if (MainApp.this.listSuppliers.getSelectedValue() != null) {
						Supplier aSupplier = MainApp.this.listSuppliers.getSelectedValue();
						if (GsonUtils.deleteObject(aSupplier)) {
							Utils.log("Deleted a Supplier. " + aSupplier.toString());
							tryRefreshGUI();
						}
					}
				}
			}
		});
		panelSuppliersButtons.add(buttonSuppliersDelete);
		buttonSuppliersDelete.setToolTipText("Removes the currently selected supplier.");

		Component horizontalStrut = Box.createHorizontalStrut(300);
		panelSuppliersButtons.add(horizontalStrut);

		JButton btnSuppliersAddStock = new JButton("Add new Stock");
		btnSuppliersAddStock.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (MainApp.this.listSuppliers.getModel().getSize() > 0) {
					if (MainApp.this.listSuppliers.getSelectedValue() != null) {
						new AddMedStockToSupplier(MainApp.this.frmMedCalc, MainApp.this.listSuppliers.getSelectedValue());
					}
				}
			}
		});
		panelSuppliersButtons.add(btnSuppliersAddStock);
		btnSuppliersAddStock.setToolTipText("Adds new Medicine Stock to the currently selected supplier.");

		JButton btnSuppliersRemoveStock = new JButton("Remove Stock");
		btnSuppliersRemoveStock.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (MainApp.this.listSuppliers.getModel().getSize() > 0) {
					if (MainApp.this.listSuppliers.getSelectedValue() != null) {
						Supplier aSupplier = MainApp.this.listSuppliers.getSelectedValue();
						if (MainApp.this.listSupplierStock.getModel().getSize() > 0) {
							if (MainApp.this.listSupplierStock.getSelectedValue() != null) {
								boolean didRemove = aSupplier.getStock().remove(MainApp.this.listSupplierStock.getSelectedValue());
								if (didRemove) {
									Utils.log("Removed MedStock from "+aSupplier.getName());
								}
								else {
									Utils.log("Failed to remove MedStock from "+aSupplier.getName());
								}
							}
						}
					}
				}
			}
		});
		btnSuppliersRemoveStock.setToolTipText("Deletes the currently selected Medicine Stock for the given supplier.");
		panelSuppliersButtons.add(btnSuppliersRemoveStock);

		JPanel panelMeds = new JPanel();
		panelMeds.setToolTipText("");
		tabbedPane.addTab(sTabNames[2], null, panelMeds, null);
		panelMeds.setLayout(new BorderLayout(0, 0));

		JSplitPane splitMedMain = new InternalSplitPane();
		splitMedMain.setResizeWeight(0.1);
		splitMedMain.setContinuousLayout(true);
		// splitPane_2.setDividerSize(0);
		panelMeds.add(splitMedMain, BorderLayout.CENTER);

		this.listMedTypes = new JList<Medicine>(/* new DefaultListModel<Medicine>() */);
		this.listMedTypes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.listMedTypes.setSize(Y, X);
		splitMedMain.setLeftComponent(new JScrollPane(this.listMedTypes));

		JPanel panelMedParent = new JPanel();
		splitMedMain.setRightComponent(panelMedParent);
		panelMedParent.setLayout(new BorderLayout(0, 0));

		JSplitPane splitMedInfo = new InternalSplitPane();
		splitMedInfo.setResizeWeight(1.0);
		splitMedInfo.setOrientation(JSplitPane.VERTICAL_SPLIT);
		panelMedParent.add(splitMedInfo, BorderLayout.CENTER);

		JPanel panelMedMain = new JPanel();
		splitMedInfo.setLeftComponent(panelMedMain);

		JPanel panelMedButtons = new JPanel();
		FlowLayout fl_panelMedButtons = (FlowLayout) panelMedButtons.getLayout();
		fl_panelMedButtons.setAlignment(FlowLayout.LEFT);
		splitMedInfo.setRightComponent(panelMedButtons);

		JButton buttonMedNew = new JButton("New Med");
		buttonMedNew.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				new AddNewMed(MainApp.this.frmMedCalc);
			}
		});
		panelMedButtons.add(buttonMedNew);

		JButton buttonMedEdit = new JButton("Edit");
		buttonMedEdit.setToolTipText("Edit the currently selected Med.");
		panelMedButtons.add(buttonMedEdit);

		JButton buttonMedDelete = new JButton("Delete");
		buttonMedDelete.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				Utils.log("Delete. ");
				if (MainApp.this.listMedTypes.getModel().getSize() > 0) {
					Utils.log("Delete. " + MainApp.this.listMedTypes.getModel().getSize());
					if (MainApp.this.listMedTypes.getSelectedValue() != null) {
						Utils.log("Delete. " + MainApp.this.listMedTypes.getSelectedValue().toString());
						Medicine aMed = MainApp.this.listMedTypes.getSelectedValue();
						String aMedName = aMed.toString();
						if (GsonUtils.deleteObject(aMed)) {
							Utils.log("Deleted Medicine. " + aMedName);
							tryRefreshGUI();
						} else {
							Utils.log("Failed to delete Medicine. " + aMedName);
						}
					}
				}
			}
		});
		buttonMedDelete.setToolTipText("Removes the currently selected Med.");
		panelMedButtons.add(buttonMedDelete);

		JPanel panelMisc = new JPanel();
		tabbedPane.addTab(sTabNames[3], null, panelMisc, null);
		panelMisc.setLayout(null);

		JTextArea multitextInfoTab = new JTextArea();
		multitextInfoTab.setWrapStyleWord(true);
		multitextInfoTab.setLineWrap(true);
		multitextInfoTab.setBounds(10, 5, 725, 390);
		multitextInfoTab.setText(
				"What is Lorem Ipsum?\r\nLorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.");
		panelMisc.add(multitextInfoTab);

		//this.mBits.add(this.listSuppliers);
		//this.mBits.add(this.listMedTypes);
		/*
		 * this.mBits.add(panelRoot); this.mBits.add(tabbedPane);
		 * this.mBits.add(panelMeds); this.mBits.add(panelMedParent);
		 * this.mBits.add(panelMedMain); this.mBits.add(panelSuppliers);
		 * this.mBits.add(panelSuppliersParent); this.mBits.add(panelSuppliersMain);
		 */

	}

	public void close() {
		this.frmMedCalc.dispose();
		this.frmMedCalc = null;
		System.exit(0);
	}

	public void tryRefreshGUI() {
		MainApp.sMedCache.clear();
		MainApp.sSupplierCache.clear();
		DATA_INSTANCE = new DataHandler();
		while (DATA_INSTANCE.isAlive()) {
			if (!DATA_INSTANCE.isAlive()) {
				break;
			}
		}
		// DefaultListModel<Medicine> modelMeds = (DefaultListModel<Medicine>)
		// this.listMedTypes.getModel();
		// DefaultListModel<Supplier> modelSups = (DefaultListModel<Supplier>)
		// this.listSuppliers.getModel();
		// modelMeds.clear();
		// for (Medicine m : MainApp.sMedCache) {
		// modelMeds.addElement(m);
		// }
		// modelSups.clear();
		// for (Supplier s : MainApp.sSupplierCache) {
		// modelSups.addElement(s);
		// }

		this.listMedTypes.setListData(sMedCache.toArray(new Medicine[sMedCache.size()]));
		this.listSuppliers.setListData(sSupplierCache.toArray(new Supplier[sSupplierCache.size()]));
		Utils.log("Debug 1 - " + this.listMedTypes.getModel().getSize());
		Utils.log("Debug 2 - " + this.listSuppliers.getModel().getSize());
		// this.frmMedCalc.repaint();
		for (JComponent j : this.mBits) {
			if (j != null) {
				//j.revalidate();
				//j.repaint();
			}
		}
		Utils.log("Med Cache: " + sMedCache.size());
		Utils.log("Supplier Cache: " + sSupplierCache.size());
		DATA_INSTANCE = null;
	}
}
