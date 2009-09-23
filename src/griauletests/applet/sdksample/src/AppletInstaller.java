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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.griaule.grfingerjava.GrFingerJava;
import com.griaule.grfingerjava.GrFingerJavaException;

/**
 * This class is used to copy all native libraries used by Fingerprint SDK and
 * load them into the Java VM.
 */
public class AppletInstaller {

	/** Code for Microsoft Windows. */
	private static final int WINDOWS_OS = 0;
	/** Code for Linux */
	private static final int LINUX_OS = 1;
	/** Code for Unknown OS. */
	private static final int UNKNOWN_OS = 2;

	/** Flag which indicates if Fingerprint SDK has already been installed. */
	private static boolean installed = false;

	/**
	 * Copies Fingerprint SDK native libraries placed on the specified URL to a
	 * local folder and make them available to thr JVM.
	 */
	public synchronized static void install(URL ZipFile) throws IOException,
			UnsupportedOperationException, GrFingerJavaException {

		// Skips re-installation.
		if (installed)
			return;

		// Create temporary folder as the destination of the native
		// libraries/license file
		File destDir = File.createTempFile("FingerprintSDKApplet", "tmp");
		destDir.delete();
		destDir.mkdirs();
		destDir.deleteOnExit();

		// Extracts the contents of the zip file
		ZipInputStream zipStream = new ZipInputStream(ZipFile.openStream());
		ZipEntry zipEntry;
		while ((zipEntry = zipStream.getNextEntry()) != null) {
			File f = new File(destDir, zipEntry.getName());
			if (zipEntry.isDirectory()) {
				f.mkdirs();
			} else {
				OutputStream out = new FileOutputStream(f);

				byte[] buffer = new byte[4096];
				while (true) {
					int bytesRead = zipStream.read(buffer);
					if (bytesRead == -1)
						break;
					out.write(buffer, 0, bytesRead);
				}
				out.close();
			}
		}

		GrFingerJava.setNativeLibrariesDirectory(destDir);
		GrFingerJava.setLicenseDirectory(destDir);
		installed = true;
	}

	/**
	 * Finds out which OS / Plataform this JVM is being runned on.
	 */
	private static int getOS() {
		// Loads the OS name / CPu architecture from system properties.
		String osName = System.getProperty("os.name");
		String arch = System.getProperty("os.arch");

		// Asserts this is a x86 compatible machine.
		// Fingerprint SDK is currently supported on this CPU architecture.
		if ((arch.equals("x86")) || (arch.equals("i386"))
				|| (arch.equals("i486")) || (arch.equals("i586"))
				|| (arch.equals("i686"))) {

			// Selects one of the two different supported OS: Win/Linux
			if (osName.startsWith("Windows"))
				return WINDOWS_OS;
			if (osName.startsWith("Linux"))
				return LINUX_OS;
		}

		// OS has not been recognized (not supported)
		return UNKNOWN_OS;
	}

}