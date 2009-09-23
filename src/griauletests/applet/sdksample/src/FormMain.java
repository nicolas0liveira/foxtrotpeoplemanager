/*
 -------------------------------------------------------------------------------
 Fingerprint SDK Sample
 (c) 2005-2007 Griaule Tecnologia Ltda.
 http://www.griaule.com
 -------------------------------------------------------------------------------

 This sample is provided with "Fingerprint SDK Recognition Library" and
 can't run without it. It's provided just as an example of using Fingerprint SDK
 Recognition Library and should not be used as basis for any
 commercial product.

 Griaule Biometrics makes no representations concerning either the merchantability
 of this software or the suitability of this sample for any particular purpose.

 THIS SAMPLE IS PROVIDED BY THE AUTHOR "AS IS" AND ANY EXPRESS OR
 IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 IN NO EVENT SHALL GRIAULE BE LIABLE FOR ANY DIRECT, INDIRECT,
 INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

 You can download the trial version of Fingerprint SDK directly from Griaule website.

 These notices must be retained in any copies of any part of this
 documentation and/or sample.

 -------------------------------------------------------------------------------
 */

package griauletests.applet.sdksample.src;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Iterator;
import java.util.Locale;

import javax.imageio.spi.IIORegistry;
import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.spi.ImageReaderWriterSpi;
import javax.imageio.spi.ImageWriterSpi;
import javax.swing.BorderFactory;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;

import com.griaule.grfingerjava.GrFingerJava;

/**
 * The main application form.
 */
public class FormMain extends JApplet {
	// object used to perform all the Fingerprint SDK-related operations
	private Util fingerprintSDKSample;

	// Components used to display the log.
	private JScrollPane logScrollPane = null;
	private JTextArea logTextArea = null;

	// Form used to set the options.
	private FormOptions optionsForm = null;

	// The image of the current fingerprint.
	private BufferedImage fingerprintImage = null;

	// Panel used to display the current fingerprint.
	private JPanel fingerprintViewPanel = null;

	// Menu item that allows saving the current fingerprint.
	// It's declared as an instace field because it's "enabled" state changes.
	private JMenuItem saveMenuItem = null;

	// Some of the buttons at the right part of the form.
	// They're declared as instace fields because their "enabled" state changes.
	private JButton enrollButton = null;
	private JButton verifyButton = null;
	private JButton identifyButton = null;
	private JButton extractButton = null;

	/**
	 * Creates a new "Fingerprint SDK Sample" Window.
	 */
	public FormMain() {
		super();
	}

	/**
	 * Initializes the GUI.
	 */
	public void init() {
		// Sets the System Look and Feel to the system's default.
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
		}

		// Sets the window size
		this.setSize(515, 560);

		// Adds the contents of the window: The menu bar and the content pane
		this.setJMenuBar(createMenuBar());
		this.setContentPane(createContentPane());

