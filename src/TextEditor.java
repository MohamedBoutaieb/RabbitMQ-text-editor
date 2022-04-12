import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TextEditor extends JFrame implements ActionListener {

    public static String content1;
    public static String content2;
    static JTextArea textArea1;
    static JTextArea textArea2;
    JScrollPane scrollPane;
    JScrollPane scrollPane2;
    JSpinner fontSizeSpinner;
    JLabel fontLabel;
    JButton fontColorButton ;
    JComboBox fontBox ;
    Boolean open = false;
    JMenuBar menuBar;
    JMenu fileMenu;
    JMenuItem DisplayText;
    JMenuItem editSection1;
    JMenuItem editSection2;
    void initializeJFrame() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("RabbitMQ Text Editor");
        this.setSize(600,800);
        this.setLayout(new FlowLayout());
        this.setLocationRelativeTo(null);
        textArea1 = new JTextArea();
        // textArea.setPreferredSize(new Dimension(500,700));
        textArea1.setLineWrap(true);
        textArea1.setWrapStyleWord(true);
        textArea1.setFont(new Font("Arial",Font.PLAIN,20));
        scrollPane = new JScrollPane(textArea1);
        scrollPane.setPreferredSize(new Dimension(500,300));
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        textArea2 = new JTextArea();
        // textArea.setPreferredSize(new Dimension(500,700));
        textArea2.setLineWrap(true);
        textArea2.setWrapStyleWord(true);
        textArea2.setFont(new Font("Arial",Font.PLAIN,20));
        scrollPane2 = new JScrollPane(textArea2);
        scrollPane2.setPreferredSize(new Dimension(500,300));
        scrollPane2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);


        fontLabel= new JLabel("Font: ");

        fontSizeSpinner = new JSpinner();
        fontSizeSpinner.setPreferredSize(new Dimension(50,25));
        fontSizeSpinner.setValue(20);
        fontSizeSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                textArea1.setFont(new Font(textArea1.getFont().getFamily(),Font.PLAIN,(int )fontSizeSpinner.getValue()));
                textArea2.setFont(new Font(textArea2.getFont().getFamily(),Font.PLAIN,(int )fontSizeSpinner.getValue()));
            }
        });

        fontColorButton = new JButton("Color");
        fontColorButton.addActionListener(this);
        String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        fontBox = new JComboBox(fonts);
        fontBox.addActionListener(this);
        fontBox.setSelectedItem("Arial");
        // menuBar
        menuBar = new JMenuBar();
        fileMenu= new JMenu("Text");
        DisplayText = new JMenuItem("Display");
        editSection1 = new JMenuItem("Edit Section 1");
        editSection2 = new JMenuItem("Edit Section 2");

        DisplayText.addActionListener(this);
        editSection1.addActionListener(this);
        editSection2.addActionListener(this);

        fileMenu.add(DisplayText);
        fileMenu.add(editSection1);
        fileMenu.add(editSection2);
        menuBar.add(fileMenu);
        this.add(menuBar);
        this.add(fontBox);
        this.add(fontColorButton);
        this.add(fontLabel);
        this.add(fontSizeSpinner);
        this.add(scrollPane);
        this.add(scrollPane2);
        this.setVisible(true);
    }
    TextEditor() throws Exception {
        this.initializeJFrame();

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource()== DisplayText) {
            try {
                //Client.RequestText();
                User.sendText("section1");
                User.sendText("section2");

            } catch (Exception exception) {
                exception.printStackTrace();
            }

        }
        if (e.getSource()== editSection1) {
            content1=textArea1.getText();
            String section1 = content1;
            try {
                //   ServerText.RequestSection1();
                synchronized (this){
                    User.updateSection(section1,"Section1");
                    User.sendText("section1");
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        if (e.getSource()== editSection2) {
            content2=textArea2.getText();
            String section2 = content2;
            try {
                //ServerText.RequestSection2();
                synchronized (this){
                    User.updateSection(section2,"Section2");
                    User.sendText("section2");
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }/*
        if(e.getSource()==fontColorButton){
            JColorChooser colorChooser = new JColorChooser() ;
            Color color = colorChooser.showDialog(null, "Choose a color", Color.black);
            textArea.setForeground(color);
        }
        if (e.getSource()==fontBox){
            textArea.setFont(new Font((String)fontBox.getSelectedItem(),Font.PLAIN,textArea.getFont().getSize()));
        }


    }*/
     static void updateContent(String s){
         if (s.equals("section1"))
         {textArea1.setText("");
        textArea1.append(content1);}
         else {
             textArea2.setText("");
             textArea2.append(content2);
         }

    }



}
