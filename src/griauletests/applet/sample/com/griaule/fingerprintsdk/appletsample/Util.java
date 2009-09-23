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

package griauletests.applet.sample.com.griaule.fingerprintsdk.appletsample;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriter;
import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.spi.ImageWriterSpi;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;

import com.griaule.grfingerjava.FingerprintImage;
import com.griaule.grfingerjava.GrFingerJava;
import com.griaule.grfingerjava.GrFingerJavaException;
import com.griaule.grfingerjava.IFingerEventListener;
import com.griaule.grfingerjava.IImageEventListener;
import com.griaule.grfingerjava.IStatusEventListener;
import com.griaule.grfingerjava.MatchingContext;
import com.griaule.grfingerjava.Template;

/**
 * Class responsible for handling Fingerprint SDK.
 * 
 * It handles fingerprint loading/capturing, template extraction, fingerprint
 * image saving and storing/retrieving from template base.
 */
public class Util implements IStatusEventListener, IImageEventListener,
		IFingerEventListener {

	/**
	 * Fingerprint SDK context used for capture / extraction / matching of
	 * fingerprints.
	 */
	private MatchingContext fingerprintSDK;
	/** User interface, where logs, images and other things will be sent. */
	private FormMain ui;

	/** Sets if template must be automatically extracted after capture. */
	private boolean autoExtract = true;
	/**
	 * Sets if template must be automatically identified after capture. It's
	 * only effective when *autoExtract == true)
	 */
	private boolean autoIdentify = false;

	/** The last fingerprint image acquired. */
	private FingerprintImage fingerprint;
	/** The template extracted from the last acquired image. */
	private Template template;

	/**
	 * Creates a new Util to be used by the specified Main Form.
	 * 
	 * Initializes fingerprint capture and database connection.
	 */
	public Util(FormMain ui) {
		this.ui = ui;

		// Initializes DB connection
		initDB();
		// Initializes Fingerprint SDK and enables fingerprint capture.
		initFingerprintSDK();
	}

	/**
	 * Stops fingerprint capture and closes the database connection.
	 */
	public void destroy() {
		destroyFingerprintSDK();
		destroyDB();
	}

	/**
	 * Initializes Fingerprint SDK and enables fingerprint capture.
	 */
	private void initFingerprintSDK() {
		try {
			// install Fingerprint SDK files in a temporary directory
			AppletInstaller.install(getClass().getResource(
					"/FingerprintSDKLibs.zip"));

			fingerprintSDK = new MatchingContext();
			// Starts fingerprint capture.
			GrFingerJava.initializeCapture(this);

			ui.writeLog("**Fingerprint SDK Initialized Successfull**");

		} catch (Exception e) {
			// If any error ocurred while initializing Fingerprint SDK,
			// writes the error to log
			ui.writeLog(e.getMessage());
		}
	}

	/**
	 * Stops fingerprint capture.
	 */
	private void destroyFingerprintSDK() {
		try {
			GrFingerJava.finalizeCapture();
		} catch (GrFingerJavaException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This function is called every time a fingerprint reader is plugged.
	 * 
	 * @see griaule.grFinger.StatusCallBack#onPlug(java.lang.String)
	 */
	public void onSensorPlug(String idSensor) {
		// Logs the sensor has been pluged.
		ui.writeLog("Sensor: " + idSensor + ". Event: Plugged.");
		try {
			// Start capturing from plugged sensor.
			GrFingerJava.startCapture(idSensor, this, this);
		} catch (GrFingerJavaException e) {
			// write error to log
			ui.writeLog(e.getMessage());
		}
	}

	/**
	 * This function is called every time a fingerprint reader is unplugged.
	 * 
	 * @see griaule.grFinger.StatusCallBack#onUnplug(java.lang.String)
	 */
	public void onSensorUnplug(String idSensor) {
		// Logs the sensor has been unpluged.
		ui.writeLog("Sensor: " + idSensor + ". Event: Unplugged.");
		try {
			GrFingerJava.stopCapture(idSensor);
		} catch (GrFingerJavaException e) {
			ui.writeLog(e.getMessage());
		}
	}

	/**
	 * This function is called every time a fingerfrint image is captured.
	 * 
	 * @see griaule.grFinger.ImageCallBack#onImage(java.lang.String,
	 *      griaule.grFinger.FingerprintImage)
	 */
	public void onImageAcquired(String idSensor, FingerprintImage fingerprint) {
		// Logs that an Image Event occurred.
		ui.writeLog("Sensor: " + idSensor + ". Event: Image Captured.");

		// Stores the captured Fingerprint Image
		this.fingerprint = fingerprint;

		// Display fingerprint image
		ui.showImage(fingerprint);

		// now we have a fingerprint, the ONLY thing we can do is to extract the
		// template.
		ui.enableImage();

		// If auto-extraction is enabled, let's extract the image.
		if (autoExtract) {
			extract();

			// If auto-Identify is also enabled, let's identify it.
			if (autoIdentify)
				identify();
		}

	}

	/**
	 * This Function is called every time a finger is placed on sensor.
	 * 
	 * @see griaule.grFinger.FingerCallBack#onFingerDown(java.lang.String)
	 */
	public void onFingerDown(String idSensor) {
		// Just signals that a finger event ocurred.
		ui.writeLog("Sensor: " + idSensor + ". Event: Finger Placed.");

	}

	/**
	 * This Function is called every time a finger is removed from sensor.
	 * 
	 * @see griaule.grFinger.FingerCallBack#onFingerUp(java.lang.String)
	 */
	public void onFingerUp(String idSensor) {
		// Just signals that a finger event ocurred.
		ui.writeLog("Sensor: " + idSensor + ". Event: Finger Removed.");

	}

	/**
	 * Sets the colors used to paint templates.
	 */
	public void setBiometricDisplayColors(Color minutiaeColor,
			Color minutiaeMatchColor, Color segmentColor,
			Color segmentMatchColor, Color directionColor,
			Color directionMatchColor) {
		try {
			// set new colors for BiometricDisplay
			GrFingerJava.setBiometricImageColors(minutiaeColor,
					minutiaeMatchColor, segmentColor, segmentMatchColor,
					directionColor, directionMatchColor);

		} catch (GrFingerJavaException e) {
			// write error to log
			ui.writeLog(e.getMessage());
		}
	}

	/**
	 * Returns a String containing information about the version of Fingerprint
	 * SDK being used.
	 * 
	 * For instace: -------------------------------- Fingerprint SDK version
	 * 5.0. The license type is 'Identification'.
	 * --------------------------------
	 */
	public String getFingerprintSDKVersion() {
		try {
			return "Fingerprint SDK version "
					+ GrFingerJava.getMajorVersion()
					+ "."
					+ GrFingerJava.getMinorVersion()
					+ "\n"
					+ "License type is '"
					+ (GrFingerJava.getLicenseType() == GrFingerJava.GRFINGER_JAVA_FULL ? "Identification"
							: "Verification") + "'.";

		} catch (GrFingerJavaException e) {
			return null;
		}
	}

	/**
	 * returns the current fingerprint image, without any biometric information.
	 */
	public BufferedImage getFingerprint() {
		return this.fingerprint;
	}

	/**
	 * Saves the fingerprint image to a file using an ImageWriterSpi. See
	 * ImageIO API.
	 */
	public void saveToFile(File file, ImageWriterSpi spi) {
		try {
			// Creates a image writer.
			ImageWriter writer = spi.createWriterInstance();
			ImageOutputStream output = ImageIO.createImageOutputStream(file);
			writer.setOutput(output);

			// Writes the image.
			writer.write(fingerprint);

			// Closes the stream.
			output.close();
			writer.dispose();
		} catch (IOException e) {
			// write error to log
			ui.writeLog(e.toString());
		}

	}

	/**
	 * Loads a fingerprint image from file using an ImageReaderSpi. See ImageIO
	 * API.
	 */
	public void loadFile(File file, int resolution, ImageReaderSpi spi) {
		try {
			// Creates a image reader.
			ImageReader reader = spi.createReaderInstance();
			ImageInputStream input = ImageIO.createImageInputStream(file);
			reader.setInput(input);
			// Reads the image.
			BufferedImage img = reader.read(0);
			// Close the stream
			reader.dispose();
			input.close();
			// creates and processes the fingerprint image
			onImageAcquired("File", new FingerprintImage(img, resolution));
		} catch (Exception e) {
			// write error to log
			ui.writeLog(e.toString());
		}
	}

	/**
	 * Sets the parameters used for identifications / verifications.
	 */
	public void setParameters(int identifyThreshold,
			int identifyRotationTolerance, int verifyThreshold,
			int verifyRotationTolorance) {
		try {
			fingerprintSDK.setIdentificationThreshold(identifyThreshold);
			fingerprintSDK
					.setIdentificationRotationTolerance(identifyRotationTolerance);
			fingerprintSDK
					.setVerificationRotationTolerance(verifyRotationTolorance);
			fingerprintSDK.setVerificationThreshold(verifyThreshold);

		} catch (GrFingerJavaException e) {
			// write error to log
			ui.writeLog(e.getMessage());
		}
	}

	/**
	 * Returns the current verification threshold.
	 */
	public int getVerifyThreshold() {
		try {
			// Try to get the parameters from Fingerprint SDK.
			return fingerprintSDK.getVerificationThreshold();
		} catch (GrFingerJavaException e) {
			// If fails to load the parameters, writes error to log and returns
			// 0
			ui.writeLog(e.getMessage());
			return 0;
		}
	}

	/**
	 * Returns the current rotation tolerance on verifications.
	 */
	public int getVerifyRotationTolerance() {
		try {
			// Try to get the parameters from Fingerprint SDK.
			return fingerprintSDK.getVerificationRotationTolerance();
		} catch (GrFingerJavaException e) {
			// If fails to load the parameters, writes error to log and returns
			// 0
			ui.writeLog(e.getMessage());
			return 0;
		}
	}

	/**
	 * Returns the current identification threshold.
	 */
	public int getIdentifyThreshold() {
		try {
			// Try to get the parameters from Fingerprint SDK.
			return fingerprintSDK.getIdentificationThreshold();
		} catch (GrFingerJavaException e) {
			// If fails to load the parameters, writes error to log and returns
			// 0
			ui.writeLog(e.getMessage());
			return 0;
		}
	}

	/**
	 * Returns the current rotation tolerance on identification.
	 */
	public int getIdentifyRotationTolerance() {
		try {
			// Try to get the parameters from Fingerprint SDK.
			return fingerprintSDK.getIdentificationRotationTolerance();
		} catch (GrFingerJavaException e) {
			// If fails to load the parameters, writes error to log and returns
			// 0
			ui.writeLog(e.getMessage());
			return 0;
		}
	}

	/**
	 * Enables / Disables automatic fingerprint identification after capture.
	 * 
	 * As identification must be done after template extraction, this property
	 * will only be effective if autoExtract if set to true.
	 */
	public void setAutoIdentify(boolean state) {
		autoIdentify = state;
	}

	/**
	 * Enables / Disables automatic fingerprint extraction after capture.
	 */
	public void setAutoExtract(boolean state) {
		autoExtract = state;
	}

	/** The applet version of this sample stores all fingerprints in memory. */
	private List database;

	/**
	 * Initializes the database.
	 */
	private void initDB() {
		// On the applet sample, the database is stored on a in-memory list.
		database = new ArrayList();
	}

	/**
	 * Closes the connection to the database and frees any resources used.
	 */
	private void destroyDB() {
		// On the applet sample, no database connection is used.
	}

	/**
	 * Add the current fingerprint template to the DB.
	 */
	public void enroll() {
		// Inserts the template on the database
		database.add(template);
		ui.writeLog("Fingerprint enrolled with id = " + (database.size() - 1));
	}

	/**
	 * Check current fingerprint against another one in the DB.
	 */
	public void verify(int id) {
		try {
			// Gets the template with supplied ID from database.
			Template referenceTemplate = (Template) database.get(id);

			// Compares the templates.
			boolean matched = fingerprintSDK
					.verify(template, referenceTemplate);

			// If the templates match, display matching
			// minutiae/segments/directions
			if (matched) {
				// displays minutiae/segments/directions that matched.
				ui.showImage(GrFingerJava.getBiometricImage(template,
						fingerprint, fingerprintSDK));
				// Notifies the templates did match.
				ui.writeLog("Matched with score = " + fingerprintSDK.getScore()
						+ ".");
			} else {
				// Notifies the templates did not match.
				ui.writeLog("Did not match with score = "
						+ fingerprintSDK.getScore() + ".");
			}
		} catch (IndexOutOfBoundsException e) {
			// Invalid ID was typed.
			ui.writeLog("The suplied ID does not exists.");
		} catch (GrFingerJavaException e) {
			// write error to log
			ui.writeLog(e.getMessage());
		}
	}

	/**
	 * Identifies the current fingerprint on the DB.
	 */
	public void identify() {
		try {
			// Starts identification process by supplying query template.
			fingerprintSDK.prepareForIdentification(this.template);

			// Iterate over all templates in database
			for (int i = 0; i < database.size(); i++) {
				// Picks the current template
				Template referenceTemplate = (Template) database.get(i);

				// Compares current template.
				boolean matched = fingerprintSDK.identify(referenceTemplate);

				// If the templates match, display matching
				// minutiae/segments/directions.
				if (matched) {
					// displays minutiae/segments/directions that matched.
					ui.showImage(GrFingerJava.getBiometricImage(template,
							fingerprint, fingerprintSDK));
					// Notifies the template was identified.
					ui.writeLog("Fingerprint identified. ID = " + i
							+ ". Score = " + fingerprintSDK.getScore() + ".");

					// Stops searching
					return;
				}
			}

			// If all templates on the DB have been compared, and none of them
			// match, notifies it has not been found.
			ui.writeLog("Fingerprint not found.");

		} catch (GrFingerJavaException e) {
			// write error to log
			ui.writeLog(e.getMessage());
		}

	}

	/**
	 * Extract a fingerprint template from current image.
	 */
	public void extract() {

		try {
			// Extracts a template from the current fingerprint image.
			template = fingerprintSDK.extract(fingerprint);

			// Notifies it has been extracted and the quality of the extraction
			String msg = "Template extracted successfully. ";
			// write template quality to log
			switch (template.getQuality()) {
			case Template.HIGH_QUALITY:
				msg += "High quality.";
				break;
			case Template.MEDIUM_QUALITY:
				msg += "Medium quality.";
				break;
			case Template.LOW_QUALITY:
				msg += "Low quality.";
				break;
			}
			ui.writeLog(msg);

			// Notifies the UI that template operations can be enabled.
			ui.enableTemplate();
			// display minutiae/segments/directions into image
			ui.showImage(GrFingerJava.getBiometricImage(template, fingerprint));

		} catch (GrFingerJavaException e) {
			// write error to log
			ui.writeLog(e.getMessage());
		}

	}

	/**
	 * Removes all templates from the database.
	 */
	public void clearDB() {
		database.clear();
		ui.writeLog("Database is clear...");
	}
}