		// Creates an "Util" instance.
		this.fingerprintSDKSample = new Util(this);
	}

	/**
	 * Frees Fingerprint SDK resources and finished the program.
	 */
	public void destroy() {
		fingerprintSDKSample.destroy();
	}

	/**
	 * This method creates the frame's menu bar.
	 */
	private JMenuBar createMenuBar() {
		// Creates "Root" menu bar
		JMenuBar menuBar = new JMenuBar();

		// --------------------------------------------
		// Creates the "Image" menu
		// Root->Image
		JMenu imageMenu = new JMenu("Image");
		menuBar.add(imageMenu);

		// --------------------------------------------
		// Creates the "Save Image" sub-menu
		// Root->Image->Save
		saveMenuItem = new JMenuItem("Save");
		imageMenu.add(saveMenuItem);

		// You can't save an image until the first image is loaded/captured
		saveMenuItem.setEnabled(false);
		// Adds an event-handler to this menu item
		saveMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				saveImage();
			}
		});

		// --------------------------------------------
		// Creates the "Load From File" sub-menu
		// Root->Image->Load From File
		JMenuItem loadFromFileMenuItem = new JMenuItem("Load From File");
		imageMenu.add(loadFromFileMenuItem);

		// Adds an event-handler to this menu item
		loadFromFileMenuItem
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent e) {
						loadFromFile();
					}
				});

		// --------------------------------------------
		// Creates the "Options..." menu
		// Root->Options...
		JMenuItem optionsMenu = new JMenuItem("Options...");
		menuBar.add(optionsMenu);

		// Sets this menu's maximum size. If it's not set, the menubar's layout
		// gets "strange".
		optionsMenu.setMaximumSize(new Dimension(70, 100));

		// Adds an event-handler to this menu item
		optionsMenu.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				showOptions();
			}
		});

		// --------------------------------------------
		// Creates the "Version" menu
		// Root->Version
		JMenuItem versionMenu = new JMenuItem("Version");
		menuBar.add(versionMenu);

		// Sets this menu's maximum size. If it's not set, the menubar's layout
		// gets "strange".
		versionMenu.setMaximumSize(new Dimension(70, 100));

		// Adds an event-handler to this menu item
		versionMenu.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				showVersion();
			}
		});

		// Returns the menu bar.
		return menuBar;
	}

	/**
	 * This method creates the window's root panel.
	 */
	private JComponent createContentPane() {
		// Creates a new JPanel
		JPanel contentPane = new JPanel(new BorderLayout());

		// On it's lower part, adds the Log Viewer
		contentPane.add(createLogArea(), BorderLayout.SOUTH);

		// ont the right, the buttons.
		contentPane.add(createButtonsPanel(), BorderLayout.EAST);

		// And, on the middle, the Fingerprint Image
		contentPane.add(createFingerprintViewPanel(), BorderLayout.CENTER);

		return contentPane;
	}

	/**
	 * This method creates the log area initializes jScrollPane
	 */
	private JComponent createLogArea() {
		// Creates the textArea where the log will be written
		logTextArea = new JTextArea();
		logTextArea.setEditable(false); // It's not editable
		logTextArea.setLineWrap(true); // Enabled wrapping
		logTextArea.setFont(Font.decode("arial-11")); // Sets the font

		// Creates a ScrollPane, so that we can scroll down the log when it's
		// too big
		logScrollPane = new JScrollPane(logTextArea,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		// Sets it's size
		logScrollPane.setPreferredSize(new java.awt.Dimension(0, 130));
		// Enables auto-scrolling
		logScrollPane.setAutoscrolls(true);

		// Adds a little border around it
		logScrollPane.setBorder(new CompoundBorder(new EmptyBorder(2, 2, 2, 2),
				new BevelBorder(BevelBorder.LOWERED)));

		return logScrollPane;
	}

	/**
	 * This method creates the panel with buttons placed on the right part of
	 * the frame.
	 */
	private JComponent createButtonsPanel() {
		// Creates a JPanel for containing all buttons on the right-side of the
		// screen.
		// The buttons are arranjed in grid with only 1 column
		JPanel buttonsPanel = new JPanel(new GridLayout(0, 1, 5, 5));
		// Adds a border, in order to keep space between the buttons and the
		// rest of the form.
		buttonsPanel.setBorder(BorderFactory.createEmptyBorder(45, 5, 35, 5));

		// --------------------------------------------
		// Creates the "Enroll" button
		enrollButton = new JButton("Enroll");
		buttonsPanel.add(enrollButton);

		// this button is , by default, disabled.
		enrollButton.setEnabled(false);

		// Adds an event-handler to this button
		enrollButton.addActionListener(new java.awt.event.ActionListener() {
			// enrolls the current fingerprint.
			public void actionPerformed(java.awt.event.ActionEvent e) {
				fingerprintSDKSample.enroll();
			}
		});

		// --------------------------------------------
		// Creates the "Verify" button
		verifyButton = new JButton("Verify");
		buttonsPanel.add(verifyButton);

		// this button is , by default, disabled.
		verifyButton.setEnabled(false);

		// Adds an event-handler to this button
		verifyButton.addActionListener(new java.awt.event.ActionListener() {
			// Asks the user for a template ID, and then makes a verification
			// against it
			public void actionPerformed(java.awt.event.ActionEvent e) {
				// Shows a input dialog asking the template ID to verify
				String id = JOptionPane.showInputDialog(rootPane,
						"Enter the ID to verify.", "Verify",
						JOptionPane.QUESTION_MESSAGE);

				// If the user did not cancel the verification
				if (id != null) {
					try {
						// Makes the verification
						fingerprintSDKSample.verify(Integer.parseInt(id));

					} catch (NumberFormatException e1) {
						// If the id is not a number, shows an error message.
						writeLog("Invalid ID.");
					}
				}
			}
		});

		// --------------------------------------------
		// Creates the "Identify" button
		identifyButton = new JButton("Identify");
		buttonsPanel.add(identifyButton);

		// this button is , by default, disabled.
		identifyButton.setEnabled(false);

		// Adds an event-handler to this button
		identifyButton.addActionListener(new java.awt.event.ActionListener() {
			// Identifies the current template
			public void actionPerformed(java.awt.event.ActionEvent e) {
				fingerprintSDKSample.identify();
			}
		});

		// --------------------------------------------
		// Creates the "Extract" button
		extractButton = new JButton("Extract template");
		buttonsPanel.add(extractButton);

		// this button is , by default, disabled.
		extractButton.setEnabled(false);

		// Adds an event-handler to this button
		extractButton.addActionListener(new java.awt.event.ActionListener() {
			// Extracts a template from the current image
			public void actionPerformed(java.awt.event.ActionEvent e) {
				fingerprintSDKSample.extract();
			}
		});

		// --------------------------------------------
		// Adds en empty label, just in order to add a little spacing
		buttonsPanel.add(new JLabel());

		// --------------------------------------------
		// Creates the "Clear DB" button
		JButton clearDBButton = new JButton("Clear database");
		buttonsPanel.add(clearDBButton);

		// Adds an event-handler to this button
		clearDBButton.addActionListener(new java.awt.event.ActionListener() {
			// Clear all the templates stored on the DB
			public void actionPerformed(java.awt.event.ActionEvent e) {
				fingerprintSDKSample.clearDB();
			}
		});

		// --------------------------------------------
		// Creates the "Clear Log" button
		JButton clearLogButton = new JButton("Clear log");
		buttonsPanel.add(clearLogButton);

		// Adds an event-handler to this button
		clearLogButton.addActionListener(new java.awt.event.ActionListener() {
			// Puts empty data on the log
			public void actionPerformed(java.awt.event.ActionEvent e) {
				logTextArea.setText("");
			}
		});

		// --------------------------------------------
		// Creates the "Auto identify" checkBox
		JCheckBox autoIdentifyCheckBox = new JCheckBox("Auto identify", false); // Default
		// state:
		// Unchecked
		buttonsPanel.add(autoIdentifyCheckBox);

		// Aligns this checkbox in the center
		autoIdentifyCheckBox
				.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

		// Adds an event-handler to this CheckBox
		autoIdentifyCheckBox.addItemListener(new ItemListener() {
			// Sets if auto-identify is active
			public void itemStateChanged(ItemEvent e) {
				fingerprintSDKSample
						.setAutoIdentify(e.getStateChange() == ItemEvent.SELECTED);
			}
		});

		// --------------------------------------------
		// Creates the "Auto Extract" checkBox
		JCheckBox autoExtractCheckBox = new JCheckBox("Auto Extract", true); // Default
		// state:
		// Checked
		buttonsPanel.add(autoExtractCheckBox);

		// Aligns this checkbox in the center
		autoExtractCheckBox.setHorizontalAlignment(SwingConstants.CENTER);

		// Adds an event-handler to this CheckBox
		autoExtractCheckBox.addItemListener(new ItemListener() {
			// Sets if auto-extract is active
			public void itemStateChanged(ItemEvent e) {
				fingerprintSDKSample
						.setAutoExtract(e.getStateChange() == ItemEvent.SELECTED);
			}
		});

		// returns the panel with all the buttons.
		return buttonsPanel;
	}

	/**
	 * This method creates the panel placed in the center of the frame, which
	 * contains the image of the fingerprint being processed.
	 */
	private JComponent createFingerprintViewPanel() {
		// Creates a new panel to show the fingerprint image
		fingerprintViewPanel = new JPanel() {
			// Overrides the paintComponent method for painting the image
			public void paintComponent(Graphics g) {
				super.paintComponent(g);

				// If there's a image to be drawn...
				if (fingerprintImage != null) {
					// calculates the size/position of the image being drawn,
					// so it's size is stretched to fill the whole space
					Insets insets = getInsets();
					int transX = insets.left;
					int transY = insets.top;
					int width = getWidth() - getInsets().right
							- getInsets().left;
					int height = getHeight() - getInsets().bottom
							- getInsets().top;

					// draw it!
					g.drawImage(fingerprintImage, transX, transY, width,
							height, null);
				}

			}

		};

		// Adds a border around it
		fingerprintViewPanel.setBorder(new CompoundBorder(new EmptyBorder(2, 2,
				2, 2), new BevelBorder(BevelBorder.LOWERED)));

		return fingerprintViewPanel;
	}

	// #######################################################################
	// ############### End of GUI start-up methods #################
	// #######################################################################

	/**
	 * Sets the current fingerprint image. It is shown on the middle of the
	 * frame.
	 */
	public void showImage(BufferedImage image) {
		// uses the imageProducer to create the fingerprint image
		fingerprintImage = image;
		// Repaint, so that the new image is shown.
		repaint();
	}

	/**
	 * Adds the specified text to the log output
	 */
	public void writeLog(String text) {
		// Appends the text
		logTextArea.append(text + "\n");

		// Auto-scrolls to the last message.
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				// picks the vertical scrollBar, and sets it to the maximum.
				JScrollBar vbar = logScrollPane.getVerticalScrollBar();
				vbar.setValue(vbar.getMaximum());
			}
		});
	}

	/**
	 * This method is called when an image is captured / loaded, int order to
	 * enable "Extract" button and "Save" menu item.
	 * 
	 * "Enroll", "Verify" and "Identify" buttons are disabled, because those
	 * operations require a template, which has not been extracted yet.
	 */
	public void enableImage() {
		saveMenuItem.setEnabled(true);

		extractButton.setEnabled(true);
		enrollButton.setEnabled(false);
		verifyButton.setEnabled(false);
		identifyButton.setEnabled(false);
	}

	/**
	 * This method is called after an template has been extracted from the
	 * current Image, in order to enable "Enroll", "Verify" and "Identify"
	 * buttons
	 */
	public void enableTemplate() {
		enrollButton.setEnabled(true);
		verifyButton.setEnabled(true);
		identifyButton.setEnabled(true);
	}

	/**
	 * This method displayes the Options form and updates Fingerprint SDK with
	 * the new configurations.
	 */
	private void showOptions() {
		// If it's the first time the button is pressed, creates the options
		// from.
		if (optionsForm == null)
			optionsForm = new FormOptions();

		// Load current option values from Fingerprint SDK into the options
		// form.
		optionsForm.setIdentifyThreshold(fingerprintSDKSample
				.getIdentifyThreshold());
		optionsForm.setIdentifyRotationTolerance(fingerprintSDKSample
				.getIdentifyRotationTolerance());
		optionsForm.setVerifyThreshold(fingerprintSDKSample
				.getVerifyThreshold());
		optionsForm.setVerifyRotationTolorance(fingerprintSDKSample
				.getVerifyRotationTolerance());

		// Saves current option values, in case the user presses "Cancel"
		optionsForm.saveState();

		// Shows the options dialog
		JDialog dialog = optionsForm.createDialog(rootPane, "Options");
		dialog.setVisible(true);
		dialog.dispose();

		// Checks if the user has pressed "OK"
		if ((optionsForm.getValue() != null)
				&& ((Integer) optionsForm.getValue()).intValue() == JOptionPane.OK_OPTION) {
			try {
				// Sets the matching parameters on Fingerprint SDK
				fingerprintSDKSample.setParameters(optionsForm
						.getIdentifyThreshold(), optionsForm
						.getIdentifyRotationTolerance(), optionsForm
						.getVerifyThreshold(), optionsForm
						.getVerifyRotationTolorance());

				// Picks the colors to be used when painting each kind of
				// object,
				// or null, when shouldn't paint this kind of object.
				Color minutiaeColor = optionsForm.mustShowMinutiae() ? optionsForm
						.getMinutiaeColor()
						: GrFingerJava.TRANSPARENT_COLOR;
				Color minutiaeMatchColor = optionsForm.mustShowMinutiaeMatch() ? optionsForm
						.getMinutiaeMatchColor()
						: GrFingerJava.TRANSPARENT_COLOR;
				Color segmentColor = optionsForm.mustShowSegment() ? optionsForm
						.getSegmentColor()
						: GrFingerJava.TRANSPARENT_COLOR;
				Color segmentMatchColor = optionsForm.mustShowSegmentMatch() ? optionsForm
						.getSegmentMatchColor()
						: GrFingerJava.TRANSPARENT_COLOR;
				Color directionColor = optionsForm.mustShowDirection() ? optionsForm
						.getDirectionColor()
						: GrFingerJava.TRANSPARENT_COLOR;
				Color directionMatchColor = optionsForm
						.mustShowDirectionMatch() ? optionsForm
						.getDirectionMatchColor()
						: GrFingerJava.TRANSPARENT_COLOR;

				// Sets, on Fingerprint SDK, the colors to be used
				fingerprintSDKSample.setBiometricDisplayColors(minutiaeColor,
						minutiaeMatchColor, segmentColor, segmentMatchColor,
						directionColor, directionMatchColor);

			} catch (NumberFormatException e) {
				// shows an error message if a numeric value can't be parsed
				// (i.e., The user typed "abc" instead of "123")
				JOptionPane.showMessageDialog(rootPane, "Invalid values.",
						"Error", JOptionPane.ERROR_MESSAGE);
			}

			// If the user closed the window / cancelled
		} else {
			// Restores last configurations.
			optionsForm.restoreState();
		}

	}

	/**
	 * This method shows a dialog containing current Fingerprint SDK version
	 * (i.e., Fingerprint SDK 4.2 FULL)
	 */
	private void showVersion() {
		// Gets the version of Fingerprint SDK
		String version = fingerprintSDKSample.getFingerprintSDKVersion();
		// displays it an a message dialog.
		JOptionPane.showMessageDialog(rootPane, version,
				"Fingerprint SDK Version", JOptionPane.PLAIN_MESSAGE);
	}

	/**
	 * Saves the current fingerprint on a Bitmap (.bmp) file.
	 * 
	 * It first shows a fileChooser to let the user choose the destination file.
	 */
	private void saveImage() {
		// Creates a FileChooser, which only accepts Bitmap Image files
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setAcceptAllFileFilterUsed(false);

		// Adds file filters to different image types
		Iterator filterIterator = IIORegistry.getDefaultInstance()
				.getServiceProviders(ImageWriterSpi.class, false);
		while (filterIterator.hasNext()) {
			ImageWriterSpi spi = (ImageWriterSpi) filterIterator.next();
			if (spi.canEncodeImage(fingerprintSDKSample.getFingerprint()))
				fileChooser.setFileFilter(new ImageFileFilter(spi));
		}

		// Shows the fileChooser, and check if the user pressed "ok"
		if (fileChooser.showSaveDialog(rootPane) == JFileChooser.APPROVE_OPTION) {
			// Picks the image writer needed to write the image.
			ImageWriterSpi spi = (ImageWriterSpi) ((ImageFileFilter) fileChooser
					.getFileFilter()).getSpi();

			fingerprintSDKSample.saveToFile(fileChooser.getSelectedFile(), spi);
		}
	}

	/**
	 * Loads a fingerprint Image from a file
	 * 
	 * It first shows a fileChooser to let the user choose which file to load.
	 */
	private void loadFromFile() {
		// Creates a FileChooser, which only accepts image files
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setAcceptAllFileFilterUsed(false);

		// Adds file filters to different image types
		Iterator filterIterator = IIORegistry.getDefaultInstance()
				.getServiceProviders(ImageReaderSpi.class, false);
		while (filterIterator.hasNext()) {
			fileChooser.setFileFilter(new ImageFileFilter(
					(ImageReaderSpi) filterIterator.next()));
		}

		// Shows the fileChooser, and check if the user pressed "ok"
		if (fileChooser.showOpenDialog(rootPane) == JFileChooser.APPROVE_OPTION) {
			// Shows an input dialog asking for the image resolution.
			String resolution = JOptionPane.showInputDialog(rootPane,
					"What is the image resolution?", "Resolution",
					JOptionPane.QUESTION_MESSAGE);

			// If the user did not cancel the resolution dialog....
			if (resolution != null) {
				try {
					// Parses the resolution value typed by the user
					int resolutionValue = Integer.parseInt(resolution);

					// Picks the image reader needed to read the image.
					ImageReaderSpi spi = (ImageReaderSpi) ((ImageFileFilter) fileChooser
							.getFileFilter()).getSpi();

					// Loads the file
					fingerprintSDKSample
							.loadFile(fileChooser.getSelectedFile(),
									resolutionValue, spi);

				} catch (NumberFormatException e1) {
					// If the resolution typed was not a valid number, logs an
					// error.
					writeLog("Invalid resolution.");
				}
			}
		}
	}

	/**
	 * Inner class used to filter image files compatible with the specified
	 * ImageReaderWriterSpi
	 * 
	 * It's used on the "Image->Load from File" and "Image->Save" to allow
	 * selecting different image types.
	 */
	private class ImageFileFilter extends FileFilter {
		// The Image I/O provider which must be used for this filter.
		private ImageReaderWriterSpi spi;

		/**
		 * Creates a new filter that accepts only the given list of file
		 * extensions.
		 */
		public ImageFileFilter(ImageReaderWriterSpi spi) {
			this.spi = spi;
		}

		/**
		 * Returns the Image I/O provider shich must be used to open/write
		 * Images selected with this filter.
		 */
		public ImageReaderWriterSpi getSpi() {
			return spi;
		}

		/**
		 * Returns if the given file must the shown on the listing
		 */
		public boolean accept(File file) {
			// Directories can be shown
			if (file.isDirectory()) {
				return true;
			}

			// check if the file matches any of the supported extensions
			// (Ignores the case)
			for (int i = 0; i < spi.getFileSuffixes().length; i++) {
				if (file.getName().toLowerCase().endsWith(
						spi.getFileSuffixes()[i].toLowerCase())) {
					return true;
				}
			}

			// Didn't match any of the supported formats
			return false;
		}

		/**
		 * Returns the Description of this filter
		 */
		public String getDescription() {
			// uses the description specified by the constructor
			// AND a list of all the extensions supported.
			String description = spi.getDescription(Locale.getDefault());

			// Adds the list of extensions after the description e.g.(*.bmp,
			// *.jpg) .
			description = description + " (";
			for (int i = 0; i < spi.getFileSuffixes().length; i++) {
				// Adds an extension
				description = description + "*." + spi.getFileSuffixes()[i];
				// Adds a comma-separator to the next item, if there will be
				// one.
				if (i < spi.getFileSuffixes().length - 1)
					description = description + ", ";
			}
			description = description + ")";

			return description;
		}
	}
}
