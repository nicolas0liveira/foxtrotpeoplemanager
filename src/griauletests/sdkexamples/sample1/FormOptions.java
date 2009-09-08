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

package griauletests.sdkexamples.sample1;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * The options form.
 * Here, you can configure Identification / Verification
 * parameters (Rotation Tolerance and Threshold) and which colors
 * to use when painting a fingerprint.
 */
public class FormOptions extends JOptionPane {

    //The panels used to select the colors used to paint fingerprints.
    private ColorPane colorPaneMinutias = null;
    private ColorPane colorPaneSegments = null;
    private ColorPane colorPaneDirections = null;

    //The textfields containing identification/verification parameters.
    private JTextField identifyThresholdTextField = null;
    private JTextField identifyRotationToleranceTextField = null;
    private JTextField verifyThresholdTextField = null;
    private JTextField verifyRotationToleranceTextField = null;


    /**
     * Creates a new Options form.
     */
    public FormOptions() {
        //It extends a Dialog with Ok/Cancel buttons
        super(null, JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION);

        //Sets the content of this dialog
        setMessage(createContentPane());
    }

    /**
     * Creates the root pane of this dialog (excluding Ok/Cancel buttons)
     */
    private JPanel createContentPane() {
        //Creates a new root panel
        JPanel contentPane = new JPanel(new BorderLayout());

        //Adds the Verify/Identify configurations on the upper part of the panel
        contentPane.add(createVerifyIdentifyPanel(), BorderLayout.NORTH);
        //Adds the color choosers in the central part of the panel
        contentPane.add(createColorSelectionPanel(), BorderLayout.CENTER);

        return contentPane;
    }

    /**
     * Creates the upper part of the dialog, with the verification and
     * identification configurations.
     */
    private JPanel createVerifyIdentifyPanel () {
        //Creates a panel split in 2 columns
        JPanel verifyIdentifyPanel = new JPanel(new GridLayout(1,2,10,0));

        //Adds the verify configurations on one side...
        verifyIdentifyPanel.add(createIdentifyPanel());
        //and the identify configurations on the other
        verifyIdentifyPanel.add(createVerifyPanel());

        return verifyIdentifyPanel;
    }


    /**
     * Creates the verify configuration panel.
     *
     * It's where you can configure the Threshold and
     * the Rotation tolerance for verifications
     */
    private JPanel createVerifyPanel() {
        JPanel verifyPanel = new JPanel( new GridLayout(2,2,5,5) );

        //Adds a border with "Verify" as caption.
        verifyPanel.setBorder(BorderFactory.createTitledBorder("Verify"));

        //Creates the textboxes where the user will type the rotation tolerance / threshold.
        verifyThresholdTextField         = new JTextField();
        verifyRotationToleranceTextField = new JTextField();

        //Adds the TextBoxes to this panel and their labels to this panel.
        verifyPanel.add(new JLabel("Threshold"));
        verifyPanel.add(verifyThresholdTextField);
        verifyPanel.add(new JLabel("Rotation tolerance"));
        verifyPanel.add(verifyRotationToleranceTextField);

        return verifyPanel;
    }


    /**
     * Creates the identify configuration panel.
     *
     * It's where you can configure the Threshold and
     * the Rotation tolerance for Identifications
     */
    private JPanel createIdentifyPanel() {
        JPanel identifyPanel = new JPanel( new GridLayout(2,2,5,5) );

        //Adds a border with "Verify" as caption.
        identifyPanel.setBorder(BorderFactory.createTitledBorder("identify"));

        //Creates the textboxes where the user will type the rotation tolerance / threshold.
        identifyThresholdTextField         = new JTextField();
        identifyRotationToleranceTextField = new JTextField();

        //Adds the TextBoxes to this panel and their labels to this panel.
        identifyPanel.add(new JLabel("Threshold"));
        identifyPanel.add(identifyThresholdTextField);
        identifyPanel.add(new JLabel("Rotation tolerance"));
        identifyPanel.add(identifyRotationToleranceTextField);

        return identifyPanel;
    }


