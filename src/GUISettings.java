// Alex Eidt
// Basketball Statistics Tracking Program
// GUISettings
// The GUISettings are used to format Components to keep a consistent style throughout the
// program.

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.text.*;

@SuppressWarnings("serial")
public abstract class GUISettings extends BasketballMain {
	
	// Post: Returns a JLabel with the given 'text' and 'fontSize' while using the colors
    //       given by the 'box' GameSettings.
    protected static JLabel formatLabel(String text, int fontSize, GameSettings settings) {
    	JLabel label = new JLabel(text, JLabel.CENTER);
        label.setForeground((Color) settings.getSetting(Setting.FONT_COLOR));
        label.setFont(new Font(DEFAULT_FONT_TYPE, Font.BOLD, fontSize));
        return label;
    }
    
    // Post: Formats the given 'frame' with the parameters given.
    protected static void formatFrame(JFrame frame, Component panel, int width, int height) {
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(width, height);
        frame.setLocationRelativeTo(null);
        frame.add(panel);
        // Add logo here
        try {
			//frame.setIconImage(ImageIO.read(new File("logo.jpg")));
		} catch (Exception e) {
			System.out.println("Icon not found.");
		}
    }
    
    // Post: Formats a JButton with the given width, height, font size and colors/font type given in the 'settings'.
    //       Every button is formatted using this method.
    protected static void formatButton(JButton button, int width, int height, int fontSize, GameSettings settings) {
        button.setPreferredSize(new Dimension(width, height));
        button.setFont(new Font(DEFAULT_FONT_TYPE, Font.BOLD, fontSize));
        button.setForeground((Color) settings.getSetting(Setting.FONT_COLOR));
        button.setBackground((Color) settings.getSetting(Setting.BACKGROUND_COLOR));
        button.setBorder(new LineBorder(DEFAULT_TEXT_BORDER_COLOR, 6, ROUNDED_BORDERS));
    	Font originalFont = button.getFont();
    	// Hover Effect
        button.getModel().addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                if (button.getModel().isRollover()) { 
                	button.setFont(new Font(DEFAULT_FONT_TYPE, Font.BOLD, fontSize + 3));
                } else {
                    button.setFont(originalFont);
                }
            }
        });
    }
    
    // Post: Returns an array of JButton with names given by 'buttonNames' and dimensions given by 'size'.
    protected static JButton[] createButtonArray(String[] buttonNames, int[] size, GameSettings settings) {
    	JButton[] list = new JButton[buttonNames.length];
    	for (int i = 0; i < buttonNames.length; i++) {
    		JButton btn = new JButton(buttonNames[i]);
    		formatButton(btn, getDimension(formatLabel(buttonNames[i], size[i] + 8, settings), Dim.WIDTH) + ICON_SIZE,
    				          Math.max(getDimension(formatLabel(buttonNames[i], size[i], settings), Dim.HEIGHT), BUTTON_HEIGHT),
    				          size[i], settings);
    		list[i] = btn;
    	}
    	return list;
    }
    
    // Post: Returns a JTextArea formatted using 'settings' with given 'width' and 'height'.
    public static JTextArea formatTextArea(int width, int height, GameSettings settings) {
    	JTextArea area = new JTextArea();
    	area.setSize(width, height);
        area.setEditable(false);
        area.setForeground((Color) settings.getSetting(Setting.FONT_COLOR));
        area.setBackground((Color) settings.getSetting(Setting.BACKGROUND_COLOR));
        area.setFont(new Font(DEFAULT_FONT_TYPE, Font.BOLD, 35));
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        return area;
    }
    
    // Post: Formats the given JPanel 'panel'.
    public static void formatPanel(JPanel panel, Component[] components, int[] borderDimension,
    		                       LayoutManager layout, Color borderColor, Color bckgrnd) {
    	// Add components to panel
    	for (Component component : components) {
    		panel.add(component);
    	}
    	// Set layout of panel
    	if (layout == null) {
    		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    	} else {
        	panel.setLayout(layout);
    	}
    	// Set border of panel if necessary
    	if (borderDimension != null) {
    		panel.setBorder(new MatteBorder(borderDimension[0], borderDimension[1], 
    				                        borderDimension[2], borderDimension[3], borderColor));
    	}
    	panel.setBackground(bckgrnd);
    }
    
    // Post: Returns a JScrollPane for the given JPanel 'panel'.
    public static JScrollPane addScrollPane(JPanel panel) {
    	JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(SCREENWIDTH, SCREENHEIGHT));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        // Set Default View position to start at the top of the panel
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                scrollPane.getViewport().setViewPosition(new Point(0, 0));
            }
        });	
        return scrollPane;
    }
    
    // Post: Creates a pop-up window with the given 'text'.
    protected static void confirmPanel(String text, String frameText, int fontSize, GameSettings box) {
    	Color background = (Color) box.getSetting(Setting.BACKGROUND_COLOR);
        JFrame frame = new JFrame(frameText); 
        
        JPanel askPanel = new JPanel();
        askPanel.setBackground(background);
        
        JTextPane ask = formatTextPane(text, fontSize, box);  
        askPanel.setBorder(new MatteBorder(20, 20, 20, 20, background));
        askPanel.add(ask, Alignment.CENTER);
        askPanel.setLayout(new GridLayout(1, 1));
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(background);
        noButton(frame, buttonPanel, fontSize - 5, 150, 80, "Okay", box, false);
        
        JPanel panels = new JPanel();
        panels.add(askPanel);
        panels.add(buttonPanel);
        panels.setBackground(background);
        panels.setLayout(new BoxLayout(panels, BoxLayout.Y_AXIS));
        formatFrame(frame, panels, SCREENWIDTH / 2, (int) (SCREENHEIGHT / 2.5));
    }
    
    // Post: Returns a JTextPane with the given 'text' and 'fontSize'.
    protected static JTextPane formatTextPane(String text, int fontSize, GameSettings box) {
        JTextPane pane = new JTextPane();
        pane.setEditable(false);
        pane.setFont(new Font(DEFAULT_FONT_TYPE, Font.PLAIN, fontSize));
        pane.setBackground((Color) box.getSetting(Setting.BACKGROUND_COLOR));
        StyledDocument doc = pane.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);
        Style style = pane.addStyle("", null);
        StyleConstants.setForeground(style, DEFAULT_FONT_COLOR);
		try {
			doc.insertString(doc.getLength(), text, style);
		} catch (BadLocationException e1) {
			e1.printStackTrace();
		}   
		return pane;
    }
    
    // Post: Adds a button that closes the given JFrame 'frame'.
    protected static void noButton(JFrame frame, JPanel panel, int fontsize, int width, int height, 
    		                       String text, GameSettings box, boolean isSettings) {
        JButton no = new JButton(text);
        formatButton(no, width, height, fontsize, box);
        if (isSettings) {
        	no.setBackground(DEFAULT_BACKGROUND_COLOR);
        }
        no.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });
        panel.add(no);
    }
    
    enum Dim {
    	HEIGHT,
    	WIDTH;
    }
    // Post: Returns the width if d = Dim.WIDTH and height if d = Dim.HEIGHT
    //       of the 'component'.
    protected static int getDimension(Component component, Dim d) {
    	JFrame frame = new JFrame();
    	frame.add(component);
    	frame.pack();
    	int height = frame.getHeight();
    	int width = frame.getWidth();
    	frame.dispose();
    	frame = null;
    	switch (d) {
    	case HEIGHT:
    		return height;
    	case WIDTH:
    		return width;
    	}
    	return -1;
    }
    
    // Post: Adds an Icon with file name 'image' to the given button at index 'index'
    //       in 'buttons'.
    protected void setIcon(JButton[] buttons, int index, String image) {
    	try {
        	Image icon = new ImageIcon(this.getClass().getResource(image)).getImage();
        	Image newImage = icon.getScaledInstance(ICON_SIZE, ICON_SIZE, java.awt.Image.SCALE_SMOOTH);  
        	buttons[index].setIcon(new ImageIcon(newImage));
    	} catch (Exception e) {
    		System.out.println(image + " is not in the directory.");
    	}
    }
    
    // Post: Adds an Icon with file name 'image' to the given JLabel 'component'.
    protected void setIcon(JLabel component, String image) {
    	try {
        	Image icon = new ImageIcon(this.getClass().getResource(image)).getImage();
        	Image newImage = icon.getScaledInstance(ICON_SIZE, ICON_SIZE, java.awt.Image.SCALE_SMOOTH);  
        	component.setIcon(new ImageIcon(newImage));
    	} catch (Exception e) {
    		System.out.println(image + " is not in the directory.");
    	}
    }
    
    // Post: Reformats the given button at index 'index' in 'buttons' to align text and icon.
    protected void setButton(JButton[] buttons, int index, Border border) {
    	buttons[index].setHorizontalAlignment(SwingConstants.LEFT);
    	buttons[index].setHorizontalTextPosition(SwingConstants.RIGHT);
    	buttons[index].setBorder(null);
    }
    
    // Post: Adds an icon to each JButton in 'buttons' and aligns the buttons text with the icon.
    protected void formatIcons(JButton[] buttons, int[] indices, String[] icons) {
    	for (int i = 0; i < indices.length; i++) {
    		setIcon(buttons, indices[i], icons[i]);
    		setButton(buttons, indices[i], null);
    	}
    }
}
