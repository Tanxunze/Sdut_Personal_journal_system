import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JournalApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            createAndShowGUI();
        });
    }

    private static void createAndShowGUI() {
        // 创建主窗口
        JFrame frame = new JFrame("Personal Journal Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        // 创建文本区域
        JTextArea textArea = new JTextArea();
        textArea.setBorder(BorderFactory.createTitledBorder("Write your journal here"));

        // 创建日志列表
        DefaultListModel<String> listModel = new DefaultListModel<>();
        JList<String> journalList = new JList<>(listModel);
        journalList.setBorder(BorderFactory.createTitledBorder("Your Journals"));

        // 创建按钮
        JButton addButton = new JButton("Add Journal");
        JButton deleteButton = new JButton("Delete Journal");

        // 按钮事件处理
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listModel.addElement(textArea.getText());
                textArea.setText("");
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = journalList.getSelectedIndex();
                if (selectedIndex != -1) {
                    listModel.remove(selectedIndex);
                }
            }
        });

        // 布局设置
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(addButton);
        bottomPanel.add(deleteButton);

        frame.setLayout(new BorderLayout());
        frame.add(new JScrollPane(textArea), BorderLayout.CENTER);
        frame.add(new JScrollPane(journalList), BorderLayout.EAST);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        // 显示窗口
        frame.setVisible(true);
    }
}
