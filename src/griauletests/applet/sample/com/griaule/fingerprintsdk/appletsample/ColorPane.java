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

package com.griaule.fingerprintsdk.appletsample;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * A panel for selecting colors for Regular/Match parameters, and whenever they
 * must be shown.
 *
 * For instance, it's used in the options frame, for choosing
 * the color used to draw the minutiaes the matched,
 * the color used to draw the minutiaes that did not match,
 * and to choose whenever they must be shown of not.
 *
 */
public class ColorPane extends JPanel {

    //The "Buttons" used to select the desired color for regular/matched objects.
    private JPanel canvasColorRegular;
    private JPanel canvasColorMatched;

    //The CheckBoxes used to define whenever regular/matched objects must be shown.
    private JCheckBox checkboxShowRegular;
    private JCheckBox checkBoxShowMatched;


    //Stores the previous value of the regular/matched colors to be used, in case the user presses "Cancel".
    private Color stateColorRegular;
    private Color stateColorMatched;

    //Stores the previous value of whener it must show regular/matched object, in case the user presses "Cancel".
    private boolean stateShowRegular;
    private boolean stateShowMatched;




    /**
     * Creates a new ColorPane, with the given label and default color values.
     */
    public ColorPane(String label, Color colorRegular, Color colorMatched){
        //Adds a border on this panel, with the specified label.
        setBorder(BorderFactory.createTitledBorder(null, label));

        //Creates a grid layout composed of 2 columns:
        GridLayout gridLayout = new GridLayout(1,2,5,5);
        setLayout(gridLayout);

        //Adds the color selection buttons on the left column
        add(createChooserPanel(colorRegular,colorMatched));
        //Adds this label to the right side of this panel
        add(new JLabel("Double click the color to change it."));
    }

    /**
     * Saves the values chosen by the user, so that they can be actually used.
     *
     * This is called after the "Ok" button is pressed on the Options Form.
     */
    public void saveState(){
        //Store current values on the state variables.
        stateColorRegular = getColorRegular();
        stateColorMatched = getColorMatched();
        stateShowRegular = mustShowRegular();
        stateShowMatched = mustShowMatched();
    }

    /**
     * Restores configurations to their previous values.
     *
     * This is called after the "Cancel" button is pressed on the Options Form.
     */
    public void restoreState(){
        //Restore state values into current variables.
        canvasColorRegular.setBackground(stateColorRegular);
        canvasColorMatched.setBackground(stateColorMatched);
        checkboxShowRegular.setSelected(stateShowRegular);
        checkBoxShowMatched.setSelected(stateShowMatched);
    }

    /**
     * Creates the panel used to choose the object's color and
     * to choose if they must be shown.
     */
    private JPanel createChooserPanel(Color colorRegular, Color colorMatched){
        //Creates the new Panel, with 2 rows and 3 columns:
        //On the first row, parameters for regular objects,
        //on the second, parameters for matched objects.
        //On the first column, a "Regular"/"Matched" Label,
        //on the second, the color picker, and, on the third,
        //a checkbox to choose if objects of that kind must be shown
        JPanel panel = new JPanel();
        GridLayout gridLayout = new GridLayout(2,3,5,5);
        panel.setLayout(gridLayout);


        //Line 1: Regular objects parameters.
        panel.add(new JLabel("Regular:"));

        //Creates the button to pick the desired color.
        canvasColorRegular = new JPanel();
        canvasColorRegular.setBackground(colorRegular);
        panel.add(canvasColorRegular);

        //Creates the checkBox used to select if objects must be shown.
        checkboxShowRegular = new JCheckBox("Show",true);
        panel.add(checkboxShowRegular);


        //Line 2: Matched objects parameters.
        panel.add(new JLabel("Match:"),null);

        //Creates the button to pick the desired color.
        canvasColorMatched = new JPanel();
        canvasColorMatched.setBackground(colorMatched);
        panel.add(canvasColorMatched);

        //Creates the checkBoxes used to select if objects must be shown
        checkBoxShowMatched = new JCheckBox("Show",true);
        panel.add(checkBoxShowMatched);





        //Creates a MouseListener to receive events when the user click
        //the color selection buttons.
        MouseListener colorChooserMouseListener = new MouseAdapter() {
            //Handles clicks happen
            public void mouseClicked(MouseEvent e) {
                //Checks if it is a double-click
                if (e.getClickCount() == 2){
                    //Pick the button which has been pressed.
                    JComponent c = (JComponent) e.getSource();

                    //Shows a dialog to select the new color.
                    Color newColor = JColorChooser.showDialog(getParent(),"Color",c.getBackground());

                    //If the user did not press "Cancel" (newColor != null)
                    if (newColor!=null)
                        //Uses the new color.
                        c.setBackground(newColor);
                }
            }
        };
        //Adds the mouseListener to the color selection buttons.
        canvasColorRegular.addMouseListener(colorChooserMouseListener);
        canvasColorMatched.addMouseListener(colorChooserMouseListener);


        //Returns the assembled panel
        return panel;
    }




    /**Returns the color that must be used to paint regular objects.*/
    public Color getColorRegular(){
        return canvasColorRegular.getBackground();
    }
    /**Returns the color that must be used to paint matched objects.*/
    public Color getColorMatched(){
        return canvasColorMatched.getBackground();
    }

    /**Returns whenever regular objects must be painted.*/
    public boolean mustShowRegular(){
        return checkboxShowRegular.isSelected();
    }
    /**Returns whenever matched objects must be painted.*/
    public boolean mustShowMatched(){
        return checkBoxShowMatched.isSelected();
    }
}
