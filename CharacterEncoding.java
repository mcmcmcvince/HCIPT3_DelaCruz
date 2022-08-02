import java.awt.Dimension;
import java.awt.Font;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class CharacterEncoding extends JFrame {
    JTextArea text, unicode, utf8;
    
    public CharacterEncoding() {
        super("Character Encoding");
        JComponent content = (JComponent) getContentPane();
        content.setLayout(new BoxLayout(content, BoxLayout.PAGE_AXIS));
        
        add(new JLabel("Text:", SwingConstants.LEFT));
        add(new JScrollPane(text = makeTextArea()));
        text.setFont(new Font("Arial", Font.PLAIN, 12));
        text.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                textChanged();
            }
            public void removeUpdate(DocumentEvent e) {
                textChanged();
            }
            public void changedUpdate(DocumentEvent e) {
                textChanged();
            }
        });
        
        add(new JLabel("Unicode:", SwingConstants.LEFT));
        add(new JScrollPane(unicode = makeTextArea()));
        
        add(new JLabel("UTF8:", SwingConstants.LEFT));
        add(new JScrollPane(utf8 = makeTextArea()));

        fixLayout(content);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private JTextArea makeTextArea() {
        JTextArea area = new JTextArea(3, 20);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        return area;
    }
    
    private void fixLayout(JComponent content) {
        for (int i = 0; i < content.getComponentCount(); ++i) {
            JComponent c = (JComponent) content.getComponent(i);
            c.setAlignmentX(LEFT_ALIGNMENT);
            c.setMaximumSize(new Dimension(1000, c.getMaximumSize().height));
        }
    }
    
    private void textChanged() {
        unicode.setText(toUnicodeValues(text.getText()));
        utf8.setText(toUTF8Values(text.getText()));
    }
    
    private String toUnicodeValues(String s) {
        String t = "";
        for (int i = 0; i < s.length(); ++i) {
            t += asUnsigned(s.charAt(i)) + " "; 
        }
        return t;
    }

    private String toUTF8Values(String s) {
        Charset charset = Charset.forName("UTF-8");
        ByteBuffer buffer = charset.encode(s);
        String t = "";
        while (buffer.hasRemaining()) t += asUnsigned(buffer.get()) + " ";
        return t;
    }
    
    private int asUnsigned(byte b) {
        return ((int) b) & 0xFF;
    }

    private int asUnsigned(char c) {
        return ((int) c) & 0xFFFF;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run () {
                new CharacterEncoding().setVisible(true);
            }
        });
    }

}
