import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI extends JFrame {
    private JPanel panel1;
    private JTextField genotypTextField;
    private JButton klasyfikujButton;
    private JTextField dokladnoscField;
    private JLabel wynikLabel;
    private JButton pokażOsobnikiNajslabszeButton;
    private JButton pokażOsobnikiNajsilniejszeButton;
    private JButton eksportujBazeDoXMLButton;
    private JTextArea wynikTextArea;
    private JButton klWieleButton;

    public GUI() {
        super("bacteria");
        setSize(600, 600);
        setContentPane(panel1);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);

        klasyfikujButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DataProcessing dp = new DataProcessing();
                wynikLabel.setText(dp.clasify(Integer.parseInt(genotypTextField.getText()), Integer.parseInt(dokladnoscField.getText())));
            }
        });
        eksportujBazeDoXMLButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DataProcessing dp = new DataProcessing();
                dp.exportToXml();
            }
        });
        pokażOsobnikiNajslabszeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DataProcessing dp = new DataProcessing();
                wynikTextArea.setText(dp.weakest());
            }
        });
        pokażOsobnikiNajsilniejszeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DataProcessing dp = new DataProcessing();
                wynikTextArea.setText(dp.strongest());
            }
        });
        klWieleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DataProcessing dp = new DataProcessing();
                dp.clasifyMany(genotypTextField.getText(), Integer.parseInt(dokladnoscField.getText()));
            }
        });
    }
}