    /**
     * Creates the color configuration panel.
     *
     * In it you can choose whenever/with which color
     * minutiaes, segments and directions will be painted.
     *
     * They hold different values for "commom" and "matching".
     */
    private JPanel createColorSelectionPanel(){
        //Creates the color selection panel.
        //Sun-components will be distributed in a single column
        JPanel colorSelectionPanel = new JPanel(new GridLayout(0,1));

        //Creates Color selection panes for each kind of
        //object that can be painted (minutiaes, segments and directions)
        colorPaneMinutias   = new ColorPane("Minutiae Colors"          ,Color.BLUE  ,Color.MAGENTA);
        colorPaneSegments   = new ColorPane("Segments Colors"          ,Color.GREEN ,Color.MAGENTA);
        colorPaneDirections = new ColorPane("Minutiae Direction Colors",Color.RED   ,Color.MAGENTA);

        //Adds those panes.
        colorSelectionPanel.add(colorPaneMinutias);
        colorSelectionPanel.add(colorPaneSegments);
        colorSelectionPanel.add(colorPaneDirections);

        return colorSelectionPanel;

    }



    /**
     * Restores last configuration for minutiae/segment/direction colors.
     *
     * Identify/Verify parameters are not restored because they're always
     * reloaded from Fingerprint SDK before invoking the dialog.
     */
    public void restoreState() {
        colorPaneMinutias.restoreState();
        colorPaneSegments.restoreState();
        colorPaneDirections.restoreState();
    }

    /**
     * Saves the current configuration for minutiae/segment/direction colors
     *
     * Identify/Verify parameters are saved because they're always
     * reloaded from Fingerprint SDK before invoking the dialog.
     */
    public void saveState() {
        colorPaneMinutias.saveState();
        colorPaneSegments.saveState();
        colorPaneDirections.saveState();
    }


    //#######################################################################
    //######### Here comes all the Get / Set methods        #################
    //#######################################################################


    //Methods for getting Identification/Verification configurations
    public int getIdentifyThreshold() throws NumberFormatException{
        return Integer.parseInt(identifyThresholdTextField.getText());
    }
    public int getIdentifyRotationTolerance() throws NumberFormatException{
        return Integer.parseInt(identifyRotationToleranceTextField.getText());
    }
    public int getVerifyThreshold() throws NumberFormatException{
        return Integer.parseInt(verifyThresholdTextField.getText());
    }
    public int getVerifyRotationTolorance() throws NumberFormatException{
        return Integer.parseInt(verifyRotationToleranceTextField.getText());
    }


    //Methods for setting Identification/Verification configurations
    public void setIdentifyThreshold(int threshold) {
        identifyThresholdTextField.setText(Integer.toString(threshold));
    }
    public void setIdentifyRotationTolerance(int rotationTolerance) {
        identifyRotationToleranceTextField.setText(Integer.toString(rotationTolerance));
    }
    public void setVerifyThreshold(int threshold) {
        verifyThresholdTextField.setText(Integer.toString(threshold));
    }
    public void setVerifyRotationTolorance(int rotationTolerance) {
        verifyRotationToleranceTextField.setText(Integer.toString(rotationTolerance));
    }





    //Methods for retrieving if / with which color minutiaes,
    //segments and directions will be painted, and if they
    //must be painted.
    public Color getMinutiaeColor(){
        return colorPaneMinutias.getColorRegular();
    }
    public Color getMinutiaeMatchColor(){
        return colorPaneMinutias.getColorMatched();
    }
    public boolean mustShowMinutiae(){
        return colorPaneMinutias.mustShowRegular();
    }
    public boolean mustShowMinutiaeMatch(){
        return colorPaneMinutias.mustShowMatched();
    }
    public Color getSegmentColor(){
        return colorPaneSegments.getColorRegular();
    }
    public Color getSegmentMatchColor(){
        return colorPaneSegments.getColorMatched();
    }
    public boolean mustShowSegment(){
        return colorPaneSegments.mustShowRegular();
    }
    public boolean mustShowSegmentMatch(){
        return colorPaneSegments.mustShowMatched();
    }
    public Color getDirectionColor(){
        return colorPaneDirections.getColorRegular();
    }
    public Color getDirectionMatchColor(){
        return colorPaneDirections.getColorMatched();
    }
    public boolean mustShowDirection(){
        return colorPaneDirections.mustShowRegular();
    }
    public boolean mustShowDirectionMatch(){
        return colorPaneDirections.mustShowMatched();
    }


}
